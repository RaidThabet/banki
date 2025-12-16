import React, { createContext, useContext, useState, useEffect, useMemo } from "react";
import { getAccounts } from "../api/accounts";

const AccountContext = createContext(null);

export function AccountProvider({ children }) {
  const [accounts, setAccounts] = useState([]);
  const [activeAccountId, setActiveAccountId] = useState(null);
  const [showBalance, setShowBalance] = useState(false);
  const [loading, setLoading] = useState(false);

  // Fetch accounts from backend
  const fetchAccounts = async () => {
    setLoading(true);
    try {
      const data = await getAccounts(); // GET /accounts
      setAccounts(data);
      if (data.length > 0 && !activeAccountId) setActiveAccountId(data[0].id);
    } catch (error) {
      console.error("Error fetching accounts:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAccounts();
  }, []);

  const activeAccount = useMemo(
    () => accounts.find((a) => a.id === activeAccountId),
    [accounts, activeAccountId]
  );

  const balance = activeAccount?.balance ?? 0;

  const balanceState = useMemo(() => {
    if (balance < 0) return "negative";
    if (balance < 500) return "warning";
    return "positive";
  }, [balance]);

  const stateStyles = {
    positive: {
      glow: "0 0 40px 4px #00C16D",
      card: "from-green-700 to-green-500",
      status: "bg-green-100 text-green-700",
      label: "Healthy • No issues detected",
    },
    warning: {
      glow: "0 0 40px 4px #FACC15",
      card: "from-yellow-500 to-yellow-400",
      status: "bg-yellow-100 text-yellow-700",
      label: "Low Balance • Attention needed",
    },
    negative: {
      glow: "0 0 40px 4px #EF4444",
      card: "from-red-600 to-red-500",
      status: "bg-red-100 text-red-700",
      label: "Overdraft • Immediate action required",
    },
  };

  return (
    <AccountContext.Provider
      value={{
        accounts,
        activeAccount,
        setActiveAccountId,
        showBalance,
        setShowBalance,
        balance,
        styles: stateStyles[balanceState],
        loading,
        fetchAccounts,
      }}
    >
      {children}
    </AccountContext.Provider>
  );
}

export function useAccount() {
  const context = useContext(AccountContext);
  if (!context) throw new Error("useAccount must be used within AccountProvider");
  return context;
}
