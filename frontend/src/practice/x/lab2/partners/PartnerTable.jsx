import { Table } from "reactstrap";

export default function PartnerTable({ partners }) {
    return (
        <div>
            <h1 className="text-center">Partners</h1>
            <Table aria-label="partners" className="mt-4">
                <thead>
                    <tr>
                        <th className="text-center">Name</th>
                        <th className="text-center">Sector</th>
                    </tr>
                </thead>
                <tbody>
                    {partners.map((p) => (
                        <tr key={p.id}>
                            <td className="text-center">{p.name}</td>
                            <td className="text-center">{p.sector}</td>
                        </tr>
                    ))}
                </tbody>
            </Table>
        </div>
    );
}
