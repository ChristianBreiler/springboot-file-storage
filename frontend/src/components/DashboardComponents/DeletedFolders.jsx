import {useState, useEffect} from "react";
import api from "../../api/axiosConfig";
import DeletedFolder from "./DeletedFolder";
import LoadingBar from "../loading/LoadingBar";
import {useTranslation} from "react-i18next";

const DeletedFolders = () => {
    const [loading, setLoading] = useState(true);
    const [folders, setFolders] = useState(null);
    const {t} = useTranslation();

    useEffect(() => {
        document.title = `File Storage App - Deleted Folders`;
    }, []);

    useEffect(() => {
        const fetchDeletedFolders = async () => {
            setLoading(true);
            try {
                const response = await api.get("deleted_folders");
                setFolders(response.data);
            } catch (err) {
                console.error("Failed to fetch deleted folders:", err);
            } finally {
                setLoading(false);
            }
        };
        fetchDeletedFolders();
    }, []);

    // Remove the folder from the list after it was restored or permanently deleted
    const handleRemoved = (uuid) => {
        setFolders((prev) => prev.filter((folder) => folder.uuid !== uuid));
    };

    if (loading) return <LoadingBar/>;
    if (!folders) return <div className="p-8 text-red-500">Error while loading deleted folders</div>;

    return (
        <div className="p-6">
            {folders.length > 0 ? (
                <>
                    <h2 className="text-sm font-bold text-slate-400 uppercase tracking-wider mb-4">{t('deletedFolders')}</h2>
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-10">
                        {folders.map((folder) => (
                            <DeletedFolder key={folder.uuid} uuid={folder.uuid} name={folder.name}
                                           onRemoved={handleRemoved}/>
                        ))}
                    </div>
                </>
            ) : (
                <div className="text-center py-20 bg-slate-50 rounded-2xl border-2 border-dashed border-slate-200">
                    <p className="text-slate-400 font-medium">{t('noDeletedFolders')}</p>
                </div>
            )}
        </div>
    );
};

export default DeletedFolders;
