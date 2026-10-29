const imgNotFound = "https://cdn-icons-png.flaticon.com/512/48/48639.png";

// Presentational: it only decides how one sponsor is shown
export default function SponsorRow({ sponsor }) {
    // Optional chaining: a sponsor without properties must not throw a TypeError
    const logoUrl = sponsor.properties?.logoUrl;

    return (
        <tr>
            <td className="text-center">{sponsor.name}</td>
            <td className="text-center">{sponsor.email}</td>
            <td className="text-center">
                <a href={sponsor.url}>{sponsor.url}</a>
            </td>
            <td className="text-center">
                <img src={logoUrl ? logoUrl : imgNotFound} alt={sponsor.name} width="50px" />
            </td>
        </tr>
    );
}
