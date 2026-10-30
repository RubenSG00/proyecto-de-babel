import userEvent from "@testing-library/user-event";
import { MemoryRouter } from "react-router-dom";
import { render, screen } from "@testing-library/react";
import TravelApp from "./travelApp";

function renderAt(url) {
    render(
        <MemoryRouter initialEntries={[url]}>
            <TravelApp />
        </MemoryRouter>
    );
}

describe("TravelApp", () => {
    test("home page", () => {
        renderAt("/");
        expect(screen.getByRole("heading", { level: 1, name: "Travel planner" })).toBeInTheDocument();
        expect(screen.getByRole("link", { name: "Destinations" })).toHaveAttribute("href", "/destinations");
    });

    test("destinations page", () => {
        renderAt("/destinations");
        expect(screen.getByRole("heading", { level: 1, name: "Destinations" })).toBeInTheDocument();
        expect(screen.getByRole("link", { name: "Lisbon" })).toHaveAttribute("href", "/destinations/1");
        expect(screen.getByRole("link", { name: "Porto" })).toHaveAttribute("href", "/destinations/2");
    });

    test("the id of the URL is read with useParams", () => {
        renderAt("/destinations/2");
        expect(screen.getByRole("heading", { level: 1, name: "Destination 2" })).toBeInTheDocument();
        expect(screen.getByRole("link", { name: "Back" })).toHaveAttribute("href", "/destinations");
    });

    test("navigating with the links", async () => {
        const user = userEvent.setup();
        renderAt("/");
        await user.click(screen.getByRole("link", { name: "Destinations" }));
        await user.click(screen.getByRole("link", { name: "Lisbon" }));
        expect(screen.getByRole("heading", { level: 1, name: "Destination 1" })).toBeInTheDocument();
        await user.click(screen.getByRole("link", { name: "Back" }));
        expect(screen.getByRole("heading", { level: 1, name: "Destinations" })).toBeInTheDocument();
    });

    test("any other path", () => {
        renderAt("/somewhere/else");
        expect(screen.getByRole("heading", { level: 1, name: "Page not found" })).toBeInTheDocument();
    });
});
