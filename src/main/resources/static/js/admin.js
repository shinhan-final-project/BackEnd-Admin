const fetchLogs = () =>{
    $.get("/api/log", ({success, data, message}) =>{
        if(!success){
            console.error(message);
        }

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
            $("#desc").text(titleValue.replace(/<[^>]*>/g, '').replace(/\n|&amp;|&lt;|&gt;|&quot;|&apos;|&nbsp;/g, ''));
        });
    }).fail(function (error) {
        fetched = false;
        console.error("데이터를 가져오지 못했습니다:", error);
    });
}

const fetchStocks = () => {
    $.get("/api/admin/invest-items", ({ success, data, message }) => {

        if (!success) {
            alert(message);
            return;
        }

        /**
         * 종목 리스트
         */

        let select = $("#investGameTarget");

        $.each(data, (index, item) => {
            select.append($("<option></option>")
                .attr("value", item.id) // 각 옵션의 값 설정
                .text(item.companyName)); // 각 옵션의 표시 텍스트 설정
        });

        $("#investGameTarget").select2(); // select2 라이브러리를 사용하여 검색 기능 추가

        $("#investGameTarget").on("change", function () {
            /**
             * 종목 선택
             */
            const selectedValue = $(this).val();
            $('#termId').val(selectedValue);

            const selectedOption = $(this).find("option:selected");
            const titleValue = selectedOption.attr("title");
            $("#desc").text(titleValue);
        });
    }).fail(function (error) {
        fetchedStocks = false;
        console.error("데이터를 가져오지 못했습니다:", error);
    });
};

const initForm = () => {
    $('#quizStartYear').attr("min", 2013);
    $('#quizStartYear').attr("max", new Date().getFullYear());
    $('#quizStartYear').on('input', function() {
        // 변경된 값 가져오기
        var inputValue = +$(this).val();
        
        for (let i = 0; i < 5; i++) {
            $(".year" + i).text(inputValue + i);
        }
    });

    $("#investGameForm").submit(function (event) {
        event.preventDefault();

        const itemId = $("#investGameTarget").find("option:selected").val();
        if (!itemId) {
            alert("종목을 선택해주세요.");
            return;
        }

        var { isValid, data: jsonData } = formDataToJSON(this);
        
        if (!isValid) {
            alert("모든 정보를 입력해주세요.");
            return;
        }

        jsonData['itemId'] = itemId;

        // POST 요청 보내기
        $.ajax({
            url: "/api/admin/invest",
            type: "POST",
            data: JSON.stringify(jsonData), // 데이터를 JSON 문자열로 변환
            contentType: "application/json; charset=utf-8", // Content-Type을 application/json으로 설정
            dataType: "json", // 서버로부터 받을 데이터의 타입을 JSON으로 설정
            success: function({ success, data, message }) {
                if (success) {
                    alert(data);
                    location.reload();
                } else {
                    alert(message);
                }
            },
            error: function(xhr, status, error) {
                console.error(xhr.responseText);
                try {
                    const err = JSON.parse(xhr.responseText);
                    alert(err.message);
                } catch (error) {
                    
                }
            }
        });
    });

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
                    location.reload();
                } else {
                    alert(message);
                }
            },
            error: function (error) {
                console.error("전송 실패:", error);
            }
        });
    });
}

$(document).ready(() => {

    let fetched = false;
    let fetchedStocks = false;

    $("#makeTermQuizBtn").click(() => {
        $('.toggleWrapper').css('display', 'none');
        $('#myDiv').css('display', 'block');
        $('#termQuizFormWrapper').css('display', 'block');

        if (fetched) {
            return;
        }

        fetched = true;
        fetchTerms();
    });

    $("#showLog").click(() => {
        $('.toggleWrapper').css('display', 'none');
        $('#tableContainer').css('display', 'block');

        fetchLogs();
    });

    $("#makeInvestGameBtn").click(() => {
        $('.toggleWrapper').css('display', 'none');
        $("#investGameFormContainer").css('display', 'block');

        if (fetchedStocks) {
            return;
        }
        fetchStocks();
        fetchedStocks = true;
    });

    initForm();
});

const formDataToJSON = (form) => {
    var formData = $(form).serializeArray();
    const json = {};
    const titles = [];
    const urls = [];
    const result = formData.every(function(field) {
        if (!field.value || field.value.length == 0) {
            return false;
        }

        if (field.name.startsWith("title")) {
            titles.push(field.value);
        } else if (field.name.startsWith("url")) {
            urls.push(field.value);
        } else {
            json[field.name] = field.value;
        }
        
        return true;
    });
    
    if (!result) {
        return {
            isValid: false,
        };
    }

    let idx = 0;
    const newList = [];
    for (let i = 0; i < 5; i++) {
        // NewsInfo
        const element = [];
        
        for (let j = 0; j < 5; j++) {
            // News
            element.push({
                title: titles[idx],
                url: urls[idx],
            });
            idx++;            
        }

        newList.push({
            year: $('.year' + i).text(),
            news: element,
        });
    }
    json['newList'] = newList;

    return {
        isValid: true,
        data: json,
    };
}
