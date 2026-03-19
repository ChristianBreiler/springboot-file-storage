import { useParams } from "react-router-dom";

const UploadFileModal = () => {
    // If no id given -> id === undefined
    const { folderId } = useParams();
}

export default UploadFileModal;