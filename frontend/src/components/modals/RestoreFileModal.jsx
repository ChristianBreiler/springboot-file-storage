import { useState } from "react";
import { createPortal } from "react-dom";
import api from "../../api/axiosConfig";
import { X, RotateCcw } from "lucide-react";
import { useTranslation } from "react-i18next";

// Modal to restore deleted file
const RestoreFileModal = ({ isOpen, onClose, uuid, originalFilename }) => {
    const [loading, setLoading] = useState(false);
    const { t } = useTranslation();

    if (!isOpen) return null;

    const handleRestore = async (e) => {
        e.preventDefault();

        setLoading(true);
        try {
            await api.post(`deleted_files/restore/${uuid}`);
            onClose();
            window.location.reload();
        } catch (err) {
            console.error("Restore failed:", err);
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
            <div className="relative w-full max-w-md rounded-3xl bg-slate-900 p-8 shadow-2xl ring-1 ring-slate-800 animate-in fade-in zoom-in duration-200">
                <div className="flex justify-between items-center mb-6">
                    <div className="flex items-center gap-3">
                        <div className="p-2 bg-green-500/10 rounded-lg">
                            <RotateCcw className="text-green-500" size={24} />
                        </div>
                        <h2 className="text-xl font-bold text-white">
                            {t('restoreFile')}
                        </h2>
                    </div>
                    <button
                        onClick={onClose}
                        className="p-2 text-slate-400 hover:text-white hover:bg-slate-800 rounded-xl transition-colors"
                    >
                        <X size={20} />
                    </button>
                </div>
                <div className="mb-8">
                    <p className="text-slate-300">
                        {t('restoreFileInfo', {originalFilename: originalFilename})}
                    </p>
                </div>
                <div className="flex justify-end gap-3">
                    <button
                        type="button"
                        onClick={onClose}
                        className="px-6 py-3 text-sm font-semibold text-slate-400 hover:text-white transition-colors"
                    >
                        {t('cancel')}
                    </button>
                    <button
                        onClick={handleRestore}
                        disabled={loading}
                        className="rounded-2xl bg-green-600 px-8 py-3 text-sm font-bold text-white hover:bg-green-500 active:scale-95 transition-all shadow-lg shadow-green-600/20 disabled:opacity-30 disabled:cursor-not-allowed"
                    >
                        {loading ? "Restoring..." : t('restore')}
                    </button>
                </div>
            </div>
        </div>,
        document.body
    );
};

export default RestoreFileModal;