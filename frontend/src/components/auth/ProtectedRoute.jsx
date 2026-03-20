import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "./AuthContext";

const ProtectedRoute = () => {
  const auth = useAuth();

  if (!auth) {
    console.error("Auth context is not available!");
    return null; 
  }

  const { token } = auth;

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;