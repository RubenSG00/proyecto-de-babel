import api from "./api";

export const getRefereeById = async (id) => {
    const res = await api.get(`/referees/${id}`);
    return res.data;
};

export const createReferee = async (referee) => {
    const res = await api.post("/referees", referee);
    return res.data;
};

export const updateReferee = async (id, referee) => {
    const res = await api.put(`/referees/${id}`, referee);
    return res.data;
};
