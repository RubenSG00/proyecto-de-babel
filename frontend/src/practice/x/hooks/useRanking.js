import { useEffect, useState } from "react";
import { fetchRanking } from "./rankingClient";

// The hook holds the state and maps the API format to the local one { username, points }
export default function useRanking(challengeId) {
    const [ranking, setRanking] = useState([]);

    useEffect(() => {
        let ignore = false;
        fetchRanking(challengeId).then((entries) => {
            if (!ignore) setRanking(entries.map((e) => ({ username: e.user.username, points: e.points })));
        });
        return () => {
            ignore = true;
        };
    }, [challengeId]);

    return { ranking };
}
