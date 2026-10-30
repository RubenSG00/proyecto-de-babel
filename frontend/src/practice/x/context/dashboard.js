import UserContext from "./userContext";
import Greeting from "./greeting";

// Sidebar does not receive the username: no prop drilling through the intermediate component
export function Sidebar() {
    return (
        <aside>
            <Greeting />
        </aside>
    );
}

export default function Dashboard({ username }) {
    return (
        <UserContext.Provider value={{ username }}>
            <Sidebar />
        </UserContext.Provider>
    );
}
