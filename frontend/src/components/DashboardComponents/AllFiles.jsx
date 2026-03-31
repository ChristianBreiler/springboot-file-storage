import api from "../../api/axiosConfig";
import { useState, useEffect } from "react";
import File from "./File";
import FileViewPage from "./FileViewPage";
import LoadingBar from "../loading/LoadingBar";

const AllFiles = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedId, setSelectedId] = useState(null);

  useEffect(() => {
    const fetchFolderData = async () => {
      setLoading(true);
      try {
        const response = await api.get(`files`);
        setData(response.data);
      } catch (err) {
        console.error("Failed to fetch files:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchFolderData();
  }, []);

  if (loading) return <LoadingBar />

  return (
    <div className="p-6">
      {data && data.length > 0 ? (
        <div className="grid grid-cols-1 gap-2">
          {data.map((file) => (
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
      ) : (
        <div className="text-center py-20 bg-slate-50 rounded-2xl border-2 border-dashed border-slate-200">
          <p className="text-slate-400 font-medium">No files found</p>
        </div>
      )}
      {selectedId && (
        <FileViewPage
          uuid={selectedId}
          onClose={() => setSelectedId(null)}
        />
      )}
    </div>
  );
}

export default AllFiles;