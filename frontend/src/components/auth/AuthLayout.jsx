import { Outlet } from "react-router-dom";
import backgroundImage from "../../assets/img.jpg"
import i18n from "../../utils/i18n";

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