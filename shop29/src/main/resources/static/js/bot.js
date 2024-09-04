document.querySelector('.chatbot-toggle-btn').addEventListener('click', function () {
    var botChat = document.querySelector('.chatbot-chatbox');
    if (botChat.style.display === 'none' || botChat.style.display === '') {
        botChat.style.display = 'block';
    } else {
        botChat.style.display = 'none';
    }
});

function handleUserInput(event) {
    event.preventDefault(); // 폼의 기본 동작(페이지 리로드)을 막습니다.
    const submitButton = document.querySelector(".chatbot-submit-btn");
    const inputField = document.querySelector(".chatbot-input");
    const userMessage = inputField.value.trim();
    const messagesContainer = document.querySelector(".chatbot-messages");
    const scrollElement = document.querySelector(".chatbot-chatbox");

    if (userMessage === "") return; // 빈 메시지는 처리하지 않음

    const userMessageDiv = document.createElement("div");
    userMessageDiv.classList.add("message", "user");
    userMessageDiv.innerHTML = `<strong>사용자</strong><p>${userMessage}</p>`;
    messagesContainer.appendChild(userMessageDiv);
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
    scrollElement.scrollTop = scrollElement.scrollHeight;

    // POST 요청을 위해 Fetch API를 사용합니다.
    fetch("/chatbot", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({message: userMessage}),
    })
        .then(response => response.json())
        .then(data => {
            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = data.message.trim();
            messagesContainer.appendChild(tempDiv);

            // 어시스턴트 메시지 추가 후에도 스크롤을 맨 아래로 이동시킵니다.
            scrollElement.scrollTop = scrollElement.scrollHeight;
        })
        .catch(error => {
            console.error("Error:", error);
        });

    // 입력 필드를 비웁니다.
    inputField.value = "";
}

// 버튼 클릭 시 메시지 처리 함수 호출
document.querySelector(".chatbot-submit-btn").addEventListener("click", handleUserInput);

document.querySelector(".chatbot-input").addEventListener("keyup", function (event) {
    if (event.key === "Enter") {
        event.preventDefault(); // 기본 동작 방지
        handleUserInput(event);
    }
});

function requestUserOrderHistory(event) {
    const messagesContainer = document.querySelector(".chatbot-messages");
    const scrollElement = document.querySelector(".chatbot-chatbox");
    const requestId = event.id;
    // POST 요청을 위해 Fetch API를 사용합니다.
    fetch(`/chatbot/order-history?id=${requestId}`, {
        method: "GET",
    })
        .then(response => response.json())
        .then(data => {
            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = data.message.trim();
            messagesContainer.appendChild(tempDiv);

            // 어시스턴트 메시지 추가 후에도 스크롤을 맨 아래로 이동시킵니다.
            scrollElement.scrollTop = scrollElement.scrollHeight;
        })
        .catch(error => {
            console.error("Error:", error);
        });
}

document.querySelector('.menu-btn').addEventListener('click', function () {
    // 1. chatbot-message 클래스의 자식 요소 모두 제거
    const chatbotMessage = document.querySelector('.chatbot-messages');
    const scrollElement = document.querySelector(".chatbot-chatbox");

    // 2. 새로운 HTML 구조 추가
    chatbotMessage.insertAdjacentHTML('beforeend', `
                <div class="select-option-container">
                    <div class="option-container">
                        <button onclick="requestProduct(this)" class="option-btn">상품 검색
                        </button>
                        <button onclick="" class="option-btn">회원 정보
                        </button>
                        <button onclick="requestUserAllHistory(this)" class="option-btn">주문 내역
                        </button>
                        <button onclick="requestUserRefundableList(this)" class="option-btn">환불 신청
                        </button>
                    </div>
                </div>
    `);
    scrollElement.scrollTop = scrollElement.scrollHeight;
});

function requestUserAllHistory(event) {
    const messagesContainer = document.querySelector(".chatbot-messages");
    const scrollElement = document.querySelector(".chatbot-chatbox");

    // POST 요청을 위해 Fetch API를 사용합니다.
    fetch("/chatbot", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({message: "주문내역"}),
    })
        .then(response => response.json())
        .then(data => {
            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = data.message.trim();
            messagesContainer.appendChild(tempDiv);

            // 어시스턴트 메시지 추가 후에도 스크롤을 맨 아래로 이동시킵니다.
            scrollElement.scrollTop = scrollElement.scrollHeight;
        })
        .catch(error => {
            console.error("Error:", error);
        });
}

