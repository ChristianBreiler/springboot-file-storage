import { FileText, FileImage, File as FileIcon, MoreVertical } from "lucide-react";
import { Link } from "react-router-dom";
import { useMemo } from "react";

const File = ({ id, originalFilename, size, filetype }) => {

  const formatSize = (bytes) => {
    if (!bytes) return "0 Bytes";
    const k = 1024;
    const sizes = ["Bytes", "KB", "MB", "GB"];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(1)) + " " + sizes[i];
  };

  const iconConfig = useMemo(() => {
    switch (filetype) {
      case "PDF":
        return { icon: <FileText size={20} />, color: "text-red-500", bg: "bg-red-50" };
      case "JPG":
        return { icon: <FileImage size={20} />, color: "text-blue-500", bg: "bg-blue-50" };
      default:
        return { icon: <FileIcon size={20} />, color: "text-slate-500", bg: "bg-slate-50" };
    }
  }, [filetype]);

  return (
    <div className="group flex items-center justify-between p-3 bg-white border border-slate-100 rounded-xl hover:border-blue-200 hover:shadow-sm transition-all mb-2">
      <Link to={`/files/${id}`} className="flex items-center gap-4 flex-1 min-w-0">
        <div className={`flex h-10 w-10 shrink-0 items-center justify-center rounded-lg overflow-hidden ${iconConfig.bg} ${iconConfig.color}`}>
          {(
            iconConfig.icon
          )}
        </div>
        <div className="flex flex-col min-w-0">
          <span className="text-sm font-semibold text-slate-700 truncate">
            {originalFilename}
          </span>
          <div className="flex items-center gap-2 text-[11px] text-slate-400">
            <span className="uppercase font-bold">{filetype}</span>
            <span>•</span>
            <span>{formatSize(size)}</span>
          </div>
        </div>
      </Link>
      <button className="p-2 text-slate-400 hover:text-slate-600 hover:bg-slate-50 rounded-lg opacity-0 group-hover:opacity-100 transition-opacity">
        <MoreVertical size={16} />
      </button>
    </div>
  );
};

export default File;