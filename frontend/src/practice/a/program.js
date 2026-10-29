import { useEffect, useState } from "react";
import tokenService from "../../services/token.service";

export default function Program({ programId }) {
    const [leaderboard, setLeaderboard] = useState([]);
    const jwt = tokenService.getLocalAccessToken();

    // Runs again only when programId (or the token) changes
    useEffect(() => {
        fetch(`/api/v1/gym/programs/${programId}/leaderboard`, {
            headers: { Authorization: `Bearer ${jwt}` },
        })
            .then((response) => response.json())
            .then((json) => setLeaderboard(json));
    }, [programId, jwt]);

    return (
        <div>
            <h1>Program info</h1>
            <h3>Leaderboard</h3>
            {leaderboard.length > 0 && (
                <div>
                    Top member: {leaderboard[0].user.username} ({leaderboard[0].points} points)
                </div>
            )}
        </div>
    );
}
