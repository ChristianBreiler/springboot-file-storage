import { useState } from "react";
import { Settings, Trash, FolderOpen, ChevronLeft, ChevronRight, LogOut } from "lucide-react";
import { Link, useLocation } from "react-router-dom";
import StorageBar from "../DashboardComponents/StorageBar";

const Sidebar = () => {
  const [isExpanded, setIsExpanded] = useState(true);
  const location = useLocation();

  const isActive = (path) => location.pathname === path;

  const navLinkClass = (path) => `
    flex items-center gap-3 px-3 py-2.5 rounded-xl transition-all duration-200 group
    ${isActive(path) ? "bg-blue-600/10 text-blue-400" : "hover:bg-slate-900 hover:text-slate-100 text-slate-400"}
    ${!isExpanded && "justify-center px-0"}
  `;

  return (
    <aside className={`h-screen bg-slate-950 flex flex-col border-r border-slate-800 transition-all duration-300 ${isExpanded ? "w-64" : "w-20"}`}>
      <div className={`h-16 flex items-center px-6 gap-3 ${!isExpanded && "justify-center px-0"}`}>
        <Link to="/" className={navLinkClass("/")}>
          <div className="w-8 h-8 bg-blue-600 rounded-lg flex-shrink-0 flex items-center justify-center text-white">
            <FolderOpen size={20} />
          </div>
          {isExpanded && <span className="text-white font-semibold tracking-tight truncate">File Storage</span>}
        </Link>
      </div>
      <nav className="flex-1 px-4 py-6 space-y-1">
        {isExpanded && (
          <p className="px-3 text-[10px] font-bold uppercase tracking-widest text-slate-500 mb-4">
            Main Menu
          </p>
        )}
        <Link to="/files" className={navLinkClass("/files")}>
          <FolderOpen size={18} className="shrink-0" />
          {isExpanded && <span className="text-sm font-medium">All Files</span>}
        </Link>
        <Link to="/deleted_files" className={navLinkClass("/deleted_files")}>
          <Trash size={18} className="shrink-0" />
          {isExpanded && <span className="text-sm font-medium">Trash</span>}
        </Link>
      </nav>
      <div className="p-4 border-t border-slate-900 space-y-4">
        {isExpanded && <StorageBar />}
        <button
          onClick={() => setIsExpanded(!isExpanded)}
          className="w-full flex items-center gap-3 px-3 py-2.5 rounded-xl transition-all duration-200 text-slate-400 hover:bg-slate-900 hover:text-slate-100 justify-center"
        >
          {isExpanded ? (
            <>
              <ChevronLeft size={18} />
            </>
          ) : (
            <ChevronRight size={18} />
          )}
        </button>
      </div>
    </aside>
  );
};

export default Sidebar;