import { OrbitProgress } from "react-loading-indicators";

const LoadingBar = () => {
    return (
        <div className="flex h-64 w-full items-center justify-center">
            <OrbitProgress variant="track-disc" speedPlus="-5" easing="ease-in-out" />
        </div>
    );
}

export default LoadingBar;