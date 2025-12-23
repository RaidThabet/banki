import React, { createContext, useContext, useState, useEffect } from "react";
import { getBeneficiaries } from "../api/beneficiaries";
import { createTransaction } from "../api/transactions";
import {useAccount} from "./AccountContext.jsx";

const TransferContext = createContext(null);

export function TransferProvider({ children }) {
  const { activeAccount } = useAccount();
  const [form, setForm] = useState({ beneficiaryId: "", amount: "", accountId: "" });
  const [beneficiaries, setBeneficiaries] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (activeAccount?.id) {
      setForm((prev) => ({ ...prev, accountId: activeAccount.id }));
    }
  }, [activeAccount]);

  const fetchBeneficiaries = async () => {
    setLoading(true);
    try {
      const data = await getBeneficiaries();
      setBeneficiaries(data);
    } catch (error) {
      console.error("Failed to fetch beneficiaries:", error);
    }
    setLoading(false);
  };

  useEffect(() => {
    fetchBeneficiaries();
  }, []);

  const handleInputChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleTransfer = async () => {
    if (!form.beneficiaryId || !form.amount || !form.accountId) {
      console.log(form.beneficiaryId, form.amount, form.accountId);
      return alert("Please fill all fields");
    }

    try {
      await createTransaction(form.accountId, { beneficiaryId: form.beneficiaryId, amount: parseFloat(form.amount) });
      alert("Transfer successful!");
      setForm({ beneficiaryId: "", amount: "", accountId: "" });
    } catch (error) {
      console.error(error);
      alert("Transfer failed");
    }
  };

  return (
    <TransferContext.Provider value={{ form, setForm, beneficiaries, handleInputChange, handleTransfer, loading }}>
      {children}
    </TransferContext.Provider>
  );
}

export function useTransfer() {
  const context = useContext(TransferContext);
  if (!context) throw new Error("useTransfer must be used within TransferProvider");
  return context;
}
