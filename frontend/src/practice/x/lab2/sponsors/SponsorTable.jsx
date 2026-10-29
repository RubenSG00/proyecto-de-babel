import { Table } from "reactstrap";
import SponsorRow from "./SponsorRow";

export default function SponsorTable({ sponsors }) {
    return (
        <div>
            <h1 className="text-center">Sponsors</h1>
            <Table aria-label="sponsors" className="mt-4">
                <thead>
                    <tr>
                        <th className="text-center">Name</th>
                        <th className="text-center">e-mail</th>
                        <th className="text-center">URL</th>
                        <th className="text-center">Logo</th>
                    </tr>
                </thead>
                <tbody>
                    {sponsors.map((s) => (
                        <SponsorRow key={s.id} sponsor={s} />
                    ))}
                </tbody>
            </Table>
        </div>
    );
}
