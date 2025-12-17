import React, { createContext, useContext, useState } from "react";
import { getTransactions } from "../api/transactions";

const HistoryContext = createContext(null);

export function HistoryProvider({ children }) {
  const [transactions, setTransactions] = useState([]);
  const [form, setForm] = useState({ accountId: "", limit: 10 });
  const [loading, setLoading] = useState(false);

  const handleInputChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const fetchTransactionsHandler = async () => {
    if (!form.accountId) return alert("Select an account");
    setLoading(true);
    try {
      const data = await getTransactions(form.accountId, form.limit);
      setTransactions(data);
    } catch (error) {
      console.error(error);
      alert("Failed to fetch transactions");
    }
    setLoading(false);
  };

  return (
    <HistoryContext.Provider value={{ transactions, form, handleInputChange, fetchTransactions: fetchTransactionsHandler, loading }}>
      {children}
    </HistoryContext.Provider>
  );
}

export function useHistoryContext() {
  const context = useContext(HistoryContext);
  if (!context) throw new Error("useHistoryContext must be used within HistoryProvider");
  return context;
}
