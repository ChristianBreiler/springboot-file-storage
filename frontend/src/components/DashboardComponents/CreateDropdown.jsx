import { Plus, FolderPlus, Upload } from "lucide-react";
import { useState } from "react";
import CreateFolderModal from "../modals/CreateFolderModal";
import UploadFileModal from "../modals/UploadFileModal";

function CreateDropdown() {
  const [showFolder, setShowFolder] = useState(false);
  const [showFile, setShowFile] = useState(false);

  return (
    <>
      <div className="relative hidden md:block mr-2 group">
        <button className="flex items-center gap-2 bg-slate-900 hover:bg-slate-800 text-white px-4 py-2 rounded-xl transition-all active:scale-95 shadow-lg">
          <Plus size={18} />
          <span className="text-sm font-semibold tracking-tight">Create New</span>
        </button>
        <div className="absolute right-0 mt-2 w-48 bg-slate-900 border border-slate-800 rounded-xl shadow-xl overflow-hidden 
                        opacity-0 invisible group-hover:opacity-100 group-hover:visible 
                        transition-all duration-200 z-40">
          <button
            className="flex items-center gap-2 w-full px-4 py-2 text-sm text-slate-200 hover:bg-slate-800"
            onClick={() => setShowFolder(true)}
          >
            <FolderPlus size={16} />
            New Folder
          </button>
          <button
            className="flex items-center gap-2 w-full px-4 py-2 text-sm text-slate-200 hover:bg-slate-800"
            onClick={() => setShowFile(true)}
          >
            <Upload size={16} />
            Upload File
          </button>
        </div>
      </div>
      <CreateFolderModal
        isOpen={showFolder}
        onClose={() => setShowFolder(false)}
      />
      {showFile && (
        <UploadFileModal
          isOpen={showFile}
          onClose={() => setShowFile(false)}
        />
      )}
    </>
  );
}

export default CreateDropdown;