import { Link } from "react-router-dom";
import { Folder as FolderIcon, MoreVertical } from "lucide-react";

const Folder = ({ id, name = "New Folder" }) => {
  return (
    <Link
      to={`/folders/${id}`}
      className="group flex items-center justify-between p-4 bg-white border border-slate-200 rounded-2xl hover:border-blue-400 hover:shadow-xl hover:shadow-blue-500/5 transition-all duration-200 active:scale-[0.98]">
      <div className="flex items-center gap-4 min-w-0">
        <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-blue-50 text-blue-600 group-hover:bg-blue-600 group-hover:text-white transition-colors duration-200">
          <FolderIcon size={24} fill="currentColor" fillOpacity={0.2} />
        </div>
        <div className="flex flex-col min-w-0">
          <span className="text-sm font-semibold text-slate-700 group-hover:text-blue-700 truncate transition-colors">
            {name}
          </span>
        </div>
      </div>
      <button
        onClick={(e) => {
          e.preventDefault();
          console.log("Open options");
        }}
        className="p-1.5 rounded-lg text-slate-400 hover:bg-slate-100 hover:text-slate-600 opacity-0 group-hover:opacity-100 transition-opacity"
      >
        <MoreVertical size={18} />
      </button>
    </Link>
  );
};

export default Folder;