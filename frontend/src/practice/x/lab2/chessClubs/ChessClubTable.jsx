import { Button, Table } from "reactstrap";

export default function ChessClubTable({ clubs, onDelete }) {
    return (
        <Table aria-label="chess clubs" className="mt-4">
            <thead>
                <tr>
                    <th className="text-center">Name</th>
                    <th className="text-center">City</th>
                    <th className="text-center">Actions</th>
                </tr>
            </thead>
            <tbody>
                {clubs.map((c) => (
                    <tr key={c.id}>
                        <td className="text-center">{c.name}</td>
                        <td className="text-center">{c.city}</td>
                        <td className="text-center">
                            <Button outline color="danger" onClick={() => onDelete(c.id)}>
                                Delete
                            </Button>
                        </td>
                    </tr>
                ))}
            </tbody>
        </Table>
    );
}
