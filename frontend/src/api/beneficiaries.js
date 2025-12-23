import api from "./api";

export const getBeneficiaries = async () => {
  try {
    const response = await api.get("/beneficiaries");
    console.log(response);
    // Ensure we always return an array
    return Array.isArray(response.data)
      ? response.data
      : response.data.beneficiaries || [];
  } catch (error) {
    console.error("Failed to fetch beneficiaries:", error);
    return [];
  }
};

export const createBeneficiary = async (beneficiary) => {
  try {
    const response = await api.post("/beneficiaries", beneficiary);
    return response.data;
  } catch (error) {
    console.error("Failed to create beneficiary:", error);
    return null;
  }
};
