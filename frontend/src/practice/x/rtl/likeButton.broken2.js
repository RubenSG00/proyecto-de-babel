import { useState } from "react";

// Broken copy: the message only appears from 4 likes on.
export default function LikeButton() {
    const [likes, setLikes] = useState(0);
    return (
        <div>
            <button onClick={() => setLikes(likes + 1)}>Like ({likes})</button>
            {likes >= 4 && <p>You love this!</p>}
        </div>
    );
}
