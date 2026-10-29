import { useEffect, useState } from "react";
import tokenService from "../services/token.service";

export default function Challenge({ challengeId }) {
    const [ranking, setRanking] = useState([]);
    const jwt = tokenService.getLocalAccessToken();

    // The dependency array makes the request run once per challenge, not on every render
    useEffect(() => {
        fetch(`/api/v1/challenges/${challengeId}/ranking`, {
            headers: { Authorization: `Bearer ${jwt}` },
        })
            .then((response) => response.json())
            .then((json) => setRanking(json));
    }, [challengeId, jwt]);

    return (
        <div>
            <h1>Challenge info</h1>
            <h3>Ranking</h3>
            {ranking.length > 0 && (
                <div>
                    Leader: {ranking[0].user.username} ({ranking[0].points} points)
                </div>
            )}
        </div>
    );
}
