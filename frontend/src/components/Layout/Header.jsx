import { Sun, Settings } from "lucide-react";
import { Link, useLocation } from "react-router-dom";
import CreateDropdown from "../DashboardComponents/CreateDropdown";
import ProfileDropdown from "../DashboardComponents/ProfileDropdown";
import SearchBar from "../DashboardComponents/SearchBar";

const Header = () => {
  const location = useLocation();

  const navLinkClass = (path) => {
    const isActive = location.pathname === path;
    return `group flex h-10 w-10 items-center justify-center rounded-xl transition-all ${isActive
      ? "bg-blue-50 text-blue-600 shadow-sm"
      : "text-slate-500 hover:bg-white hover:shadow-sm hover:text-blue-600"
      }`;
  };

  return (
    <header className="sticky top-0 z-30 flex h-16 items-center justify-between border-b border-slate-100 bg-white/70 px-8 backdrop-blur-xl">
      <div className="flex flex-1 items-center gap-6">
        <SearchBar />
      </div>
      <div className="flex items-center gap-3">
        <CreateDropdown />
        <div className="h-6 w-px bg-slate-200 mx-2 hidden md:block" />
        <div className="flex items-center gap-1">
          <Link to="/settings" className={navLinkClass("/settings")}>
            <Settings size={20} />
          </Link>
        </div>
        <ProfileDropdown />
      </div>
    </header>
  );
};

export default Header;