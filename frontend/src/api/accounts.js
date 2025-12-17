import api from "./api";

// Fetch all accounts
export const getAccounts = async () => {
  try {
    const response = await api.get("/accounts");
    // Ensure we always return an array
    // Adjust this if your backend wraps the accounts in an object
    return Array.isArray(response.data) ? response.data : response.data.accounts || [];
  } catch (error) {
    console.error("Failed to fetch accounts:", error);
    return []; // return empty array on error
  }
};

// Fetch account by id
export const getAccountById = async (id) => {
  try {
    const response = await api.get(`/accounts/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Failed to fetch account ${id}:`, error);
    return null;
  }
};

// Create new account
export const createAccount = async (account) => {
  try {
    const response = await api.post("/accounts", account);
    return response.data;
  } catch (error) {
    console.error("Failed to create account:", error);
    return null;
  }
};
