import { Folder as FolderIcon } from "lucide-react";
import api from "../../api/axiosConfig";

{/* Represents the Homefolder with its folders and files */}

const HomeFolder = ({ name }) => {
  return (
    <div className="group flex items-center gap-3 px-3 py-2 rounded-lg cursor-pointer transition-all duration-200 hover:bg-indigo-50 active:bg-indigo-100">
      <div className="flex items-center justify-center">
        <FolderIcon
          size={20}
          className="text-slate-400 group-hover:text-indigo-600 transition-colors duration-200"
          fill="currentColor"
          fillOpacity={0}
          strokeWidth={2}
        />
      </div>
      <span className="text-sm font-medium text-slate-700 group-hover:text-indigo-900 truncate">
        {name}
      </span>
    </div>
  );
};

export default HomeFolder;
