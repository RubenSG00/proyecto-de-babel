import { render, screen } from "../../../../test-utils";
import { loginWith } from "./fakeJwt";
import PracticeNavbar from "./PracticeNavbar";

function links() {
    return screen.queryAllByRole("link").map((a) => `${a.textContent.trim()} ${a.getAttribute("href")}`);
}

afterEach(() => localStorage.clear());

describe("PracticeNavbar", () => {
    test("nobody logged in", () => {
        render(<PracticeNavbar />);
        expect(links().sort()).toEqual(["Home /", "Login /login", "Register /register"].sort());
    });

    test("ADMIN", () => {
        loginWith("ADMIN");
        render(<PracticeNavbar />);
        expect(links().sort()).toEqual(["Developers /developers", "Home /", "Logout /logout", "Users /users"].sort());
    });

    test("PLAYER", () => {
        loginWith("PLAYER");
        render(<PracticeNavbar />);
        expect(links().sort()).toEqual(["Achievements /achievements", "Home /", "Logout /logout"].sort());
    });
});
