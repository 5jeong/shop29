document.querySelector('.btn-bot').addEventListener('click', function () {
    var botChat = document.querySelector('.bot-chat');
    if (botChat.style.display === 'none' || botChat.style.display === '') {
        botChat.style.display = 'block';
    } else {
        botChat.style.display = 'none';
    }
});
