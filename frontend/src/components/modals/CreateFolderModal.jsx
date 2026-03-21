import { useParams } from "react-router-dom"; 
import api from "../../api/axiosConfig";
import { useState } from "react";

const CreateFolderModal = () => {
    // If no id given -> id === undefined
    const { id } = useParams();
    
    const [data, setData] = useState(null);
    const [folderName, setFolderName] = useState("");

    const createFolder = async () => {
        try {
            let response;
            if (id === undefined) {
                // Sending folderName as the body
                response = await api.post("folders/createfolder", { name: folderName });
            } else {
                response = await api.post(`folders/createfolder/${id}`, { name: folderName });
            }
            setData(response.data);
        } catch (err) {
            console.error("Failed to create folder:", err);
        }
    }

}

export default CreateFolderModal;