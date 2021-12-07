
$(document).ready(function() {
    getDetails();
})

/**
 * 상세정보
 */
function getDetails() {

    let id = getParameterByName('id');

    $.ajax({
        type: "GET",
        url: `/details/${id}/answer`,
        success: function (res) {
            console.log(res);
            let questionId = res['questionId']
            let username = res['username']
            let title = res['title']
            let content = res['content']
            let status = res['status']
            let createdAt = res['createdAt']
            let date = dateFormat(new Date(createdAt))

            let answerId = res['answerId']
            let answerContent = res['answerContent']

            $('#request-title').html(title);
            $('#user-name').html(username);
            $('#created-at').html(date);

            $('#question-status').text(status)

            // 답변이 있는 경우 편집이 불가능하도록 <p> 태그 내에 답변 내용을 랜더링
            if (answerContent) {
                $('#content-answer-markdown-box').hide()
                $('#addAnswerBtn').hide()
                $('#content-answer-text').show()
                $('#content-answer-text').html(content)
            // 답변이 없는 경우 답변이 가능하도록 textarea (markdown폼)을 랜더링
            } else {
                $('#addAnswerBtn').show()
                $('#content-answer-markdown-box').show()
                $('#content-answer-text').hide()
            }
        }
    })
}

/**
 * 답변하기
 */
function addAnswer() {
    let questionId = getParameterByName('id');
    let content = CKEDITOR.instances['content-answer'].getData();


    let data = {
        content: content
    }

    $.ajax({
        type: "POST",
        url: `/reviewer/request/${questionId}`,
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(data),
        success: function (res) {
            alert('리뷰 작성을 완료했습니다.')
            window.location.reload();
        }
    })
}

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

function dateFormat(date) {
    let month = date.getMonth() + 1;
    let day = date.getDate();

    month = month >= 10 ? month : '0' + month;
    day = day >= 10 ? day : '0' + day;

    return date.getFullYear() + '.' + month + '.' + day;
}