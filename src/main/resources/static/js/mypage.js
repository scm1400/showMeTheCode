// ========================================
// 요청받은 코드리뷰 목록 조회
// ========================================
function ReceivedRequestList() {

    $.ajax({
        type: "GET",
        url: `/uesr/received`,
        success: function (res) {
            let reviews = res['reviews']
            if (reviews.length > 0) {
                for (let i = 0; i < reviews.length; i++) {
                    let tmp_html = `<li class="question-container">
                                    <a href="/questions/4">
                                        <div class="question-list__question  e-detail">
                                            <div class="question__info">
                                                <div class="question__info-cover">
                                                    <div class="question__info--main">
                                                        <div class="question__title">
                                                            <h3 class="title__text">
                                                                <span>코드리뷰 부탁드립니다.</span>
                                                                <span class="infd-icon icon-box-24"><svg width="24" height="24"  width="512" height="512" viewBox="0 0 512 512" xmlns="http://www.w3.org/2000/svg"><path fill="#00C471" clip-rule="evenodd" d="M464 64H48C21.49 64 0 85.49 0 112v288c0 26.51 21.49 48 48 48h416c26.51 0 48-21.49 48-48V112c0-26.51-21.49-48-48-48zm16 336c0 8.822-7.178 16-16 16H48c-8.822 0-16-7.178-16-16V112c0-8.822 7.178-16 16-16h416c8.822 0 16 7.178 16 16v288zM112 232c30.928 0 56-25.072 56-56s-25.072-56-56-56-56 25.072-56 56 25.072 56 56 56zm0-80c13.234 0 24 10.766 24 24s-10.766 24-24 24-24-10.766-24-24 10.766-24 24-24zm207.029 23.029L224 270.059l-31.029-31.029c-9.373-9.373-24.569-9.373-33.941 0l-88 88A23.998 23.998 0 0 0 64 344v28c0 6.627 5.373 12 12 12h360c6.627 0 12-5.373 12-12v-92c0-6.365-2.529-12.47-7.029-16.971l-88-88c-9.373-9.372-24.569-9.372-33.942 0zM416 352H96v-4.686l80-80 48 48 112-112 80 80V352z"  fill-rule="evenodd"/></svg></span>
                                                            </h3>
                                                        </div>
                                                        <p class="question__body">
                                                            제가 작성한 코드가 어떤지 봐주세요.

                                                        </p>

                                                        <div class="question__tags question__tags--exist">

                                                            <button class="ac-button is-sm is-solid is-gray  ac-tag ac-tag--blue "   ><span class="ac-tag__hashtag">#&nbsp;</span><span class="ac-tag__name">spring</span></button>
                                                        </div>

                                                    </div>
                                                    <div class="question__info-footer">
                                                        <div class="footer__cover">
                                                            <span class="footer__name">아이디</span>
                                                            <span class="footer__dot"> ·</span>
                                                            <span class="footer__info">1시간 전 · #Java</span>
                                                        </div>

                                                        <div class="footer__additional-info">

                                                            <div class="additional-info ">
                                                                <span class="additional-info__icon"><span class="infd-icon icon-box-16"><svg width="16" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16"><path fill="#616568"  fill-rule="evenodd" d="M2.167 2.667c-.092 0-.167.074-.167.166v8.334c0 .092.075.166.167.166h1.666c.276 0 .5.224.5.5v2.127l2.48-2.48c.094-.094.221-.147.354-.147h6.666c.092 0 .167-.074.167-.166V2.833c0-.092-.075-.166-.167-.166H2.167zM1 2.833c0-.644.522-1.166 1.167-1.166h11.666c.645 0 1.167.522 1.167 1.166v8.334c0 .644-.522 1.166-1.167 1.166h-6.46l-2.381 2.383c-.183.182-.43.284-.687.284-.537 0-.972-.435-.972-.971v-1.696H2.167c-.645 0-1.167-.522-1.167-1.166V2.833z" clip-rule="evenodd"/></svg></span></span>
                                                                <span class="additional-info__count additional-info__count--cmt">1</span>
                                                            </div>

                                                            <div class="additional-info ">
                                                                <span class="additional-info__icon"><span class="infd-icon icon-box-16"><svg width="16" width="16" height="16" viewBox="0 0 16 16"  xmlns="http://www.w3.org/2000/svg"><path fill="#616568" fill-rule="evenodd" clip-rule="evenodd" d="M4.49095 2.66666C3.10493 2.66666 1.66663 3.92028 1.66663 5.67567C1.66663 7.74725 3.21569 9.64919 4.90742 11.0894C5.73796 11.7965 6.571 12.3653 7.19759 12.7576C7.51037 12.9534 7.7704 13.1045 7.95123 13.2061C7.96818 13.2156 7.98443 13.2247 7.99996 13.2333C8.01549 13.2247 8.03174 13.2156 8.04869 13.2061C8.22952 13.1045 8.48955 12.9534 8.80233 12.7576C9.42892 12.3653 10.262 11.7965 11.0925 11.0894C12.7842 9.64919 14.3333 7.74725 14.3333 5.67567C14.3333 3.92028 12.895 2.66666 11.509 2.66666C10.1054 2.66666 8.9751 3.59266 8.4743 5.09505C8.40624 5.29922 8.21518 5.43693 7.99996 5.43693C7.78474 5.43693 7.59368 5.29922 7.52562 5.09505C7.02482 3.59266 5.89453 2.66666 4.49095 2.66666ZM7.99996 13.8018L8.22836 14.2466C8.08499 14.3202 7.91493 14.3202 7.77156 14.2466L7.99996 13.8018ZM0.666626 5.67567C0.666626 3.368 2.55265 1.66666 4.49095 1.66666C6.01983 1.66666 7.25381 2.48414 7.99996 3.73655C8.74611 2.48414 9.98009 1.66666 11.509 1.66666C13.4473 1.66666 15.3333 3.368 15.3333 5.67567C15.3333 8.22121 13.4657 10.3823 11.7407 11.8509C10.863 12.5982 9.98767 13.1953 9.33301 13.6052C9.00516 13.8104 8.73133 13.9696 8.53847 14.0779C8.44201 14.1321 8.36571 14.1737 8.31292 14.2019C8.28653 14.2161 8.26601 14.2269 8.25177 14.2344L8.2352 14.2431L8.23054 14.2455L8.22914 14.2462C8.22897 14.2463 8.22836 14.2466 7.99996 13.8018C7.77156 14.2466 7.77173 14.2467 7.77156 14.2466L7.76938 14.2455L7.76472 14.2431L7.74815 14.2344C7.73391 14.2269 7.71339 14.2161 7.687 14.2019C7.63421 14.1737 7.55791 14.1321 7.46145 14.0779C7.26858 13.9696 6.99476 13.8104 6.66691 13.6052C6.01225 13.1953 5.13695 12.5982 4.25917 11.8509C2.53423 10.3823 0.666626 8.22121 0.666626 5.67567Z" /></svg></span></span>
                                                                <span class="additional-info__count ">0</span>
                                                            </div>

                                                            <div class="additional-info ">
                                                                <span class="additional-info__icon"><span class="infd-icon icon-box-16"><svg width="16" xmlns="http://www.w3.org/2000/svg" width="16" height="16"  viewBox="0 0 384 512"><path fill="#616568"  d="M336 0H48C21.49 0 0 21.49 0 48v464l192-112 192 112V48c0-26.51-21.49-48-48-48zm16 456.287l-160-93.333-160 93.333V48c0-8.822 7.178-16 16-16h288c8.822 0 16 7.178 16 16v408.287z"></path></svg></span></span>
                                                                <span class="additional-info__count ">0</span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                            </div>
                                        </div>
                                    </a>
                                </li>`
                    $('#received-list').append(tmp_html);
                }
            } else {
                let tmp_html = `<p>조회된 결과가 없습니다.</p>`
                $('#received-list').append(tmp_html);
            }
        }
    })
}