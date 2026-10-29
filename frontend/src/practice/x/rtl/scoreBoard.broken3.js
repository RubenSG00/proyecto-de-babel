// Broken copy: only the first three players are shown.
export default function ScoreBoard({ title, players }) {
    return (
        <div>
            <h2>{title}</h2>
            {players.length > 0 ? (
                <ul>
                    {players.slice(0, 3).map((p) => (
                        <li key={p.username}>
                            {p.username}: {p.points}
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No players yet</p>
            )}
        </div>
    );
}
