import { Link } from "react-router-dom";

// Page for 404 Errors
const NotFound = () => {
  return (
    <div className="h-screen w-full flex flex-col items-center justify-center bg-slate-50 text-center px-6">
      <h1 className="text-8xl font-extrabold text-slate-900 tracking-tight">
        404
      </h1>
      <p className="mt-4 text-lg text-slate-600">
        Oops! The page you are looking for does not exist.
      </p>
      <Link
        to="/"
        className="mt-6 px-6 py-3 bg-slate-900 text-white rounded-xl hover:bg-slate-800 transition-all active:scale-95 shadow-lg shadow-slate-200"
      >
        Go back home
      </Link>
    </div>
  );
};

export default NotFound;