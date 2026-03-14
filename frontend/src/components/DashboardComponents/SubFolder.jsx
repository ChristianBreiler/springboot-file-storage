import api from "../../api/axiosConfig";
import { useState, useEffect } from "react";

{/* Represents an entire subfolder with its folders and files */}

const SubFolder = () => {

  const { id } = useParams();

  const [folder, setFolder] = useState();

  const getFolder = async () => {
    try {
      const response = await api.get("folders/${id}");
      setFolder(response);
    } catch (err) {
      console.log(err);
    }
  };
};

export default SubFolder;
