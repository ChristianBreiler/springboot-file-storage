import Sidebar from "./components/Layout/Sidebar";
import Header from "./components/Layout/Header";
import Homepage from "./components/DashboardComponents/Homepage";
import { createBrowserRouter, RouterProvider } from "react-router-dom";

const router = createBrowserRouter([
  { path: "/", element: <Homepage />, errorElement: <div> 404 Not found</div> },
  {
    path: "/folder/:id",
    element: <Homepage />,
    errorElement: <div> 404 Not found</div>,
  },
]);

function App() {
  return (
    <div className="flex h-screen w-full bg-gray-50">
      <Sidebar />
      <div className="flex flex-1 flex-col">
        <Header />
        <RouterProvider router={router} />
      </div>
    </div>
  );
}

export default App;
