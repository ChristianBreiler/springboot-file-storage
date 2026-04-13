import { FileText, FileImage, File as FileIcon, MoreVertical, Trash2, Undo2, Pencil } from "lucide-react";
import { useState, useMemo, useRef, useEffect } from "react";
import DeleteFileModal from "../modals/DeleteFileModal";
import RestoreFileModal from "../modals/RestoreFileModal";
import RenameFileModal from "../modals/RenameFileModal";
import { useDraggable } from "@dnd-kit/core";
import { CSS } from "@dnd-kit/utilities";
import { useTranslation } from "react-i18next";

const File = ({ uuid, originalFilename, size, filetype, isDeleted, cardLayout, onClick }) => {
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [isRestoreModalOpen, setIsRestoreModalOpen] = useState(false);
  const [isRenameOpen, setIsRenameOpen] = useState(false);
  const dropdownRef = useRef(null);
  const { t } = useTranslation();

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
    return name.split('.').slice(0, -1).join('.');
  }

  const iconConfig = useMemo(() => {
    const iconSize = cardLayout ? 32 : 20;
    switch (filetype) {
      case "PDF": return { icon: <FileText size={iconSize} />, color: "text-red-500", bg: "bg-red-50" };
      case "JPG":
      case "PNG": return { icon: <FileImage size={iconSize} />, color: "text-blue-500", bg: "bg-blue-50" };
      default: return { icon: <FileIcon size={iconSize} />, color: "text-slate-500", bg: "bg-slate-50" };
    }
  }, [filetype, cardLayout]);

  return (
    <>
      <div
        ref={setNodeRef}
        style={style}
        {...listeners}
        {...attributes}
        className={`group bg-white border transition-all relative touch-none ${isDragging ? "opacity-50 border-blue-500 shadow-2xl scale-105 cursor-grabbing" : "border-slate-100 hover:border-blue-200 hover:shadow-sm cursor-grab"
          } ${cardLayout
            ? "flex flex-col items-center justify-center p-6 rounded-2xl text-center"
            : "flex items-center justify-between p-3 rounded-xl mb-2"
          }`}
        onClick={() => onClick(uuid)}
      >
        <div className={`flex items-center min-w-0 ${cardLayout ? "flex-col gap-3" : "gap-4 flex-1"}`}>
          <div className={`${cardLayout ? "h-16 w-16" : "h-10 w-10"} flex shrink-0 items-center justify-center rounded-lg overflow-hidden ${iconConfig.bg} ${iconConfig.color}`}>
            {iconConfig.icon}
          </div>
          <div className={`flex flex-col min-w-0 ${cardLayout ? "items-center" : ""}`}>
            <span className={`text-sm font-semibold text-slate-700 truncate ${cardLayout ? "max-w-[140px]" : ""}`}>
              {originalFilename}
            </span>
            <div className="flex items-center gap-2 text-[11px] text-slate-400">
              <span className="uppercase font-bold">{filetype}</span>
              <span>•</span>
              <span>{formatSize(size)}</span>
            </div>
          </div>
        </div>
        <div className={`${cardLayout ? "absolute top-2 right-2" : "relative"}`} ref={dropdownRef} onClick={(e) => e.stopPropagation()}>
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
                <Trash2 size={14} /> {isDeleted ? t('deleteFileForever') : t('restoreFile')}
              </button>
              {!isDeleted && (
                <button
                  className="flex items-center gap-2 w-full px-4 py-2 text-sm text-slate-700 hover:bg-slate-50 transition-colors"
                  onClick={(e) => {
                    e.preventDefault(); e.stopPropagation();
                    setIsDropdownOpen(false); setIsRenameOpen(true);
                  }}
                >
                  <Pencil size={14} /> Rename
                </button>
              )}
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