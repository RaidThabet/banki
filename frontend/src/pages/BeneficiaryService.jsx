import React, { useEffect } from "react";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import { useBeneficiaries } from "../contexts/BeneficiaryContext.jsx";

export default function BeneficiaryService() {
  const {
    beneficiaries,
    form,
    handleInputChange,
    fetchBeneficiaries,
    addBeneficiary,
    tunisianBanks,
  } = useBeneficiaries();

  useEffect(() => {
    fetchBeneficiaries();
  }, [fetchBeneficiaries]);

  return (
    <>
      <Navbar />

      <div className="min-h-screen bg-gray-100 flex justify-center items-start pt-32 px-4 md:px-8 lg:px-40">
        <div
          className="relative w-full max-w-7xl bg-white rounded-3xl p-8 md:p-16 shadow-xl overflow-hidden"
          style={{ boxShadow: "0 0 40px 4px #00C16D" }}
        >
          <h2 className="text-4xl md:text-5xl font-extrabold text-gray-900 mb-10 text-center">
            Beneficiary Management
          </h2>

          {/* FORM */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
            <input
              className="p-3 border rounded-xl border-gray-300 focus:outline-none focus:ring-2 focus:ring-green-400"
              placeholder="Beneficiary Name"
              name="name"
              value={form.name}
              onChange={handleInputChange}
            />

            <input
              className="p-3 border rounded-xl border-gray-300 focus:outline-none focus:ring-2 focus:ring-green-400"
              placeholder="Account Number"
              name="accountNumber"
              value={form.accountNumber}
              onChange={handleInputChange}
            />

            <select
              name="bankName"
              value={form.bankName}
              onChange={handleInputChange}
              className="p-3 border rounded-xl border-gray-300 focus:outline-none focus:ring-2 focus:ring-green-400 md:col-span-2 bg-white"
            >
              <option value="">Select Bank</option>
              {tunisianBanks.map((bank) => (
                <option key={bank} value={bank}>
                  {bank}
                </option>
              ))}
            </select>
          </div>

          <button
            className="bg-green-700 hover:bg-green-500 text-white font-semibold px-8 py-3 rounded-xl shadow-lg transition duration-300"
            onClick={addBeneficiary}
          >
            Add Beneficiary
          </button>

          {/* BENEFICIARY LIST */}
          <div className="mt-12">
            <h3 className="text-xl font-bold mb-6">Your Beneficiaries</h3>

            {beneficiaries.length === 0 ? (
              <p className="text-gray-500 text-center">
                No beneficiaries found.
              </p>
            ) : (
              <ul className="space-y-4">
                {beneficiaries.map((b) => (
                  <li
                    key={b.id}
                    className="flex justify-between items-center p-4 border rounded-xl border-gray-200 shadow-sm"
                  >
                    <div>
                      <p className="font-semibold">
                        {b?.name ?? "Unnamed beneficiary"}
                      </p>
                      <p className="text-sm text-gray-600">
                        {b?.accountNumber} • {b?.bankName}
                      </p>
                    </div>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>
      </div>

      <Footer />
    </>
  );
}
