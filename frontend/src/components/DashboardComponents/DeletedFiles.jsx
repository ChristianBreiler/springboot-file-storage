import { useState, useEffect } from "react";
import api from "../../api/axiosConfig";
import File from "./File";

const DeletedFiles = () => {

  const [loading, setLoading] = useState(true);
  const [data, setData] = useState(null);
  const [selectedId, setSelectedId] = useState(null);

  useEffect(() => {
    const fetchFolderData = async () => {
      setLoading(true);
      try {
        const response = await api.get(`deleted_files`);
        setData(response.data);
      } catch (err) {
        // TODO: Render Erro page here?
        console.error("Failed to fetch folders:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchFolderData();
  }, []);


  if (loading) return <div className="p-8 text-slate-500 font-medium animate-pulse">Loading storage...</div>;
  if (!data) return <div className="p-8 text-red-500">Error while loading deleted Folders</div>;

  const filesToRender = Array.isArray(data) ? data : (data?.files || []);

  return (
    <div className="p-6">
      {filesToRender.length > 0 ? (
        <>
          <h2 className="text-sm font-bold text-slate-400 uppercase tracking-wider mb-4">Files</h2>
          <div className="grid grid-cols-1 gap-2">
            {filesToRender.map((file) => (
              <File
                key={file.uuid}
                uuid={file.uuid}
                originalFilename={file.originalFilename}
                size={file.size}
                filetype={file.filetype}
                isDeleted={file.isDeleted}
                onClick={(uuid) => setSelectedId(uuid)}
              />
            ))}
          </div>
        </>
      ) : (
        <div className="text-center py-20 bg-slate-50 rounded-2xl border-2 border-dashed border-slate-200">
          <p className="text-slate-400 font-medium">No deleted files found</p>
        </div>
      )}
    </div>
  );
};
export default DeletedFiles;