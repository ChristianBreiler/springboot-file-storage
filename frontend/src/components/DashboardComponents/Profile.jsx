import { useEffect, useState } from "react";
import api from "../../api/axiosConfig";

const Profile = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProfileData = async () => {
      setLoading(true);
      try {
        const response = await api.get(`profile`);
        setData(response.data);
      } catch (err) {
        setError(err.response?.status === 403 ? "Access Denied" : "Failed to load profile");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchProfileData();
  }, []);

  if (loading) {
    return <div className="flex justify-center items-center h-64 text-gray-500 text-lg">Loading profile...</div>;
  }

  if (error) {
    return (
      <div className="max-w-md mx-auto mt-10 p-6 bg-red-50 border border-red-200 rounded-lg text-center">
        <h2 className="text-red-700 font-bold text-xl">Oops!</h2>
        <p className="text-red-600 mt-2">{error}</p>
        <button 
          onClick={() => window.location.reload()} 
          className="mt-4 px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700"
        >
          Try Again
        </button>
      </div>
    );
  }

  return (
    <div className="max-w-2xl mx-auto my-10 bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
      <div className="h-24 bg-gradient-to-r from-slate-700 to-slate-900" />
      <div className="px-8 pb-8">
        <div className="relative -top-12 flex items-center gap-6">
            {/* data?.profilePictureUrl */}
          <img
            src={"https://ui-avatars.com/api/?name=" + data?.firstname}
            alt="Profile"
            className="w-28 h-28 rounded-2xl border-4 border-white shadow-md object-cover bg-gray-200"
          />
          <div className="pt-8">
            <h1 className="text-2xl font-bold text-gray-900 leading-tight">
              {data?.firstname} {data?.lastname}
            </h1>
            <span className="inline-block mt-1 px-3 py-0.5 bg-blue-50 text-blue-600 text-xs font-bold uppercase tracking-wider rounded-full">
              {data?.role}
            </span>
          </div>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mt-2">
          <div className="space-y-1">
            <label className="text-xs font-semibold text-gray-400 uppercase">Username</label>
            <p className="text-gray-700 font-medium">@{data?.username}</p>
          </div>
          
          <div className="space-y-1">
            <label className="text-xs font-semibold text-gray-400 uppercase">Email Address</label>
            <p className="text-gray-700 font-medium">{data?.email}</p>
          </div>
        </div>
        <div className="mt-10 pt-6 border-t border-gray-50 flex gap-3">
          <button className="px-6 py-2 bg-gray-900 text-white rounded-lg font-medium hover:bg-gray-800 transition-colors">
            Edit Profile
          </button>
          <button className="px-6 py-2 border border-gray-200 text-gray-600 rounded-lg font-medium hover:bg-gray-50 transition-colors">
            Security Settings
          </button>
        </div>
      </div>
    </div>
  );
};

export default Profile;