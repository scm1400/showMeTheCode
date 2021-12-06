function go_back() {
    history.go(-1);
}

let subscribeUrl = "http://localhost:8080/sub";

$(document).ready(function () {

    getQuestionList();

    if (sessionStorage.getItem("mytoken") != null) {
        let token = sessionStorage.getItem("mytoken");
        let eventSource = new EventSource(subscribeUrl + "?token=" + token);

        eventSource.addEventListener("addComment", function (event) {
            let message = event.data;
            alert(message);
        })

        eventSource.addEventListener("error", function (event) {
            eventSource.close()
        })
    }
})

function getQuestionList() {
    $('#reviewQuestionList').empty();
    let currentPage = getParameterByName('page');
    if (currentPage == null) {
        currentPage = 1
    }
    $.ajax({
        type: "GET",
        url: "/questions",
        data: {
            page: currentPage
        },
        success: function (res) {
            let data = res['data']
            let pagination = `<nav class="pagination is-centered is-small" role="navagation" aria-label="pagination">`
            console.log(res)
            let totalpage = res.totalPage
            console.log(totalpage)
            if (totalpage == 1) {
                pagination += `
                                    <ul class="pagination-list" id="pagingList">
                                        <li>
                                            <a class="pagination-link is-current" href="?page=1" aria-label="1 페이지로 이동">
                                                1
                                            </a>
                                        </li>
                                    </ul>
                                </nav>
                            `
            } else {
                pagination += `<a class="pagination-next" href="?page=${currentPage + 1}">다음 페이지</a>
                                <ul class="pagination-list" id="pagingList">`

                for (let i = 1; i <= totalpage; i++) {
                    if (totalpage == 11) break;
                    if (currentPage == i) {
                        pagination += `
                                <li>
                                    <a class="pagination-link is-current" href="?page=${i}" aria-label="${i} 페이지로 이동">
                                        ${i}
                                    </a>
                                </li>
                                `
                    } else {
                        pagination += `
                                <li>
                                    <a class="pagination-link" href="?page=${i}" aria-label="${i} 페이지로 이동">
                                        ${i}
                                    </a>
                                </li>
                                `
                    }

                }
                pagination += `</ul></nav>`
            }
            $('#community-body').append(pagination)
            for (let i = 0; i < data.length; i++) {
                let li = `<li class="question-container"><a href="/details.html?id=${data[i].reviewRequestId}">
                                <div class="question">
                                    <div class="question__info">
                                        <div class="question__title">
    
                                            <h3 class="title__text">
                                                ${data[i].title}
                                                <span class="infd-icon title__icon">
                                                </span>
                                            </h3>
                                        </div>
                                        <p class="question__body">
                                            ${data[i].content}
                                        </p>
                                        <div class="question__tags">
    
                                        </div>
                                        <div class="question__info-footer">
                                            ${data[i].languageName} · ${data[i].createdAt}  · ${data[i].status} 
                                        </div>
                                    </div>
                                    <div class="question__additional-info">
                                        <div class="question__comment">
                                            <span class="comment__count">0</span>
                                            <span class="comment__description">답변</span>
                                        </div>
    
                                        <button class="ac-button is-md is-text question__like e-like">
                                            <svg width="16" xmlns="http://www.w3.org/2000/svg" width="16" height="16"
                                                 viewBox="0 0 16 16">
                                                <path fill="#616568"
                                                      d="M9.333 13.605c-.328.205-.602.365-.795.473-.102.057-.205.113-.308.168h-.002c-.143.074-.313.074-.456 0-.105-.054-.208-.11-.31-.168-.193-.108-.467-.268-.795-.473-.655-.41-1.53-1.007-2.408-1.754C2.534 10.382.667 8.22.667 5.676c0-2.308 1.886-4.01 3.824-4.01 1.529 0 2.763.818 3.509 2.07.746-1.252 1.98-2.07 3.509-2.07 1.938 0 3.824 1.702 3.824 4.01 0 2.545-1.867 4.706-3.592 6.175-.878.747-1.753 1.344-2.408 1.754z"/>
                                            </svg>
                                            0
                                        </button>
                                    </div>
                                </div>
                            </a></li>`
                $('#reviewQuestionList').append(li);
            } // end-for
        }
    })
}

// ========================================
// ajax 요청시 token이 있다면 헤더에 추가하도록 설정
// ========================================
$.ajaxPrefilter(function (options, originalOptions, jqXHR) {
    if (sessionStorage.getItem('mytoken') != null) {
        jqXHR.setRequestHeader('Authorization', 'Bearer ' + sessionStorage.getItem('mytoken'));
    }
});

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}