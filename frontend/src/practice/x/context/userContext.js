import { createContext } from "react";

// The default value is used by the components rendered outside any Provider
const UserContext = createContext({ username: "guest" });

export default UserContext;
