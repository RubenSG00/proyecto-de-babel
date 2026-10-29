import SponsorTable from "./SponsorTable";

// The container decides which data is shown: replacing these test data with API data
// later only changes this file
const sponsors = [
    {
        id: 1, name: "Chess Base", email: "info@chessbase.com", url: "https://chessbase.com",
        properties: { logoUrl: "https://example.com/chessbase.png" },
    },
    { id: 2, name: "Lichess", email: "contact@lichess.org", url: "https://lichess.org" },
];

export default function SponsorContainer() {
    return <SponsorTable sponsors={sponsors} />;
}
