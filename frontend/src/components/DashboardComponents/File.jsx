import { FileText, FileImage, File as FileIcon, MoreVertical, Trash2 } from "lucide-react";
import { useState, useMemo, useRef, useEffect } from "react";
import DeleteFileModal from "../modals/DeleteFileModal";

const File = ({ id, originalFilename, size, filetype, isDeleted, onClick }) => {
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const dropdownRef = useRef(null);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) setIsDropdownOpen(false);
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const formatSize = (bytes) => {
    if (!bytes) return "0 Bytes";
    const k = 1024;
    const sizes = ["Bytes", "KB", "MB", "GB"];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(1)) + " " + sizes[i];
  };

  const iconConfig = useMemo(() => {
    switch (filetype) {
      case "PDF":
        return { icon: <FileText size={20} />, color: "text-red-500", bg: "bg-red-50" };
      case "JPG":
      case "PNG":
        return { icon: <FileImage size={20} />, color: "text-blue-500", bg: "bg-blue-50" };
      default:
        return { icon: <FileIcon size={20} />, color: "text-slate-500", bg: "bg-slate-50" };
    }
  }, [filetype]);

  return (
    <>
      <div
        className="group flex items-center justify-between p-3 bg-white border border-slate-100 rounded-xl hover:border-blue-200 hover:shadow-sm transition-all mb-2 cursor-pointer relative"
        onClick={() => onClick(id)}
      >
        <div className="flex items-center gap-4 flex-1 min-w-0">
          <div className={`flex h-10 w-10 shrink-0 items-center justify-center rounded-lg overflow-hidden ${iconConfig.bg} ${iconConfig.color}`}>
            {iconConfig.icon}
          </div>
          <div className="flex flex-col min-w-0">
            <span className="text-sm font-semibold text-slate-700 truncate">
              {originalFilename}
            </span>
            <div className="flex items-center gap-2 text-[11px] text-slate-400">
              <span className="uppercase font-bold">{filetype}</span>
              <span>•</span>
              <span>{formatSize(size)}</span>
            </div>
          </div>
        </div>
        <div className="relative" ref={dropdownRef} onClick={(e) => e.stopPropagation()}>
          <button
            onClick={() => setIsDropdownOpen(!isDropdownOpen)}
            className="p-2 text-slate-400 hover:text-slate-600 hover:bg-slate-50 rounded-lg group-hover:opacity-100 transition-opacity"
          >
            <MoreVertical size={16} />
          </button>
          {isDropdownOpen && (
            <div className="absolute right-0 mt-2 w-48 bg-white border border-slate-100 rounded-xl shadow-xl z-50 py-1 animate-in fade-in slide-in-from-top-2 duration-200">
              <button
                onClick={() => {
                  setIsDeleteModalOpen(true);
                  setIsDropdownOpen(false);
                }}
                className="w-full flex items-center gap-2 px-4 py-2 text-sm text-red-600 hover:bg-red-50 transition-colors"
              >
                <Trash2 size={14} />
                Delete File
              </button>
            </div>
          )}
        </div>
      </div>
      <DeleteFileModal
        isOpen={isDeleteModalOpen}
        onClose={() => setIsDeleteModalOpen(false)}
        originalFilename={originalFilename}
        fileId={id}
        isDeleted={isDeleted}
      />
    </>
  );
};

export default File;