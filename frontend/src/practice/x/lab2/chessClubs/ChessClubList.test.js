import userEvent from "@testing-library/user-event";
import { rest } from "msw";
import { server } from "../../../../mocks/server";
import { render, screen, within } from "../../../../test-utils";
import ChessClubList from "./ChessClubList";

const clubs = [
    { id: 1, name: "Club Triana", city: "Sevilla" },
    { id: 2, name: "Club Alfil", city: "Madrid" },
    { id: 3, name: "Club Torre", city: "Bilbao" },
];

let deleted;

beforeEach(() => {
    deleted = [];
    jest.spyOn(window, "confirm").mockImplementation(() => true);
    server.use(
        rest.get("*/api/v1/chess-clubs", (req, res, ctx) => res(ctx.json(clubs))),
        rest.delete("*/api/v1/chess-clubs/:id", (req, res, ctx) => {
            deleted.push(req.params.id);
            if (req.params.id === "1") return res(ctx.status(204));
            if (req.params.id === "2") return res(ctx.json({ message: "Club deleted" }));
            return res(ctx.status(403), ctx.json({ message: "Not allowed" }));
        })
    );
});

afterEach(() => jest.restoreAllMocks());

async function deleteRow(name) {
    const row = (await screen.findByText(name)).closest("tr");
    await userEvent.setup().click(within(row).getByRole("button", { name: "Delete" }));
}

describe("ChessClubList", () => {
    test("shows the heading and the clubs", async () => {
        render(<ChessClubList />);
        expect(screen.getByRole("heading", { level: 1, name: "Chess clubs" })).toBeInTheDocument();
        expect(await screen.findByText("Club Torre")).toBeInTheDocument();
    });

    test("a 204 removes the club without any alert", async () => {
        render(<ChessClubList />);
        await deleteRow("Club Triana");
        await screen.findByText("Club Alfil");
        expect(screen.queryByText("Club Triana")).not.toBeInTheDocument();
        expect(deleted).toEqual(["1"]);
        expect(screen.queryByRole("alert")).not.toBeInTheDocument();
    });

    test("a confirmation message is shown as an alert", async () => {
        render(<ChessClubList />);
        await deleteRow("Club Alfil");
        expect(await screen.findByText("Club deleted")).toBeInTheDocument();
        expect(screen.queryByText("Club Alfil")).not.toBeInTheDocument();
    });

    test("an error is shown in the modal and the club stays", async () => {
        render(<ChessClubList />);
        await deleteRow("Club Torre");
        expect(await screen.findByText("Not allowed")).toBeInTheDocument();
        expect(screen.getByText("Club Torre")).toBeInTheDocument();
    });

    test("nothing happens if the user does not confirm", async () => {
        window.confirm.mockImplementation(() => false);
        render(<ChessClubList />);
        await deleteRow("Club Triana");
        expect(screen.getByText("Club Triana")).toBeInTheDocument();
        expect(deleted).toEqual([]);
    });

    test("an error while loading is shown in the modal", async () => {
        server.use(rest.get("*/api/v1/chess-clubs", (req, res, ctx) => res(ctx.status(500), ctx.json({ message: "Database down" }))));
        render(<ChessClubList />);
        expect(await screen.findByText("Database down")).toBeInTheDocument();
    });
});
