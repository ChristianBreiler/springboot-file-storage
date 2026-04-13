import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api/axiosConfig";
import LoadingBar from "../loading/LoadingBar";
import { X } from "lucide-react";
import { useTranslation } from "react-i18next";

const EditProfile = () => {
    const navigate = useNavigate();

    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const [status, setStatus] = useState(null);
    const [errorMsg, setErrorMsg] = useState("");

    const [formData, setFormData] = useState({
        firstname: "",
        lastname: "",
        email: ""
    });
    const { t } = useTranslation();

    useEffect(() => {
        const fetchCurrentProfile = async () => {
            try {
                const response = await api.get("profile");
                const { firstname, lastname, email } = response.data;
                setFormData({ firstname, lastname, email });
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
        if (errorMsg) setErrorMsg("");
    };

    const isEmailAddressValid = (email) => {
        const regex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
        return regex.test(email);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrorMsg("");

        if (!formData.firstname.trim() || !formData.lastname.trim()) {
            setErrorMsg(t('namesRequired') || "Names cannot be empty");
            return;
        }

        if (!isEmailAddressValid(formData.email)) {
            setErrorMsg(t('enterValidEmail') || "Please enter a valid email address");
            return;
        }

        setSubmitting(true);

        const data = new FormData();
        data.append("firstname", formData.firstname);
        data.append("lastname", formData.lastname);
        data.append("email", formData.email);

        try {
            await api.post("/profile/edit", data);
            setStatus("success");
            setTimeout(() => navigate("/profile"), 1500);
        } catch (err) {
            console.log(err);
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
                                <h2 className="text-3xl font-bold text-gray-900">{t('editProfile')}</h2>
                            </div>
                            <button type="button" onClick={() => navigate(-1)} className="p-2 hover:bg-gray-100 rounded-full transition-colors">
                                <X size={24} className="text-gray-400" />
                            </button>
                        </div>
                        <div className="flex flex-col items-center py-4">
                            <div className="relative group">
                                <img
                                    src={`https://ui-avatars.com/api/?name=${formData?.firstname}+${formData?.lastname}&background=random&size=128`}
                                    alt="Profile"
                                    className="w-32 h-32 rounded-xl border-2 border-white shadow-xl object-cover bg-gray-200"
                                />
                            </div>
                        </div>
                        <div className="space-y-6">
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                <div className="space-y-1">
                                    <label className="text-[10px] font-bold text-gray-400 uppercase tracking-widest ml-1">{t('firstName')}</label>
                                    <input
                                        type="text"
                                        name="firstname"
                                        value={formData.firstname}
                                        onChange={handleChange}
                                        className="w-full px-5 py-3 bg-gray-50 border border-gray-200 rounded-2xl focus:bg-white focus:ring-2 focus:ring-gray-900 focus:border-transparent transition-all outline-none"
                                    />
                                </div>
                                <div className="space-y-1">
                                    <label className="text-[10px] font-bold text-gray-400 uppercase tracking-widest ml-1">{t('lastName')}</label>
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
                                <label className="text-[10px] font-bold text-gray-400 uppercase tracking-widest ml-1">{t('emailAddress')}</label>
                                <input
                                    type="email"
                                    name="email"
                                    value={formData.email}
                                    onChange={handleChange}
                                    className="w-full px-5 py-3 bg-gray-50 border border-gray-200 rounded-2xl focus:bg-white focus:ring-2 focus:ring-gray-900 focus:border-transparent transition-all outline-none"
                                />
                            </div>
                        </div>
                        {errorMsg && (
                            <div className="p-4 rounded-2xl text-sm font-semibold border bg-amber-50 border-amber-100 text-amber-700">
                                {errorMsg}
                            </div>
                        )}
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
                                {submitting ? "Updating..." : t('saveChanges')}
                            </button>
                            <button
                                type="button"
                                onClick={() => navigate("/profile")}
                                className="w-full py-4 text-gray-500 font-semibold hover:text-gray-800 transition-colors"
                            >
                                {t('cancel')}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default EditProfile;