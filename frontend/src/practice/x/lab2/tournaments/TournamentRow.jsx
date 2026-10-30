import { Button } from "reactstrap";
import { Link } from "react-router-dom";

// Presentational: it only reports which tournament must be deleted; the container deletes it
export default function TournamentRow({ tournament, onDelete }) {
    return (
        <tr>
            <td className="text-center">{tournament.name}</td>
            <td className="text-center">{tournament.format}</td>
            <td className="text-center">{tournament.prizePool}</td>
            <td className="text-center">
                <Button outline color="warning">
                    <Link to={`/tournaments/${tournament.id}`} className="btn sm" style={{ textDecoration: "none" }}>
                        Edit
                    </Link>
                </Button>{" "}
                {/* The arrow function delays the call until the click */}
                <Button outline color="danger" onClick={() => onDelete(tournament.id)}>
                    Delete
                </Button>
            </td>
        </tr>
    );
}
