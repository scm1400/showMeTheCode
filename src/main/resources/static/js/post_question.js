function findReviewer() {
    $('#select-reviewer').empty();
    let query = $('#language-input').val()

    $.ajax({
        type: "GET",
        url: `/user/language?language=${query}`,
        success: function(res) {
            console.log(res);
            for (let i=0;i<res.length;i++) {
                let id = res[i]['id'];
                let username = res[i]['username'];
                let answerCount = res[i]['answerCount']; // 답변수
                let point = res[i]['point']

                console.log(id, username, answerCount, point);

                let option_html = `<option>
                                    <span>${username}</span>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
                                    <span>답변수: ${answerCount}</span>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
                                    <span>평균점수: ${point}</span>
                                </option>`

                $('#select-reviewer').append(option_html);
            }
            $('#select-reviewer-box').removeClass("is-hidden");
        }
    })
}

function postQuestion(){
    let value = CKEDITOR.instances['contents'].getData()
    console.log(value);

    let data = {"code": value};

    $.ajax({
        type: "POST",
        url: "/reviewer/request",
        contentType: "application/json;charset-utf-8;",
        data: JSON.stringify(data),
        success: function(res) {
            console.log(res);
        }
    })
}