import { rest } from "msw";
import { server } from "../../mocks/server";
import { render, screen, waitFor } from "../../test-utils";
import Base from "./base.js";

const ranking = [
    { pilot: { id: 4, username: "captain_kirk" }, score: 9 },
    { pilot: { id: 5, username: "spock" }, score: 7 },
];

function serve(body) {
    server.use(
        rest.get("*/api/v1/airline/bases/:id/pilot-ranking", (req, res, ctx) =>
            res(ctx.delay(50), ctx.json(typeof body === "function" ? body(req.params.id) : body))
        )
    );
}

describe("Base info", () => {
    test("Headers are always shown", () => {
        serve(ranking);
        render(<Base baseId="1" />);
        expect(screen.getByRole("heading", { level: 1, name: /base info/i })).toBeInTheDocument();
        expect(screen.getByRole("heading", { level: 3, name: /pilot ranking/i })).toBeInTheDocument();
    });

    test("Shows Loading... until the response arrives, then the best pilot", async () => {
        serve(ranking);
        render(<Base baseId="1" />);
        expect(screen.getByText("Loading...").tagName).toBe("P");
        await waitFor(() => {
            expect(screen.getByText(/best pilot:\s*captain_kirk\s*\(9 points\)/i)).toBeInTheDocument();
        });
        expect(screen.queryByText("Loading...")).not.toBeInTheDocument();
        expect(screen.queryByText("No ranking available")).not.toBeInTheDocument();
    });

    test("Shows a message when the ranking is empty", async () => {
        serve([]);
        render(<Base baseId="1" />);
        await waitFor(() => expect(screen.getByText("No ranking available")).toBeInTheDocument());
        expect(screen.queryByText("Loading...")).not.toBeInTheDocument();
        expect(screen.queryByText(/best pilot/i)).not.toBeInTheDocument();
    });

    test("Requests the ranking of the base received in the props", async () => {
        serve((id) => (id === "3" ? [{ pilot: { id: 8, username: "sulu" }, score: 4 }] : []));
        render(<Base baseId="3" />);
        await waitFor(() => expect(screen.getByText(/best pilot:\s*sulu\s*\(4 points\)/i)).toBeInTheDocument());
    });
});
