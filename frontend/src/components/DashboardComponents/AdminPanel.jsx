import {useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import api from "../../api/axiosConfig.js";
import User from "./User.jsx";
import LoadingBar from "../loading/LoadingBar.jsx";

const AdminPanel = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const {t} = useTranslation()

    const fetchUsers = async () => {
        try {
            const response = await api.get(`/admin/users`);
            setUsers(response.data);
        } catch (err) {
            console.error("Users fetch error:", err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    if (loading) return <LoadingBar/>

    return (
        <div className="p-6">
            <div className="mb-6 flex items-center justify-between">
                <h2 className="text-sm font-bold text-gray-400 uppercase tracking-wider">{t('adminPanel')}</h2>
                <button
                    onClick={fetchUsers}
                    className="rounded-xl border border-gray-200 bg-white px-4 py-2 text-sm font-semibold text-gray-700 transition hover:bg-gray-50"
                >
                    Refresh
                </button>
            </div>
            <div className="grid grid-cols-1 gap-4 md:grid-cols-2 xl:grid-cols-3">
                {users.length > 0 && users.map((user) => (
                    <User
                        key={user.uuid}
                        userUuid={user.uuid}
                        firstname={user.firstname}
                        lastname={user.lastname}
                        username={user.username}
                        emailaddress={user.emailaddress}
                        enabled={user.enabled}
                        role={user.role}
                        onToggle={fetchUsers}
                    />
                ))}
            </div>
        </div>
    )
};

export default AdminPanel;
