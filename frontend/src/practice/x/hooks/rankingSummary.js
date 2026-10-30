import useRanking from "./useRanking";

export default function RankingSummary({ challengeId }) {
    const { ranking } = useRanking(challengeId);
    if (ranking.length === 0) return <p>No participants yet</p>;
    return (
        <div>
            <p>{ranking.length} participants</p>
            <p>Leader: {ranking[0].username}</p>
        </div>
    );
}
