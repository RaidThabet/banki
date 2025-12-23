import React from "react";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import { EyeIcon, EyeSlashIcon } from "@heroicons/react/24/outline";
import { useNavigate } from "react-router-dom";
import { useCheckBalance } from "../contexts/CheckBalanceContext.jsx";

export default function CheckBalance() {
  const navigate = useNavigate();
  const {
    accounts,
    activeAccount,
    setActiveAccountId,
    showBalance,
    setShowBalance,
    balance,
    styles,
    loading,
  } = useCheckBalance();

  if (loading) {
    return (
      <>
        <Navbar />
        <div className="min-h-screen flex justify-center items-center">
          <p className="text-gray-700 text-xl">Loading accounts...</p>
        </div>
        <Footer />
      </>
    );
  }

  if (!activeAccount) {
    return (
      <>
        <Navbar />
        <div className="min-h-screen flex justify-center items-center">
          <p className="text-gray-700 text-xl">No accounts available.</p>
        </div>
        <Footer />
      </>
    );
  }

  return (
    <>
      <Navbar />

      <div className="min-h-screen bg-gray-100 flex justify-center pt-32 px-4 md:px-8 lg:px-40">
        <div
          className="relative w-full max-w-7xl bg-white rounded-3xl p-8 md:p-16 shadow-xl flex flex-col lg:flex-row gap-12 overflow-hidden"
          style={{ boxShadow: styles.glow }}
        >
          {/* ACCOUNTS SIDEBAR */}
          <div className="w-full lg:w-64">
            <h3 className="text-lg font-bold text-gray-800 mb-6">Your Accounts</h3>
            <div className="space-y-3">
              {accounts.map(account => (
                <button
                  key={account.id}
                  onClick={() => { setActiveAccountId(account.id); setShowBalance(false); }}
                  className={`w-full text-left p-4 rounded-xl border transition ${
                    activeAccount.id === account.id
                      ? "bg-gray-900 text-white border-gray-900"
                      : "bg-white hover:bg-gray-100 border-gray-200"
                  }`}
                >
                  <p className="font-semibold">{account.id}</p>
                  {/*<p className="text-sm opacity-80">{account.type}</p>*/}
                </button>
              ))}
            </div>
          </div>

          {/* MAIN CONTENT */}
          <div className="flex-1 flex flex-col md:flex-row gap-12">
            {/* LEFT */}
            <div className="flex-1">
              <h1 className="text-4xl md:text-5xl font-extrabold text-gray-900">{activeAccount.name}</h1>
              <p className="mt-3 text-gray-600">{activeAccount.type} Account Overview</p>

              {/* BALANCE CARD */}
              <div className={`mt-10 bg-gradient-to-br ${styles.card} rounded-2xl p-6 text-white shadow-lg`}>
                <p className="text-sm uppercase tracking-wider opacity-80">Available Balance</p>
                <div className="mt-3 flex items-center gap-4">
                  <span className="text-4xl font-extrabold">
                    {showBalance ? `$${balance.toLocaleString()}` : "••••••••"}
                  </span>
                  <button onClick={() => setShowBalance(!showBalance)} className="hover:scale-110 transition">
                    {showBalance ? <EyeSlashIcon className="w-7 h-7" /> : <EyeIcon className="w-7 h-7" />}
                  </button>
                </div>
                <p className="mt-4 text-sm font-semibold">{styles.label}</p>
              </div>

              <button
                onClick={() => navigate("/history")}
                className="mt-10 bg-gray-900 hover:bg-gray-700 text-white font-semibold px-8 py-3 rounded-xl shadow-lg transition"
              >
                View Transaction History
              </button>
            </div>

            {/* RIGHT */}
            <div className="flex-1 flex justify-center">
              <div className="w-full max-w-sm bg-white rounded-2xl p-6 shadow-lg border border-gray-100">
                {/* STATUS */}
                <div className="mb-6">
                  <p className="font-semibold text-gray-700 mb-2">Account Status</p>
                  <div className={`px-4 py-3 rounded-xl text-sm font-semibold ${styles.status}`}>{styles.label}</div>
                </div>

                {/* QUICK ACTIONS */}
                <div className="mb-6">
                  <p className="font-semibold text-gray-700 mb-3">Quick Actions</p>
                  <div className="space-y-3">
                    <button
                      onClick={() => navigate("/transaction")}
                      className="w-full bg-green-700 hover:bg-green-600 text-white py-3 rounded-xl font-semibold transition"
                    >
                      Transfer Money
                    </button>
                    <button
                      onClick={() => navigate("/addbeneficiary")}
                      className="w-full bg-gray-900 hover:bg-gray-700 text-white py-3 rounded-xl font-semibold transition"
                    >
                      Manage Beneficiaries
                    </button>
                    <button
                      onClick={() => navigate("/accountInfo")}
                      className="w-full bg-gray-200 hover:bg-gray-300 text-gray-900 py-3 rounded-xl font-semibold transition"
                    >
                      Update Account Info
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <Footer />
    </>
  );
}
