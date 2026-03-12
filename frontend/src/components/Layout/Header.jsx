import { Bell, Search, User, Sun, Plus } from "lucide-react";

const Header = () => {
  return (
    <header className="flex h-16 items-center justify-between border-b bg-white px-6">
      <div className="flex items-center gap-4 bg-gray-100 px-3 py-1.5 rounded-md">
        <input type="file" className="hidden" />
        <button className="group flex items-center gap-2 bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-1.5 rounded-full transition-all active:scale-95 shadow-sm shadow-indigo-200">
          <Plus size={18} strokeWidth={3} />
          <span className="text-sm font-semibold">New</span>
        </button>
        <div className="relative group max-w-md w-full">
          <div className="absolute inset-y-0 left-3 flex items-center pointer-events-none">
            <Search
              size={16}
              className="text-slate-400 group-focus-within:text-indigo-500 transition-colors"
            />
          </div>
          <input
            type="text"
            placeholder="Search files, folders..."
            className="w-full bg-slate-50 border border-slate-200 rounded-xl py-2 pl-10 pr-12 text-sm outline-none transition-all focus:bg-white focus:ring-4 focus:ring-indigo-50 focus:border-indigo-400 placeholder:text-slate-400"
          />
          <div className="absolute inset-y-0 right-3 flex items-center">
            <kbd className="hidden sm:inline-flex h-5 items-center gap-1 rounded border border-slate-200 bg-white px-1.5 font-sans text-[10px] font-medium text-slate-400">
              <span className="text-xs">⌘</span>K
            </kbd>
          </div>
        </div>
      </div>
      <div className="flex items-center gap-2">
        <button className="flex h-10 w-10 items-center justify-center rounded-full text-slate-500 hover:bg-slate-100 transition-colors">
          <Sun size={20} />
        </button>
        <div className="h-9 w-9 rounded-full bg-indigo-600 flex items-center justify-center text-white ml-2">
          <User size={18} />
        </div>
      </div>
    </header>
  );
};

export default Header;
