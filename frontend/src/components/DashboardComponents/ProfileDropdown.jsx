import { LogOut, User, ChevronDown } from "lucide-react";
import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../../api/axiosConfig";
import { useTranslation } from "react-i18next";

function ProfileDropdown() {
  const navigate = useNavigate();
  const [data, setData] = useState(null);
  const { t } = useTranslation();

  const logOut = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  useEffect(() => {
    const fetchUserInformation = async () => {
      try {
        const response = await api.get("userinformation");
        setData(response.data);
      } catch (err) {
        console.error(err);
      }
    };
    fetchUserInformation();
  }, []);

  return (
    <div className="relative group">
      <button className="flex items-center gap-3 p-1 pl-3 rounded-2xl hover:bg-slate-100 transition-all border border-transparent hover:border-slate-200">
        <div className="hidden lg:block text-right">
          <p className="text-xs font-bold text-slate-900 leading-none">
            {data?.firstname} {data?.lastname}
          </p>
          <p className="text-[10px] text-slate-500 mt-1 uppercase font-bold tracking-wider">
            {data?.role || "User"}
          </p>
        </div>
        <div className="shrink-0 h-10 w-10">
          <img
            src={`https://ui-avatars.com/api/?name=${data?.firstname}+${data?.lastname}&background=random&size=128`}
            alt="Profile"
            className="w-full h-full rounded-xl border-2 border-white shadow-xl object-cover bg-gray-200"
          />
        </div>
        <ChevronDown size={14} className="text-slate-400 mr-1" />
      </button>
      <div className="absolute right-0 mt-2 w-52 bg-white border border-slate-200 rounded-2xl shadow-xl opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 translate-y-1 group-hover:translate-y-0 z-50 overflow-hidden">
        <div className="p-2">
          <Link to="/profile">
            <button className="flex items-center gap-3 w-full px-4 py-2.5 text-sm font-medium text-slate-700 hover:bg-slate-50 rounded-xl transition-colors">
              <User size={18} className="text-slate-400" />
              {t('viewProfile')}
            </button>
          </Link>
          <div className="my-1 border-t border-slate-100" />
          <button
            onClick={logOut}
            className="flex items-center gap-3 w-full px-4 py-2.5 text-sm font-medium text-red-600 hover:bg-red-50 rounded-xl transition-colors"
          >
            <LogOut size={18} />
            {t('signOut')}
          </button>
        </div>
      </div>
    </div>
  );
}

export default ProfileDropdown;