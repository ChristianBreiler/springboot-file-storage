/**
 * Functions to open modals
 */
function openFolderModal() {
    const modal = new bootstrap.Modal(document.getElementById('folderModal'));
    modal.show();
}

function openFileModal() {
    const modal = new bootstrap.Modal(document.getElementById('fileModal'));
    modal.show();
}

function renameFolderModal(button) {
    const folderId = button.getAttribute('data-folder-id');
    const folderName = button.getAttribute('data-folder-name');

    document.getElementById('renameFolderInput').value = folderName;
    document.getElementById('renameFolderForm')
        .setAttribute('action', `/folder/renameFolder/${folderId}`);

    const modal = new bootstrap.Modal(document.getElementById('renameFolderModal'));
    modal.show();
}

function renameFileModal(button) {
    const fileId = button.getAttribute('data-file-id');

    document.getElementById('renameFileForm')
        .setAttribute('action', `/folder/renameFile/${fileId}`);

    const modal = new bootstrap.Modal(document.getElementById('renameFileModal'));
    modal.show();
}

function openShareFileModal(button) {
    const fileShareLink = button.getAttribute('data-file-share-link');

    const modalElement = document.getElementById('shareFileModal');
    const linkSpan = modalElement.querySelector('#fileShareLink');

    linkSpan.textContent = fileShareLink;

    const modal = new bootstrap.Modal(modalElement);
    modal.show();
}


function confirmDeleteFile(button) {
    const fileId = button.getAttribute('data-file-id');
    const fileName = button.getAttribute('data-file-name');

    document.getElementById('modalFileName').textContent = fileName;
    document.getElementById('confirmFileDeleteButton')
        .setAttribute('href', `/deleteFile/${fileId}`);

    const modal = new bootstrap.Modal(document.getElementById('deleteFileModal'));
    modal.show();
}

function confirmDeleteFolder(button) {
    const folderId = button.getAttribute('data-folder-id');
    const folderName = button.getAttribute('data-folder-name');

    document.getElementById('modalFolderName').textContent = folderName;
    document.getElementById('confirmFolderDeleteButton')
        .setAttribute('href', `/folder/deleteFolder/${folderId}`);

    const modal = new bootstrap.Modal(document.getElementById('deleteFolderModal'));
    modal.show();
}