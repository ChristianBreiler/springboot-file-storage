import { FileText, FileImage, File as FileIcon, MoreVertical, Trash2, Undo2, Pencil } from "lucide-react";
import { useState, useMemo, useRef, useEffect } from "react";
import DeleteFileModal from "../modals/DeleteFileModal";
import RestoreFileModal from "../modals/RestoreFileModal";
import RenameFileModal from "../modals/RenameFileModal"
import { useDraggable } from "@dnd-kit/core";
import { CSS } from "@dnd-kit/utilities";

const File = ({ uuid, originalFilename, size, filetype, isDeleted, onClick }) => {
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [isRestoreModalOpen, setIsRestoreModalOpen] = useState(false);
  const [isRenameOpen, setIsRenameOpen] = useState(false);
  const dropdownRef = useRef(null);

  const { attributes, listeners, setNodeRef, transform, isDragging } = useDraggable({
    id: uuid,
  });

  const style = {
    transform: CSS.Translate.toString(transform),
    zIndex: isDragging ? 100 : undefined,
  };

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

  const trimFileName = (name) => {
    // Slice off the file extension here so users cant accidentally change the filetype or break the file entirely
    return name.split('.').slice(0, -1).join('.');
  }

  const iconConfig = useMemo(() => {
    switch (filetype) {
      case "PDF": return { icon: <FileText size={20} />, color: "text-red-500", bg: "bg-red-50" };
      case "JPG":
      case "PNG": return { icon: <FileImage size={20} />, color: "text-blue-500", bg: "bg-blue-50" };
      default: return { icon: <FileIcon size={20} />, color: "text-slate-500", bg: "bg-slate-50" };
    }
  }, [filetype]);

  return (
    <>
      <div
        ref={setNodeRef}
        style={style}
        {...listeners}
        {...attributes}
        className={`group flex items-center justify-between p-3 bg-white border rounded-xl transition-all mb-2 relative touch-none ${isDragging ? "opacity-50 border-blue-500 shadow-2xl scale-105 cursor-grabbing" : "border-slate-100 hover:border-blue-200 hover:shadow-sm cursor-grab"
          }`}
        onClick={() => onClick(uuid)}
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
            onClick={(e) => {
              e.stopPropagation();
              setIsDropdownOpen(!isDropdownOpen);
            }}
            className="p-2 text-slate-400 hover:text-slate-600 hover:bg-slate-50 rounded-lg transition-colors"
          >
            <MoreVertical size={16} />
          </button>
          {isDropdownOpen && (
            <div className="absolute right-0 mt-2 w-48 bg-white border border-slate-100 rounded-xl shadow-xl z-50 py-1">
              <button
                onClick={() => { setIsDeleteModalOpen(true); setIsDropdownOpen(false); }}
                className="w-full flex items-center gap-2 px-4 py-2 text-sm text-red-600 hover:bg-red-50 transition-colors"
              >
                <Trash2 size={14} /> {isDeleted ? "Delete Forever" : "Delete File"}
              </button>
              {!isDeleted && (
                <button
                  className="flex items-center gap-2 w-full px-4 py-2.5 text-xs font-medium text-slate-200 hover:bg-slate-800"
                  onClick={(e) => {
                    e.preventDefault(); e.stopPropagation();
                    setIsDropdownOpen(false); setIsRenameOpen(true);
                  }}
                >
                  <Pencil size={14} /> Rename
                </button>
              )
              }
              {isDeleted && (
                <button
                  onClick={() => { setIsRestoreModalOpen(true); setIsDropdownOpen(false); }}
                  className="w-full flex items-center gap-2 px-4 py-2 text-sm text-slate-600 hover:bg-slate-50 transition-colors"
                >
                  <Undo2 size={14} /> Restore File
                </button>
              )}
            </div>
          )}
        </div>
      </div>
      <DeleteFileModal isOpen={isDeleteModalOpen} onClose={() => setIsDeleteModalOpen(false)} originalFilename={originalFilename} fileUuid={uuid} isDeleted={isDeleted} />
      {isDeleted && <RestoreFileModal isOpen={isRestoreModalOpen} onClose={() => setIsRestoreModalOpen(false)} originalFilename={originalFilename} uuid={uuid} />}
      {isRenameOpen && <RenameFileModal isOpen={isRenameOpen} onClose={() => setIsRenameOpen(false)} currentFileName={trimFileName(originalFilename)} fileUuid={uuid} filetype={filetype} />}
    </>
  );
};

export default File;