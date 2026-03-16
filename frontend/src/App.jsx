import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Layout from "./Layout";
import Homepage from "./components/DashboardComponents/HomeFolder";
import SubFolder from "./components/DashboardComponents/SubFolder";
import DeletedFiles from "./components/DashboardComponents/DeletedFiles";
import Settings from "./components/DashboardComponents/Settings";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Layout />,
    errorElement: <div>404 Not Found</div>,
    children: [
      { index: true, element: <Homepage /> },
      { path: "folders/:id", element: <SubFolder /> },
      { path: "settings", element: <Settings /> },
      { path: "deleted_files", element: <DeletedFiles /> },
    ],
  },
]);

function App() {
  return <RouterProvider router={router} />;
}

export default App;