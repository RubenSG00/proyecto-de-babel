import jwt_decode from "jwt-decode";
import { Link } from "react-router-dom";
import { Nav, Navbar, NavItem, NavLink } from "reactstrap";
import tokenService from "../../../../services/token.service";

// The menu is a convenience for the user: the protection is in the routes and in the backend
export default function PracticeNavbar() {
    const jwt = tokenService.getLocalAccessToken();
    const roles = jwt ? jwt_decode(jwt).authorities : [];

    let adminLinks = <></>;
    let playerLinks = <></>;
    roles.forEach((role) => {
        if (role === "ADMIN")
            adminLinks = (
                <>
                    <NavItem>
                        <NavLink className="nav-link-white" tag={Link} to="/users">Users</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink className="nav-link-white" tag={Link} to="/developers">Developers</NavLink>
                    </NavItem>
                </>
            );
        if (role === "PLAYER")
            playerLinks = (
                <NavItem>
                    <NavLink className="nav-link-white" tag={Link} to="/achievements">Achievements</NavLink>
                </NavItem>
            );
    });

    const sessionLinks = jwt ? (
        <NavItem>
            <NavLink className="nav-link-white" tag={Link} to="/logout">Logout</NavLink>
        </NavItem>
    ) : (
        <>
            <NavItem>
                <NavLink className="nav-link-white" tag={Link} to="/login">Login</NavLink>
            </NavItem>
            <NavItem>
                <NavLink className="nav-link-white" tag={Link} to="/register">Register</NavLink>
            </NavItem>
        </>
    );

    return (
        <Navbar expand="md" dark color="dark">
            <Nav navbar>
                <NavItem>
                    <NavLink className="nav-link-white" tag={Link} to="/">Home</NavLink>
                </NavItem>
                {adminLinks}
                {playerLinks}
                {sessionLinks}
            </Nav>
        </Navbar>
    );
}
