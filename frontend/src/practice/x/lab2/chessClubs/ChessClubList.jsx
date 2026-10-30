import { useState } from "react";
import useErrorModal from "../hooks/useErrorModal";
import useFetchState from "../hooks/useFetchState";
import * as clubsApi from "../services/chessClubs";
import deleteFromList from "../util/deleteFromList";
import ChessClubTable from "./ChessClubTable";

export default function ChessClubList() {
    // One error channel for the screen: loading and deleting report through the same modal
    const { errorModal, showError } = useErrorModal();
    const [clubs, setClubs] = useFetchState([], clubsApi.getAllClubs, [], { onError: showError });
    const [alerts, setAlerts] = useState([]);

    // The deletion logic lives in the container; the table only reports the id
    function handleDelete(id) {
        deleteFromList(clubsApi.deleteClub, id, [clubs, setClubs], [alerts, setAlerts], { onError: showError });
    }

    return (
        <div className="admin-page-container">
            <h1 className="text-center">Chess clubs</h1>
            {alerts.map((a) => a.alert)}
            {errorModal}
            <ChessClubTable clubs={clubs} onDelete={handleDelete} />
        </div>
    );
}
