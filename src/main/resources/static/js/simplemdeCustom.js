let simplemde = new SimpleMDE({
    element: document.getElementById("content"),
    toolbar: [
        "bold", "italic", "heading", "|", "heading-smaller", "heading-bigger", "|",
        "code", "quote", "ordered-list", "unordered-list", "horizontal-rule", "|",
        "link", "image",
        {
            name: "custom",
            action: function customFunction(editor){
                document.getElementById('imageUpload').click();
            },
            className: "fa fa-external-link-square",
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
        let formData = new FormData();
        formData.append('file', file);

        fetch('/post/upload', {
            method: 'POST',
            body: formData
        })
            .then(response => response.json())
            .then(data => {
                if(data.success){
                    let url = data.url;
                    let cm = simplemde.codemirror;
                    let output = '![](' + url + ')';
                    cm.replaceSelection(output);
                } else {
                    alert('Image upload failed');
                }
            })
            .catch(error => {
                console.error('Error uploading image:', error);
                alert('Image upload failed');
            });
    }
});