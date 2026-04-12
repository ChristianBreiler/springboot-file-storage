import { Link } from "react-router-dom";
import { Folder as FolderIcon, MoreVertical, Pencil, Trash2 } from "lucide-react";
import { useState, useEffect, useRef } from "react";
import RenameFolderModal from "../modals/RenameFolderModal";
import DeleteFolderModal from "../modals/DeleteFolderModal";
import { useDroppable } from "@dnd-kit/core";
import { useTranslation } from "react-i18next";

const Folder = ({ uuid, name = "New Folder" }) => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isRenameOpen, setIsRenameOpen] = useState(false);
  const [isDeleteOpen, setIsDeleteOpen] = useState(false);
  const menuRef = useRef(null);
  const { t } = useTranslation()

  const { setNodeRef, isOver } = useDroppable({
    id: uuid,
  });

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (menuRef.current && !menuRef.current.contains(event.target)) {
        setIsMenuOpen(false);
      }
    };
    if (isMenuOpen) document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [isMenuOpen]);

  return (
    <>
      <div
        ref={setNodeRef}
        className={`relative group/card rounded-2xl transition-all duration-200 ${isOver ? "ring-2 ring-blue-500 bg-blue-50/50 scale-105 z-10" : ""
          }`}
      >
        <Link
          to={`/folders/${uuid}`}
          className={`flex items-center justify-between p-4 bg-white border rounded-2xl 
                     hover:shadow-xl hover:shadow-blue-500/5 transition-all duration-200 active:scale-[0.98] ${isOver ? "border-blue-500" : "border-slate-200"
            }`}
        >
          <div className="flex items-center gap-4 min-w-0">
            <div className={`flex h-12 w-12 items-center justify-center rounded-xl transition-colors duration-200 ${isOver ? "bg-blue-600 text-white" : "bg-blue-50 text-blue-600 group-hover/card:bg-blue-600 group-hover/card:text-white"
              }`}>
              <FolderIcon size={24} fill="currentColor" fillOpacity={0.2} />
            </div>
            <div className="flex flex-col min-w-0">
              <span className={`text-sm font-semibold truncate transition-colors ${isOver ? "text-blue-700" : "text-slate-700 group-hover/card:text-blue-700"
                }`}>
                {name}
              </span>
            </div>
          </div>
          <div className="w-8" />
        </Link>
        <div className="absolute right-3 top-1/2 -translate-y-1/2" ref={menuRef}>
          <button
            onClick={(e) => {
              e.preventDefault();
              e.stopPropagation();
              setIsMenuOpen(!isMenuOpen);
            }}
            className={`p-1.5 rounded-lg transition-all duration-200 ${isMenuOpen
              ? "bg-slate-100 text-slate-600 opacity-100"
              : "text-slate-400 hover:bg-slate-100 hover:text-slate-600 md:opacity-0 group-hover/card:opacity-100"
              }`}
          >
            <MoreVertical size={18} />
          </button>
          {isMenuOpen && (
            <div className="absolute right-0 mt-2 w-36 bg-slate-900 border border-slate-800 rounded-xl shadow-xl overflow-hidden z-50">
              <button
                className="flex items-center gap-2 w-full px-4 py-2.5 text-xs font-medium text-slate-200 hover:bg-slate-800"
                onClick={(e) => {
                  e.preventDefault(); e.stopPropagation();
                  setIsMenuOpen(false); setIsRenameOpen(true);
                }}
              >
                <Pencil size={14} />{t('rename')}
              </button>
              <button
                className="flex items-center gap-2 w-full px-4 py-2.5 text-xs font-medium text-red-400 hover:bg-red-500/10"
                onClick={(e) => {
                  e.preventDefault(); e.stopPropagation();
                  setIsMenuOpen(false); setIsDeleteOpen(true);
                }}
              >
                <Trash2 size={14} />{t('delete')}
              </button>
            </div>
          )}
        </div>
      </div>

      {isRenameOpen && <RenameFolderModal isOpen={isRenameOpen} onClose={() => setIsRenameOpen(false)} currentFolderName={name} folderUuid={uuid} />}
      {isDeleteOpen && <DeleteFolderModal isOpen={isDeleteOpen} onClose={() => setIsDeleteOpen(false)} currentFolderName={name} folderUuid={uuid} />}
    </>
  );
};

export default Folder;