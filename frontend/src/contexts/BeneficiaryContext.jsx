import { createContext, useContext, useState, useEffect, useCallback } from "react";
import { getBeneficiaries, createBeneficiary } from "../api/beneficiaries";

const BeneficiaryContext = createContext(null);

const TUNISIAN_BANKS = [
  "BIAT",
  "BH Bank",
  "Attijari Bank",
  "Amen Bank",
  "BT",
  "UIB",
];

export const BeneficiaryProvider = ({ children }) => {
  const [beneficiaries, setBeneficiaries] = useState([]); // always array
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const [form, setForm] = useState({
    name: "",
    accountNumber: "",
    bankName: "",
  });

  const fetchBeneficiaries = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await getBeneficiaries();
      setBeneficiaries(Array.isArray(data) ? data : []); // defensive
    } catch (err) {
      console.error("Failed to fetch beneficiaries:", err);
      setError(err);
      setBeneficiaries([]);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchBeneficiaries();
  }, [fetchBeneficiaries]);

  const handleInputChange = (e) => {
    setForm((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const addBeneficiary = async () => {
    if (!form.name || !form.accountNumber || !form.bankName) return;

    try {
      const newBeneficiary = await createBeneficiary(form);
      if (newBeneficiary) setBeneficiaries((prev) => [...prev, newBeneficiary]);
      setForm({ name: "", accountNumber: "", bankName: "" });
    } catch (err) {
      console.error("Failed to add beneficiary:", err);
    }
  };

  return (
    <BeneficiaryContext.Provider
      value={{
        beneficiaries,
        loading,
        error,
        form,
        tunisianBanks: TUNISIAN_BANKS,
        fetchBeneficiaries,
        handleInputChange,
        addBeneficiary,
      }}
    >
      {children}
    </BeneficiaryContext.Provider>
  );
};

export const useBeneficiaries = () => {
  const context = useContext(BeneficiaryContext);
  if (!context) throw new Error("useBeneficiaries must be used within BeneficiaryProvider");
  return context;
};
