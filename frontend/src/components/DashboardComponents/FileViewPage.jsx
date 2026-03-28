import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import LoadingBar from "../loading/LoadingBar";

const FileViewPage = () => {
    const { id } = useParams();
    const [blobUrl, setBlobUrl] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (id === null) return

        let url;
        api.get(`/files/open/${id}`, { responseType: 'blob' })
            .then(res => {
                url = URL.createObjectURL(res.data);
                setBlobUrl(url);
                setLoading(false);
            })
            .catch(err => console.error("Could not stream file", err));

        return () => { if (url) URL.revokeObjectURL(url); };
    }, [id]);

    if (loading) return <LoadingBar />

    return (
        <div className="h-screen w-full p-4">
            <iframe src={blobUrl} className="w-full h-full rounded-lg border shadow-lg" title="File Preview" />
        </div>
    );
};

export default FileViewPage;