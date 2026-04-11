import { useState, useEffect } from "react";
import api from "../../api/axiosConfig";
import { CheckCircle, Layout, List, Globe, Save } from "lucide-react";
import LoadingBar from "../loading/LoadingBar";
import i18n from "../../utils/i18n";
import { data } from "react-router-dom";

const Settings = () => {
    const [settings, setSettings] = useState({
        pageLayout: "cards",
        language: "en"
    });
    const [loading, setLoading] = useState(false);
    const [status, setStatus] = useState(null);

    useEffect(() => {
        const fetchSettings = async () => {
            setLoading(true);
            try {
                const { data } = await api.get("/settings");
                if (data) setSettings({ pageLayout: data.pageLayout, language: data.language });
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
        setStatus(null);
        try {
            await api.post("/settings/update", settings);
            localStorage.setItem("pageLayout", settings.pageLayout);
            localStorage.setItem("language", settings.language);
            i18n.changeLanguage(settings.language);
            setStatus("success");
            setTimeout(() => setStatus(null), 3000);

        } catch (err) {
            setStatus("error");
        } finally {
            setLoading(false);
        }
    };

    if (loading) return <LoadingBar />

    return (
        <div className="max-w-4xl mx-auto p-8 animate-in fade-in duration-500">
            <header className="mb-10">
                <h2 className="text-3xl font-extrabold text-slate-900 tracking-tight">Settings</h2>
                <p className="text-slate-500 mt-2">Change your settings here</p>
            </header>
            <form onSubmit={handleSubmit} className="space-y-8">
                <section className="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm">
                    <div className="flex items-center gap-3 mb-6">
                        <Globe className="text-indigo-600 w-5 h-5" />
                        <h3 className="text-lg font-bold text-slate-800">Language</h3>
                    </div>
                    <select
                        className="w-full md:w-1/2 p-3 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-indigo-500 outline-none transition-all cursor-pointer hover:bg-slate-100"
                        value={settings.language}
                        onChange={(e) => setSettings({ ...settings, language: e.target.value })}
                    >
                        <option value="en">English (US)</option>
                        <option value="de">Deutsch (German)</option>
                    </select>
                </section>
                <section className="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm">
                    <div className="flex items-center gap-3 mb-6">
                        <Layout className="text-indigo-600 w-5 h-5" />
                        <h3 className="text-lg font-bold text-slate-800">Page Layout</h3>
                    </div>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        {[
                            { id: "cards", label: "Card Grid", icon: <Layout />, desc: "Visual blocks for high-detail items." },
                            { id: "list", label: "Compact List", icon: <List />, desc: "Dense data view for fast scanning." }
                        ].map((option) => (
                            <div
                                key={option.id}
                                onClick={() => setSettings({ ...settings, pageLayout: option.id })}
                                className={`relative cursor-pointer p-5 rounded-xl border-2 transition-all flex items-start gap-4 
                                    ${settings.pageLayout === option.id
                                        ? "border-indigo-600 bg-indigo-50/30"
                                        : "border-slate-100 hover:border-slate-300 bg-white"}`}
                            >
                                <div className={`p-2 rounded-lg ${settings.pageLayout === option.id ? "bg-indigo-600 text-white" : "bg-slate-100 text-slate-500"}`}>
                                    {option.icon}
                                </div>
                                <div>
                                    <p className="font-bold text-slate-900">{option.label}</p>
                                    <p className="text-sm text-slate-500">{option.desc}</p>
                                </div>
                                {settings.pageLayout === option.id && (
                                    <CheckCircle className="absolute top-4 right-4 w-5 h-5 text-indigo-600" />
                                )}
                            </div>
                        ))}
                    </div>
                </section>
                <div className="flex items-center justify-between pt-6 border-t border-slate-200">
                    <div>
                        {status === "success" && <span className="text-emerald-600 font-medium flex items-center gap-2">✓ Changes saved successfully!</span>}
                        {status === "error" && <span className="text-red-500 font-medium text-sm">Failed to update. Try again.</span>}
                    </div>
                    <button
                        type="submit"
                        disabled={loading}
                        className="flex items-center gap-2 px-8 py-3 bg-indigo-600 hover:bg-indigo-700 text-white font-bold rounded-xl shadow-lg shadow-indigo-200 transition-all active:scale-95 disabled:opacity-50"
                    >
                        {loading ? "Saving..." : <><Save size={18} /> Save Changes</>}
                    </button>
                </div>
            </form>
        </div>
    );
};

export default Settings;