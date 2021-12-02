function postQuestion(){
    let value = CKEDITOR.instances['contents'].getData()
    console.log(value);

    let data = {"code": value};

    $.ajax({
        type: "POST",
        url: "/question",
        contentType: "application/json;charset-utf-8;",
        data: JSON.stringify(data),
        success: function(res) {
            console.log(res);
        }
    })
}