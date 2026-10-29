import { useState } from "react";

// Broken copy: every click adds two likes.
export default function LikeButton() {
    const [likes, setLikes] = useState(0);
    return (
        <div>
            <button onClick={() => setLikes(likes + 2)}>Like ({likes})</button>
            {likes >= 3 && <p>You love this!</p>}
        </div>
    );
}
