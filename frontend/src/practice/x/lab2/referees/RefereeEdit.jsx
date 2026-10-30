import { useNavigate, useParams } from "react-router-dom";
import useErrorModal from "../hooks/useErrorModal";
import useFetchState from "../hooks/useFetchState";
import * as refereesApi from "../services/referees";
import RefereeForm from "./RefereeForm";

// One container to create and to edit (DRY): "new" in the URL means creating
export default function RefereeEdit() {
    const { id } = useParams();
    const emptyReferee = { id: null, name: "", email: "", level: "REGIONAL" };
    const navigate = useNavigate();
    const { errorModal, showError } = useErrorModal();
    // The service function needs an argument, so it is wrapped in an arrow function; [id] repeats
    // the request when the id changes, and skip avoids requesting /referees/new
    const [referee, setReferee] = useFetchState(emptyReferee, () => refereesApi.getRefereeById(id), [id], {
        skip: id === "new",
        onError: showError,
    });

    // One handler for every field, using the name of the input
    function handleChange(event) {
        const { name, value } = event.target;
        setReferee({ ...referee, [name]: value });
    }

    async function handleSubmit(event) {
        event.preventDefault(); // otherwise the browser submits the form and reloads the page
        try {
            if (referee.id) await refereesApi.updateReferee(referee.id, referee);
            else await refereesApi.createReferee(referee);
            navigate("/referees");
        } catch (err) {
            showError(err.response?.data?.message ?? "Error saving referee");
        }
    }

    return (
        <>
            {errorModal}
            <RefereeForm referee={referee} onChange={handleChange} onSubmit={handleSubmit} />
        </>
    );
}
