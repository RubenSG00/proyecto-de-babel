import api from "./api";

// One named function per API operation; the /api/v1 prefix and the JWT are added by api.js
export const getAllPartners = async () => {
    const res = await api.get("/partners");
    return res.data;
};

export const getPartnerById = async (id) => {
    const res = await api.get(`/partners/${id}`);
    return res.data;
};
