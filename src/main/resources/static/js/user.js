$(document).ready(function() {

    $("#signup-id").keyup(function(){

        var email = $("#signup-id").val();

        if(email != 0)
        {
            if(isValidEmailAddress(email))
            {
                $("#error-email").addClass("form__error--hide");
            } else {
                $("#error-email").text("이메일 형식이 올바르지 않습니다.")
                $("#error-email").removeClass("form__error--hide");

            }
        } else {
            console.log(3)
        }

    });

    $("#emailConfirm").keyup(function(){

        let email = $("#signup-id").val();
        let email2 = $("#emailConfirm").val();

        if(email != email2)
        {
            $("#error-email-check").text("이메일이 일치하지 않습니다.")
            $("#error-email-check").removeClass("form__error--hide");

        } else {
            $("#error-email-check").addClass("form__error--hide");
        }

    });

});

// 로그인
function signin() {
    console.log('로그인')
    let username = $('#signin-id').val()
    let password = $('#signin-password').val()

    let data = {
        "username": username,
        "password": password
    }

    console.log(data)

    $.ajax({
        type: "POST",
        url: "/user/signin",
        contentType: "application/json;charset=utf-8;",
        data: JSON.stringify(data),
        success: function(res) {
            sessionStorage.setItem("mytoken", res['token'])
            sessionStorage.setItem("myAuthority", res['authority'])
            alert('로그인에 성공했습니다.')
            window.location.reload()
            alert('로그인에 성공했습니다.')
        }, error: function(err) {
            alert('로그인에 실패했습니다.')
        }
    })
}

// 회원가입
function signup() {

    if(!isValidEmailAddress($("#signup-id").val()))
    {
        return alert("이메일을 확인해주세요.");
    }

    let id = $('#signup-id').val()
    let password = $('#signup-password').val()
    let isReviewer = $('input[id="signup-isReviewer"]').is(":checked")
    let data = {}
    let langs = []
    if (isReviewer) {
        // $('input:checkbox[name="languageSelectBox"]').each(function() {
        //     if (this.checked) {
        //         langs.push($(this).val())
        //         console.log(langs)
        //     }
        // });
        langs.push($("select[name=language]").val())
        console.log(langs)
    }


    data = {
        "username": id,
        "password": password,
        "reviewer": isReviewer,
        "languages": langs
    }


    $.ajax({
        type: "POST",
        url: "/user/signup",
        contentType: "application/json;charset=utf-8;",
        data: JSON.stringify(data),
        success: function (res) {
            // console.log(res)
            alert('회원가입에 성공했습니다.')
            window.location.reload()
        }, error: function(err) {
            // console.log(err)
            // console.log(err.responseJSON.message)
            alert(err.responseJSON.message)
        }
    })
}

function logout() {
    $.ajax({
        type: "POST",
        url: "/user/logout",
        success: function(res) {
            if (res['result'] == "success") {
                sessionStorage.clear();
                alert(res['message'])
            }
        }
    })
    window.location.reload();
}

function openSigninModal() {
    $('#signin-id').val('')
    $('#signin-password').val('')
    $('#signinModal').modal('show');
}

function openSignupModal() {
    $('#signup-id').val('')
    $('#signup-password').val('')
    $('#signupModal').modal('show');
}



function isValidEmailAddress(emailAddress) {
    var pattern = new RegExp(/^(("[\w-\s]+")|([\w-]+(?:\.[\w-]+)*)|("[\w-\s]+")([\w-]+(?:\.[\w-]+)*))(@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$)|(@\[?((25[0-5]\.|2[0-4][0-9]\.|1[0-9]{2}\.|[0-9]{1,2}\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\]?$)/i);
    return pattern.test(emailAddress);
}

function isValidPassword(password) {
    var pattern = new RegExp(/^(("[\w-\s]+")|([\w-]+(?:\.[\w-]+)*)|("[\w-\s]+")([\w-]+(?:\.[\w-]+)*))(@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$)|(@\[?((25[0-5]\.|2[0-4][0-9]\.|1[0-9]{2}\.|[0-9]{1,2}\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\]?$)/i);
    return pattern.test(password);
}