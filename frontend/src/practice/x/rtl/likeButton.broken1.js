import { useState } from "react";

// Broken copy: the counter starts at 1.
export default function LikeButton() {
    const [likes, setLikes] = useState(1);
    return (
        <div>
            <button onClick={() => setLikes(likes + 1)}>Like ({likes})</button>
            {likes >= 3 && <p>You love this!</p>}
        </div>
    );
}
