// Broken copy: the empty list is still rendered when there are no players.
export default function ScoreBoard({ title, players }) {
    return (
        <div>
            <h2>{title}</h2>
            <ul>
                {players.map((p) => (
                    <li key={p.username}>
                        {p.username}: {p.points}
                    </li>
                ))}
            </ul>
            {players.length === 0 && <p>No players yet</p>}
        </div>
    );
}
