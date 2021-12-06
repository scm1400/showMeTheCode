$(document).ready(function() {
    let id = getParameterByName('id');

    $.ajax({
        type: "GET",
        url: `/details?id=${id}`,
        contentType: "application/json;charset-utf-8;",
        success: function (res) {
            console.log(res);
            $("#request-title").html(res.title)
            $("#user-name").html(res.username)
            $("#created-at").html(res.createdAt)
            $("#content").html(res.content)
            $("#sub-info__content").append(`<button class="ac-button is-sm is-solid is-gray  ac-tag ac-tag--blue "><span
                                                class="ac-tag__hashtag">#&nbsp;</span><span
                                                class="ac-tag__name">'${res.languageName}'</span></button>`)

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
