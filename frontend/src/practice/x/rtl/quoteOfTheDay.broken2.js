import { useEffect, useState } from "react";

// Broken copy: it shows nothing while loading.
export default function QuoteOfTheDay() {
    const [quote, setQuote] = useState(null);

    useEffect(() => {
        fetch("/api/v1/quotes/today")
            .then((response) => response.json())
            .then((json) => setQuote(json));
    }, []);

    if (quote === null) return null;
    return (
        <div>
            <blockquote>{quote.text}</blockquote>
            <p>— {quote.author}</p>
        </div>
    );
}