function requestUserRefundableList(event) {
    const messagesContainer = document.querySelector(".chatbot-messages");
    const scrollElement = document.querySelector(".chatbot-chatbox");
    const requestId = event.id;

    // 기본 URL 설정
    let url = `/chatbot/refund`;

    // requestId가 존재하면 URL에 쿼리 파라미터로 추가
    if (requestId) {
        url += `?orderId=${encodeURIComponent(requestId)}`;
    }
    // POST 요청을 위해 Fetch API를 사용합니다.
    fetch(url, {
        method: "GET",
    })
        .then(response => response.json())
        .then(data => {
            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = data.message.trim();
            messagesContainer.appendChild(tempDiv);

            // 어시스턴트 메시지 추가 후에도 스크롤을 맨 아래로 이동시킵니다.
            scrollElement.scrollTop = scrollElement.scrollHeight;
        })
        .catch(error => {
            console.error("Error:", error);
        });
}


function buttonSelected(button) {
    // 클릭된 버튼에 active 클래스를 토글
    button.classList.toggle('active');
}

function refundRequest(button) {
    var orderId = button.getAttribute('order-id');
    var selectedItems = [];
    const activeParent = button.parentElement;
    const checkboxes = activeParent.querySelectorAll('.order-product-btn.active')

    checkboxes.forEach(function (checkbox) {
        var itemData = checkbox.value.split('|');
        selectedItems.push({
            productId: itemData[0],
            productOptionId: itemData[1],
            quantity: itemData[2]
        });
    });

    if (selectedItems.length === 0) {
        alert("환불 신청할 상품을 선택해 주세요.");
        return;
    }

    const userResponse = confirm("선택한 상품을 환불하시겠습니까?");

    if (!userResponse) {
        console.log("사용자가 아니오를 선택했습니다.");
        alert("환불 진행을 취소하셨습니다.")
        return
    }


    var refundData = {
        orderId: orderId,
        productItems: selectedItems
    };
    $.ajax({
        url: '/order/refund',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(refundData),
        success: function (response) {
            // 응답이 JSON 형식일 경우 그대로 사용하고, 문자열일 경우 파싱
            const res = typeof response === 'string' ? JSON.parse(response) : response;

            // 응답의 status 값이 success일 경우
            if (res.status === 'success') {
                checkboxes.forEach(function (checkbox) {
                    const div = document.createElement('div');
                    div.className = 'order-product-container';
                    div.innerHTML = checkbox.innerHTML;
                    checkbox.parentNode.replaceChild(div, checkbox);
                });
                alert(res.message);
            } else {
                // status가 success가 아닌 경우 (옵션 처리)
                alert(res.message);
            }
        },
    });
}
function requestProductInput(el, event) {
    const code = event.code;
    console.log(event)
    if (code === 'Enter'){
        requestProduct(el)
    }
}

function requestProduct(event) {
    const messagesContainer = document.querySelector(".chatbot-messages");
    const scrollElement = document.querySelector(".chatbot-chatbox");
    const productId = event.value;

    // 기본 URL 설정
    let url = `/chatbot/product`;

    // requestId가 존재하면 URL에 쿼리 파라미터로 추가
    if (productId) {
        url += `?productId=${encodeURIComponent(productId)}`;
    }
    // POST 요청을 위해 Fetch API를 사용합니다.
    fetch(url, {
        method: "GET",
    })
        .then(response => response.json())
        .then(data => {
            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = data.message.trim();
            messagesContainer.appendChild(tempDiv);

            // 어시스턴트 메시지 추가 후에도 스크롤을 맨 아래로 이동시킵니다.
            scrollElement.scrollTop = scrollElement.scrollHeight;
        })
        .catch(error => {
            console.error("Error:", error);
        });
}

function goToProductPage(event) {
    window.location.href = `/product/${event}`;
}