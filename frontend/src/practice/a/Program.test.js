import { rest } from "msw";
import { server } from "../../mocks/server";
import { render, screen } from "../../test-utils";
import { waitFor } from "@testing-library/react";
import Program from "./program.js";

const leaderboard = [
    { user: { id: 4, username: "alice", rating: 1500 }, points: 9 },
    { user: { id: 5, username: "bob", rating: 1400 }, points: 7 },
];

describe("Program info", () => {
    beforeEach(() => {
        server.use(
            rest.get("*/api/v1/gym/programs/:id/leaderboard", (req, res, ctx) =>
                req.params.id === "1" ? res(ctx.json(leaderboard)) : res(ctx.status(404))
            )
        );
    });

    test("Headers are correct", () => {
        render(<Program programId="1" />);
        expect(screen.getByRole("heading", { level: 1, name: /program info/i })).toBeInTheDocument();
        expect(screen.getByRole("heading", { level: 3, name: /leaderboard/i })).toBeInTheDocument();
    });

    test("Shows the top member of the leaderboard of program 1", async () => {
        render(<Program programId="1" />);
        await waitFor(() => {
            expect(screen.getByText(/top member:\s*alice\s*\(9 points\)/i)).toBeInTheDocument();
        });
    });

    test("Requests the leaderboard of the program received in the props", async () => {
        const requested = [];
        server.use(
            rest.get("*/api/v1/gym/programs/:id/leaderboard", (req, res, ctx) => {
                requested.push(req.params.id);
                return res(ctx.json([{ user: { id: 9, username: "carol", rating: 1300 }, points: 5 }]));
            })
        );
        render(<Program programId="7" />);
        await waitFor(() => {
            expect(screen.getByText(/top member:\s*carol\s*\(5 points\)/i)).toBeInTheDocument();
        });
        expect(requested).toContain("7");
    });
});
