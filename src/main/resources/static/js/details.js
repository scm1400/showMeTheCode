$(document).ready(function() {
    let id = getParameterByName('id');

    $.ajax({
        type: "GET",
        url: `/details?id=${id}`,
        contentType: "application/json;charset-utf-8;",
        success: function (res) {
            let date = new Date(res.createdAt)
            let title = `<h1>`
            title = title + res.title + `</h1>`
            date = dateFormat(date)
            $("#request-title").append(title)
            $("#user-name").html(res.username)
            $("#created-at").html(`&nbsp;· `+ date)
            $("#content").html(res.content)
            $("#sub-info__content").append(`<button class="ac-button is-sm is-solid is-gray  ac-tag ac-tag--blue "><span
                                                class="ac-tag__hashtag">#&nbsp;</span><span
                                                class="ac-tag__name">'${res.languageName}'</span></button>`)
            let answer = res['reviewAnswerResponseDto']
            let comments = res['comments'];
        }
    })
})

// ========================================
// 쿼리 파라미터 받아오기
// ========================================
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