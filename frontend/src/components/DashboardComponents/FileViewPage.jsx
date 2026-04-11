import { useEffect, useState, useRef } from "react";
import api from "../../api/axiosConfig";
import LoadingBar from "../loading/LoadingBar";
import { X, FileText, Download } from "lucide-react";

const FileViewPage = ({ uuid, onClose }) => {
    const [blobUrl, setBlobUrl] = useState(null);
    const [loading, setLoading] = useState(true);
    const [downloading, setDownloading] = useState(false);
    const iframeRef = useRef(null);

    useEffect(() => {
        if (!uuid) return;

        let url;
        setLoading(true);
        api.get(`/files/open/${uuid}`, { responseType: 'blob' })
            .then(res => {
                url = URL.createObjectURL(res.data);
                setBlobUrl(url);
                setLoading(false);
            })
            .catch(err => {
                console.error("Could not stream file", err);
                setLoading(false);
            });

        return () => { if (url) URL.revokeObjectURL(url); };
    }, [uuid]);

    const handleDownload = async () => {
        // Download the file while not using the diskname (way too complicated probably)
        try {
            const response = await api.get(`/files/download/${uuid}`, { responseType: 'blob' });

            const disposition = response.headers['content-disposition'];
            let fileName = "file.pdf";

            if (disposition && disposition.includes('filename=')) {
                const match = disposition.match(/filename="(.+)"/);
                if (match && match[1]) {
                    fileName = match[1];
                }
            }

            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;

            link.download = fileName;

            document.body.appendChild(link);
            link.click();
            link.remove();
        } catch (err) {
            console.error("Download error", err);
        }
    };

    if (loading) return <LoadingBar />

    return (
        <div className="fixed inset-0 z-100 flex items-center justify-center bg-slate-900/60 backdrop-blur-sm p-4 md:p-8">
            <div className="bg-white w-full max-w-6xl h-full max-h-[95vh] rounded-2xl shadow-2xl flex flex-col overflow-hidden animate-in fade-in zoom-in duration-200">
                <div className="flex items-center justify-between px-6 py-4 border-b border-slate-100 bg-white">
                    <div className="flex items-center gap-3">
                        <div className="h-9 w-9 bg-blue-50 rounded-lg flex items-center justify-center text-blue-600">
                            <FileText size={20} />
                        </div>
                        <div>
                            <h3 className="font-semibold text-slate-800 leading-none">File Preview</h3>
                            <p className="text-[11px] text-slate-400 mt-1 uppercase font-bold tracking-wider">Document Viewer</p>
                        </div>
                    </div>
                    <div className="flex items-center gap-2">
                        <button
                            onClick={handleDownload}
                            disabled={downloading}
                            className="flex items-center gap-2 px-4 py-2 bg-blue-600 hover:bg-blue-700 disabled:bg-blue-400 text-white text-sm font-bold rounded-xl transition-all shadow-md active:scale-95"
                        >
                            <Download size={18} />
                            {downloading ? "Downloading..." : "Download"}
                        </button>
                        <button
                            onClick={onClose}
                            className="p-2 text-slate-400 hover:text-red-500 hover:bg-red-50 rounded-xl transition-all" >
                            <X size={24} />
                        </button>
                    </div>
                </div>
                <div className="flex-1 bg-slate-100/50 relative">
                    {loading ? (
                        <div className="absolute inset-0 flex flex-col items-center justify-center gap-4">
                            <LoadingBar />
                            <span className="text-sm font-medium text-slate-500 animate-pulse">Preparing your document...</span>
                        </div>
                    ) : (
                        <iframe
                            ref={iframeRef}
                            src={blobUrl}
                            className="w-full h-full border-none"
                            title="File Preview"
                        />
                    )}
                </div>
                {!loading && (
                    <div className="px-6 py-3 bg-slate-50 border-t border-slate-100 text-right">
                        <span className="text-[10px] text-slate-400 font-medium">Securely streamed via encrypted tunnel</span>
                    </div>
                )}
            </div>
        </div>
    );
};

export default FileViewPage;