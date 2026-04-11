import Sidebar from "./components/Layout/Sidebar";
import Header from "./components/Layout/Header";
import { Outlet } from "react-router-dom";

function Layout() {

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