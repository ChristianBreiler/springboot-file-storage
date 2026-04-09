import { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import { Link } from "react-router-dom";

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
        console.error("Profile fetch error:", err);
        setError(err.response?.status === 403 ? "Access Denied" : "Failed to load profile");
      } finally {
        setLoading(false);
      }
    };
    fetchProfileData();
  }, []);

  useEffect(() => {
    document.title = "File Storage App - Profile"
  }, []);

  const formatDate = (dateString) => {
    if (!dateString) return "N/A";
    return new Date(dateString).toLocaleDateString(undefined, {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-screen text-gray-500 text-lg font-medium">
        <div className="animate-pulse">Loading profile...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex justify-center items-center h-screen">
        <div className="p-8 bg-white border border-red-100 shadow-xl rounded-2xl text-center max-w-sm">
          <div className="w-16 h-16 bg-red-100 text-red-600 rounded-full flex items-center justify-center mx-auto mb-4">
            <span className="text-2xl font-bold">!</span>
          </div>
          <h2 className="text-gray-900 font-bold text-xl">Authentication Error</h2>
          <p className="text-gray-500 mt-2">{error}</p>
          <button
            onClick={() => window.location.reload()}
            className="mt-6 w-full py-2 bg-gray-900 text-white rounded-lg hover:bg-gray-800 transition-all"
          >
            Try Again
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="w-full min-h-screen bg-gray-50">
      <div className="h-48 w-full bg-linear-to-r from-slate-800 to-slate-900" />
      <div className="max-w-5xl mx-auto px-6">
        <div className="flex flex-col md:flex-row items-center md:items-end gap-6">
          <div className="relative -top-16 md:-top-12 shrink-0">
            <img
              src={`https://ui-avatars.com/api/?name=${data?.firstname}+${data?.lastname}&background=random&size=256`}
              alt="Profile"
              className="w-32 h-32 rounded-2xl border-4 border-white shadow-2xl object-cover bg-gray-200"
            />
          </div>
          <div className="text-center md:text-left pb-4">
            <h1 className="text-3xl font-bold text-gray-900 tracking-tight">
              {data?.firstname} {data?.lastname}
            </h1>
            <div className="flex items-center justify-center md:justify-start gap-2 mt-2">
              <span className="px-3 py-1 bg-blue-50 text-blue-600 text-xs font-bold uppercase tracking-wider rounded-full border border-blue-100">
                {data?.role || "User"}
              </span>
            </div>
          </div>
        </div>
        <div className="mt-4 grid grid-cols-1 md:grid-cols-2 gap-8 bg-white p-8 rounded-2xl shadow-sm border border-gray-100">
          <div className="space-y-1">
            <label className="text-[10px] font-bold text-gray-400 uppercase tracking-widest">
              Email Address
            </label>
            <p className="text-gray-800 font-semibold text-lg">{data?.email}</p>
          </div>
          <div className="space-y-1">
            <label className="text-[10px] font-bold text-gray-400 uppercase tracking-widest">
              Member Since
            </label>
            <p className="text-gray-800 font-semibold text-lg">
              {formatDate(data?.createdAt)}
            </p>
          </div>
        </div>
        <div className="mt-8 flex flex-wrap justify-center md:justify-start gap-4 pb-12">
          <Link to="/profile/edit">
            <button className="px-8 py-2.5 bg-gray-900 text-white rounded-xl font-semibold hover:bg-gray-800 hover:shadow-lg transition-all active:scale-95">
              Edit Profile
            </button>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Profile;