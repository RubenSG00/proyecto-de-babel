import { rest } from "msw";
import { server } from "../../mocks/server";
import { render, screen } from "../../test-utils";
import { waitFor } from "@testing-library/react";
import Club from "./club.js";

const report = [
    { user: { id: 4, username: "reader_one" }, overdueLoans: 3 },
    { user: { id: 5, username: "reader_two" }, overdueLoans: 1 },
];

describe("Club info", () => {
    beforeEach(() => {
        server.use(
            rest.get("*/api/v1/library/clubs/:id/overdue-report", (req, res, ctx) =>
                req.params.id === "1" ? res(ctx.json(report)) : res(ctx.status(404))
            )
        );
    });

    test("Headers are correct", () => {
        render(<Club clubId="1" />);
        expect(screen.getByRole("heading", { level: 1, name: /club info/i })).toBeInTheDocument();
        expect(screen.getByRole("heading", { level: 3, name: /overdue report/i })).toBeInTheDocument();
    });

    test("Shows the first member of the report of club 1", async () => {
        render(<Club clubId="1" />);
        await waitFor(() => {
            expect(screen.getByText(/most overdue:\s*reader_one\s*\(3 overdue loans\)/i)).toBeInTheDocument();
        });
    });

    test("Requests the report of the club received in the props", async () => {
        server.use(
            rest.get("*/api/v1/library/clubs/:id/overdue-report", (req, res, ctx) =>
                req.params.id === "2"
                    ? res(ctx.json([{ user: { id: 9, username: "bookworm" }, overdueLoans: 7 }]))
                    : res(ctx.status(404))
            )
        );
        render(<Club clubId="2" />);
        await waitFor(() => {
            expect(screen.getByText(/most overdue:\s*bookworm\s*\(7 overdue loans\)/i)).toBeInTheDocument();
        });
    });
});
