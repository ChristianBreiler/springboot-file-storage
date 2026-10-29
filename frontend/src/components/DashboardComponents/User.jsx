import api from "../../api/axiosConfig.js";

const User = ({
                  userUuid,
                  firstname,
                  lastname,
                  username,
                  emailaddress,
                  enabled,
                  role,
                  onToggle,
              }) => {

    const handleTimeOut = async () => {
        try {
            // axios throws on non-2xx responses, no need to check the status here
            await api.post(`admin/toggle_timeout/${userUuid}`, {})
            onToggle();
        } catch (error) {
            console.log(error);
        }
    }

    // Mock: deleting users is not implemented yet
    const handleDelete = () => {
    }

    return (
        <div
            className="w-full rounded-2xl border border-gray-200 bg-white p-6 shadow-sm transition hover:shadow-md">
            <div className="mb-5 flex items-start justify-between gap-4">
                <div>
                    <h2 className="text-xl font-bold text-gray-900">
                        {firstname} {lastname}
                    </h2>
                    <p className="mt-1 text-sm text-gray-500">@{username}</p>
                </div>
                <span
                    className={`shrink-0 rounded-full px-3 py-1 text-xs font-semibold ${
                        enabled
                            ? "bg-green-100 text-green-700"
                            : "bg-red-100 text-red-700"
                    }`}
                >
          {enabled ? "Enabled" : "Disabled"}
        </span>
            </div>
            <div className="space-y-3 border-t border-gray-100 pt-4">
                <div>
                    <p className="text-xs font-medium uppercase tracking-wide text-gray-400">
                        Email
                    </p>
                    <p className="mt-1 break-words text-sm text-gray-700">
                        {emailaddress}
                    </p>
                </div>

                <div>
                    <p className="text-xs font-medium uppercase tracking-wide text-gray-400">
                        Role
                    </p>
                    <p className="mt-1 text-sm font-medium text-gray-700">
                        {role}
                    </p>
                </div>
            </div>
            <div className="mt-6 flex gap-2">
                <button
                    onClick={handleTimeOut}
                    className="inline-flex flex-1 items-center justify-center rounded-xl bg-blue-600 px-4 py-2.5 text-sm font-semibold text-white transition hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                    {enabled ? "Time Out" : "Enable"}
                </button>
                <button
                    onClick={handleDelete}
                    className="inline-flex items-center justify-center rounded-xl border border-red-200 px-4 py-2.5 text-sm font-semibold text-red-600 transition hover:bg-red-50 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2"
                >
                    Delete
                </button>
            </div>
        </div>
    );
};

export default User;
