export default function TournamentRow({ tournament }) {
    return (
        <tr>
            <td className="text-center">{tournament.name}</td>
            <td className="text-center">{tournament.format}</td>
            <td className="text-center">{tournament.prizePool}</td>
            <td className="text-center"> --- </td>
        </tr>
    );
}
