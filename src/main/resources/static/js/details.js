$(document).ready(function () {
    let id = getParameterByName("id");

    getDetails(id);

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
            if (JSON.stringify(reviewAnswer) === '{}') {
                $('#answer-section').hide();
            } else {
                addAnswerHtml(reviewAnswer)
                $('#answer-section').show()
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
                $('#comment-section').show();
                addCommentHtml(comments);
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
// 리뷰어 변경 모달폼 띄우기
// ========================================
function showChangeReviewerForm(languageName) {

    temp_html = `<div id="change-reviewer-modal" class="modal ">
                    <div class="dimmed"></div>
                      <article class="sign-in-modal">
                        <span onclick="close_change_reviewer_modal()" class="e-close header__close-button">
                          <svg width="16px" xmlns="http://www.w3.org/2000/svg" height="12" viewBox="0 0 12 12"><path fill="#3E4042" fill-rule="evenodd" d="M.203.203c.27-.27.708-.27.979 0L6 5.02 10.818.203c.27-.27.709-.27.98 0 .27.27.27.708 0 .979L6.978 6l4.818 4.818c.27.27.27.709 0 .98-.27.27-.709.27-.979 0L6 6.978l-4.818 4.818c-.27.27-.709.27-.98 0-.27-.27-.27-.709 0-.979L5.022 6 .203 1.182c-.27-.27-.27-.709 0-.98z" clip-rule="evenodd"></path></svg>
                        </span>
                        <span class="header__logo">
                          <img src="./images/header_logo.png" style=" margin: auto" />
                        </span>
                        <div class="form__item">
                            <label class="form__label" for="language-input">언어이름으로 리뷰어 검색</label>
                            <div class="ac-input-with-item--large question-modal__language ">
                                <input id="language-input" value="" data-kv="language" type="text" placeholder="언어를 입력해주세요.">
                                <button class="button is-primary" onclick="findReviewer()">확인</button>
                            </div>
                        </div>
                        
                                       
                        <div id="select-reviewer-box" class="form__item is-hidden">
                            <label class="form__label" for="select-reviewer">리뷰어 지정</label>
                            <div class="select is-fullwidth">
                                <select id="select-reviewer">
        
                                </select>
                            </div>
                        </div>        
                        
                         <footer class="modal-card-foot">
                            <button onclick="close_change_reviewer_modal()"
                                    class="ac-button is-lg is-outlined is-gray question-modal__button--cancel e-cancel-question-modal">
                                취소
                            </button>
            
                            <button id="send"
                                    class="ac-button is-lg is-solid is-primary question-modal__button--cancel e-submit-question-modal"
                                    onclick="changeReviewer()">
                                변경
                            </button>
                        </footer>
                      </article>
                    </div>`

    $('body').append(temp_html);
}

function close_change_reviewer_modal() {
    $('#change-reviewer-modal').remove();
}

// ========================================
// 언어이름으로 리뷰어 찾기
// ========================================
function findReviewer() {
    $('#select-reviewer').empty();
    let query = $('#language-input').val()

    if (query != "") {
        $.ajax({
            type: "GET",
            url: `/user/language?language=${query}`,
            success: function (res) {
                $('#select-reviewer').append('<option>리뷰어를 선택하세요</option>')

                for (let i = 0; i < res.length; i++) {
                    let id = res[i]['id'];
                    let username = res[i]['username'];
                    let answerCount = res[i]['answerCount']; // 답변수
                    let point = res[i]['point']

                    let option_html = `<option value=${id}>
                                    <span>${username}</span>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
                                    <span>답변수: ${answerCount}</span>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
                                    <span>평균점수: ${point}</span>
                                </option>`

                    $('#select-reviewer').append(option_html);
                }
                $('#select-reviewer-box').removeClass("is-hidden");
            }
        })
    } else {
        alert('해당 언어의 리뷰어가 존재하지 않습니다.');
    }
}

// ========================================
// 리뷰어 변경
// ========================================
function changeReviewer() {

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
