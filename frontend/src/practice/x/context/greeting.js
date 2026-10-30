import { useContext } from "react";
import UserContext from "./userContext";

// No props: the username is read from the nearest Provider (or the default value)
export default function Greeting() {
    const { username } = useContext(UserContext);
    return <h2>Hello, {username}!</h2>;
}
