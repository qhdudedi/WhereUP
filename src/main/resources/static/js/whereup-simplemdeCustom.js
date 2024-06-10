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
        let newFileName = file.name.replaceAll("(", "_").replaceAll(")", "_").replaceAll(" ", "_");
        console.log(newFileName);
        let renamedFile = new File([file], newFileName, { type: file.type });
        let formData = new FormData();
        formData.append('file', renamedFile);

        fetch('/post/upload', {
            method: 'POST',
            body: formData
        })
            .then(response => response.json())
            .then(data => {
                if(data.success){
                    let cm = simplemde.codemirror;
                    let output = '![](' + data.url + ')';
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