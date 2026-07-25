import {useEffect, useState} from "react";
import {Link, useLocation} from "react-router-dom";
import {ChevronRight, Folder, Home} from "lucide-react";
import api from "../../api/axiosConfig.js";

const FolderNode = ({node}) => {
    const [isOpen, setIsOpen] = useState(false);
    const location = useLocation();
    const hasChildren = node.children?.length > 0;

    const toggleOpen = (e) => {
        e.preventDefault();
        e.stopPropagation();
        setIsOpen(!isOpen);
    };

    const isActive = node.uuid && location.pathname === `/folders/${node.uuid}`;

    return (
        <li className="list-none my-0.5">
            <div
                className={`flex items-center rounded-xl transition-all duration-200 group pr-2
                    ${isActive ? "bg-blue-600/10 text-blue-400" : "hover:bg-slate-900 hover:text-slate-100 text-slate-400"}`}
            >
                <button
                    onClick={toggleOpen}
                    disabled={!hasChildren}
                    className={`p-2 flex items-center justify-center transition-transform duration-200 origin-center
                        ${hasChildren ? 'text-slate-500 hover:text-slate-200 cursor-pointer' : 'opacity-0 cursor-default'}
                        ${isOpen ? 'rotate-90' : 'rotate-0'}`}
                >
                    <ChevronRight size={14}/>
                </button>

                {node.uuid ? (
                    <Link
                        to={`/folders/${node.uuid}`}
                        className="flex items-center gap-2.5 py-2 flex-grow text-sm font-medium truncate no-underline text-inherit"
                    >
                        <Folder size={16}
                                className="shrink-0 text-slate-400 group-hover:text-slate-100 transition-colors"/>
                        <span className="truncate">{node.name}</span>
                    </Link>
                ) : (
                    <Link
                        to="/"
                        className="flex items-center gap-2.5 py-2 flex-grow text-sm font-semibold tracking-wide text-slate-200 hover:text-slate-100 cursor-pointer truncate no-underline"
                    >
                        <Home size={16}
                              className="shrink-0 text-slate-400 group-hover:text-slate-200 transition-colors"/>
                        <span className="truncate">{node.name}</span>
                    </Link>
                )}
            </div>

            {hasChildren && isOpen && (
                <ul className="p-0 ml-4 pl-2 border-l border-slate-800/60 list-none space-y-0.5">
                    {node.children.map((child) => (
                        <FolderNode
                            key={child.uuid ?? child.name}
                            node={child}
                        />
                    ))}
                </ul>
            )}
        </li>
    );
};

const FolderTree = ({isSidebarExpanded}) => {
    const [foldertree, setFoldertree] = useState(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchTree = async () => {
            setLoading(true);
            try {
                const {data} = await api.get("/foldertree");
                setFoldertree(data);
            } catch (err) {
                console.error("Fetch failed:", err);
            } finally {
                setLoading(false);
            }
        };

        fetchTree();
    }, []);

    if (!isSidebarExpanded) return null;

    if (loading || !foldertree) return null;

    return (
        <div className="pt-4 border-t border-slate-900/60 mt-4 animate-fade-in">
            <p className="px-3 text-[10px] font-bold uppercase tracking-widest text-slate-500 mb-2">
                Folders
            </p>
            <ul className="p-0 m-0">
                <FolderNode node={foldertree}/>
            </ul>
        </div>
    );
};

export default FolderTree;