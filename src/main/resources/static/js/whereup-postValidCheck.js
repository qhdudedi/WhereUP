document.getElementById('postForm').addEventListener('submit', function(event) {
    let title = document.getElementById('title').value;
    if (!title) {
        alert('제목을 입력하세요.');
        event.preventDefault(); // 폼 제출을 중단합니다.
    }
    let content = document.getElementById('content').value;
    if (!content) {
        alert('본문을 입력하세요.');
        event.preventDefault();
    }
});