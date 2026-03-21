import api from "../../api/axiosConfig";
import { useState, useEffect } from "react";
import { Link } from "react-router-dom";

const StorageBar = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStorage = async () => {
      try {
        // Change "storage" to the exact endpoint your Spring Boot controller uses
        const response = await api.get(`storage`); 
        setData(response.data);
      } catch (err) {
        console.error("Failed to fetch storage data:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchStorage();
  }, []);

  if (loading) {
    return (
      <div className="w-full space-y-3 px-2 py-4 animate-pulse">
        <div className="flex justify-between">
          <div className="h-3 w-16 bg-slate-800 rounded" />
          <div className="h-3 w-12 bg-slate-800 rounded" />
        </div>
        <div className="h-2 w-full bg-slate-800 rounded-full" />
      </div>
    );
  }

  if (!data) return (
    <div className="px-2 py-4 text-[10px] text-slate-600 italic">
      Storage info unavailable
    </div>
  );

  const used = data.usedSpace || 0;
  const total = data.totalSpace || 1;
  const percentage = Math.min((used / total) * 100, 100);

  const getBarColor = () => {
    if (percentage > 90) return "bg-red-500 shadow-[0_0_10px_rgba(239,68,68,0.3)]";
    if (percentage > 70) return "bg-amber-500 shadow-[0_0_10px_rgba(245,158,11,0.3)]";
    return "bg-blue-500 shadow-[0_0_10px_rgba(59,130,246,0.3)]";
  };

  return (
    <div className="w-full font-sans px-2 py-4 bg-slate-900/40 rounded-xl border border-slate-800/50">
      <div className="flex justify-between items-end mb-3">
        <div>
          <p className="text-xs font-bold text-slate-100">
            {used.toFixed(1)} GB <span className="text-slate-500 font-normal">/ {total} GB</span>
          </p>
        </div>
        <Link 
          to="/storage_details"
          className="text-[10px] font-bold text-blue-400 hover:text-blue-300 transition-colors uppercase tracking-tight"
        >
         Details
        </Link>
      </div>

      <div className="relative w-full h-1.5 bg-slate-800 rounded-full overflow-hidden">
        <div
          className={`h-full transition-all duration-700 ease-in-out ${getBarColor()}`}
          style={{ width: `${percentage}%` }}
        />
      </div>

      {percentage > 90 && (
        <p className="mt-2 text-[10px] text-red-400 font-medium leading-tight">
          Storage almost full!
        </p>
      )}
    </div>
  );
};

export default StorageBar;