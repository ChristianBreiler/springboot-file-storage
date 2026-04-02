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
                if (response.data) {
                    setPageLayout(response.data.pageLayout);
                    setLanguage(response.data.language);
                }
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
        <div className="w-full min-h-screen p-10 bg-white border-t border-gray-200">
            <h2 className="text-3xl font-bold mb-10 text-gray-800">User Settings</h2>
            {loading && !data ? (
                <p className="text-blue-500 animate-pulse text-xl">Loading settings...</p>
            ) : (
                <form onSubmit={handleSubmit} className="space-y-10">
                    <div className="flex flex-col max-w-4xl">
                        <label className="text-lg font-semibold text-gray-600 mb-3">Language</label>
                        <select
                            className="block w-full px-4 py-3 text-lg bg-white border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all"
                            value={language}
                            onChange={(e) => setLanguage(e.target.value)}
                        >
                            <option value="en">English</option>
                            <option value="de">Deutsch</option>
                        </select>
                    </div>
                    <div className="flex flex-col">
                        <label className="text-lg font-semibold text-gray-600 mb-4">Page Layout</label>
                        <div className="flex items-center space-x-12">
                            <label className="inline-flex items-center cursor-pointer group">
                                <input
                                    type="radio"
                                    className="form-radio h-6 w-6 text-indigo-600 border-gray-300 focus:ring-indigo-500"
                                    name="layout"
                                    value="cards"
                                    checked={pageLayout === "cards"}
                                    onChange={(e) => setPageLayout(e.target.value)}
                                />
                                <span className="ml-3 text-xl text-gray-700 group-hover:text-indigo-600">Cards</span>
                            </label>
                            <label className="inline-flex items-center cursor-pointer group">
                                <input
                                    type="radio"
                                    className="form-radio h-6 w-6 text-indigo-600 border-gray-300 focus:ring-indigo-500"
                                    name="layout"
                                    value="list"
                                    checked={pageLayout === "list"}
                                    onChange={(e) => setPageLayout(e.target.value)}
                                />
                                <span className="ml-3 text-xl text-gray-700 group-hover:text-indigo-600">List</span>
                            </label>
                        </div>
                    </div>
                    <button
                        type="submit"
                        disabled={loading}
                        className={`max-w-xs py-4 px-8 rounded-xl font-bold text-lg text-white shadow-lg transition-all transform active:scale-95
                            ${loading ? "bg-gray-400 cursor-not-allowed" : "bg-indigo-600 hover:bg-indigo-700"}`}
                    >
                        {loading ? "Saving..." : "Save Settings"}
                    </button>
                </form>
            )}
            {data && (
                <div className="mt-12 p-6 bg-gray-900 rounded-2xl shadow-inner">
                    <h3 className="text-sm font-bold uppercase text-indigo-400 mb-4 tracking-widest">Server Data Log</h3>
                    <pre className="text-green-400 font-mono text-base overflow-x-auto">
                        {JSON.stringify(data, null, 2)}
                    </pre>
                </div>
            )}
        </div>
    );
};

export default Settings;