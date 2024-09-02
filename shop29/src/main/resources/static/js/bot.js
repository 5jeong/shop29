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

    if (userMessage === "") return; // 빈 메시지는 처리하지 않음

    const userMessageDiv = document.createElement("div");
    userMessageDiv.classList.add("message", "user");
    userMessageDiv.innerHTML = `<strong>사용자:</strong><p>${userMessage}</p>`;
    messagesContainer.appendChild(userMessageDiv);
    messagesContainer.scrollTop = messagesContainer.scrollHeight;

    // POST 요청을 위해 Fetch API를 사용합니다.
    fetch("/chatbot", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ message: userMessage }),
    })
        .then(response => response.json())
        .then(data => {
            // 서버 응답을 받아 어시스턴트의 메시지를 추가합니다.
            const assistantMessageDiv = document.createElement("div");
            assistantMessageDiv.classList.add("message", "assistant");
            const formattedMessage = data.message.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');

            assistantMessageDiv.innerHTML = `<strong>Assistant:</strong><p>${formattedMessage}</p>`;
            messagesContainer.appendChild(assistantMessageDiv);

            // 어시스턴트 메시지 추가 후에도 스크롤을 맨 아래로 이동시킵니다.
            messagesContainer.scrollTop = messagesContainer.scrollHeight;
        })
        .catch(error => {
            console.error("Error:", error);
        });

    // 입력 필드를 비웁니다.
    inputField.value = "";
}

// 버튼 클릭 시 메시지 처리 함수 호출
document.querySelector(".chatbot-submit-btn").addEventListener("click", handleUserInput);

document.querySelector(".chatbot-input").addEventListener("keyup", function(event) {
    if (event.key === "Enter") {
        event.preventDefault(); // 기본 동작 방지
        handleUserInput(event);
    }
});


