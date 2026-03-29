import api from "../../api/axiosConfig";
import { useState, useEffect } from "react";
import File from "./File";
import FileViewPage from "./FileViewPage";

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

  if (loading) return <div className="p-8 text-slate-500 font-medium animate-pulse">Loading files...</div>;

  return (
    <div className="p-6">
      {data && data.length > 0 ? (
        <div className="grid grid-cols-1 gap-2">
          {data.map((file) => (
            <File
              key={file.id}
              id={file.id}
              originalFilename={file.originalFilename}
              size={file.size}
              filetype={file.filetype}
              isDeleted={file.isDeleted}
              onClick={(id) => setSelectedId(id)}
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
          fileId={selectedId}
          onClose={() => setSelectedId(null)}
        />
      )}
    </div>
  );
}

export default AllFiles;