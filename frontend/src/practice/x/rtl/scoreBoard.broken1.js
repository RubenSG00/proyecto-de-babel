// Broken copy: the heading is a level-3 heading.
export default function ScoreBoard({ title, players }) {
    return (
        <div>
            <h3>{title}</h3>
            {players.length > 0 ? (
                <ul>
                    {players.map((p) => (
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
