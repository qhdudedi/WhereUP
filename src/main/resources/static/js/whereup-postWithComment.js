function editComment(commentId) {
    document.getElementById("add-form").style.display = "none";

    document.getElementById(commentId + '-btn-area').style.display = "none";
    document.getElementById(commentId + '-comment-area').style.display = "none";
    document.getElementById(commentId + '-edit-area').style.display = "block";

    let commentText = document.getElementById(commentId + '-text').innerText;
    document.getElementById(commentId + '-textarea').value = commentText;
}

function cancelComment(commentId) {
    document.getElementById("add-form").style.display = "block";

    document.getElementById(commentId + '-btn-area').style.display = "block";
    document.getElementById(commentId + '-comment-area').style.display = "block";
    document.getElementById(commentId + '-edit-area').style.display = "none";
}

document.getElementById("commentForm").onsubmit = function() {
    return validateComment('text');
}

function validateComment(textareaId) {
    let text = document.getElementById(textareaId).value.trim();
    if (!text) {
        alert('내용을 입력해주세요.');
        return false;
    }
    return true;
}

document.querySelectorAll('[id^="editForm-"]').forEach(function(form) {
    form.onsubmit = function(event) {
        let textareaId = this.querySelector('textarea').id;
        return validateComment(textareaId);
    };
});

function confirmDeletion() {
    return confirm('정말 삭제하시겠습니까?');
}
