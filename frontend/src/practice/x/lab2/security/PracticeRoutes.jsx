import jwt_decode from "jwt-decode";
import { Route, Routes } from "react-router-dom";
import PrivateRoute from "../../../../privateRoute";
import tokenService from "../../../../services/token.service";
import PublicPage from "./PublicPage";
import SponsorsAdminPage from "./SponsorsAdminPage";
import TournamentsPlayerPage from "./TournamentsPlayerPage";

export default function PracticeRoutes() {
    const jwt = tokenService.getLocalAccessToken();
    const roles = jwt ? jwt_decode(jwt).authorities : [];

    // A route that is not registered for a role cannot be reached, even by typing its URL;
    // PrivateRoute additionally asks the backend whether the token is still valid
    let adminRoutes = <></>;
    let playerRoutes = <></>;
    roles.forEach((role) => {
        if (role === "ADMIN")
            adminRoutes = (
                <Route path="/lab/sponsors" element={<PrivateRoute><SponsorsAdminPage /></PrivateRoute>} />
            );
        if (role === "PLAYER")
            playerRoutes = (
                <Route path="/lab/tournaments" element={<PrivateRoute><TournamentsPlayerPage /></PrivateRoute>} />
            );
    });

    return (
        <Routes>
            <Route path="/lab/public" element={<PublicPage />} />
            {adminRoutes}
            {playerRoutes}
            <Route path="*" element={<h1>Not available</h1>} />
        </Routes>
    );
}
