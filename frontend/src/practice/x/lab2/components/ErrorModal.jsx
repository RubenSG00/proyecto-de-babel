import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from "reactstrap";

// Copy of the ErrorModal component of the 2026-27 project template (Lab 2 p.23):
// it only renders, receiving visible, message and onClose through props.
export default function ErrorModal({ visible, message, onClose }) {
    if (!message) return null;
    return (
        <Modal isOpen={visible} toggle={onClose} keyboard={false}>
            <ModalHeader toggle={onClose}>Alert!</ModalHeader>
            <ModalBody>{message}</ModalBody>
            <ModalFooter>
                <Button color="primary" onClick={onClose}>
                    Close
                </Button>
            </ModalFooter>
        </Modal>
    );
}
