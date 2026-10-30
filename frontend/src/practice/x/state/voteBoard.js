import { useState } from "react";

// No state: data comes down through props and the click goes up through onVote
export function VoteButton({ label, count, onVote }) {
    return (
        <button onClick={onVote}>
            {label} ({count})
        </button>
    );
}

// The state lives in the closest common parent of the buttons and the total
export default function VoteBoard() {
    const [votes, setVotes] = useState({ yoga: 0, pilates: 0 });

    return (
        <div>
            <h2>Vote for the next session</h2>
            <VoteButton label="Yoga" count={votes.yoga} onVote={() => setVotes({ ...votes, yoga: votes.yoga + 1 })} />
            <VoteButton
                label="Pilates"
                count={votes.pilates}
                onVote={() => setVotes({ ...votes, pilates: votes.pilates + 1 })}
            />
            <p>Total votes: {votes.yoga + votes.pilates}</p>
        </div>
    );
}
