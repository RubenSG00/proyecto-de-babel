import { render, screen, within } from "../../test-utils";
import BaseDetail from "./baseDetail";

const ranking = [
    { pilot: { id: 4, username: "captain_kirk" }, score: 9 },
    { pilot: { id: 5, username: "spock" }, score: 7 },
    { pilot: { id: 6, username: "uhura" }, score: 3 },
];

describe("Base ranking table", () => {
    test("shows every pilot when minScore is not given", () => {
        render(<BaseDetail ranking={ranking} />);
        const table = screen.getByRole("table", { name: "pilot ranking" });
        const rows = within(table).getAllByRole("row");
        // header row + 3 pilots
        expect(rows).toHaveLength(4);
        expect(within(rows[0]).getAllByRole("columnheader").map((c) => c.textContent.trim())).toEqual(["Pilot", "Score"]);
        expect(within(rows[1]).getAllByRole("cell").map((c) => c.textContent.trim())).toEqual(["captain_kirk", "9"]);
        expect(screen.getByText("Showing 3 of 3 pilots").tagName).toBe("P");
    });

    test("shows only the pilots that reach minScore", () => {
        render(<BaseDetail ranking={ranking} minScore={7} />);
        const table = screen.getByRole("table", { name: "pilot ranking" });
        expect(within(table).getAllByRole("row")).toHaveLength(3);
        expect(within(table).queryByText("uhura")).not.toBeInTheDocument();
        expect(screen.getByText("Showing 2 of 3 pilots")).toBeInTheDocument();
    });

    test("minScore 0 shows every pilot", () => {
        render(<BaseDetail ranking={ranking} minScore={0} />);
        expect(screen.getByText("Showing 3 of 3 pilots")).toBeInTheDocument();
    });

    test("each pilot row has the id pilot-<id>", () => {
        const { container } = render(<BaseDetail ranking={ranking} minScore={5} />);
        expect(container.querySelector("#pilot-4")).toBeInTheDocument();
        expect(container.querySelector("#pilot-5")).toBeInTheDocument();
        expect(container.querySelector("#pilot-6")).not.toBeInTheDocument();
    });

    test("does not modify the ranking received in the props", () => {
        const copy = [...ranking];
        render(<BaseDetail ranking={ranking} minScore={8} />);
        expect(ranking).toEqual(copy);
        expect(screen.getByText("Showing 1 of 3 pilots")).toBeInTheDocument();
    });
});
