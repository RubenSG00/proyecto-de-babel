import { useEffect, useState } from "react";
import tokenService from "../../../services/token.service";

// Custom hook: the state and the request, reusable by any component
export default function useMembers(programId) {
    const [members, setMembers] = useState([]);
    const jwt = tokenService.getLocalAccessToken();

    useEffect(() => {
        let ignore = false;
        fetch(`/api/v1/gym/programs/${programId}/members`, { headers: { Authorization: `Bearer ${jwt}` } })
            .then((response) => response.json())
            .then((json) => {
                if (!ignore) setMembers(json);
            });
        return () => {
            ignore = true;
        };
    }, [programId, jwt]);

    return { members };
}
