import React, { useEffect } from "react";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import { useHistoryContext } from "../contexts/HistoryContext.jsx";

export default function TransactionHistoryService() {
  const { transactions, form, handleInputChange, fetchTransactions } = useHistoryContext();

  // Automatically fetch transactions for the default account on mount
  useEffect(() => {
    if (form.accountId) {
      fetchTransactions();
    }
  }, [form.accountId]);

  return (
    <>
      <Navbar />

      <div className="min-h-screen bg-gray-100 flex justify-center items-start pt-32 px-4 md:px-8 lg:px-40">
        <div
          className="relative w-full max-w-7xl bg-white rounded-3xl p-8 md:p-16 shadow-xl overflow-hidden"
          style={{ boxShadow: "0 0 40px 4px #00C16D" }}
        >
          <h2 className="text-4xl md:text-5xl font-extrabold text-gray-900 mb-10 text-center">
            Transaction History
          </h2>

          {/* INPUTS */}
          <div className="flex flex-col md:flex-row gap-4 mb-8">
            <input
              className="flex-1 p-3 border rounded-xl border-gray-300 focus:outline-none focus:ring-2 focus:ring-green-400"
              placeholder="Account ID"
              name="accountId"
              value={form.accountId}
              onChange={handleInputChange}
            />
            <input
              className="w-full md:w-32 p-3 border rounded-xl border-gray-300 focus:outline-none focus:ring-2 focus:ring-green-400"
              placeholder="Limit"
              name="limit"
              type="number"
              value={form.limit}
              onChange={handleInputChange}
            />
            <button
              className="bg-green-700 hover:bg-green-500 text-white font-semibold px-8 py-3 rounded-xl shadow-lg transition duration-300"
              onClick={fetchTransactions}
            >
              Fetch
            </button>
          </div>

          {/* TRANSACTIONS TABLE */}
          {transactions.length > 0 ? (
            <div className="overflow-x-auto">
              <table className="w-full text-left border-collapse">
                <thead>
                  <tr className="border-b border-gray-300">
                    <th className="p-3">ID</th>
                    {/*<th className="p-3">From</th>*/}
                    {/*<th className="p-3">To</th>*/}
                    <th className="p-3">Amount</th>
                    <th className="p-3">Type</th>
                    <th className="p-3">Status</th>
                  </tr>
                </thead>
                <tbody>
                  {transactions.map((t) => (
                    <tr key={t.id} className="border-b border-gray-200">
                      <td className="p-3">{t.id}</td>
                      {/*<td className="p-3">{t.from}</td>*/}
                      {/*<td className="p-3">{t.to}</td>*/}
                      <td className="p-3">${t.amount.toLocaleString()}</td>
                      <td className="p-3 capitalize">{t.type}</td>
                      <td className="p-3 capitalize">{t.status}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          ) : (
            <p className="text-center text-gray-500 mt-6">No transactions found.</p>
          )}
        </div>
      </div>

      <Footer />
    </>
  );
}
