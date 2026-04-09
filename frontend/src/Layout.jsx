import Sidebar from "./components/Layout/Sidebar";
import Header from "./components/Layout/Header";
import { Outlet } from "react-router-dom";
import { useTranslation } from "react-i18next";

function Layout() {

  const { t } = useTranslation();

  return (
    <div className="flex h-screen w-full bg-gray-50">
      <Sidebar />
      <div className="flex flex-1 flex-col">
        <Header />
        <Outlet />
      </div>
    </div>
  );
}

export default Layout;