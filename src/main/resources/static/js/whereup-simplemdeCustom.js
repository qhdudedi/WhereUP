let simplemde = new SimpleMDE({
    spellChecker: false,
    element: document.getElementById("content"),
    toolbar: [
        "bold", "italic", "heading", "|", "heading-smaller", "heading-bigger", "|",
        "code", "quote", "ordered-list", "unordered-list", "horizontal-rule", "|",
        "link",
        {
            name: "image-upload",
            action: function customFunction(){
                document.getElementById('imageUpload').click();
            },
            className: "fa fa-picture-o",
            title: "Image Upload"
        },
        "|", "preview", "side-by-side", "fullscreen", "|",
        "guide"
    ]
});
document.getElementById('imageUpload').addEventListener('change', function(){
    let file = this.files[0];
    if(file){
        let validImageTypes = ['image/jpeg', 'image/png', 'image/gif','image/bmp',
            'image/webp', 'image/tiff', 'image/svg+xml', 'image/vnd.microsoft.icon', 'image/x-icon'];
        if (!validImageTypes.includes(file.type)) {
            alert('이미지 파일만 선택할 수 있습니다.');
            return;
        }
        fetch(`/presigned-url?key=${encodeURIComponent(file.name)}`)
            .then(response => response.json())
            .then(data => {
                if (data.url) {
                    fetch(data.url, {
                        method: 'PUT',
                        body: file,
                        headers: {
                            'Content-Type': file.type
                        }
                    })
                        .then(response => {
                            if (response.ok) {
                                let cm = simplemde.codemirror;
                                let output = `![](${data.url.split('?')[0]})`;
                                cm.replaceSelection(output);
                            } else {
                                alert('이미지 업로드 실패');
                            }
                        })
                        .catch(error => {
                            console.error('이미지 업로드 중 오류 발생:', error);
                            alert('이미지 업로드 실패');
                        });
                } else {
                    alert('Presigned URL 리턴 실패');
                }
            })
            .catch(error => {
                console.error('Presigned URL 요청 실패: ', error);
                alert('Presigned URL 요청 실패');
            });
    }
});