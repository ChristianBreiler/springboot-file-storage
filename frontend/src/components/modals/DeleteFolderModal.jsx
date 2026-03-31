import { useState, useEffect } from "react";
import { createPortal } from "react-dom";
import api from "../../api/axiosConfig";
import { X, AlertTriangle, Trash2 } from "lucide-react";

const DeleteFolderModal = ({ isOpen, onClose, currentFolderName, folderUuid }) => {
    const [folderDeletionInfo, setFolderDeletionInfo] = useState(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        // Do this to prevent 403 erros since request with folderUuid = undefined cause issues
        if (!isOpen || !folderUuid || folderUuid === "undefined") {
            return;
        }

        const fetchFolderInfo = async () => {
            setLoading(true);
            try {
                const response = await api.get(`folders/delete_folder_info/${folderUuid}`);
                setFolderDeletionInfo(response.data);
            } catch (err) {
                console.error("Error fetching folder info:", err);
            } finally {
                setLoading(false);
            }
        };

        fetchFolderInfo();
    }, [isOpen, folderUuid]);

    if (!isOpen) return null;

    const handleDelete = async (e) => {
        e.preventDefault();

        if (!folderDeletionInfo?.empty) return;

        setLoading(true);
        try {
            await api.delete(`folders/delete/${folderUuid}`);
            onClose();
            window.location.reload();
        } catch (err) {
            console.error("Delete failed:", err);
        } finally {
            setLoading(false);
        }
    };

    return createPortal(
        <div className="fixed inset-0 z-9999 flex items-center justify-center p-4">
            <div
                className="absolute inset-0 bg-slate-950/60 backdrop-blur-sm"
                onClick={onClose}
            />
            <div className="relative w-full max-w-md transform rounded-3xl bg-slate-900 p-8 shadow-2xl ring-1 ring-slate-800 animate-in fade-in zoom-in duration-200">
                <div className="flex justify-between items-center mb-6">
                    <div className="flex items-center gap-3">
                        <div className="p-2 bg-red-500/10 rounded-lg">
                            <Trash2 className="text-red-500" size={24} />
                        </div>
                        <h2 className="text-xl font-bold text-white">Delete Folder</h2>
                    </div>
                    <button
                        onClick={onClose}
                        className="p-2 text-slate-400 hover:text-white hover:bg-slate-800 rounded-xl transition-colors">
                        <X size={20} />
                    </button>
                </div>

                <div className="mb-8">
                    {!loading && folderDeletionInfo && folderDeletionInfo.empty && (
                        <p className="text-slate-300">
                            Are you sure you want to delete <span className="font-bold text-white">"{currentFolderName}"</span>?
                        </p>
                    )}
                    {!loading && folderDeletionInfo && !folderDeletionInfo.empty && (
                        <div className="mt-4 flex gap-3 p-4 rounded-2xl bg-amber-500/10 border border-amber-500/20 text-amber-500">
                            <AlertTriangle size={40} className="shrink-0" />
                            <p className="text-sm">
                                This folder is not empty. You can only delete empty folders at this time.
                            </p>
                        </div>
                    )}
                </div>

                <div className="flex justify-end gap-3">
                    <button
                        type="button"
                        onClick={onClose}
                        className="px-6 py-3 text-sm font-semibold text-slate-400 hover:text-white transition-colors">
                        Cancel
                    </button>
                    <button
                        onClick={handleDelete}
                        disabled={loading || !folderDeletionInfo?.empty}
                        className="rounded-2xl bg-red-600 px-8 py-3 text-sm font-bold text-white hover:bg-red-500 active:scale-95 transition-all shadow-lg shadow-red-600/20 disabled:opacity-30 disabled:cursor-not-allowed">
                        {loading ? "Processing..." : "Delete Permanently"}
                    </button>
                </div>
            </div>
        </div>,
        document.body
    );
};

export default DeleteFolderModal;