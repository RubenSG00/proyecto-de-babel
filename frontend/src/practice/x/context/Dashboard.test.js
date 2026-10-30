import { render, screen } from "../../../test-utils";
import UserContext from "./userContext";
import Greeting from "./greeting";
import Dashboard, { Sidebar } from "./dashboard";

describe("UserContext", () => {
    test("Greeting uses the default value of the context", () => {
        render(<Greeting />);
        expect(screen.getByRole("heading", { level: 2, name: "Hello, guest!" })).toBeInTheDocument();
    });

    test("Dashboard provides the username to Greeting through Sidebar", () => {
        render(<Dashboard username="magnus" />);
        expect(screen.getByRole("heading", { level: 2, name: "Hello, magnus!" })).toBeInTheDocument();
    });

    test("Sidebar receives no props: the data travels through the context", () => {
        expect(UserContext.Provider).toBeDefined();
        render(
            <UserContext.Provider value={{ username: "hikaru" }}>
                <Sidebar />
            </UserContext.Provider>
        );
        expect(screen.getByRole("heading", { level: 2, name: "Hello, hikaru!" })).toBeInTheDocument();
    });
});
