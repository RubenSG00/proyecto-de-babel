import { Alert } from "reactstrap";

/**
 * Copy of the deleteFromList utility of the 2026-27 project template (Lab 2 p.26-27).
 *
 * After the user confirms (window.confirm), calls `deleteFn(id)`. If the request succeeds, the item
 * with that id is removed from the list state and, if the server answered with a body containing a
 * `message` (as user deletion does), that message is added to the alerts as `{ alert, id }`, where
 * `alert` is a reactstrap Alert ready to render with `{alerts.map((a) => a.alert)}`. A 204 (No
 * Content) answer has no body, so the item simply disappears. If the request fails, `onError` is
 * called with the server's message (default: window.alert).
 *
 * @param {function} deleteFn - service function that performs the DELETE request.
 * @param {any} id - id of the item to delete.
 * @param {[Array, function]} state - the list and its setter.
 * @param {[Array, function]} alerts - the alerts and their setter.
 * @param {object} [options] - `onError`: function called with an error message.
 */
export default async function deleteFromList(deleteFn, id, [state, setState], [alerts, setAlerts], { onError } = {}) {
    if (!window.confirm("Are you sure you want to delete it?")) return;
    try {
        const data = await deleteFn(id);
        setState(state.filter((item) => item.id !== id));
        if (data?.message) {
            const alertId = `alert-${id}`;
            setAlerts([
                ...alerts,
                { alert: <Alert key={alertId} color="info" id={alertId}>{data.message}</Alert>, id: alertId },
            ]);
        }
    } catch (err) {
        const message = err?.response?.data?.message ?? "Error deleting entity";
        if (onError) onError(message);
        else window.alert(message);
    }
}
