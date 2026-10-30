import { Button, Form } from "reactstrap";
import { Link } from "react-router-dom";
import FormTextField from "../components/formFields/FormTextField";
import FormSelectField from "../components/formFields/FormSelectField";

// Outside the component: not recreated on every render, and the allowed values are in one place
const FORMAT_OPTIONS = [
    { value: "SWISS", label: "SWISS" },
    { value: "ROUND_ROBIN", label: "ROUND_ROBIN" },
    { value: "KNOCKOUT", label: "KNOCKOUT" },
];

// Presentational form: it does not know how the tournament is loaded or saved
export default function TournamentForm({ tournament, onChange, onSubmit }) {
    return (
        <div>
            <h2>{tournament.id ? "Edit Tournament" : "Add Tournament"}</h2>
            <Form onSubmit={onSubmit}>
                <FormTextField name="name" label="Name" value={tournament.name} onChange={onChange} />
                <FormTextField name="description" label="Description" value={tournament.description} onChange={onChange} />
                <FormTextField name="logoUrl" label="Logo URL" value={tournament.logoUrl} onChange={onChange} required={false} />
                <FormSelectField name="format" label="Format" value={tournament.format} onChange={onChange} options={FORMAT_OPTIONS} />
                <FormTextField name="prizePool" label="Prize pool" type="number" value={tournament.prizePool} onChange={onChange} />
                <Button type="submit" color="primary">
                    Save
                </Button>{" "}
                <Link to="/tournaments" className="btn btn-secondary">
                    Cancel
                </Link>
            </Form>
        </div>
    );
}
