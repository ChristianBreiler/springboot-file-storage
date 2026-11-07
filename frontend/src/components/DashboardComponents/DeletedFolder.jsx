import {Folder as FolderIcon, MoreVertical, RotateCcw, Trash2} from "lucide-react";
import {useState, useEffect, useRef} from "react";
import RestoreFolderModal from "../modals/RestoreFolderModal";
import DeleteFolderModal from "../modals/DeleteFolderModal";
import {useTranslation} from "react-i18next";

// Same look as a normal folder, but it can't be opened, only restored or deleted permanently
const DeletedFolder = ({uuid, name, onRemoved}) => {
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const [isRestoreOpen, setIsRestoreOpen] = useState(false);
    const [isDeleteOpen, setIsDeleteOpen] = useState(false);
    const menuRef = useRef(null);
    const {t} = useTranslation();

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
            <div className={`relative group/card rounded-2xl ${isMenuOpen ? "z-40" : ""}`}>
                <div
                    className="flex items-center justify-between p-4 bg-white border border-slate-200 rounded-2xl cursor-default">
                    <div className="flex items-center gap-4 min-w-0">
                        <div className="flex h-12 w-12 items-center justify-center rounded-xl text-slate-400">
                            <FolderIcon size={24} fill="currentColor" fillOpacity={0.2}/>
                        </div>
                        <div className="flex flex-col min-w-0">
                            <span className="text-sm font-semibold truncate text-slate-500">{name}</span>
                        </div>
                    </div>
                    <div className="w-8"/>
                </div>
                <div className="absolute right-3 top-1/2 -translate-y-1/2" ref={menuRef}>
                    <button
                        onClick={() => setIsMenuOpen(!isMenuOpen)}
                        className={`p-1.5 rounded-lg transition-all duration-200 ${isMenuOpen
                            ? "bg-slate-100 text-slate-600 opacity-100"
                            : "text-slate-400 hover:bg-slate-100 hover:text-slate-600 md:opacity-0 group-hover/card:opacity-100"
                        }`}
                    >
                        <MoreVertical size={18}/>
                    </button>
                    {isMenuOpen && (
                        <div
                            className="absolute right-0 mt-2 w-48 bg-slate-900 border border-slate-800 rounded-xl shadow-xl overflow-hidden z-50">
                            <button
                                className="flex items-center gap-2 w-full px-4 py-2.5 text-xs font-medium text-slate-200 hover:bg-slate-800"
                                onClick={() => {
                                    setIsMenuOpen(false);
                                    setIsRestoreOpen(true);
                                }}
                            >
                                <RotateCcw size={14}/>{t('restore')}
                            </button>
                            <button
                                className="flex items-center gap-2 w-full px-4 py-2.5 text-xs font-medium text-red-400 hover:bg-red-500/10"
                                onClick={() => {
                                    setIsMenuOpen(false);
                                    setIsDeleteOpen(true);
                                }}
                            >
                                <Trash2 size={14}/>{t('deletePermanently')}
                            </button>
                        </div>
                    )}
                </div>
            </div>

            {isRestoreOpen &&
                <RestoreFolderModal isOpen={isRestoreOpen} onClose={() => setIsRestoreOpen(false)} folderName={name}
                                    folderUuid={uuid} onRestored={() => onRemoved?.(uuid)}/>}
            {isDeleteOpen &&
                <DeleteFolderModal isOpen={isDeleteOpen} onClose={() => setIsDeleteOpen(false)} currentFolderName={name}
                                   folderUuid={uuid} isDeleted={true} onDeleted={() => onRemoved?.(uuid)}/>}
        </>
    );
};

export default DeletedFolder;
