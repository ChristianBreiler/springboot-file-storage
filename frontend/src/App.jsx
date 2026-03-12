import Sidebar from "./components/Layout/Sidebar";
import Header from "./components/Layout/Header";
import Folders from "./components/DashboardComponents/Folders";
import Files from "./components/DashboardComponents/Files";

function App() {
  return (
    <div className="flex h-screen w-full bg-gray-50">
      <Sidebar />
      <div className="flex flex-1 flex-col">
        <Header />
        <main className="flex-1 p-6 overflow-y-auto">
          <div className="rounded-xl border-2 border-dashed border-gray-200 h-full flex items-center justify-center">
            <p className="text-gray-400 font-medium italic">Folders</p>
            <Folders />
            <p className="text-gray-400 font-medium italic">Files</p>
            <Files />
          </div>
        </main>
      </div>
    </div>
  );
}

export default App;
