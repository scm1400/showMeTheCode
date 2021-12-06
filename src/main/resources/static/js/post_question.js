let reviewerId;

$('document').ready(function() {
   $('select-reviewer').on("change", () => {
       reviewerId = $(this).val()
   })
})

/**
 * [리뷰요청] 언어이름으로 리뷰어 찾기
 */
function findReviewer() {
    $('#select-reviewer').empty();
    let query = $('#language-input').val()

    if (query != "") {
        $.ajax({
            type: "GET",
            url: `/user/language?language=${query}`,
            success: function (res) {
                for (let i = 0; i < res.length; i++) {
                    let id = res[i]['id'];
                    let username = res[i]['username'];
                    let answerCount = res[i]['answerCount']; // 답변수
                    let point = res[i]['point']

                    if (i===0) {
                        reviewerId = id;
                    }


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

/**
 * [리뷰요청] 서버로 요청
 */
function postQuestion(){
    let content = CKEDITOR.instances['contents'].getData()
    let title = $('#title-input').val()
    let language = $('#language-input').val()

    let data = {
        title: title,
        content: content,
        language: language,
        reviewerId: reviewerId,
    };

    $.ajax({
        type: "POST",
        url: "/question",
        contentType: "application/json;charset-utf-8;",
        data: JSON.stringify(data),
        success: function(res) {
            alert('등록완료');
            location.href = 'index.html';
        }
    })
}