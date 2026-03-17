import { LogOut, User } from "lucide-react";
import { Link } from "react-router-dom";

function ProfileDropdown() {
  return (
    <div className="relative group">
      <button className="flex items-center gap-2 p-1 pl-3 rounded-2xl hover:bg-slate-100 transition-colors border border-transparent hover:border-slate-200">
        <div className="hidden lg:block text-right">
          <p className="text-xs font-bold text-slate-900 leading-none">Alex Rivera</p>
          <p className="text-[10px] text-slate-500 mt-1">Admin Account</p>
        </div>
        <div className="h-9 w-9 rounded-xl bg-gradient-to-br from-blue-500 to-indigo-600 p-[2px]">
          <div className="h-full w-full rounded-[10px] bg-white flex items-center justify-center text-indigo-600 font-bold">
            AR
          </div>
        </div>
      </button>
      <div className="absolute right-0 mt-2 w-48 bg-white border border-slate-200 rounded-xl shadow-xl opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 overflow-hidden">      
        <Link to={`/profile`}>
          <button className="flex items-center gap-2 w-full px-4 py-2 text-sm text-slate-700 hover:bg-slate-100">
          <User size={16} />
          Profile
        </button>
        </Link>
        <div className="border-t border-slate-200" />
        <button className="flex items-center gap-2 w-full px-4 py-2 text-sm text-red-600 hover:bg-slate-100">
          <LogOut size={16} />
          Logout
        </button>
      </div>
    </div>
  );
}

export default ProfileDropdown;