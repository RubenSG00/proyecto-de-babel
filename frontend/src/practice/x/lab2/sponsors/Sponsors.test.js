import { render, screen, within } from "../../../../test-utils";
import SponsorContainer from "./index";
import SponsorRow from "./SponsorRow";
import SponsorTable from "./SponsorTable";

const DEFAULT_LOGO = "https://cdn-icons-png.flaticon.com/512/48/48639.png";

const sponsors = [
    { id: 7, name: "Acme", email: "hi@acme.com", url: "https://acme.com", properties: { logoUrl: "https://acme.com/logo.png" } },
    { id: 8, name: "Globex", email: "info@globex.com", url: "https://globex.com", properties: {} },
];

function renderRow(sponsor) {
    render(
        <table>
            <tbody>
                <SponsorRow sponsor={sponsor} />
            </tbody>
        </table>
    );
}

describe("SponsorRow", () => {
    test("shows the name, the e-mail, a link to the url and the logo", () => {
        renderRow(sponsors[0]);
        const row = screen.getByRole("row");
        expect(within(row).getAllByRole("cell")).toHaveLength(4);
        expect(within(row).getByText("Acme")).toBeInTheDocument();
        expect(within(row).getByText("hi@acme.com")).toBeInTheDocument();
        expect(within(row).getByRole("link", { name: "https://acme.com" })).toHaveAttribute("href", "https://acme.com");
        expect(within(row).getByRole("img", { name: "Acme" })).toHaveAttribute("src", "https://acme.com/logo.png");
    });

    test("uses the default logo when properties has no logoUrl", () => {
        renderRow(sponsors[1]);
        expect(screen.getByRole("img", { name: "Globex" })).toHaveAttribute("src", DEFAULT_LOGO);
    });

    test("uses the default logo when the sponsor has no properties", () => {
        renderRow({ id: 9, name: "Initech", email: "it@initech.com", url: "https://initech.com" });
        expect(screen.getByRole("img", { name: "Initech" })).toHaveAttribute("src", DEFAULT_LOGO);
    });
});

describe("SponsorTable", () => {
    test("shows the heading and a table with one row per sponsor", () => {
        render(<SponsorTable sponsors={sponsors} />);
        expect(screen.getByRole("heading", { level: 1, name: "Sponsors" })).toBeInTheDocument();
        const table = screen.getByRole("table", { name: "sponsors" });
        const headers = within(table).getAllByRole("columnheader").map((h) => h.textContent.trim());
        expect(headers).toEqual(["Name", "e-mail", "URL", "Logo"]);
        // header row + one row per sponsor
        expect(within(table).getAllByRole("row")).toHaveLength(3);
        expect(within(table).getByText("Globex")).toBeInTheDocument();
    });
});

describe("SponsorContainer", () => {
    test("passes the test data to the table", () => {
        render(<SponsorContainer />);
        const table = screen.getByRole("table", { name: "sponsors" });
        expect(within(table).getAllByRole("row")).toHaveLength(3);
        expect(within(table).getByRole("img", { name: "Chess Base" })).toHaveAttribute("src", "https://example.com/chessbase.png");
        expect(within(table).getByRole("img", { name: "Lichess" })).toHaveAttribute("src", DEFAULT_LOGO);
        expect(within(table).getByText("contact@lichess.org")).toBeInTheDocument();
    });
});
