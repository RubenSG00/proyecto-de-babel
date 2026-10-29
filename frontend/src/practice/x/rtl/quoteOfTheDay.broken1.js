import { useEffect, useState } from "react";

// Broken copy: it requests the wrong URL.
export default function QuoteOfTheDay() {
    const [quote, setQuote] = useState(null);

    useEffect(() => {
        fetch("/api/v1/quotes/random")
            .then((response) => response.json())
            .then((json) => setQuote(json));
    }, []);

    if (quote === null) return <p>Loading...</p>;
    return (
        <div>
            <blockquote>{quote.text}</blockquote>
            <p>— {quote.author}</p>
        </div>
    );
}
