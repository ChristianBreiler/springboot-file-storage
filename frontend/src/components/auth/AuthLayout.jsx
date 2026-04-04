import { Outlet } from "react-router-dom";
import backgroundImage from "../../assets/img.jpg"

function AuthLayout() {
  return (
    <div className="flex min-h-screen items-center justify-center bg-slate-50 p-4"
    style={{ backgroundImage: `url(${backgroundImage})` }}
    >
        <Outlet />
    </div>
  );
}

export default AuthLayout;