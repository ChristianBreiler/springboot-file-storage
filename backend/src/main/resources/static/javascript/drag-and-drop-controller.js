const draggables = document.querySelectorAll('.draggable');
const containers = document.querySelectorAll('.container');

draggables.forEach(draggable => {
    draggable.addEventListener('dragstart', e => {
        const fileId = draggable.dataset.fileId;
        e.dataTransfer.setData("fileId", fileId);
        draggable.classList.add('dragging');
    })
    draggable.addEventListener('dragend', e => {
        draggable.classList.remove('dragging');
    })
})

containers.forEach(container => {
    container.addEventListener('dragover', e => {
        e.preventDefault();
        e.stopPropagation();
        const containerId = e.currentTarget.dataset.folderId;
        if (containerId === undefined) return
        console.log(containerId)
        const img = document.getElementById("folderImg-" + containerId);
        if (img !== null) img.src = "/images/folder_open.png";
    });
    container.addEventListener('drop', e => {
        e.preventDefault();
        e.stopPropagation();

        const containerId = e.currentTarget.dataset.folderId;
        if (containerId === undefined) return
        const fileId = e.dataTransfer.getData("fileId");
        const containerName = e.currentTarget.dataset.folderName;
        const fileName = e.currentTarget.dataset.fileName;
        console.log(fileId)
        console.log(containerId)
        console.log(containerName)
        console.log(fileName)
        // TODO: Add move folder stuff
        if (fileId !== "") openDragModal(fileId, containerId, containerName, fileName);
        else closeFileImage(containerId);
    });
    container.addEventListener('dragleave', e => {
        e.preventDefault();
        e.stopPropagation();
        const containerId = e.currentTarget.dataset.folderId;
        if (containerId === undefined) return
        closeFileImage(containerId);
    })
})

// Modal to confirm a drop
function openDragModal(fileId, containerId, containerName, fileName) {
    closeFileImage(containerId);
    document.getElementById('dragFileName').textContent = fileName;
    document.getElementById('dragFolderName').textContent = containerName;
    const homeId = -1
    console.log("Test")
    document.getElementById('confirmFileDragButton')
        .onclick = () => {
        fetch('/move-file', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({fileId, containerId})
        })
            .then(res => {
                if (!res.ok) throw new Error(res.statusText);
                return res.text()
            })
            .then(data => console.log("Server response:", data))
            .then(() => window.location.reload())
            .catch(err => console.error(err));
    }
    const modal = new bootstrap.Modal(document.getElementById('dragFileModal'));
    modal.show();
    console.log("Test2")
}

// Close the folder image so it doesn't stay open after the drop
function closeFileImage(containerId) {
    const img = document.getElementById("folderImg-" + containerId);
    if (img != null) img.src = "/images/folder_closed.png";
}