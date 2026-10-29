import api from "./api";

export const getAllClubs = async () => {
    const res = await api.get("/chess-clubs");
    return res.data;
};

export const deleteClub = async (id) => {
    const res = await api.delete(`/chess-clubs/${id}`);
    return res.data;
};
