
$(document).ready(function () {
    let mytoken = sessionStorage.getItem("mytoken");
    let myAuthority = sessionStorage.getItem("myAuthority")

    console.log(mytoken, myAuthority)


    $('#mypage-menu-list').empty()
    if (mytoken != null && myAuthority != null) {
        if (myAuthority === "ROLE_USER") {
            let tmp_html = `<li id="menu-request">
                                    <a class="btn_wrap  " onclick="myRequestQuestionList()">
                                        <span>내가 요청한 코드리뷰 목록</span>
                                    </a>
                                </li>`
            $('#mypage-menu-list').append(tmp_html)

        } else if (myAuthority === "ROLE_REVIEWER") {
            let tmp_html = `<li id="menu-received">
                                    <a class="btn_wrap " onclick="myReceivedQuestionList()">
                                        <span>나에게 요청된 코드리뷰 목록</span>
                                    </a>
                                </li>
                                <li id="menu-request">
                                    <a class="btn_wrap  " onclick="myRequestQuestionList()">
                                        <span>내가 요청한 코드리뷰 목록</span>
                                    </a>
                                </li>`
            $('#mypage-menu-list').append(tmp_html);
        }
    }
})


// ========================================
// 요청받은 코드리뷰 목록 조회
// ========================================
function myReceivedQuestionList() {

    $.ajax({
        type: "GET",
        url: `/uesr/received`,
        success: function (res) {
            $('#question-list').empty()
            console.log(res);

            let reviews = res['data']
            addReviewList(reviews);
        }
    })
}

// ========================================
// 내가 요청한 코드리뷰 목록
// ========================================
function myRequestQuestionList() {

    $.ajax({
        type: "GET",
        url: "/user/requests",
        success: function (res) {
            $('#question-list').empty()
            console.log(res);

            let reviews = res['data']
            addReviewList(reviews);
        }
    })
}

function addReviewList(reviews) {
    if (reviews.length > 0) {
        for (let i = 0; i < reviews.length; i++) {
            let tmp_html = `<li class="question-container">
                                    <a onclick="showQuestionDetails('${reviews[i].reviewRequestId}')">
                                        <div class="question-list__question  e-detail">
                                            <div class="question__info">
                                                <div class="question__info-cover">
                                                    <div class="question__info--main">
                                                        <div class="question__title">
                                                            <h3 class="title__text">
                                                                <span>${reviews[i].title}</span>
                                                            </h3>
                                                        </div>
                                                        <p class="question__body">
                                                           ${reviews[i].content}
                                                        </p>


                                                    </div>
                                                    <div class="question__info-footer">
                                                        <div class="footer__cover">
                                                            <span class="footer__name">${reviews[i].username}</span>
                                                            <span class="footer__dot"> ·</span>
                                                            <span class="footer__info">${reviews[i].createdAt}</span>
                                                            <span class="footer__dot"> ·</span>
                                                            <span class="footer__info">${reviews[i].languageName}</span>
                                                        </div>

                                                        <div class="footer__additional-info">

                                                            <div class="additional-info ">
                                                                <span class="additional-info__icon"><span class="infd-icon icon-box-16"><svg width="16" width="16" height="16" viewBox="0 0 16 16"  xmlns="http://www.w3.org/2000/svg"><path fill="#616568" fill-rule="evenodd" clip-rule="evenodd" d="M4.49095 2.66666C3.10493 2.66666 1.66663 3.92028 1.66663 5.67567C1.66663 7.74725 3.21569 9.64919 4.90742 11.0894C5.73796 11.7965 6.571 12.3653 7.19759 12.7576C7.51037 12.9534 7.7704 13.1045 7.95123 13.2061C7.96818 13.2156 7.98443 13.2247 7.99996 13.2333C8.01549 13.2247 8.03174 13.2156 8.04869 13.2061C8.22952 13.1045 8.48955 12.9534 8.80233 12.7576C9.42892 12.3653 10.262 11.7965 11.0925 11.0894C12.7842 9.64919 14.3333 7.74725 14.3333 5.67567C14.3333 3.92028 12.895 2.66666 11.509 2.66666C10.1054 2.66666 8.9751 3.59266 8.4743 5.09505C8.40624 5.29922 8.21518 5.43693 7.99996 5.43693C7.78474 5.43693 7.59368 5.29922 7.52562 5.09505C7.02482 3.59266 5.89453 2.66666 4.49095 2.66666ZM7.99996 13.8018L8.22836 14.2466C8.08499 14.3202 7.91493 14.3202 7.77156 14.2466L7.99996 13.8018ZM0.666626 5.67567C0.666626 3.368 2.55265 1.66666 4.49095 1.66666C6.01983 1.66666 7.25381 2.48414 7.99996 3.73655C8.74611 2.48414 9.98009 1.66666 11.509 1.66666C13.4473 1.66666 15.3333 3.368 15.3333 5.67567C15.3333 8.22121 13.4657 10.3823 11.7407 11.8509C10.863 12.5982 9.98767 13.1953 9.33301 13.6052C9.00516 13.8104 8.73133 13.9696 8.53847 14.0779C8.44201 14.1321 8.36571 14.1737 8.31292 14.2019C8.28653 14.2161 8.26601 14.2269 8.25177 14.2344L8.2352 14.2431L8.23054 14.2455L8.22914 14.2462C8.22897 14.2463 8.22836 14.2466 7.99996 13.8018C7.77156 14.2466 7.77173 14.2467 7.77156 14.2466L7.76938 14.2455L7.76472 14.2431L7.74815 14.2344C7.73391 14.2269 7.71339 14.2161 7.687 14.2019C7.63421 14.1737 7.55791 14.1321 7.46145 14.0779C7.26858 13.9696 6.99476 13.8104 6.66691 13.6052C6.01225 13.1953 5.13695 12.5982 4.25917 11.8509C2.53423 10.3823 0.666626 8.22121 0.666626 5.67567Z" /></svg></span></span>
                                                                <span class="additional-info__count ">0</span>
                                                            </div>

                                                            <div class="additional-info ">
                                                                <span class="additional-info__icon"><span class="infd-icon icon-box-16"><svg width="16" xmlns="http://www.w3.org/2000/svg" width="16" height="16"  viewBox="0 0 384 512"><path fill="#616568"  d="M336 0H48C21.49 0 0 21.49 0 48v464l192-112 192 112V48c0-26.51-21.49-48-48-48zm16 456.287l-160-93.333-160 93.333V48c0-8.822 7.178-16 16-16h288c8.822 0 16 7.178 16 16v408.287z"></path></svg></span></span>
                                                                <span class="additional-info__count ">${reviews[i].commentCount}/span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                            </div>
                                        </div>
                                    </a>
                                </li>`
            $('#question-list').append(tmp_html);
        }
    } else {
        let tmp_html = `<p>조회된 결과가 없습니다.</p>`
        $('#received-list').append(tmp_html);
    }
}




function showQuestionDetails(id) {
    location.href = `details.html?id=${id}`
}

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}
