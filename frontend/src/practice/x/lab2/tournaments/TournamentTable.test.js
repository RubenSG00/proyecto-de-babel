import userEvent from "@testing-library/user-event";
import { render, screen, within } from "../../../../test-utils";
import TournamentTable from "./TournamentTable";

const tournaments = [
    { id: 1, name: "Seville Open", format: "SWISS", prizePool: 5000 },
    { id: 2, name: "Blitz Masters", format: "ROUND_ROBIN", prizePool: 1200 },
];

describe("TournamentTable with actions", () => {
    test("each row has an Edit link to its tournament", () => {
        render(<TournamentTable tournaments={tournaments} onDelete={() => {}} />);
        const rows = within(screen.getByRole("table", { name: "tournaments" })).getAllByRole("row").slice(1);
        expect(within(rows[0]).getByRole("link", { name: "Edit" })).toHaveAttribute("href", "/tournaments/1");
        expect(within(rows[1]).getByRole("link", { name: "Edit" })).toHaveAttribute("href", "/tournaments/2");
        expect(screen.queryByText("---")).not.toBeInTheDocument();
    });

    test("Delete reports the id of its row and nothing is deleted while rendering", async () => {
        const onDelete = jest.fn();
        render(<TournamentTable tournaments={tournaments} onDelete={onDelete} />);
        expect(onDelete).not.toHaveBeenCalled();
        const rows = within(screen.getByRole("table", { name: "tournaments" })).getAllByRole("row").slice(1);
        await userEvent.setup().click(within(rows[1]).getByRole("button", { name: "Delete" }));
        expect(onDelete).toHaveBeenCalledTimes(1);
        expect(onDelete).toHaveBeenCalledWith(2);
    });
});
