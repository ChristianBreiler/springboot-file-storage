import { Home, Settings, Trash, FolderOpen, LogOut } from "lucide-react";
import { Link, useLocation } from "react-router-dom";

const Sidebar = () => {
  const location = useLocation();

  const isActive = (path) => location.pathname === path;

  const navLinkClass = (path) => `
    flex items-center gap-3 px-3 py-2.5 rounded-xl transition-all duration-200 group
    ${isActive(path) 
      ? "bg-blue-600/10 text-blue-400" 
      : "hover:bg-slate-900 hover:text-slate-100 text-slate-400"}
  `;

  return (
    <aside className="w-64 h-screen bg-slate-950 flex flex-col border-r border-slate-800">
      {/* Logo Section */}
      <div className="h-16 flex items-center px-6 gap-3">
        <div className="w-8 h-8 bg-blue-600 rounded-lg flex items-center justify-center text-white">
          <FolderOpen size={20} />
        </div>
        <span className="text-white font-semibold tracking-tight">CloudVault</span>
      </div>

      {/* Navigation */}
      <nav className="flex-1 px-4 py-6 space-y-1">
        <p className="px-3 text-[10px] font-bold uppercase tracking-widest text-slate-500 mb-4">
          Main Menu
        </p>

        <Link to="/folders/home" className={navLinkClass("/folders/home")}>
          <Home size={18} className={isActive("/folders/home") ? "text-blue-400" : "text-slate-500 group-hover:text-slate-100"} />
          <span className="text-sm font-medium">Dashboard</span>
        </Link>

        <Link to="/files" className={navLinkClass("/files")}>
          <FolderOpen size={18} className={isActive("/files") ? "text-blue-400" : "text-slate-500 group-hover:text-slate-100"} />
          <span className="text-sm font-medium">All Files</span>
        </Link>

        <Link to="/deleted_files" className={navLinkClass("/trash")}>
          <Trash size={18} className={isActive("/trash") ? "text-blue-400" : "text-slate-500 group-hover:text-slate-100"} />
          <span className="text-sm font-medium">Trash</span>
        </Link>
      </nav>

      {/* Footer / User Profile */}
      <div className="p-4 border-t border-slate-900">
        <Link to="/settings" className={navLinkClass("/settings") + " mb-4"}>
          <Settings size={18} className={isActive("/settings") ? "text-blue-400" : "text-slate-500 group-hover:text-slate-100"} />
          <span className="text-sm font-medium">Settings</span>
        </Link>
        
        <div className="flex items-center gap-3 px-3 py-2 bg-slate-900/50 rounded-2xl border border-slate-800/50">
          <div className="w-8 h-8 rounded-full bg-gradient-to-tr from-blue-500 to-purple-500" />
          <div className="flex-1 min-w-0">
            <p className="text-xs font-semibold text-slate-100 truncate">Alex Rivera</p>
            <p className="text-[10px] text-slate-500 truncate">Pro Plan</p>
          </div>
          <button className="text-slate-500 hover:text-red-400 transition-colors">
            <LogOut size={16} />
          </button>
        </div>
      </div>
    </aside>
  );
};

export default Sidebar;