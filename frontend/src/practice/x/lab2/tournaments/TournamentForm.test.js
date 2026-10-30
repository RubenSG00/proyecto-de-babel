import fs from "fs";
import path from "path";
import userEvent from "@testing-library/user-event";
import { fireEvent, render, screen, within } from "../../../../test-utils";
import FormTextField from "../components/formFields/FormTextField";
import FormSelectField from "../components/formFields/FormSelectField";
import TournamentForm from "./TournamentForm";

const tournament = { id: 3, name: "Junior Cup", description: "Knockout for juniors", logoUrl: null,
    format: "KNOCKOUT", prizePool: 0 };

describe("FormTextField", () => {
    test("renders a label associated with an input", async () => {
        const onChange = jest.fn();
        render(<FormTextField name="city" label="City" value="Sevilla" onChange={onChange} />);
        const input = screen.getByLabelText("City");
        expect(input).toHaveAttribute("name", "city");
        expect(input).toHaveAttribute("id", "city");
        expect(input).toHaveAttribute("type", "text");
        expect(input).toBeRequired();
        expect(input).toHaveValue("Sevilla");
        await userEvent.setup().type(input, "x");
        expect(onChange).toHaveBeenCalled();
    });

    test("accepts a type, an optional field and a null value", () => {
        render(<FormTextField name="seats" label="Seats" value={null} onChange={() => {}} type="number" required={false} />);
        const input = screen.getByLabelText("Seats");
        expect(input).toHaveAttribute("type", "number");
        expect(input).not.toBeRequired();
        expect(input).toHaveValue(null);
    });
});

describe("FormSelectField", () => {
    test("renders None followed by the options", () => {
        const options = [{ value: "A", label: "Option A" }, { value: "B", label: "Option B" }];
        render(<FormSelectField name="kind" label="Kind" value="B" onChange={() => {}} options={options} />);
        const select = screen.getByLabelText("Kind");
        expect(select.tagName).toBe("SELECT");
        const rendered = within(select).getAllByRole("option");
        expect(rendered.map((o) => o.textContent)).toEqual(["None", "Option A", "Option B"]);
        expect(rendered[0]).toHaveValue("");
        expect(select).toHaveValue("B");
    });
});

describe("TournamentForm", () => {
    test("edit mode: heading and the values of the tournament", () => {
        render(<TournamentForm tournament={tournament} onChange={() => {}} onSubmit={() => {}} />);
        expect(screen.getByRole("heading", { level: 2, name: "Edit Tournament" })).toBeInTheDocument();
        expect(screen.getByLabelText("Name")).toHaveValue("Junior Cup");
        expect(screen.getByLabelText("Description")).toHaveValue("Knockout for juniors");
        expect(screen.getByLabelText("Logo URL")).toHaveValue("");
        expect(screen.getByLabelText("Format")).toHaveValue("KNOCKOUT");
        expect(screen.getByLabelText("Prize pool")).toHaveAttribute("type", "number");
        expect(screen.getByLabelText("Prize pool")).toHaveValue(0);
        const options = within(screen.getByLabelText("Format")).getAllByRole("option").map((o) => o.value);
        expect(options).toEqual(["", "SWISS", "ROUND_ROBIN", "KNOCKOUT"]);
        expect(screen.getByRole("button", { name: "Save" })).toBeInTheDocument();
        expect(screen.getByRole("link", { name: "Cancel" })).toHaveAttribute("href", "/tournaments");
    });

    test("add mode", () => {
        render(<TournamentForm tournament={{ ...tournament, id: null }} onChange={() => {}} onSubmit={() => {}} />);
        expect(screen.getByRole("heading", { level: 2, name: "Add Tournament" })).toBeInTheDocument();
    });

    test("reports changes and the submission to its parent", async () => {
        const onChange = jest.fn();
        const onSubmit = jest.fn((e) => e.preventDefault());
        const { container } = render(<TournamentForm tournament={tournament} onChange={onChange} onSubmit={onSubmit} />);
        await userEvent.setup().type(screen.getByLabelText("Name"), "s");
        expect(onChange).toHaveBeenCalled();
        fireEvent.submit(container.querySelector("form"));
        expect(onSubmit).toHaveBeenCalledTimes(1);
    });

    test("is built only with the reusable field components", () => {
        const source = fs.readFileSync(path.join(__dirname, "TournamentForm.jsx"), "utf8");
        expect(source).toMatch(/FormTextField/);
        expect(source).toMatch(/FormSelectField/);
        expect(source).not.toMatch(/<Input\b|<input\b|<select\b/);
        // the options are declared in a constant outside the component
        expect(source).toMatch(/^(export )?const \w+\s*=\s*\[/m);
    });
});
