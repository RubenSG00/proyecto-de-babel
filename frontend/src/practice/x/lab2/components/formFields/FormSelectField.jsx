import { Input, Label } from "reactstrap";

export default function FormSelectField({ name, label, value, onChange, options, required = true }) {
    return (
        <>
            <Label for={name}>{label}</Label>
            <Input type="select" required={required} name={name} id={name} onChange={onChange} value={value ?? ""}>
                <option value="">None</option>
                {options.map((o) => (
                    <option key={o.value} value={o.value}>
                        {o.label}
                    </option>
                ))}
            </Input>
        </>
    );
}
