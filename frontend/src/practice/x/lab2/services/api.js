// Copy of the API client of the 2026-27 project template (Lab 2 p.8, p.16): an axios instance
// with the base URL /api/v1 and an interceptor that adds the JWT stored at login to every request.
import axios from "axios";
import tokenService from "../../../../services/token.service";

const api = axios.create({
    baseURL: "/api/v1",
    headers: {
        "Content-Type": "application/json",
    },
});

api.interceptors.request.use((config) => {
    const token = tokenService.getLocalAccessToken();
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default api;
