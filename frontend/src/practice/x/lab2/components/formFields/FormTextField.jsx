import { Input, Label } from "reactstrap";

// Reusable field (DRY): a label associated with its input through for/id
export default function FormTextField({ name, label, value, onChange, type = "text", required = true }) {
    return (
        <>
            <Label for={name}>{label}</Label>
            <Input
                type={type}
                required={required}
                name={name}
                id={name}
                onChange={onChange}
                value={value ?? ""} // a controlled input must never receive undefined/null
            />
        </>
    );
}
