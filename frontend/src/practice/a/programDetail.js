export default function ProgramDetail({ leaderboard }) {
    return (
        <div>
            <ul>
                {/* The position comes from the index; key and id use the stable user id */}
                {leaderboard.map((entry, index) => (
                    <li key={entry.user.id} id={`member-${entry.user.id}`}>
                        #{index + 1} {entry.user.username} ({entry.points} pts)
                    </li>
                ))}
            </ul>
        </div>
    );
}
