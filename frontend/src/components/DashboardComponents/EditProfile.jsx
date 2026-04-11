import { useEffect, useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api/axiosConfig";
import LoadingBar from "../loading/LoadingBar";
import { ArrowLeft, Save, Camera, X } from "lucide-react";

const EditProfile = () => {
    const navigate = useNavigate();
    const fileInputRef = useRef(null);

    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const [status, setStatus] = useState(null);

    const [formData, setFormData] = useState({
        firstname: "",
        lastname: "",
        email: ""
    });
    const [selectedFile, setSelectedFile] = useState(null);
    const [previewUrl, setPreviewUrl] = useState(null);

    useEffect(() => {
        const fetchCurrentProfile = async () => {
            try {
                const response = await api.get("profile");
                const { firstname, lastname, email, profilePic } = response.data;
                setFormData({ firstname, lastname, email });
                if (profilePic) setPreviewUrl(profilePic);
            } catch (err) {
                setStatus("error");
            } finally {
                setLoading(false);
            }
        };
        fetchCurrentProfile();
    }, []);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleFileChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            setSelectedFile(file);
            setPreviewUrl(URL.createObjectURL(file));
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSubmitting(true);

        const data = new FormData();
        data.append("firstname", formData.firstname);
        data.append("lastname", formData.lastname);
        data.append("email", formData.email);

        if (selectedFile) {
            data.append("profilePic", selectedFile);
        }

        try {
            await api.post("/profile/edit", data);

            setStatus("success");
            setTimeout(() => navigate("/profile"), 1500);
        } catch (err) {
            console.log(err)
            setStatus("error");
        } finally {
            setSubmitting(false);
        }
    };

    if (loading) return <LoadingBar />;

    return (
        <div className="w-full min-h-screen bg-gray-50 pb-20">
            <div className="h-32 w-full bg-slate-900" />
            <div className="max-w-2xl mx-auto px-6 mt-12">
                <div className="bg-white rounded-3xl shadow-xl border border-gray-100 overflow-hidden">

                    <form onSubmit={handleSubmit} className="p-8 md:p-12 space-y-8">
                        <div className="flex justify-between items-start">
                            <div>
                                <h2 className="text-3xl font-bold text-gray-900">Edit Profile</h2>
                                <p className="text-gray-500 mt-1">Manage your public information</p>
                            </div>
                            <button type="button" onClick={() => navigate(-1)} className="p-2 hover:bg-gray-100 rounded-full transition-colors">
                                <X size={24} className="text-gray-400" />
                            </button>
                        </div>
                        <div className="flex flex-col items-center py-4">
                            <div className="relative group">
                                <img
                                    src={previewUrl || `https://ui-avatars.com/api/?name=${formData.firstname}+${formData.lastname}&size=256`}
                                    alt="Preview"
                                    className="w-32 h-32 rounded-3xl object-cover border-4 border-white shadow-lg bg-gray-100"
                                />
                                <button
                                    type="button"
                                    onClick={() => fileInputRef.current.click()}
                                    className="absolute inset-0 flex items-center justify-center bg-black/40 rounded-3xl opacity-0 group-hover:opacity-100 transition-opacity cursor-pointer"
                                >
                                    <Camera className="text-white" size={28} />
                                </button>
                            </div>
                            <input
                                type="file"
                                ref={fileInputRef}
                                onChange={handleFileChange}
                                className="hidden"
                                accept="image/*"
                            />
                            <p className="mt-3 text-xs font-bold text-blue-600 uppercase tracking-wider cursor-pointer" onClick={() => fileInputRef.current.click()}>
                                Change Photo
                            </p>
                        </div>
                        <div className="space-y-6">
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                <div className="space-y-1">
                                    <label className="text-[10px] font-bold text-gray-400 uppercase tracking-widest ml-1">First Name</label>
                                    <input
                                        type="text"
                                        name="firstname"
                                        value={formData.firstname}
                                        onChange={handleChange}
                                        className="w-full px-5 py-3 bg-gray-50 border border-gray-200 rounded-2xl focus:bg-white focus:ring-2 focus:ring-gray-900 focus:border-transparent transition-all outline-none"
                                    />
                                </div>
                                <div className="space-y-1">
                                    <label className="text-[10px] font-bold text-gray-400 uppercase tracking-widest ml-1">Last Name</label>
                                    <input
                                        type="text"
                                        name="lastname"
                                        value={formData.lastname}
                                        onChange={handleChange}
                                        className="w-full px-5 py-3 bg-gray-50 border border-gray-200 rounded-2xl focus:bg-white focus:ring-2 focus:ring-gray-900 focus:border-transparent transition-all outline-none"
                                    />
                                </div>
                            </div>
                            <div className="space-y-1">
                                <label className="text-[10px] font-bold text-gray-400 uppercase tracking-widest ml-1">Email</label>
                                <input
                                    type="email"
                                    name="email"
                                    value={formData.email}
                                    onChange={handleChange}
                                    className="w-full px-5 py-3 bg-gray-50 border border-gray-200 rounded-2xl focus:bg-white focus:ring-2 focus:ring-gray-900 focus:border-transparent transition-all outline-none"
                                />
                            </div>
                        </div>
                        {status && (
                            <div className={`p-4 rounded-2xl text-sm font-semibold border ${status === 'success' ? 'bg-green-50 border-green-100 text-green-700' : 'bg-red-50 border-red-100 text-red-700'}`}>
                                {status === 'success' ? "Changes saved successfully!" : "Something went wrong. Please try again."}
                            </div>
                        )}
                        <div className="flex flex-col gap-3 pt-4">
                            <button
                                type="submit"
                                disabled={submitting}
                                className="w-full py-4 bg-gray-900 text-white rounded-2xl font-bold shadow-lg hover:bg-gray-800 transition-all active:scale-[0.98] disabled:opacity-50"
                            >
                                {submitting ? "Updating..." : "Save Changes"}
                            </button>
                            <button
                                type="button"
                                onClick={() => navigate("/profile")}
                                className="w-full py-4 text-gray-500 font-semibold hover:text-gray-800 transition-colors"
                            >
                                Cancel
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default EditProfile;