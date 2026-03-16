import { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import Folder from "./Folder";
import File from "./File";

const HomeFolder = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchFolderData = async () => {
      try {
        const response = await api.get("folders/home");
        setData(response.data); 
      } catch (err) {
        console.error("Failed to fetch folders:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchFolderData();
  }, []);

  // TODO: Dedicated Loading page
  if (loading) return <div className="p-8 text-slate-500">Loading</div>;

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold text-slate-800 mb-6">Folders</h1>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {data?.folders?.map((folder) => (
          <Folder 
            key={folder.id} 
            id={folder.id} 
            name={folder.name} 
          />
        ))}
      </div>
      <h1 className="text-2xl font-bold text-slate-800 mb-6">Files</h1>
      {data?.files?.map((file) => (
        <File
          key={file.id}
          originalFilename={file.originalFilename}
          size={file.size}
          filetype={file.filetype}
        />
      ))}
        {data?.folders?.length === 0 && data?.files?.length === 0 && (
        <div className="text-center py-20 bg-slate-50 rounded-2xl border-2 border-dashed border-slate-200">
          <p className="text-slate-400">This Folder is Empty</p>
        </div>
      )}
    </div>
  );
};

export default HomeFolder;