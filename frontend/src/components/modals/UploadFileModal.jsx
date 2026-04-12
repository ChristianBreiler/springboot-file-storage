import { createPortal } from "react-dom";
import { useParams } from "react-router-dom";
import api from "../../api/axiosConfig";
import { useState } from "react";
import { X } from "lucide-react";
import { useTranslation } from "react-i18next";

const UploadFileModal = ({ isOpen, onClose }) => {
    // Id of the current folder from the url
    const { uuid } = useParams();
    const [file, setFile] = useState(null);
    const [loading, setLoading] = useState(false);
    const { t } = useTranslation()

    if (!isOpen) return null;

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!file) return;

        setLoading(true);

        // Use fordata here for file uploads
        const formData = new FormData();

        formData.append("file", file);

        try {
            const endpoint = uuid ? `files/upload/${uuid}` : "files/upload";
            await api.post(endpoint, formData);

            setFile(null);
            onClose();
            window.location.reload();
        } catch (err) {
            console.error("Upload error:", err.response?.data || err.message);
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
                        <h2 className="text-xl font-bold text-white">{t('newFile')}</h2>
                        <p className="text-sm text-slate-400 mt-1">{t('newFileInfo')}</p>
                    </div>
                    <button
                        onClick={onClose}
                        className="p-2 text-slate-400 hover:text-white hover:bg-slate-800 rounded-xl transition-colors">
                        <X size={20} />
                    </button>
                </div>
                <form onSubmit={handleSubmit} className="space-y-5">
                    <div className="relative">
                        <input
                            type="file"
                            onChange={(e) => setFile(e.target.files[0])}
                            className="block w-full text-sm text-slate-400 file:mr-4 file:py-3 file:px-6 file:rounded-2xl file:border-0 file:text-sm file:font-bold 
                                    file:bg-blue-600 file:text-white hover:file:bg-blue-500 file:cursor-pointer cursor-pointer bg-slate-800 rounded-2xl pr-4 focus:outline-none"
                            required
                        />
                        {file && (
                            <p className="mt-2 text-xs text-blue-400 italic">
                                {t('selected')} {file.name}
                            </p>
                        )}
                    </div>
                    <div className="flex justify-end gap-3 pt-2">
                        <button
                            type="button"
                            onClick={onClose}
                            className="px-6 py-3 text-sm font-semibold text-slate-400 hover:text-white transition-colors">
                            {t('cancel')}
                        </button>
                        <button
                            type="submit"
                            disabled={loading || file === null}
                            className="rounded-2xl bg-blue-600 px-8 py-3 text-sm font-bold text-white hover:bg-blue-500 active:scale-95 transition-all shadow-lg shadow-blue-600/20 disabled:opacity-50">
                            {loading ? "Creating..." : t('uploadFile')}
                        </button>
                    </div>
                </form>
            </div>
        </div>,
        document.body
    );
};

export default UploadFileModal;