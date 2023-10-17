const fetchLogs = () =>{
    $.get("/api/log", ({success, data, message}) =>{
        if(!success){
            console.error(message);
        }
        console.log(data);
        let table = '<table><thead><tr><th>ID</th><th>플레이어 나이</th><th>플레이어 성별</th><th>플레이어 투자경력 연차</th><th>진행한 게임</th><th>받은(받게 될) 보상</th><th>푼 문제 용어</th><th>전문가가 작성한 용어에 대한 설명</th><th>문제의 정답에 대한 설명</th><th>사용자가 선택한 문제</th><th>문제 정답 여부</th><th>문제를 작성한 사람</th><th>생성 날짜</th></tr></thead><tbody>';
        $.each(data, (index, item) =>{
            const gender = item.memberGender==="M"?"남성":"여성";
            const isCorrect = item.correct?"정답":"오답";
            table += '<tr>'
                + '<td>' + item.logId + '</td>'
                + '<td>' + item.age + '</td>'
                + '<td>' + gender + '</td>'
                + '<td>' + item.investCareerYear + '</td>'
                + '<td>' + item.gameType + '</td>'
                + '<td>' + item.gameReward + '</td>'
                + '<td>' + item.term + '</td>'
                + '<td>' + item.explanation + '</td>'
                + '<td>' + item.answerExplanation + '</td>'
                + '<td>' + item.selectExplanation + '</td>'
                + '<td>' + isCorrect + '</td>'
                + '<td>' + item.writerInformation + '</td>'
                + '<td>' + item.createdAt + '</td>';
        });
        table += '</tbody></table>';
        $('#tableContainer').html(table);
    });
}
const fetchTerms = () => {
    $.get("/api/admin/term", ({ success, data, message }) => {

        if (!success) {
            alert(message);
            return;
        }

        /**
         * 용어 리스트
         */

        let select = $("<select></select>");
        select.append($("<option selected disabled>문제를 만들고 싶은 주식 용어 선택</option>"));

        $.each(data, (index, term) => {
            select.append($("<option></option>")
                .attr("value", term.id) // 각 옵션의 값 설정
                .attr("title", term.description)
                .text(term.term)); // 각 옵션의 표시 텍스트 설정
        });

        select.attr("id", "mySelect"); // select 요소에 ID 추가
        $("#myDiv").append(select);
        $("#mySelect").select2(); // select2 라이브러리를 사용하여 검색 기능 추가

        $("#mySelect").on("change", function () {
            /**
             * 용어 선택
             */
            const selectedValue = $(this).val();
            $('#termId').val(selectedValue);

            const selectedOption = $(this).find("option:selected");
            const titleValue = selectedOption.attr("title");
            $("#desc").text(titleValue);
        });
    }).fail(function (error) {
        fetched = false;
        console.error("데이터를 가져오지 못했습니다:", error);
    });
}

const initForm = () => {
    $("#termQuizForm").submit(function (event) {
        event.preventDefault(); // 폼 제출 방지

        var jsonData = {};

        jsonData['termId'] = $('#termId').val();
        jsonData['explanation'] = $('#explanation').val();

        jsonData['questionItem'] = [];
        let answerNum = $("input[name='isAnswer']:checked").val();
        for (let i = 1; i <= 3; i++) {
            jsonData['questionItem'].push({
                questionOption: $('#questionOption' + i).val(),
                isAnswer: i == answerNum,
            });
        }

        const plusPoint = document.getElementById("plusPoint").value;

        // 입력값이 정수인지 확인
        if (Number.isInteger(parseInt(plusPoint))) {
            jsonData['plusPoint'] = plusPoint;
        } else {
            alert("올바른 정수를 입력하세요.");
            return;
        }

        const minusPoint = document.getElementById("minusPoint").value;

        // 입력값이 정수인지 확인
        if (Number.isInteger(parseInt(minusPoint))) {
            jsonData['minusPoint'] = minusPoint;
        } else {
            alert("올바른 정수를 입력하세요.");
            return;
        }

        console.log(jsonData);
        const jsonString = JSON.stringify(jsonData);

        $.ajax({
            type: "POST",
            url: "/api/admin/term", // 서버 API 엔드포인트 URL
            contentType: "application/json", // 전송하는 데이터의 형식을 JSON으로 지정
            data: jsonString, // JSON 데이터를 전송
            success: function (response) {
                console.log("성공적으로 전송됨:", response);

                const { success, data, message } = response;
                if (success) {
                    alert(data);
                } else {
                    alert(message);
                }

                location.reload();
            },
            error: function (error) {
                console.error("전송 실패:", error);
            }
        });
    });
}

$(document).ready(() => {

    let fetched = false;

    $("#makeTermQuizBtn").click(() => {
        $('#myDiv').css('display', 'block');
        $('#termQuizFormContainer').css('display', 'block');

        if (fetched) {
            return;
        }

        fetched = true;
        fetchTerms();
    });

    $("#showLog").click(() => {
        console.log("로그 가져오기");

        $('#myDiv').css('display', 'none');
        $('#termQuizFormContainer').css('display', 'none');
        fetchLogs();
    });

    initForm();
});