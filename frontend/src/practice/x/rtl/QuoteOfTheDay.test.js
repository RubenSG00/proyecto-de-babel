import { rest } from "msw";
import { server } from "../../../mocks/server";
import { render, screen } from "../../../test-utils";
import QuoteOfTheDay from "./quoteOfTheDay";

beforeEach(() => {
    // The fake API only answers the right URL
    server.use(
        rest.get("*/api/v1/quotes/today", (req, res, ctx) =>
            res(ctx.json({ text: "Every chess master was once a beginner.", author: "Irving Chernev" }))
        )
    );
});

describe("QuoteOfTheDay", () => {
    test("shows Loading... while the quote has not arrived", () => {
        render(<QuoteOfTheDay />);
        expect(screen.getByText("Loading...")).toBeInTheDocument();
    });

    test("shows the quote and its author when it arrives", async () => {
        render(<QuoteOfTheDay />);
        // findBy waits: the data arrives asynchronously
        expect(await screen.findByText("Every chess master was once a beginner.")).toBeInTheDocument();
        expect(screen.getByText("— Irving Chernev")).toBeInTheDocument();
        expect(screen.queryByText("Loading...")).not.toBeInTheDocument();
    });
});
