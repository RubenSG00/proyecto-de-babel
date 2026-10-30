import { Link, Route, Routes, useParams } from "react-router-dom";

function Home() {
    return (
        <div>
            <h1>Travel planner</h1>
            <Link to="/destinations">Destinations</Link>
        </div>
    );
}

function Destinations() {
    return (
        <div>
            <h1>Destinations</h1>
            <Link to="/destinations/1">Lisbon</Link> <Link to="/destinations/2">Porto</Link>
        </div>
    );
}

function Destination() {
    // The :id segment of the route
    const { id } = useParams();
    return (
        <div>
            <h1>Destination {id}</h1>
            <Link to="/destinations">Back</Link>
        </div>
    );
}

// No BrowserRouter here: the parent provides the router (a router cannot contain another one)
export default function TravelApp() {
    return (
        <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/destinations" element={<Destinations />} />
            <Route path="/destinations/:id" element={<Destination />} />
            <Route path="*" element={<h1>Page not found</h1>} />
        </Routes>
    );
}
