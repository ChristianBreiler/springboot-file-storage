import { useEffect, useState } from "react";
import { useSearchParams, Navigate } from "react-router-dom";
import api from "../../api/axiosConfig";
import Folder from "./Folder";
import File from "./File";
import FileViewPage from "./FileViewPage";
import LoadingBar from "../loading/LoadingBar";
import { Search, FileQuestion } from "lucide-react";
import { useTranslation } from "react-i18next";

const SearchResults = () => {
    const [searchParams] = useSearchParams();
    const searchTerm = searchParams.get("query");
    const [loading, setLoading] = useState(false);
    const [data, setData] = useState({ folders: [], files: [] });
    const [selectedId, setSelectedId] = useState(null);
    const { t } = useTranslation();

    useEffect(() => {
        document.title = `File Storage App - Search Results: ${searchTerm}` 
    }, []);

    useEffect(() => {
        const fetchSearchResults = async () => {
            if (!searchTerm) return;

            setLoading(true);
            try {
                const response = await api.get("search", {
                    params: { query: searchTerm }
                });
                setData(response.data);
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchSearchResults();
    }, [searchTerm]);

    if (!searchTerm) {
        return <Navigate to="/" replace />;
    }

    if (loading) return <LoadingBar />;

    const hasResults = (data.folders?.length > 0) || (data.files?.length > 0);

    return (
        <div className="p-6">
            <div className="flex items-center gap-2 mb-8 text-slate-600">
                <Search size={20} />
                <h2 className="text-lg font-semibold">
                    {t('searchResultsFor')}: <span className="text-blue-600">"{searchTerm}"</span>
                </h2>
            </div>
            {data.folders?.length > 0 && (
                <div className="mb-10">
                    <h3 className="text-sm font-bold text-slate-400 uppercase tracking-wider mb-4">
                        {t('folders')}
                    </h3>
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
                        {data.folders.map((folder) => (
                            <Folder
                                key={folder.uuid}
                                uuid={folder.uuid}
                                name={folder.name}
                            />
                        ))}
                    </div>
                </div>
            )}
            {data.files?.length > 0 && (
                <div>
                    <h3 className="text-sm font-bold text-slate-400 uppercase tracking-wider mb-4">
                        {t('files')}
                    </h3>
                    <div className="grid grid-cols-1 gap-2">
                        {data.files.map((file) => (
                            <File
                                key={file.uuid}
                                uuid={file.uuid}
                                originalFilename={file.originalFilename}
                                size={file.size}
                                filetype={file.filetype}
                                isDeleted={file.isDeleted}
                                onClick={(uuid) => setSelectedId(uuid)}
                            />
                        ))}
                    </div>
                </div>
            )}
            {!hasResults && !loading && (
                <div className="text-center py-20 bg-slate-50 rounded-2xl border-2 border-dashed border-slate-200">
                    <FileQuestion size={48} className="mx-auto text-slate-300 mb-4" />
                    <p className="text-slate-400 font-medium">{t('noResultsFound')}</p>
                </div>
            )}
            {selectedId && (
                <FileViewPage
                    uuid={selectedId}
                    onClose={() => setSelectedId(null)}
                />
            )}
        </div>
    );
};

export default SearchResults;