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

			$("#question-comment-content-box").empty();
			let comments = res["comments"];
			if (comments.length > 0) {
				getComments(comments);
			}
		},
	});
}

// ========================================
// 댓글 랜더링
// ========================================
function getComments(comments) {
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
                                 src="https://cdn.inflearn.com/public/main/profile/default_profile.png"
                                 alt="[아이디] 프로필">
                            <div class="flex-column">
                                <div class="flex-row" id="question-comment-username">
                                    <a href="/users/@deeplyrooted" class="comment__user-name">${username}</a>
                                </div>
                                <span class="comment__updated-at">${createdAt}</span>
                            </div>
                        </div>
                        <div class="comment__body markdown-body">
                            <p id="question-comment-content">${content}</p>
                            <div class="comment__features flex-row">

                                <div class="comment__like e-comment-like" data-id="122943" data-status="1" data-cnt="1">

                                    <button class="ac-button is-md is-solid is-red button-rounded undefined"
                                            style="min-width: 75px">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"
                                             viewBox="0 0 16 16">
                                            <path fill="#E5503C"
                                                  d="M9.333 13.605c-.328.205-.602.365-.795.473-.102.057-.205.113-.308.168h-.002c-.143.074-.313.074-.456 0-.105-.054-.208-.11-.31-.168-.193-.108-.467-.268-.795-.473-.655-.41-1.53-1.007-2.408-1.754C2.534 10.382.667 8.22.667 5.676c0-2.308 1.886-4.01 3.824-4.01 1.529 0 2.763.818 3.509 2.07.746-1.252 1.98-2.07 3.509-2.07 1.938 0 3.824 1.702 3.824 4.01 0 2.545-1.867 4.706-3.592 6.175-.878.747-1.753 1.344-2.408 1.754z"/>
                                        </svg>
                                        1
                                    </button>
                                </div>

                            </div>
                        </div>
                    </div>`;
		$("#question-comment-content-box").append(tmp_html);
	}
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
