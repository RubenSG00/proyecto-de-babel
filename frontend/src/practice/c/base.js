import { useEffect, useState } from "react";
import tokenService from "../../services/token.service";

export default function Base({ baseId }) {
    // null = not loaded yet; [] = loaded and empty
    const [ranking, setRanking] = useState(null);
    const jwt = tokenService.getLocalAccessToken();

    useEffect(() => {
        fetch(`/api/v1/airline/bases/${baseId}/pilot-ranking`, {
            headers: { Authorization: `Bearer ${jwt}` },
        })
            .then((response) => response.json())
            .then((json) => setRanking(json));
    }, [baseId, jwt]);

    let content;
    if (ranking === null) content = <p>Loading...</p>;
    else if (ranking.length === 0) content = <div>No ranking available</div>;
    else
        content = (
            <div>
                Best pilot: {ranking[0].pilot.username} ({ranking[0].score} points)
            </div>
        );

    return (
        <div>
            <h1>Base info</h1>
            <h3>Pilot ranking</h3>
            {content}
        </div>
    );
}
