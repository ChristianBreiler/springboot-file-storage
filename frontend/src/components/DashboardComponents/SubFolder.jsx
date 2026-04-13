import api from "../../api/axiosConfig";
import { ChevronRight, Home } from "lucide-react";
import { useState, useEffect } from "react";
import { Link, useParams } from "react-router-dom";
import Folder from "./Folder";
import File from "./File";
import FileViewPage from "./FileViewPage";
import LoadingBar from "../loading/LoadingBar";
import { DndContext, PointerSensor, useSensor, useSensors, useDroppable } from "@dnd-kit/core";
import { useTranslation } from "react-i18next";

// Helper to make breadcrumbs droppable
const BreadcrumbItem = ({ uuid, children, isHome = false }) => {
  const { setNodeRef, isOver } = useDroppable({
    id: isHome ? "home" : uuid,
  });

  return (
    <div
      ref={setNodeRef}
      className={`rounded-md transition-all ${isOver ? "bg-blue-100 scale-105 text-blue-700 shadow-sm" : ""}`}
    >
      {children}
    </div>
  );
};

const SubFolder = () => {
  const { uuid } = useParams();
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [selectedId, setSelectedId] = useState(null);
  const [cardLayout, setCardLayout] = useState(false);
  const { t } = useTranslation();

  const sensors = useSensors(
    useSensor(PointerSensor, {
      activationConstraint: { distance: 5 },
    })
  );

  useEffect(() => {
    if (data?.name) document.title = `File Storage App - ${data.name}`;
    else document.title = `File Storage App - Home`
  }, [data?.name]);

  useEffect(() => {
    const fetchFolderData = async () => {
      setLoading(true);
      try {
        const path = uuid ? `folders/${uuid}` : "folders/home";
        const response = await api.get(path);
        setData(response.data);
      } catch (err) {
        console.error("Failed to fetch folders:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchFolderData();
  }, [uuid]);

  useEffect(() => {
    const fetchCardLayout = () => {
      const layout = localStorage.getItem("pageLayout");
      setCardLayout(layout === "cards");
    };
    fetchCardLayout();
  }, []);

  async function handleDragEnd(event) {
    const { active, over } = event;
    if (!over) return;

    const fileUuid = active.id;
    const folderUuid = over.id === "home" ? null : over.id;

    if (folderUuid === uuid) return;

    setLoading(true);
    try {
      const response = await api.post("drag/move-file", {
        fileUuid: fileUuid,
        folderUuid: folderUuid
      });
      setData(response.data);
    } catch (err) {
      console.error("Failed to move file:", err);
    } finally {
      setLoading(false);
    }
  }

  if (loading) return <LoadingBar />;
  if (!data) return <div className="p-8 text-red-500">Folder not found.</div>;

  return (
    <DndContext sensors={sensors} onDragEnd={handleDragEnd}>
      <div className="p-6">
        <nav className="flex items-center space-x-1 text-sm font-medium text-slate-500 mb-6 overflow-x-auto whitespace-nowrap pb-2">
          <BreadcrumbItem isHome={true}>
            <Link to="/" className="flex items-center gap-1.5 px-2 py-1 hover:text-blue-600 transition-colors">
              <Home size={16} />
            </Link>
          </BreadcrumbItem>
          {data.parentFolders?.map((folder) => (
            <div key={folder.uuid} className="flex items-center">
              <ChevronRight size={14} className="mx-1 text-slate-400 shrink-0" />
              <BreadcrumbItem uuid={folder.uuid}>
                <Link
                  to={`/folders/${folder.uuid}`}
                  className="hover:text-blue-600 hover:bg-blue-50 px-2 py-1 rounded-md transition-all max-w-30 truncate block"
                >
                  {folder.name}
                </Link>
              </BreadcrumbItem>
            </div>
          ))}
          {data.name && (
            <div className="flex items-center">
              <ChevronRight size={14} className="mx-1 text-slate-400 shrink-0" />
              <span className="text-slate-900 font-semibold px-2 py-1 truncate max-w-50">
                {data.name}
              </span>
            </div>
          )}
        </nav>
        {data.folders?.length > 0 && (
          <>
            <h2 className="text-sm font-bold text-slate-400 uppercase tracking-wider mb-4">{t('folders')}</h2>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-10">
              {data.folders.map((folder) => (
                <Folder key={folder.uuid} uuid={folder.uuid} name={folder.name} />
              ))}
            </div>
          </>
        )}
        {data.files?.length > 0 && (
          <>
            <h2 className="text-sm font-bold text-slate-400 uppercase tracking-wider mb-4">{t('files')}</h2>
            <div className={cardLayout
              ? "grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4"
              : "grid grid-cols-1 gap-2"
            }>
              {data.files.map((file) => (
                <File
                  key={file.uuid}
                  uuid={file.uuid}
                  originalFilename={file.originalFilename}
                  size={file.size}
                  filetype={file.filetype}
                  isDeleted={file.isDeleted}
                  cardLayout={cardLayout}
                  onClick={(uuid) => setSelectedId(uuid)}
                />
              ))}
            </div>
          </>
        )}
        {data.folders?.length === 0 && data.files?.length === 0 && (
          <div className="text-center py-20 bg-slate-50 rounded-2xl border-2 border-dashed border-slate-200">
            <p className="text-slate-400 font-medium">{t('folderEmpty')}</p>
          </div>
        )}
        {selectedId && <FileViewPage uuid={selectedId} onClose={() => setSelectedId(null)} />}
      </div>
    </DndContext>
  );
};

export default SubFolder;