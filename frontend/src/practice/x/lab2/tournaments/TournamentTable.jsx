import { Table } from "reactstrap";
import TournamentRow from "./TournamentRow";

export default function TournamentTable({ tournaments }) {
    return (
        <Table aria-label="tournaments" className="mt-4">
            <thead>
                <tr>
                    <th className="text-center">Name</th>
                    <th className="text-center">Format</th>
                    <th className="text-center">Prize pool</th>
                    <th className="text-center">Actions</th>
                </tr>
            </thead>
            <tbody>
                {tournaments.map((t) => (
                    <TournamentRow key={t.id} tournament={t} />
                ))}
            </tbody>
        </Table>
    );
}
