import {useDroppable} from "@dnd-kit/core";

const BreadcrumbItem = ({uuid, children, isHome = false}) => {
    const {setNodeRef, isOver} = useDroppable({
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

export default BreadcrumbItem;