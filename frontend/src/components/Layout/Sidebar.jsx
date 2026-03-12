import { Home, Settings, Trash } from "lucide-react";

const Sidebar = () => {
  const menuItems = [
    { icon: <Home size={20} />, label: "Dashboard" },
    { icon: <Trash size={20} />, label: "Deleted" },
    { icon: <Settings size={20} />, label: "Settings" },
  ];

  return (
    <aside className="w-64 bg-slate-900 text-slate-300 flex flex-col">
      <div className="h-16 flex items-center px-6 text-white font-bold text-xl border-b border-slate-800">
        File Storage
      </div>
      <nav className="flex-1 p-4 space-y-2">
        {menuItems.map((item, index) => (
          <a
            key={index}
            href="#"
            className="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-slate-800 hover:text-white transition-colors"
          >
            {item.icon}
            <span className="text-sm font-medium">{item.label}</span>
          </a>
        ))}
      </nav>
    </aside>
  );
};

export default Sidebar;
