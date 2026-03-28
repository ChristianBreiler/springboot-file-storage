import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Layout from "./Layout";
import Homepage from "./components/DashboardComponents/HomeFolder";
import SubFolder from "./components/DashboardComponents/SubFolder";
import DeletedFiles from "./components/DashboardComponents/DeletedFiles";
import Settings from "./components/DashboardComponents/Settings";
import StorageDetails from "./components/DashboardComponents/StorageDetails";
import Profile from "./components/DashboardComponents/Profile";
import AllFiles from "./components/DashboardComponents/AllFiles";
import NotFound from "./components/error/NotFound";
import Login from "./components/auth/Login";
import { AuthProvider } from "./components/auth/AuthContext";
import ProtectedRoute from "./components/auth/ProtectedRoute";
import Register from "./components/auth/Register";

const router = createBrowserRouter([
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/register",
    element: <Register />,
  },
  {
    element: <ProtectedRoute />,
    errorElement: <NotFound />,
    children: [
      {
        path: "/",
        element: <Layout />,
        children: [
          { index: true, element: <Homepage /> },
          { path: "folders/:id", element: <SubFolder /> },
          { path: "files", element: <AllFiles /> },
          { path: "settings", element: <Settings /> },
          { path: "deleted_files", element: <DeletedFiles /> },
          { path: "storage_details", element: <StorageDetails /> },
          { path: "profile", element: <Profile /> },
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