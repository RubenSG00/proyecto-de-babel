// Presentation component: no state, it only renders what it receives
export default function MemberList({ members }) {
    if (members.length === 0) return <p>No members</p>;
    return (
        <ul>
            {members.map((m) => (
                <li key={m.id} id={`member-${m.id}`}>
                    {m.username}
                </li>
            ))}
        </ul>
    );
}
