import api from "../../api/axiosConfig";
import { ChevronRight, Home } from "lucide-react";
import { useState, useEffect } from "react";
import { Link, useParams } from "react-router-dom"; 
import Folder from "./Folder"; 
import File from "./File";
import LoadingBar from "../loading/LoadingBar";

const SubFolder = () => {
  const { id } = useParams();
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchFolderData = async () => {
      setLoading(true); 
      try {
        const response = await api.get(`folders/${id}`);
        setData(response.data);
      } catch (err) {
        // TODO: Render Erro page here?
        console.error("Failed to fetch folders:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchFolderData();
  }, [id]); 

  if (loading) return <LoadingBar />
  if (!data) return <div className="p-8 text-red-500">Folder not found.</div>;

  return (
    <div className="p-6">
      <nav className="flex items-center space-x-1 text-sm font-medium text-slate-500 mb-6 overflow-x-auto whitespace-nowrap pb-2">
        <Link to="/folders/home" className="flex items-center gap-1.5 hover:text-blue-600 transition-colors">
          <Home size={16} />
        </Link>
        {data.parentFolders?.map((folder) => (
          <div key={folder.id} className="flex items-center">
            <ChevronRight size={14} className="mx-1 text-slate-400 flex-shrink-0" />
            <Link
              to={`/folders/${folder.id}`}
              className="hover:text-blue-600 hover:bg-blue-50 px-2 py-1 rounded-md transition-all max-w-[120px] truncate"
            >
              {folder.name}
            </Link>
          </div>
        ))}
        {data.name && (
          <div className="flex items-center">
            <ChevronRight size={14} className="mx-1 text-slate-400 flex-shrink-0" />
            <span className="text-slate-900 font-semibold px-2 py-1 truncate max-w-[200px]">
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
              <Folder key={folder.id} id={folder.id} name={folder.name} />
            ))}
          </div>
        </>
      )}
      {data.files?.length > 0 && (
        <>
          <h2 className="text-sm font-bold text-slate-400 uppercase tracking-wider mb-4">Files</h2>
          <div className="grid grid-cols-1 gap-2">
            {data.files.map((file) => (
              <File
                key={file.id}
                originalFilename={file.originalFilename}
                size={file.size}
                filetype={file.filetype}
              />
            ))}
          </div>
        </>
      )}
      {data.folders?.length === 0 && data.files?.length === 0 && (
        <div className="text-center py-20 bg-slate-50 rounded-2xl border-2 border-dashed border-slate-200">
          <p className="text-slate-400 font-medium">This Folder is Empty</p>
        </div>
      )}
    </div>
  );
};

export default SubFolder;