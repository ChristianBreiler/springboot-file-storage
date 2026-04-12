import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../../api/axiosConfig";
import { useTranslation } from "react-i18next";

const Register = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [repeatedPassword, setRepeatedPassword] = useState("")
  const [firstname, setFirstname] = useState("");
  const [lastname, setLastname] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [showRepeatedPassword, setShowRepeatedPassword] = useState(false);
  const navigate = useNavigate();
  const { t } = useTranslation();

  const isEmailAddressValid = (email) => {
    const regex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
    return regex.test(email);
  };

  /**
   * At least one lowercase letter, one uppercase letter,
   * one digit, one special character, and must be at least 8 characters long.
 */

  const isPasswordValid = (password) => {
    const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    return regex.test(password);
  };

  const isPasswordRepeated = () => {
    return password === repeatedPassword;
  }

  const handleRegister = async (e) => {
    e.preventDefault();
    setError("");

    if (!isEmailAddressValid(email)) {
      setError(t('enterValidPassword'));
      return;
    }
    if (!isPasswordValid(password)) {
      setError(t('invalidPassword'));
      return;
    }
    if (!isPasswordRepeated()) {
      setError(t('repeatPassword'));
      return;
    }

    setIsLoading(true);

    try {
      const response = await api.post("auth/register", {
        email,
        password,
        firstname,
        lastname,
      });

      if (response.status === 200 || response.status === 201) {
        setSuccess(t('userCreatedSuccessfully', {
          firstname: firstname,
          lastname: lastname
        }));
      }
    } catch (err) {
      const message = err.response?.data?.message || "Registration failed. Try again.";
      setError(message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="w-full max-w-md bg-white p-8 rounded-2xl shadow-xl border border-gray-100">
      <div className="mb-8 text-center">
        <h2 className="text-3xl font-bold text-gray-900">{t('createAccount')}</h2>
        <p className="text-gray-500 mt-2">{t('createNewAccount')}</p>
      </div>
      <form onSubmit={handleRegister} className="space-y-4">
        {success && (
          <div className="bg-green-50 border-l-4 border-green-500 text-green-700 p-3 rounded text-sm mb-4 animate-in fade-in duration-300">
            <div className="flex items-center">
              <svg className="w-4 h-4 mr-2" fill="currentColor" viewBox="0 0 20 20">
                <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
              </svg>
              {success}
            </div>
          </div>
        )}
        {error && (
          <div className="bg-red-50 border-l-4 border-red-500 text-red-700 p-3 rounded text-sm">
            {error}
          </div>
        )}
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">{t('firstName')}</label>
            <input
              type="text"
              value={firstname}
              onChange={(e) => setFirstname(e.target.value)}
              required
              className="w-full p-3 border border-gray-300 rounded-lg text-gray-900 bg-white focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">{t('lastName')}</label>
            <input
              type="text"
              value={lastname}
              onChange={(e) => setLastname(e.target.value)}
              required
              className="w-full p-3 border border-gray-300 rounded-lg text-gray-900 bg-white focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all"
            />
          </div>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">{t('emailAddress')}</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            className="w-full p-3 border border-gray-300 rounded-lg text-gray-900 bg-white focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all"
          />
        </div>
        <div className="relative">
          <label className="block text-sm font-medium text-gray-700 mb-1">{t('password')}</label>
          <div className="relative">
            <input
              type={showPassword ? "text" : "password"}
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className="w-full p-3 pr-12 border border-gray-300 rounded-lg text-gray-900 bg-white focus:ring-2 focus:ring-indigo-500 outline-none transition-all"
            />
            <button
              type="button"
              onClick={() => setShowPassword(!showPassword)}
              className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-indigo-600 transition-colors"
            >
              {showPassword ? (
                <span className="text-xs font-bold uppercase">{t('hide')}</span>
              ) : (
                <span className="text-xs font-bold uppercase">{t('show')}</span>
              )}
            </button>
          </div>
        </div>
        <div className="relative">
          <label className="block text-sm font-medium text-gray-700 mb-1">{t('repeatPassword')}</label>
          <div className="relative">
            <input
              type={showRepeatedPassword ? "text" : "password"}
              value={repeatedPassword}
              onChange={(e) => setRepeatedPassword(e.target.value)}
              required
              className="w-full p-3 pr-12 border border-gray-300 rounded-lg text-gray-900 bg-white focus:ring-2 focus:ring-indigo-500 outline-none transition-all"
            />
            <button
              type="button"
              onClick={() => setShowRepeatedPassword(!showRepeatedPassword)}
              className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-indigo-600 transition-colors"
            >
              {showRepeatedPassword ? (
                <span className="text-xs font-bold uppercase">{t('hide')}</span>
              ) : (
                <span className="text-xs font-bold uppercase">{t('show')}</span>
              )}
            </button>
          </div>
        </div>
        <button
          type="submit"
          disabled={isLoading}
          className={`w-full py-3 rounded-lg font-semibold text-white transition-all shadow-md 
              ${isLoading ? 'bg-indigo-400 cursor-not-allowed' : 'bg-indigo-600 hover:bg-indigo-700 active:scale-[0.98]'}`}
        >
          {isLoading ? "Creating account..." : t('signIn')}
        </button>
      </form>
      <p className="text-center text-sm text-gray-600 mt-6">
        {t('alreadyHaveAccount')}{" "}
        <Link to="/login" className="text-indigo-600 font-medium hover:underline">
          {t('signIn')}
        </Link>
      </p>
    </div>
  );
};

export default Register;