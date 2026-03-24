import { Bell, Search, User, Sun, Plus, Command } from "lucide-react";
import CreateDropdown from "../DashboardComponents/CreateDropdown";
import ProfileDropdown from "../DashboardComponents/ProfileDropdown";

const Header = () => {
  return (
    <header className="sticky top-0 z-30 flex h-16 items-center justify-between border-b border-slate-100 bg-white/70 px-8 backdrop-blur-xl">
      <div className="flex flex-1 items-center gap-6">
        <div className="relative group w-full max-w-md">
          <Search
            size={18}
            className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 group-focus-within:text-blue-600 transition-colors"
          />
          <input
            type="text"
            placeholder="Search documents, people, or settings..."
            className="w-full rounded-xl bg-slate-100/50 border-transparent py-2.5 pl-10 pr-12 text-sm outline-none transition-all focus:bg-white focus:ring-2 focus:ring-blue-100 focus:border-blue-200 placeholder:text-slate-500"
          />
          <div className="absolute right-2 top-1/2 -translate-y-1/2 hidden md:flex items-center gap-1 px-1.5 py-1 rounded bg-white border border-slate-200 shadow-sm">
            <Command size={10} className="text-slate-400" />
            <span className="text-[10px] font-bold text-slate-400">K</span>
          </div>
        </div>
      </div>
      <div className="flex items-center gap-3">
        <CreateDropdown />
        <div className="h-6 w-px bg-slate-200 mx-2 hidden md:block" />
        <div className="flex items-center gap-1">
          <button className="group flex h-10 w-10 items-center justify-center rounded-xl text-slate-500 hover:bg-white hover:shadow-sm hover:text-orange-500 transition-all">
            <Sun size={20} />
          </button>
          <button className="group relative flex h-10 w-10 items-center justify-center rounded-xl text-slate-500 hover:bg-white hover:shadow-sm hover:text-blue-600 transition-all">
            <Bell size={20} />
            <span className="absolute top-2.5 right-2.5 h-2 w-2 rounded-full border-2 border-white bg-red-500"></span>
          </button>
        </div>
        <ProfileDropdown />
      </div>
    </header>
  );
};

export default Header;