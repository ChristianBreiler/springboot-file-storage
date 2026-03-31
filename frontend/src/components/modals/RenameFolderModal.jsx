import { createPortal } from "react-dom";
import api from "../../api/axiosConfig";
import { useState } from "react";
import { X } from "lucide-react";

const RenameFolderModal = ({ isOpen, onClose, currentFolderName, folderUuid }) => {
    const [newFolderName, setNewFolderName] = useState(currentFolderName);
    const [loading, setLoading] = useState(false);

    if (!isOpen) return null;

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            const endpoint = folderUuid ? `folders/rename/${folderUuid}` : "folders/rename";
            await api.post(endpoint, { newFolderName: newFolderName });
            setNewFolderName("")
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
                className="absolute inset-0 bg-slate-950/60 backdrop-blur-sm"
                onClick={onClose}
            />
            <div className="relative w-full max-w-md transform rounded-3xl bg-slate-900 p-8 shadow-2xl ring-1 ring-slate-800 animate-in fade-in zoom-in duration-200">
                <div className="flex justify-between items-center mb-6">
                    <div>
                        <h2 className="text-xl font-bold text-white">Rename your Folder {currentFolderName}</h2>
                    </div>
                    <button
                        onClick={onClose}
                        className="p-2 text-slate-400 hover:text-white hover:bg-slate-800 rounded-xl transition-colors">
                        <X size={20} />
                    </button>
                </div>
                <form onSubmit={handleSubmit} className="space-y-5">
                    <div>
                        <input
                            type="text"
                            autoFocus
                            placeholder="Folder Name"
                            className="w-full rounded-2xl border-none bg-slate-800 px-5 py-4 text-white placeholder:text-slate-500 focus:ring-2 focus:ring-blue-500 outline-none transition-all"
                            value={newFolderName}
                            onChange={(e) => setNewFolderName(e.target.value)}
                            required
                        />
                    </div>
                    <div className="flex justify-end gap-3 pt-2">
                        <button
                            type="button"
                            onClick={onClose}
                            className="px-6 py-3 text-sm font-semibold text-slate-400 hover:text-white transition-colors">
                            Cancel
                        </button>
                        <button
                            type="submit"
                            disabled={loading || !newFolderName.trim()}
                            className="rounded-2xl bg-blue-600 px-8 py-3 text-sm font-bold text-white hover:bg-blue-500 active:scale-95 transition-all shadow-lg shadow-blue-600/20 disabled:opacity-50">
                            {loading ? "Creating..." : "Create Folder"}
                        </button>
                    </div>
                </form>
            </div>
        </div>,
        document.body
    );
}

export default RenameFolderModal;