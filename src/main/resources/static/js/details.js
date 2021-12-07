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
			$("#request-status").html(res.status);
			$("#sub-info__content")
				.append(`<button class="ac-button is-sm is-solid is-gray  ac-tag ac-tag--blue "><span
                                                class="ac-tag__hashtag">#&nbsp;</span><span
                                                class="ac-tag__name">'${res.languageName}'</span></button>`);

			let reviewAnswer = res["reviewAnswer"];
			if (reviewAnswer) {
				addAnswerHtml(reviewAnswer)
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

	let data = { content: content };

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
