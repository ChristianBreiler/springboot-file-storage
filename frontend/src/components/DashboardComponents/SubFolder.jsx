import api from "../../api/axiosConfig";
import { ChevronRight, Home } from "lucide-react";
import { useState, useEffect } from "react";
import { Link, useParams } from "react-router-dom";
import Folder from "./Folder";
import File from "./File";
import FileViewPage from "./FileViewPage";
import LoadingBar from "../loading/LoadingBar";

// Represents the view of a specific Folder with all its Files and other subfolders
const SubFolder = () => {
  const { uuid } = useParams();
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [selectedId, setSelectedId] = useState(null);

  useEffect(() => {
    const fetchFolderData = async () => {
      setLoading(true);
      try {
        const path = uuid ? `folders/${uuid}` : "folders/home";
        const response = await api.get(path);
        setData(response.data);
      } catch (err) {
        console.error("Failed to fetch folders:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchFolderData();
  }, [uuid]);

  if (loading) return <LoadingBar />;
  if (!data) return <div className="p-8 text-red-500">Folder not found.</div>;

  return (
    <div className="p-6">
      <nav className="flex items-center space-x-1 text-sm font-medium text-slate-500 mb-6 overflow-x-auto whitespace-nowrap pb-2">
        <Link to="/" className="flex items-center gap-1.5 hover:text-blue-600 transition-colors">
          <Home size={16} />
        </Link>
        {data.parentFolders?.map((folder) => (
          <div key={folder.uuid} className="flex items-center">
            <ChevronRight size={14} className="mx-1 text-slate-400 shrink-0" />
            <Link
              to={`/folders/${folder.uuid}`}
              className="hover:text-blue-600 hover:bg-blue-50 px-2 py-1 rounded-md transition-all max-w-30 truncate"
            >
              {folder.name}
            </Link>
          </div>
        ))}
        {data.name && (
          <div className="flex items-center">
            <ChevronRight size={14} className="mx-1 text-slate-400 shrink-0" />
            <span className="text-slate-900 font-semibold px-2 py-1 truncate max-w-50">
              {data.name}
            </span>
          </div>
        )}
      </nav>
      {data.folders?.length > 0 && (
        <>
          <h2 className="text-sm font-bold text-slate-400 uppercase tracking-wider mb-4">Folders</h2>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-10">
            {data.folders.map((folder) => (
              <div draggable>
                <Folder
                  key={folder.uuid}
                  uuid={folder.uuid}
                  name={folder.name}
                />
              </div>
            ))}
          </div>
        </>
      )}
      {data.files?.length > 0 && (
        <>
          <h2 className="text-sm font-bold text-slate-400 uppercase tracking-wider mb-4">Files</h2>
          <div className="grid grid-cols-1 gap-2">
            {data.files.map((file) => (
              <div draggable>
                <File
                  key={file.uuid}
                  uuid={file.uuid}
                  originalFilename={file.originalFilename}
                  size={file.size}
                  filetype={file.filetype}
                  isDeleted={file.isDeleted}
                  onClick={(uuid) => setSelectedId(uuid)}
                />
              </div>
            ))}
          </div>
        </>
      )}
      {data.folders?.length === 0 && data.files?.length === 0 && (
        <div className="text-center py-20 bg-slate-50 rounded-2xl border-2 border-dashed border-slate-200">
          <p className="text-slate-400 font-medium">This Folder is Empty</p>
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
};

export default SubFolder;