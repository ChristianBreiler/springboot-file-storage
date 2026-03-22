import api from "../../api/axiosConfig";
import { useState, useEffect } from "react";
import LoadingBar from "../loading/LoadingBar";

const StorageDetails = () => {
  const [storage, setStorage] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStorage = async () => {
      try {
        const response = await api.get("/storage/details");
        setStorage(response.data);
      } catch (err) {
        console.error("Failed to fetch storage stats:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchStorage();
  }, []);

  if (loading) return <LoadingBar />;
  if (!storage) return <div className="p-10 text-slate-600">No data available.</div>;

  const usedPercentage = storage.totalSpace > 0 
    ? (storage.usedSpace / storage.totalSpace) * 100 
    : 0;

  return (
    <div className="min-h-screen w-full bg-white text-slate-900">
      {/* Top Header Bar */}
      <header className="w-full border-b border-slate-200 px-8 py-6 bg-slate-50">
        <h2 className="text-3xl font-extrabold tracking-tight text-slate-900">Storage Management</h2>
        <p className="text-slate-500">Real-time overview of your drive usage and file distribution.</p>
      </header>

      <div className="flex flex-col lg:flex-row w-full h-full">
        
        {/* Left Sidebar: Stats & Progress (Stretches on mobile, fixed width on desktop) */}
        <aside className="w-full lg:w-1/3 p-8 border-r border-slate-200 bg-slate-50/50">
          <div className="sticky top-8">
            <h3 className="text-sm font-bold text-slate-400 uppercase tracking-widest mb-6">Usage Summary</h3>
            
            {/* Circular-style Progress logic or Bar */}
            <div className="mb-10">
              <div className="flex justify-between items-end mb-3">
                <span className="text-4xl font-light">{usedPercentage.toFixed(1)}<span className="text-xl">%</span></span>
                <span className="text-slate-400 pb-1">{storage.usedSpace.toFixed(2)} GB / {storage.totalSpace} GB</span>
              </div>
              <div className="w-full bg-slate-200 rounded-full h-4 overflow-hidden">
                <div 
                  className={`h-full transition-all duration-700 ease-in-out ${
                    usedPercentage > 90 ? 'bg-rose-500' : 'bg-indigo-600'
                  }`}
                  style={{ width: `${Math.min(usedPercentage, 100)}%` }}
                />
              </div>
            </div>

            <div className="space-y-4">
              <div className="flex justify-between p-4 bg-white border border-slate-200 rounded-lg shadow-sm">
                <span className="text-slate-500">Total Folders</span>
                <span className="font-bold text-xl">{storage.numberOfFolders}</span>
              </div>
              <div className="flex justify-between p-4 bg-white border border-slate-200 rounded-lg shadow-sm">
                <span className="text-slate-500">Total Files</span>
                <span className="font-bold text-xl">{storage.numberOfFiles}</span>
              </div>
            </div>
          </div>
        </aside>

        {/* Right Main Content: File List (Stretches to fill remaining space) */}
        <main className="flex-1 p-8 bg-white">
          <h3 className="text-sm font-bold text-slate-400 uppercase tracking-widest mb-6">Largest Files in Storage</h3>
          
          <div className="overflow-hidden border border-slate-200 rounded-xl">
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="bg-slate-50 border-b border-slate-200">
                  <th className="px-6 py-4 text-xs font-bold text-slate-500 uppercase">File Name</th>
                  <th className="px-6 py-4 text-xs font-bold text-slate-500 uppercase text-right">Size</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {(storage.fiveLargestFiles || []).length > 0 ? (
                  storage.fiveLargestFiles.map((file, index) => (
                    <tr key={index} className="hover:bg-slate-50 transition-colors group">
                      <td className="px-6 py-4 text-slate-700 font-medium">
                        <div className="flex items-center">
                          <span className="mr-3 text-slate-300 group-hover:text-indigo-500 transition-colors">📄</span>
                          {file.filename}
                        </div>
                      </td>
                      <td className="px-6 py-4 text-right">
                        <span className="inline-block px-3 py-1 rounded-full bg-indigo-50 text-indigo-700 text-xs font-bold border border-indigo-100">
                          {file.size.toFixed(2)} MB
                        </span>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="2" className="px-6 py-10 text-center text-slate-400 italic">
                      No files available to display.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </main>
      </div>
    </div>
  );
};

export default StorageDetails;