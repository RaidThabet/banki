import React from "react";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import { useTransfer } from "../contexts/TransferContext.jsx";
import { useAccount } from "../contexts/AccountContext.jsx";

export default function TransferService() {
  const { form, beneficiaries, handleInputChange, handleTransfer } = useTransfer();
  const { activeAccount } = useAccount();

  const handleSubmit = async () => {
    if (!activeAccount) {
      alert("Please select an active account.");
      return;
    }

    if (!form.beneficiaryId || !form.amount) {
      alert("Please select a beneficiary and enter an amount.");
      return;
    }

    await handleTransfer(activeAccount.id);
  };

  return (
    <>
      <Navbar />

      <div className="min-h-screen bg-gray-100 flex justify-center items-start pt-32 px-4 md:px-8 lg:px-40">
        <div
          className="relative w-full max-w-7xl bg-white rounded-3xl p-8 md:p-16 shadow-xl overflow-hidden"
          style={{ boxShadow: "0 0 40px 4px #00C16D" }}
        >
          <h2 className="text-4xl md:text-5xl font-extrabold text-gray-900 mb-10 text-center">
            Bank Transfer
          </h2>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
            {/* Beneficiary Select */}
            <select
              name="beneficiaryId"
              value={form.beneficiaryId}
              onChange={handleInputChange}
              className="p-3 border rounded-xl border-gray-300 bg-white focus:outline-none focus:ring-2 focus:ring-green-400"
            >
              <option value="">Select Beneficiary</option>
              {beneficiaries.map((b) => (
                <option key={b.id} value={b.id}>
                  {b.name} — {b.bankName}
                </option>
              ))}
            </select>

            {/* Amount */}
            <input
              className="p-3 border rounded-xl border-gray-300 focus:outline-none focus:ring-2 focus:ring-green-400"
              placeholder="Amount"
              name="amount"
              type="number"
              min="0"
              value={form.amount}
              onChange={handleInputChange}
            />
          </div>

          <button
            className="bg-green-700 hover:bg-green-500 text-white font-semibold px-8 py-3 rounded-xl shadow-lg transition duration-300"
            onClick={handleSubmit}
          >
            Transfer
          </button>
        </div>
      </div>

      <Footer />
    </>
  );
}
