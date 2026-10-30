import { MemoryRouter } from "react-router-dom";
import { render, screen } from "@testing-library/react";
import { rest } from "msw";
import { server } from "../../../../mocks/server";
import { loginWith } from "./fakeJwt";
import PracticeRoutes from "./PracticeRoutes";

let tokenIsValid;

function renderAt(url) {
    render(
        <MemoryRouter initialEntries={[url]}>
            <PracticeRoutes />
        </MemoryRouter>
    );
}

beforeEach(() => {
    tokenIsValid = true;
    server.use(rest.get("*/api/v1/auth/validate", (req, res, ctx) => res(ctx.json(tokenIsValid))));
});

afterEach(() => localStorage.clear());

describe("PracticeRoutes", () => {
    test("the public page is available for everybody", () => {
        renderAt("/lab/public");
        expect(screen.getByRole("heading", { name: "Welcome to the chess tournament" })).toBeInTheDocument();
    });

    test("the public page is also available for a logged-in user", () => {
        loginWith("PLAYER");
        renderAt("/lab/public");
        expect(screen.getByRole("heading", { name: "Welcome to the chess tournament" })).toBeInTheDocument();
    });

    test("without login, the protected pages are not available", () => {
        renderAt("/lab/sponsors");
        expect(screen.getByRole("heading", { level: 1, name: "Not available" })).toBeInTheDocument();
    });

    test("an ADMIN reaches the sponsors page, after PrivateRoute validates the token", async () => {
        loginWith("ADMIN");
        renderAt("/lab/sponsors");
        expect(await screen.findByRole("heading", { name: "Sponsors administration" })).toBeInTheDocument();
    });

    test("an ADMIN cannot reach the page of the players", () => {
        loginWith("ADMIN");
        renderAt("/lab/tournaments");
        expect(screen.getByRole("heading", { level: 1, name: "Not available" })).toBeInTheDocument();
    });

    test("a PLAYER reaches the tournaments page", async () => {
        loginWith("PLAYER");
        renderAt("/lab/tournaments");
        expect(await screen.findByRole("heading", { name: "My tournaments" })).toBeInTheDocument();
    });

    test("a PLAYER cannot reach the sponsors page", () => {
        loginWith("PLAYER");
        renderAt("/lab/sponsors");
        expect(screen.getByRole("heading", { level: 1, name: "Not available" })).toBeInTheDocument();
    });

    test("an expired token does not open the protected page (PrivateRoute)", async () => {
        tokenIsValid = false;
        loginWith("ADMIN");
        renderAt("/lab/sponsors");
        expect(await screen.findByText("Loading...")).toBeInTheDocument();
        await new Promise((resolve) => setTimeout(resolve, 100));
        expect(screen.queryByRole("heading", { name: "Sponsors administration" })).not.toBeInTheDocument();
    });

    test("unknown paths are not available", () => {
        renderAt("/lab/nowhere");
        expect(screen.getByRole("heading", { level: 1, name: "Not available" })).toBeInTheDocument();
    });
});
