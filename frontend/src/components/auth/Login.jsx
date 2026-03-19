import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api/axiosConfig";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const response = await api.post("auth/login", { email, password });
      const accessToken = response.data.token;

      if (accessToken) {
        localStorage.setItem("token", accessToken);
        navigate("/");
      }
    } catch (err) {
      setError(err);
      console.error("Login Error:", err);
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-indigo-500 to-purple-600">
      <form
        onSubmit={handleLogin}
        className="w-full max-w-sm bg-white p-8 rounded-xl shadow-lg space-y-5"
      >
        <h2 className="text-2xl font-semibold text-center text-gray-800">
          Welcome Back
        </h2>

        {error && (
          <div className="bg-red-100 text-red-600 text-sm p-2 rounded text-center">
            {error}
          </div>
        )}

        <input
  type="email"
  placeholder="Email address"
  value={email}
  onChange={(e) => setEmail(e.target.value)}
  required
  className="w-full p-3 border border-gray-300 rounded-md text-gray-900 bg-white focus:outline-none focus:ring-2 focus:ring-indigo-500"
/>

<input
  type="password"
  placeholder="Password"
  value={password}
  onChange={(e) => setPassword(e.target.value)}
  required
  className="w-full p-3 border border-gray-300 rounded-md text-gray-900 bg-white focus:outline-none focus:ring-2 focus:ring-indigo-500"
/>

        <button
          type="submit"
          className="w-full bg-indigo-600 text-white py-3 rounded-md font-medium hover:bg-indigo-700 transition"
        >
          Sign In
        </button>
      </form>
    </div>
  );
};

export default Login;