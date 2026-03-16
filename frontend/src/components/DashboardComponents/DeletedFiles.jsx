import { useState, useEffect } from "react";

const DeletedFiles = () => {

    const [data, setData] = useState(null);

    useEffect(() => {
        const fetchFolderData = async () => {
          setLoading(true); 
          try {
            const response = await api.get(`/deleted_files`);
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
    if (!data) return <div className="p-8 text-red-500">Folder not found.</div>;

    return (
    <div className="p-6">
      <nav className="flex items-center space-x-1 text-sm font-medium text-slate-500 mb-6 overflow-x-auto whitespace-nowrap pb-2">
        <Link to="/folders/home" className="flex items-center gap-1.5 hover:text-blue-600 transition-colors">
          <Home size={16} />
        </Link>
        {data.name && (
          <div className="flex items-center">
            <ChevronRight size={14} className="mx-1 text-slate-400 flex-shrink-0" />
            <span className="text-slate-900 font-semibold px-2 py-1 truncate max-w-[200px]">
              Deleted Files
            </span>
          </div>
        )}
      </nav>
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
    </div>
  );
}

export default DeletedFiles;