import tokenService from "../../../services/token.service";

// Network client: the only place that knows the URL and calls fetch
export async function fetchRanking(challengeId) {
    const jwt = tokenService.getLocalAccessToken();
    const response = await fetch(`/api/v1/challenges/${challengeId}/ranking`, {
        headers: { Authorization: `Bearer ${jwt}` },
    });
    return response.json();
}
