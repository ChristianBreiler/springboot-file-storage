import Sidebar from "./components/Layout/Sidebar";
import Header from "./components/Layout/Header";
import Homepage from "./components/DashboardComponents/HomeFolder";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import HomeFolder from "./components/DashboardComponents/HomeFolder";
import SubFolder from "./components/DashboardComponents/SubFolder";

const router = createBrowserRouter([
  { 
    path: "/", 
    element: <Homepage />, 
    errorElement: <div> 404 Not found</div> 
  },
  {
  path: "/folders/:id",
  element: <SubFolder />
  }
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
