import { Button, Form, Input, Label } from "reactstrap";

const LEVEL_OPTIONS = ["REGIONAL", "NATIONAL", "INTERNATIONAL"];

// Presentational form: it does not know how the referee is loaded or saved
export default function RefereeForm({ referee, onChange, onSubmit }) {
    return (
        <div>
            <h2>{referee.id ? "Edit Referee" : "Add Referee"}</h2>
            <Form onSubmit={onSubmit}>
                <Label for="name">Name</Label>
                <Input type="text" required name="name" id="name" value={referee.name ?? ""} onChange={onChange} />
                <Label for="email">E-mail</Label>
                <Input type="email" required name="email" id="email" value={referee.email ?? ""} onChange={onChange} />
                <Label for="level">Level</Label>
                <Input type="select" required name="level" id="level" value={referee.level ?? ""} onChange={onChange}>
                    {LEVEL_OPTIONS.map((l) => (
                        <option key={l} value={l}>
                            {l}
                        </option>
                    ))}
                </Input>
                <Button type="submit" color="primary">
                    Save
                </Button>
            </Form>
        </div>
    );
}
