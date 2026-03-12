import Folder from "./Folder";
import api from "../../api/axiosConfig";
import { useState, useEffect } from "react";

const Folders = () => {
  const [folder, setFolder] = useState();

  const getFolder = async () => {
    try {
      const response = await api.get("");
      setFolder(response);
    } catch (err) {
      console.log(err);
    }
  };

  return <Folder />;
};

export default Folders;
