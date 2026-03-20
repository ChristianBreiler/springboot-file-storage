// AuthContext.js
import { createContext, useContext, useState } from "react";
// REMOVE THIS: import { useNavigate } from "react-router-dom"; 

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  // REMOVE THIS: const navigate = useNavigate();
  const [token, setToken] = useState(localStorage.getItem("token"));

  const logout = () => {
    localStorage.removeItem("token");
    setToken(null);
    // Use this instead to break the dependency loop:
    window.location.href = "/login"; 
  };

  return (
    <AuthContext.Provider value={{ token, setToken, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);