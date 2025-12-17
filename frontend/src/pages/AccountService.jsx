import React from "react";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import { EyeIcon, EyeSlashIcon } from "@heroicons/react/24/outline";
import { useAccount } from "../contexts/AccountContext.jsx";

export default function AccountService() {
  const {
    user,
    showEmail,
    showPhone,
    modalType,
    value,
    setValue,
    toggleEmailVisibility,
    togglePhoneVisibility,
    openModal,
    closeModal,
    updateUser,
    loading,
  } = useAccount();

  if (loading) {
    return (
      <>
        <Navbar />
        <div className="min-h-screen flex justify-center items-center">
          <p className="text-gray-700 text-xl">Loading account info...</p>
        </div>
        <Footer />
      </>
    );
  }

  if (!user) {
    return (
      <>
        <Navbar />
        <div className="min-h-screen flex justify-center items-center">
          <p className="text-gray-700 text-xl">No user data found.</p>
        </div>
        <Footer />
      </>
    );
  }

  return (
    <>
      <Navbar />

      <div className="min-h-screen bg-gray-100 flex justify-center items-start pt-32 px-4 md:px-8 lg:px-40">
        <div
          className="relative w-full max-w-4xl bg-white rounded-3xl p-8 md:p-12 shadow-xl"
          style={{ boxShadow: "0 0 40px 4px #00C16D" }}
        >
          <h2 className="text-4xl font-extrabold text-gray-900 mb-10 text-center">
            Account Information
          </h2>

          {/* USER INFO */}
          <div className="space-y-6">
            {/* NAME */}
            <div>
              <p className="text-sm text-gray-500">Full Name</p>
              <p className="text-xl font-semibold text-gray-900">{user.name}</p>
            </div>

            {/* EMAIL */}
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-500">Email</p>
                <p className="text-lg font-semibold">{showEmail ? user.email : "••••••••••••"}</p>
              </div>

              <div className="flex items-center gap-4">
                <button onClick={toggleEmailVisibility}>
                  {showEmail ? (
                    <EyeSlashIcon className="w-6 h-6 text-gray-600" />
                  ) : (
                    <EyeIcon className="w-6 h-6 text-gray-600" />
                  )}
                </button>

                <button
                  onClick={() => openModal("email")}
                  className="bg-green-700 hover:bg-green-600 text-white px-4 py-2 rounded-lg font-semibold transition"
                >
                  Update
                </button>
              </div>
            </div>

            {/* PHONE */}
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-500">Phone Number</p>
                <p className="text-lg font-semibold">{showPhone ? user.phone : "••••••••••"}</p>
              </div>

              <div className="flex items-center gap-4">
                <button onClick={togglePhoneVisibility}>
                  {showPhone ? (
                    <EyeSlashIcon className="w-6 h-6 text-gray-600" />
                  ) : (
                    <EyeIcon className="w-6 h-6 text-gray-600" />
                  )}
                </button>

                <button
                  onClick={() => openModal("phone")}
                  className="bg-green-700 hover:bg-green-600 text-white px-4 py-2 rounded-lg font-semibold transition"
                >
                  Update
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* MODAL */}
      {modalType && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
          <div className="bg-white rounded-2xl w-full max-w-md p-8 shadow-2xl">
            <h3 className="text-2xl font-bold mb-6 text-gray-900">
              Update {modalType === "email" ? "Email" : "Phone Number"}
            </h3>

            <input
              type={modalType === "email" ? "email" : "text"}
              placeholder={modalType === "email" ? "New Email Address" : "New Phone Number"}
              value={value}
              onChange={(e) => setValue(e.target.value)}
              className="w-full p-3 border rounded-xl border-gray-300 focus:outline-none focus:ring-2 focus:ring-green-400 mb-6"
            />

            <div className="flex justify-end gap-4">
              <button
                onClick={closeModal}
                className="px-5 py-2 rounded-xl bg-gray-200 hover:bg-gray-300 font-semibold transition"
              >
                Cancel
              </button>

              <button
                onClick={updateUser}
                className="px-6 py-2 rounded-xl bg-green-700 hover:bg-green-600 text-white font-semibold transition"
              >
                Save
              </button>
            </div>
          </div>
        </div>
      )}

      <Footer />
    </>
  );
}
