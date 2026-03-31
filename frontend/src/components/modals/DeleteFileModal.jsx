import { useState } from "react";
import { createPortal } from "react-dom";
import api from "../../api/axiosConfig";
import { X, Trash2, AlertTriangle, Loader2 } from "lucide-react";

const DeleteFileModal = ({ isOpen, onClose, originalFilename, fileUuid, isDeleted }) => {
    const [loading, setLoading] = useState(false);

    if (!isOpen) return null;

    const handleDelete = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            const path = isDeleted ? `deleted_files/delete/${fileUuid}` : `files/delete/${fileUuid}`;
            await api.delete(path);
            onClose();
            window.location.reload();
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    return createPortal(
        <div className="fixed inset-0 z-9999 flex items-center justify-center p-4">
            <div
                className="absolute inset-0 bg-slate-950/40 backdrop-blur-md"
                onClick={onClose}
            />
            <div className="relative w-full max-w-md transform rounded-4xl bg-slate-900 border border-slate-800 p-8 shadow-2xl animate-in fade-in zoom-in-95 duration-200">
                <div className="flex justify-between items-start mb-6">
                    <div className={`p-3 rounded-2xl ${isDeleted ? 'bg-red-500/10' : 'bg-slate-800'}`}>
                        <Trash2 className={isDeleted ? "text-red-500" : "text-slate-400"} size={28} />
                    </div>
                    <button
                        onClick={onClose}
                        className="p-2 text-slate-500 hover:text-white hover:bg-slate-800 rounded-full transition-all"
                    >
                        <X size={20} />
                    </button>
                </div>
                <div className="space-y-4 mb-8">
                    <h2 className="text-2xl font-bold text-white tracking-tight">
                        {isDeleted ? "Delete Permanently" : "Move to Trash"}
                    </h2>
                    <p className="text-slate-400 leading-relaxed">
                        Are you sure you want to delete <span className="text-slate-100 font-medium italic">"{originalFilename}"</span>?
                    </p>
                    {isDeleted && (
                        <div className="flex items-center gap-3 p-4 bg-amber-500/10 border border-amber-500/20 rounded-2xl">
                            <AlertTriangle className="text-amber-500 shrink-0" size={18} />
                            <p className="text-sm text-amber-200/80 font-medium">
                                This action cannot be undone.
                            </p>
                        </div>
                    )}
                </div>
                <div className="flex flex-col sm:flex-row gap-3">
                    <button
                        type="button"
                        onClick={onClose}
                        disabled={loading}
                        className="flex-1 px-6 py-3.5 text-sm font-bold text-slate-400 hover:text-white hover:bg-slate-800 rounded-2xl transition-all disabled:opacity-50"
                    >
                        Cancel
                    </button>
                    <button
                        onClick={handleDelete}
                        disabled={loading}
                        className={`flex-[1.5] flex items-center justify-center gap-2 rounded-2xl px-8 py-3.5 text-sm font-bold text-white transition-all active:scale-95 disabled:opacity-50
              ${isDeleted
                                ? "bg-red-600 hover:bg-red-500 shadow-lg shadow-red-600/20"
                                : "bg-slate-700 hover:bg-slate-600"}
            `}
                    >
                        {loading ? (
                            <>
                                <Loader2 className="w-4 h-4 animate-spin" />
                                <span>Processing</span>
                            </>
                        ) : (
                            isDeleted ? "Delete Forever" : "Move to Trash"
                        )}
                    </button>
                </div>
            </div>
        </div>,
        document.body
    );
};

export default DeleteFileModal;