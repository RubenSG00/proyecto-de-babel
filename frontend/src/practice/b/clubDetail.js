export default function ClubDetail({ report }) {
    // Conditional rendering: an empty report shows a message instead of an empty list
    if (report.length === 0) return <p>No overdue loans</p>;

    return (
        <div>
            <ul>
                {report.map((entry) => (
                    <li key={entry.user.id} id={`overdue-${entry.user.id}`}>
                        {entry.user.username}: {entry.overdueLoans} overdue
                    </li>
                ))}
            </ul>
        </div>
    );
}
