export default function BaseDetail({ ranking, minScore }) {
    // filter returns a new array: the prop is never modified
    const shown = ranking.filter((entry) => minScore === undefined || entry.score >= minScore);

    return (
        <div>
            <table aria-label="pilot ranking">
                <thead>
                    <tr>
                        <th>Pilot</th>
                        <th>Score</th>
                    </tr>
                </thead>
                <tbody>
                    {shown.map((entry) => (
                        <tr key={entry.pilot.id} id={`pilot-${entry.pilot.id}`}>
                            <td>{entry.pilot.username}</td>
                            <td>{entry.score}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <p>
                Showing {shown.length} of {ranking.length} pilots
            </p>
        </div>
    );
}
