import api from "../../api/axiosConfig";
import { useState, useEffect } from "react";
import { Link } from "react-router-dom";

const StorageBar = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStorage = async () => {
      try {
        const { data: storageData } = await api.get(`storage`);
        setData(storageData);
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
      <div className="w-full space-y-2 animate-pulse">
        <div className="h-3 w-full bg-slate-200 rounded-full" />
        <div className="h-3 w-24 bg-slate-100 rounded-full" />
      </div>
    );
  }

  if (!data) return null;

  const percentage = Math.min((data.usedSpace / data.totalSpace) * 100, 100);

  const getBarColor = () => {
    if (percentage > 90) return "bg-red-500";
    if (percentage > 70) return "bg-amber-500";
    return "bg-blue-600";
  };

  return (
    <div className="w-full font-sans p-1">
      <div className="flex justify-between items-end mb-2">
        <div>
          <span className="text-sm font-semibold text-slate-700">
            {data.usedSpace.toFixed(1)} GB
          </span>
          <span className="text-xs text-slate-500 ml-1">used of {data.totalSpace} GB</span>
        </div>
        <Link 
          to="storage_details" 
          className="text-xs font-medium text-blue-600 hover:text-blue-800 transition-colors"
        >
          View Details →
        </Link>
      </div>

      <div className="relative w-full h-2.5 bg-slate-100 rounded-full overflow-hidden border border-slate-200">
        <div
          className={`h-full transition-all duration-500 ease-out ${getBarColor()}`}
          style={{ width: `${percentage}%` }}
        />
      </div>

      {percentage > 90 && (
        <p className="mt-1.5 text-[10px] text-red-600 font-medium">
          Running low on space. Consider upgrading.
        </p>
      )}
    </div>
  );
};

export default StorageBar;