import Folders from "./components/DashboardComponents/Folders";
import Files from "./components/DashboardComponents/Files";

const Homepage = () => {
  return (
    <main className="flex-1 p-6 overflow-y-auto">
      <div className="rounded-xl border-2 border-dashed border-gray-200 h-full flex items-center justify-center">
        <p className="text-gray-400 font-medium italic">Folders</p>
        <Folders />
        <p className="text-gray-400 font-medium italic">Files</p>
        <Files />
      </div>
    </main>
  );
};

export default Homepage;
