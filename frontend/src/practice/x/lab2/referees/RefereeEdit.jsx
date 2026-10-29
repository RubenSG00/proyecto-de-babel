import RefereeForm from "./RefereeForm";

// TODO: Implement to solve Test X31
export default function RefereeEdit() {
    return <RefereeForm referee={{ id: null, name: "", email: "", level: "REGIONAL" }} onChange={() => {}} onSubmit={(e) => e.preventDefault()} />;
}
