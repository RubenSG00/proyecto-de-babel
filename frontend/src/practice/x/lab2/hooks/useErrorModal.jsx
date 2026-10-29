import { useCallback, useState } from "react";
import ErrorModal from "../components/ErrorModal";

/**
 * Copy of the useErrorModal hook of the 2026-27 project template (Lab 2 p.21-23).
 *
 * @returns {{errorModal: JSX.Element, showError: function}} `errorModal` is the prebuilt modal,
 *   to be rendered once in the component; `showError(message)` opens it with that message and can
 *   be passed as `onError` to useFetchState and deleteFromList.
 */
export default function useErrorModal() {
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);

    const showError = useCallback((newMessage) => {
        setMessage(newMessage);
        setVisible(true);
    }, []);

    const errorModal = <ErrorModal visible={visible} message={message} onClose={() => setVisible(false)} />;

    return { errorModal, showError };
}
