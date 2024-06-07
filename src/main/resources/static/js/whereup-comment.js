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