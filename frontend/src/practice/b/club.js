import { useEffect, useState } from "react";
import tokenService from "../../services/token.service";

export default function Club({ clubId }) {
    const [report, setReport] = useState([]);
    const jwt = tokenService.getLocalAccessToken();

    useEffect(() => {
        fetch(`/api/v1/library/clubs/${clubId}/overdue-report`, {
            headers: { Authorization: `Bearer ${jwt}` },
        })
            .then((response) => response.json())
            .then((json) => setReport(json));
    }, [clubId, jwt]);

    return (
        <div>
            <h1>Club info</h1>
            <h3>Overdue report</h3>
            {report.length > 0 && (
                <div>
                    Most overdue: {report[0].user.username} ({report[0].overdueLoans} overdue loans)
                </div>
            )}
        </div>
    );
}
