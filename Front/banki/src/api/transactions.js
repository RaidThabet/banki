import api from "./api";

export const getTransactions = async (accountId) => {
  const response = await api.get(`/accounts/${accountId}/transactions`);
  return response.data;
};

export const createTransaction = async (accountId, transaction) => {
  const response = await api.post(`/accounts/${accountId}/transactions`, transaction);
  return response.data;
};
