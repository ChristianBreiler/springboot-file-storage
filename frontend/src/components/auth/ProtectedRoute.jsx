import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "./AuthContext";

// Define this outside the main function to prohibit rendering issues
const isTokenExpired = (token) => {
  if (!token || typeof token !== "string") return true;
  try {
    const parts = token.split('.');
    if (parts.length !== 3) return true;

    const payload = JSON.parse(atob(parts[1]));
    // Buffer of 10 seconds to prevent edge-case loops
    return (payload.exp * 1000) < (Date.now() + 10000);
  } catch (e) {
    return true;
  }
};

const ProtectedRoute = () => {
  const { token, loading } = useAuth();

  if (loading) {
    return <div>Loading...</div>;
  }

  if (!token || isTokenExpired(token)) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;