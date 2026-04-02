import { useState, useEffect } from "react";
import api from "../../api/axiosConfig";

const Settings = () => {
    const [pageLayout, setPageLayout] = useState("cards");
    const [language, setLanguage] = useState("en");
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchSettings = async () => {
            setLoading(true);
            try {
                const response = await api.get("/settings");
                setPageLayout(response.data.pageLayout);
                setLanguage(response.data.language);
            } catch (err) {
                console.error("Fetch failed:", err);
            } finally {
                setLoading(false);
            }
        };

        fetchSettings();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            const response = await api.post("/settings/update", {
                pageLayout: pageLayout,
                language: language
            });
            setData(response.data);
        } catch (err) {
            console.error("Update failed:", err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="settings-container">
            <h2>User Settings</h2>
            {loading && !data ? <p>Loading...</p> : (
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Language:</label>
                        <select
                            value={language}
                            onChange={(e) => setLanguage(e.target.value)}
                        >
                            <option value="en">English</option>
                            <option value="de">Deutsch</option>
                        </select>
                    </div>
                    <div className="form-group">
                        <label>Page Layout:</label>
                        <div>
                            <input
                                type="radio"
                                id="cards"
                                name="layout"
                                value="cards"
                                checked={pageLayout === "cards"}
                                onChange={(e) => setPageLayout(e.target.value)}
                            />
                            <label htmlFor="cards">Cards</label>
                        </div>
                        <div>
                            <input
                                type="radio"
                                id="list"
                                name="layout"
                                value="list"
                                checked={pageLayout === "list"}
                                onChange={(e) => setPageLayout(e.target.value)}
                            />
                            <label htmlFor="list">List</label>
                        </div>
                    </div>
                    <button type="submit" disabled={loading}>
                        {loading ? "Saving..." : "Save Settings"}
                    </button>
                </form>
            )}
            {data && (
                <div className="response-preview">
                    <h3>Server Response:</h3>
                    <pre>{JSON.stringify(data, null, 2)}</pre>
                </div>
            )}
        </div>
    );
};

export default Settings;