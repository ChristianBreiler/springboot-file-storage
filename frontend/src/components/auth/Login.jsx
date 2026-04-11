import { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../../api/axiosConfig";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [rememberMe, setRememberMe] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token") || sessionStorage.getItem("token");
    if (token) {
      navigate("/", { replace: true });
    }
  }, [navigate]);

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");
    setIsLoading(true);

    try {
      const response = await api.post("auth/login", {
        email,
        password,
        rememberMe,
      });

      const accessToken = response.data.token;

      if (accessToken) {
        if (rememberMe) {
          localStorage.setItem("token", accessToken);
        } else {
          localStorage.setItem("token", accessToken);
        }
        window.location.href = "/";
      }
    } catch (err) {
      let message;
      if (err.response) {
        message = "Invalid email or password";
      } else {
        message = "Could not reach the server";
      }
      setError(message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
      <div className="w-full max-w-md bg-white p-8 rounded-2xl shadow-xl border border-gray-100">
        <div className="mb-8 text-center">
          <h2 className="text-3xl font-bold text-gray-900">Welcome Back</h2>
          <p className="text-gray-500 mt-2">Please enter your details</p>
        </div>
        <form onSubmit={handleLogin} className="space-y-4">
          {error && (
            <div className="bg-red-50 border-l-4 border-red-500 text-red-700 p-3 rounded text-sm">
              {error}
            </div>
          )}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Email
            </label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              className="w-full p-3 border border-gray-300 rounded-lg text-gray-900 bg-white focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all"
            />
          </div>
          <div className="relative">
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Password
            </label>
            <input
              type={showPassword ? "text" : "password"}
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className="w-full p-3 border border-gray-300 rounded-lg text-gray-900 bg-white focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all pr-12"
            />
            <button
              type="button"
              onClick={() => setShowPassword(!showPassword)}
              className="absolute right-3 top-9.5 text-gray-400 hover:text-indigo-600 transition-colors"
            >
              {showPassword ? "Hide" : "Show"}
            </button>
          </div>
          <div className="flex items-center">
            <input
              type="checkbox"
              id="rememberMe"
              checked={rememberMe}
              onChange={(e) => setRememberMe(e.target.checked)}
              className="h-4 w-4 text-indigo-600 border-gray-300 rounded focus:ring-indigo-500"
            />
            <label
              htmlFor="rememberMe"
              className="ml-2 block text-sm text-gray-700"
            >
              Remember me
            </label>
          </div>
          <button
            type="submit"
            disabled={isLoading}
            className={`w-full py-3 rounded-lg font-semibold text-white transition-all shadow-md 
              ${isLoading
                ? "bg-indigo-400 cursor-not-allowed"
                : "bg-indigo-600 hover:bg-indigo-700 active:scale-[0.98]"
              }`}
          >
            {isLoading ? "Signing in..." : "Sign In"}
          </button>
        </form>
        <p className="text-center text-sm text-gray-600 mt-6">
          Don't have an account?{" "}
          <Link
            to="/register"
            className="text-indigo-600 font-medium hover:underline"
          >
            Create one
          </Link>
        </p>
      </div>
  );
};

export default Login;