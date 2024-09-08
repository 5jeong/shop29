document.querySelector('.chatbot-toggle-btn').addEventListener('click', function () {
    var botChat = document.querySelector('.chatbot-chatbox');
    if (botChat.style.display === 'none' || botChat.style.display === '') {
        botChat.style.display = 'block';
    } else {
        botChat.style.display = 'none';
    }
});

function handleUserInput(event) {
    event.preventDefault();
    const submitButton = document.querySelector(".chatbot-submit-btn");
    const inputField = document.querySelector(".chatbot-input");
    const userMessage = inputField.value.trim();
    const messagesContainer = document.querySelector(".chatbot-messages");
    const scrollElement = document.querySelector(".chatbot-chatbox");

    if (userMessage === "") return;

    const userMessageDiv = document.createElement("div");
    userMessageDiv.classList.add("message", "user");
    userMessageDiv.innerHTML = `<strong>사용자</strong><p>${userMessage}</p>`;
    messagesContainer.appendChild(userMessageDiv);

    messagesContainer.scrollTo({
        top: messagesContainer.scrollHeight,
        behavior: 'smooth'
    });

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

            messagesContainer.scrollTo({
                top: messagesContainer.scrollHeight,
                behavior: 'smooth'
            });
        })
        .catch(error => {
            console.error("Error:", error);
        });

    inputField.value = "";
}

document.querySelector(".chatbot-submit-btn").addEventListener("click", handleUserInput);

document.querySelector(".chatbot-input").addEventListener("keyup", function (event) {
    if (event.key === "Enter") {
        event.preventDefault();
        handleUserInput(event);
    }
});

function requestUserOrderHistory(event) {
    const messagesContainer = document.querySelector(".chatbot-messages");
    const scrollElement = document.querySelector(".chatbot-chatbox");
    const requestId = event.id;
    fetch(`/chatbot/order-history?id=${requestId}`, {
        method: "GET",
    })
        .then(response => response.json())
        .then(data => {
            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = data.message.trim();
            messagesContainer.appendChild(tempDiv);

            messagesContainer.scrollTo({
                top: messagesContainer.scrollHeight,
                behavior: 'smooth'
            });
        })
        .catch(error => {
            console.error("Error:", error);
        });
}

document.querySelector('.menu-btn').addEventListener('click', function () {
    const chatbotMessage = document.querySelector('.chatbot-messages');
    const scrollElement = document.querySelector(".chatbot-chatbox");

    chatbotMessage.insertAdjacentHTML('beforeend', `
                <div class="select-option-container">
                    <div class="option-container">
                        <button onclick="requestProduct(this)" class="option-btn">상품 검색
                        </button>
                        <button onclick="requestQnaList(this)" class="option-btn">문의 내역
                        </button>
                        <button onclick="requestUserAllHistory(this)" class="option-btn">주문 내역
                        </button>
                        <button onclick="requestUserRefundableList(this)" class="option-btn">환불 신청
                        </button>
                    </div>
                </div>
    `);

    chatbotMessage.scrollTo({
        top: chatbotMessage.scrollHeight,
        behavior: 'smooth'
    });
});

function requestUserAllHistory(event) {
    const messagesContainer = document.querySelector(".chatbot-messages");
    const scrollElement = document.querySelector(".chatbot-chatbox");

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

            messagesContainer.scrollTo({
                top: messagesContainer.scrollHeight,
                behavior: 'smooth'
            });
        })
        .catch(error => {
            console.error("Error:", error);
        });
}

function requestQnaList(event) {
    const messagesContainer = document.querySelector(".chatbot-messages");

    const tempDiv = document.createElement('div', {class: 'chatbot-message'});
    tempDiv.innerText = "1:1문의내역을 조회합니다."
    messagesContainer.appendChild(tempDiv);

    fetch("/chatbot", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({message: "최근 1:1문의내역 3건 조회"}),
    })
        .then(response => response.json())
        .then(data => {
            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = data.message.trim();
            messagesContainer.appendChild(tempDiv);

            messagesContainer.scrollTo({
                top: messagesContainer.scrollHeight,
                behavior: 'smooth'
            });
        })
        .catch(error => {
            console.error("Error:", error);
        });
}

function requestUserRefundableList(event) {
    const messagesContainer = document.querySelector(".chatbot-messages");
    const scrollElement = document.querySelector(".chatbot-chatbox");
    const requestId = event.id;

    let url = `/chatbot/refund`;

    if (requestId) {
        url += `?orderId=${encodeURIComponent(requestId)}`;
    }
    fetch(url, {
        method: "GET",
    })
        .then(response => response.json())
        .then(data => {
            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = data.message.trim();
            messagesContainer.appendChild(tempDiv);

            messagesContainer.scrollTo({
                top: messagesContainer.scrollHeight,
                behavior: 'smooth'
            });
        })
        .catch(error => {
            console.error("Error:", error);
        });
}


function buttonSelected(button) {
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
            const res = typeof response === 'string' ? JSON.parse(response) : response;

            if (res.status === 'success') {
                checkboxes.forEach(function (checkbox) {
                    const div = document.createElement('div');
                    div.className = 'order-product-container';
                    div.innerHTML = checkbox.innerHTML;
                    checkbox.parentNode.replaceChild(div, checkbox);
                });
                alert(res.message);
            } else {
                alert(res.message);
            }
        },
    });
}
function requestProductInput(el, event) {
    const code = event.code;
    if (code === 'Enter'){
        requestProduct(el)
    }
}

function requestProduct(event) {
    const messagesContainer = document.querySelector(".chatbot-messages");
    const productId = event.value;

    let url = `/chatbot/product`;

    if (productId) {
        url += `?productId=${encodeURIComponent(productId)}`;
    }
    fetch(url, {
        method: "GET",
    })
        .then(response => response.json())
        .then(data => {
            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = data.message.trim();
            messagesContainer.appendChild(tempDiv);

            messagesContainer.scrollTo({
                top: messagesContainer.scrollHeight,
                behavior: 'smooth'
            });
        })
        .catch(error => {
            console.error("Error:", error);
        });
}

function goToProductPage(event) {
    window.location.href = `/product/${event}`;
}

function scrollToTag(target) {
    target.scrollIntoView({ behavior: 'smooth' }); // 부드러운 스크롤
}