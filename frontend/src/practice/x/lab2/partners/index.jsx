import useFetchState from "../hooks/useFetchState";
import * as partnersApi from "../services/partners";
import PartnerTable from "./PartnerTable";

export default function PartnerContainer() {
    // The function itself is passed (no parentheses): useFetchState calls it when the component mounts
    const [partners] = useFetchState([], partnersApi.getAllPartners);
    return <PartnerTable partners={partners} />;
}
