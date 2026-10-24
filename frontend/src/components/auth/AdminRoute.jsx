import { useEffect, useState } from "react";
import { Navigate, Outlet } from "react-router-dom";
import api from "../../api/axiosConfig";
import LoadingBar from "../loading/LoadingBar";

// Only renders the child routes if the authenticated user is an admin
const AdminRoute = () => {
  const [isAdmin, setIsAdmin] = useState(null);

  useEffect(() => {
    const checkAdmin = async () => {
      try {
        const response = await api.get("users/me/is_admin");
        setIsAdmin(response.data.isAdmin);
      } catch (err) {
        console.error(err);
        setIsAdmin(false);
      }
    };
    checkAdmin();
  }, []);

  if (isAdmin === null) return <LoadingBar />;

  return isAdmin ? <Outlet /> : <Navigate to="/" replace />;
};

export default AdminRoute;
