export default function ChallengeDetail({ ranking }) {
    return (
        <div>
            <ul>
                {/* key is only for React and is not written to the DOM: the real id attribute is separate */}
                {ranking.map((entry) => (
                    <li key={entry.user.id} id={`ranking-${entry.user.id}`}>
                        {entry.user.username} - {entry.points}
                    </li>
                ))}
            </ul>
        </div>
    );
}
