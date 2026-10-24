import api from "../../api/axiosConfig.js";

const User = ({
                  userUuid,
                  firstname,
                  lastname,
                  username,
                  emailaddress,
                  enabled,
                  role,
              }) => {

    const handleTimeOut = async () => {
        try {
            const response = await api.post(`admin/toggle_timeout/${userUuid}`, {})

            if (!response.ok) {
                throw new Error("Failed to time out");
            }
        } catch (error) {
            console.log(error);
        }
    }

    return (
        <div
            className="w-full max-w-md rounded-2xl border border-gray-200 bg-white p-6 shadow-sm transition hover:shadow-md">
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
            <button
                onClick={handleTimeOut}
                className="mt-6 inline-flex w-full items-center justify-center rounded-xl bg-blue-600 px-4 py-2.5 text-sm font-semibold text-white transition hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
            >
                Time Out
            </button>
        </div>
    );
};

export default User;
