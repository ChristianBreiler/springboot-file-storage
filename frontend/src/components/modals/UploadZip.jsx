import {createPortal} from "react-dom";
import {useParams} from "react-router-dom";
import api from "../../api/axiosConfig";
import {useState} from "react";
import {Trash2, X} from "lucide-react";
import {useTranslation} from "react-i18next";

const isZipFile = (file) => file.name.toLowerCase().endsWith(".zip");

const UploadZip = ({isOpen, onClose}) => {
    // Id of the current folder from the url
    const {uuid} = useParams();

    const [files, setFiles] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const {t} = useTranslation();

    if (!isOpen) return null;

    const handleFileChange = (e) => {
        const selectedFiles = Array.from(e.target.files);
        const zipFiles = selectedFiles.filter(isZipFile);

        setFiles(zipFiles);
        setError(
            zipFiles.length === selectedFiles.length
                ? ""
                : t("onlyZipFilesAllowed")
        );

        e.target.value = "";
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (files.length === 0) return;

        setLoading(true);
        setError("");

        try {
            const endpoint = uuid ? `zip/import/${uuid}` : "zip/import";

            for (const file of files) {
                const formData = new FormData();
                formData.append("file", file);
                await api.post(endpoint, formData);
            }

            setFiles([]);
            onClose();

            window.location.reload();
        } catch (err) {
            console.error(
                "Upload error:",
                err.response?.data || err.message
            );

            const data = err.response?.data;
            const errorMessage =
                (typeof data === "string" && data) ||
                data?.message ||
                data?.error ||
                err.message ||
                "Upload failed";

            setError(errorMessage);
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
            <div
                className="relative w-full max-w-md transform rounded-3xl bg-slate-900 p-8 shadow-2xl ring-1 ring-slate-800 animate-in fade-in zoom-in duration-200">
                <div className="flex justify-between items-center mb-6">
                    <div>
                        <h2 className="text-xl font-bold text-white">
                            {t("importZip")}
                        </h2>
                        <p className="text-sm text-slate-400 mt-1">
                            {t("importZipInfo")}
                        </p>
                    </div>
                    <button
                        onClick={onClose}
                        className="p-2 text-slate-400 hover:text-white hover:bg-slate-800 rounded-xl transition-colors"
                    >
                        <X size={20}/>
                    </button>
                </div>
                <form onSubmit={handleSubmit} className="space-y-5">
                    {error && (
                        <div className="rounded-xl border border-red-500/20 bg-red-500/10 p-3 text-sm text-red-400">
                            {error}
                        </div>
                    )}
                    <div className="relative">
                        <label className="block text-xs font-semibold text-slate-400 px-1 mb-2">
                            {t("selectZipFiles")}
                        </label>
                        <input
                            type="file"
                            accept=".zip,application/zip,application/x-zip-compressed"
                            multiple
                            onChange={handleFileChange}
                            className="block w-full cursor-pointer rounded-2xl bg-slate-800 pr-4 text-sm text-slate-400 focus:outline-none
                            file:mr-4 file:cursor-pointer file:rounded-2xl file:border-0
                            file:bg-blue-600 file:px-6 file:py-3 file:text-sm
                            file:font-bold file:text-white hover:file:bg-blue-500"
                        />
                        {files.length > 0 && (
                            <div className="mt-3 max-h-32 overflow-y-auto space-y-1 rounded-xl bg-slate-950/40 p-2">
                                {files.map((file, index) => (
                                    <div
                                        key={index}
                                        className="flex items-center justify-between text-xs text-blue-400 px-1 py-0.5">
                                        <span className="italic truncate mr-2">
                                            • {file.name}
                                        </span>
                                        <button
                                            type="button"
                                            onClick={() => {
                                                setFiles((prevFiles) =>
                                                    prevFiles.filter((_, i) => i !== index)
                                                );
                                            }}
                                            className="text-red-400 hover:text-red-500 font-medium focus:outline-none shrink-0"
                                        >
                                            <Trash2 size={14}/>
                                        </button>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>
                    <div className="flex justify-end gap-3 pt-2">
                        <button
                            type="button"
                            onClick={onClose}
                            className="px-6 py-3 text-sm font-semibold text-slate-400 transition-colors hover:text-white"
                        >
                            {t("cancel")}
                        </button>

                        <button
                            type="submit"
                            disabled={loading || files.length === 0}
                            className="rounded-2xl bg-blue-600 px-8 py-3 text-sm font-bold text-white shadow-lg shadow-blue-600/20 transition-all hover:bg-blue-500 active:scale-95 disabled:opacity-50"
                        >
                            {loading ? t("importing") : t("import")}
                        </button>
                    </div>
                </form>
            </div>
        </div>,
        document.body
    );
};

export default UploadZip;
