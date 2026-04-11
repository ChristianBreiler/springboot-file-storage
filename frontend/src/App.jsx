import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Layout from "./Layout";
import SubFolder from "./components/DashboardComponents/SubFolder";
import DeletedFiles from "./components/DashboardComponents/DeletedFiles";
import Settings from "./components/DashboardComponents/Settings";
import StorageDetails from "./components/DashboardComponents/StorageDetails";
import Profile from "./components/DashboardComponents/Profile";
import EditProfile from "./components/DashboardComponents/EditProfile";
import AllFiles from "./components/DashboardComponents/AllFiles";
import FileViewPage from "./components/DashboardComponents/FileViewPage";
import NotFound from "./components/error/NotFound";
import Login from "./components/auth/Login";
import { AuthProvider } from "./components/auth/AuthContext";
import ProtectedRoute from "./components/auth/ProtectedRoute";
import Register from "./components/auth/Register";
import AuthLayout from "./components/auth/AuthLayout";
import { useTranslation } from "react-i18next";

const router = createBrowserRouter([
  {
    element: <AuthLayout />,
    children: [
      {
        path: "/login",
        element: <Login />,
      },
      {
        path: "/register",
        element: <Register />,
      },
    ],
  },
  {
    element: <ProtectedRoute />,
    errorElement: <NotFound />,
    children: [
      {
        path: "/",
        element: <Layout />,
        children: [
          { index: true, element: <SubFolder /> },
          { path: "folders/:uuid", element: <SubFolder /> },
          { path: "files", element: <AllFiles /> },
          { path: "files/:uuid", element: <FileViewPage /> },
          { path: "settings", element: <Settings /> },
          { path: "deleted_files", element: <DeletedFiles /> },
          { path: "storage_details", element: <StorageDetails /> },
          { path: "profile", element: <Profile /> },
          { path: "profile/edit", element: <EditProfile /> },
        ],
      },
    ],
  },
]);

function App() {

  return (
    <AuthProvider>
      <RouterProvider router={router} />
    </AuthProvider>
  );
}

export default App;