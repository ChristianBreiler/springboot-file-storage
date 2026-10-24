import {useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import api from "../../api/axiosConfig.js";
import User from "./User.jsx";
import LoadingBar from "../loading/LoadingBar.jsx";

const AdminPanel = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const {t} = useTranslation()

    useEffect(() => {
        const fetchUsers = async () => {
            setLoading(true);
            try {
                const response = await api.get(`/admin/users`);
                setUsers(response.data);
            } catch (err) {
                console.error("Profile fetch error:", err);
            } finally {
                setLoading(false);
            }
        };
        fetchUsers();
    }, []);

    if (loading) return <LoadingBar/>

    return (
        <div>
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
                />
            ))}
        </div>

    )
};

export default AdminPanel;
