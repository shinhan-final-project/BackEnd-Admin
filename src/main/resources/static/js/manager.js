$(document).ready(() => {
    $('#fetchData').click(() => {
        $.get("/api/manager/wait-list", ({ success, data, message }) => {
            if (!success) {
                console.error(message);
            }
            
            let table = '<table><thead><tr><th>ID</th><th>Name</th><th>Department</th><th>Select</th></tr></thead><tbody>';
            /* JSON 데이터를 테이블로 변환 */
            $.each(data, (index, item) => {
                table += '<tr>'
                    + '<td>' + item.name + '</td>'
                    + '<td>' + item.department + '</td>'
                    + '<td><input type="checkbox" name="selectedItems" value="' + item.id + '"></td>'
                    + '</tr>';
            });
            table += '</tbody></table>';
            $('#tableContainer').html(table);
        });
    });

    // 체크박스가 변경되면 처리하는 이벤트 리스너
    $('#tableContainer').on('change', 'input[type="checkbox"]', () => {
        if ($('input[name="selectedItems"]:checked').length > 0) {
            $('#approveButton').show();
            $('#refuseButton').show();
        } else {
            $('#approveButton').hide();
            $('#refuseButton').hide();
        }
    });
    // "승인하기" 버튼 클릭 이벤트
    $('#approveButton').click(() => {
        let selectedIds = $('input[name="selectedItems"]:checked').map((index, element) => {
            return $(element).val();
        }).get();

        $.ajax({
            type: "POST",
            url: "/api/manager/approve",
            contentType: "application/json",
            data: JSON.stringify(selectedIds),
            success: (response) => {
                console.log("성공:", response);
                location.reload();
            },
            error: (jqXHR, textStatus, errorThrown) => {
                console.log("오류:", errorThrown);
            }
        });
    });
    $('#refuseButton').click(() => {
        let selectedIds = $('input[name="selectedItems"]:checked').map((index, element) => {
            return $(element).val();
        }).get();

        $.ajax({
            type: "DELETE",
            url: "/api/manager/refuse",
            contentType: "application/json",
            data: JSON.stringify(selectedIds),
            success: (response) => {
                console.log("성공:", response);
                location.reload();
            },
            error: (jqXHR, textStatus, errorThrown) => {
                console.log("오류:", errorThrown);
            }
        });
    });
});