// AuthContext.jsx
import { createContext, useContext, useState, useEffect } from "react";
import api from "../../api/axiosConfig";
import i18n from "../../utils/i18n";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const initializeAuth = () => {
      const savedToken = localStorage.getItem("token");
      if (savedToken) {
        setToken(savedToken);
      }
      setLoading(false);
    };
    initializeAuth();
  }, []);

  useEffect(() => {
    const fetchSettings = async () => {
      if (!token) return;

      try {
        const response = await api.get(`settings`);
        // Set settings for a user in the localstorage to simplify things for this project
        localStorage.setItem("pageLayout", response.data.pageLayout);
        localStorage.setItem("language", response.data.language);
        i18n.changeLanguage(response.data.language)
      } catch (err) {
        console.error("Failed to fetch settings:", err);
      }
    };

    fetchSettings();
  }, [token]);

  const logout = () => {
    localStorage.removeItem("token");
    setToken(null);
  };

  return (
    <AuthContext.Provider value={{ token, setToken, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);