import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Search, X } from "lucide-react";
import { useTranslation } from "react-i18next";

const SearchBar = () => {
    const [term, setTerm] = useState("");
    const navigate = useNavigate();
    const { t } = useTranslation();

    const handleSearch = (e) => {
        e.preventDefault();
        if (term.trim()) {
            navigate(`/search?query=${encodeURIComponent(term.trim())}`);
        }
    };

    const clearSearch = () => {
        setTerm("");
    };

    return (
        <form
            onSubmit={handleSearch}
            className="relative w-full max-w-xl group"
        >
            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <Search size={18} className="text-slate-400 group-focus-within:text-blue-500 transition-colors" />
            </div>
            <input
                type="text"
                className="block w-full pl-10 pr-12 py-2.5 bg-slate-100 border border-transparent rounded-xl text-sm placeholder-slate-500 focus:bg-white focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 outline-none transition-all"
                placeholder={t('search') || "Search for files or folders..."}
                value={term}
                onChange={(e) => setTerm(e.target.value)}
            />
            {term && (
                <button
                    type="button"
                    onClick={clearSearch}
                    className="absolute inset-y-0 right-0 pr-3 flex items-center text-slate-400 hover:text-slate-600"
                >
                    <X size={16} />
                </button>
            )}
            <button type="submit" className="hidden">Search</button>
        </form>
    );
};

export default SearchBar;