import { useParams } from "react-router-dom"; 

const CreateFolderModal = () => {
    // If no id given -> id === undefined
    const { id } = useParams();
}

export default CreateFolderModal;