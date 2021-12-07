$(document).ready(function () {
    let id = getParameterByName("id");

    let tmp_html = `<div class="flex-row feature__status e-status e-hover-toggle" data-status="1">
									<button onclick="showEvaluateForm()" class="ac-button is-md is-solid is-primary button-rounded undefined">평가하기</button>
								</div>`
    $('#request-status-box').append(tmp_html)

    getDetails(id);

    $('input[name="star-input"]:radio').on("change", function () {
        //라디오 버튼 값을 가져온다.
        console.log($(this).val());

    });
});

// ========================================
// 상세내용 랜더링
// ========================================
function getDetails(id) {
    $.ajax({
        type: "GET",
        url: `/details?id=${id}`,
        contentType: "application/json;charset-utf-8;",
        success: function (res) {

            console.log(res);

            let date = new Date(res.createdAt);
            let title = `<h1>`;
            title = title + res.title + `</h1>`;
            date = dateFormat(date);
            $("#request-title").append(title);
            $("#user-name").html(res.username);
            $("#created-at").html(`&nbsp;· ` + date);
            $("#content").html(res.content);

            $("#sub-info__content")
                .append(`<button class="ac-button is-sm is-solid is-gray  ac-tag ac-tag--blue "><span
                                                class="ac-tag__hashtag">#&nbsp;</span><span
                                                class="ac-tag__name">'${res.languageName}'</span></button>`);

            let reviewAnswer = res["reviewAnswer"];
            if (reviewAnswer) {
                addAnswerHtml(reviewAnswer)
            }

            let status = res.status
            $("#request-status").html(res.status);
            if (status === "해결됨") {
                let tmp_html = `<div class="flex-row feature__status e-status e-hover-toggle" data-status="1">
									<button onclick="showEvaluateForm()" class="ac-button is-md is-solid is-success button-rounded undefined">평가하기</button>
								</div>`
                $('#request-status-box').append(tmp_html)
            }

            $("#question-comment-content-box").empty();
            let comments = res["comments"];
            if (comments.length > 0) {
                $('#answer-section').show();
                addCommentHtml(comments);
            } else {
                $('#answer-section').hide();
            }
        },
    });
}

// ========================================
// 댓글 랜더링
// ========================================
function addCommentHtml(comments) {
    let size = comments.length;

    $("#question-comment-qty").html(`총 ${size}개 댓글이 달렸습니다.`);

    for (let i = 0; i < size; i++) {
        let commentId = comments[i]["commentId"];
        let userId = comments[i]["userId"];
        let username = comments[i]["username"];
        let content = comments[i]["content"];
        let createdAt = comments[i]["createdAt"];

        let tmp_html = `<div class="comment__index">${i + 1}</div>
                    <div class="comment__card">
                        <div class="comment__header flex-row">
                            <img class="comment__user-profile"
                                 src="./images/default_profile.png"
                                 alt="[아이디] 프로필">
                            <div class="flex-column">
                                <div class="flex-row" id="question-comment-username">
                                    <a href="/users/@deeplyrooted" class="comment__user-name">${username}</a>
                                </div>
                                <span class="comment__updated-at">${createdAt}</span>
                            </div>
                        </div>
                        <div class="comment__body markdown-body">
                            <p id="question-comment-content">
								${content}
							</p>
                        </div>
                    </div>`;
        $("#question-comment-content-box").append(tmp_html);
    }
}

// ========================================
// 답변 랜더링
// ========================================
function addAnswerHtml(answer) {

    console.log(answer);

    let answerId = answer['reviewAnswerId'];
    let questionId = answer['reviewRequestId'];
    let username = answer['username']
    let content = answer['answerContent'];
    let point = answer['point'];
    let createdAt = answer['createdAt'];

    $('#answer-username').html(username);
    $('#answer-date').html(dateFormat(new Date(createdAt)));
    $('#answer-content').html(content);

}


// ========================================
// 댓글등록
// ========================================
function addComment() {
    let questionId = getParameterByName("id");
    let content = CKEDITOR.instances["content-comment"].getData();

    let data = {content: content};

    console.log(data);

    $.ajax({
        type: "POST",
        url: `/question/${questionId}/comment`,
        contentType: "application/json;charset=utf-8;",
        data: JSON.stringify(data),
        success: function (res) {
            console.log(res);
            alert("댓글작성 완료");
            window.location.reload();
        },
    });
}

// ========================================
// 평가하기 폼 띄우기
// ========================================
function showEvaluateForm(answerId) {
    temp_html = `<div id="eval_modal" class="modal ">
					<div class="dimmed"></div>
					  <article class="sign-in-modal">
						<span onclick="close_login_modal()" class="e-close header__close-button">
						  <svg width="16px" xmlns="http://www.w3.org/2000/svg" height="12" viewBox="0 0 12 12"><path fill="#3E4042" fill-rule="evenodd" d="M.203.203c.27-.27.708-.27.979 0L6 5.02 10.818.203c.27-.27.709-.27.98 0 .27.27.27.708 0 .979L6.978 6l4.818 4.818c.27.27.27.709 0 .98-.27.27-.709.27-.979 0L6 6.978l-4.818 4.818c-.27.27-.709.27-.98 0-.27-.27-.27-.709 0-.979L5.022 6 .203 1.182c-.27-.27-.27-.709 0-.98z" clip-rule="evenodd"></path></svg>
						</span>
						<span class="header__logo">
						  <h2>평가하기</h2>
						</span>
						
				
					  </article>
					  </div>`

    $('body').append(temp_html);
}

function close_eval_modal() {
    $('#eval_modal').remove();
}

// ========================================
// 쿼리 파라미터 받아오기
// ========================================
function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results == null
        ? ""
        : decodeURIComponent(results[1].replace(/\+/g, " "));
}

function dateFormat(date) {
    let month = date.getMonth() + 1;
    let day = date.getDate();

    month = month >= 10 ? month : "0" + month;
    day = day >= 10 ? day : "0" + day;

    return date.getFullYear() + "." + month + "." + day;
}
