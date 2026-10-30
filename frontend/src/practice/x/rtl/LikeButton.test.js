import userEvent from "@testing-library/user-event";
import { render, screen } from "../../../test-utils";
import LikeButton from "./likeButton";

describe("LikeButton", () => {
    test("starts at 0 likes and without the message", () => {
        render(<LikeButton />);
        expect(screen.getByRole("button", { name: "Like (0)" })).toBeInTheDocument();
        expect(screen.queryByText("You love this!")).not.toBeInTheDocument();
    });

    test("each click adds one like", async () => {
        const user = userEvent.setup();
        render(<LikeButton />);
        await user.click(screen.getByRole("button", { name: /like/i }));
        expect(screen.getByRole("button", { name: "Like (1)" })).toBeInTheDocument();
        await user.click(screen.getByRole("button", { name: /like/i }));
        expect(screen.getByRole("button", { name: "Like (2)" })).toBeInTheDocument();
        expect(screen.queryByText("You love this!")).not.toBeInTheDocument();
    });

    test("from 3 likes on it shows the message", async () => {
        const user = userEvent.setup();
        render(<LikeButton />);
        for (let i = 0; i < 3; i++) await user.click(screen.getByRole("button", { name: /like/i }));
        expect(screen.getByRole("button", { name: "Like (3)" })).toBeInTheDocument();
        expect(screen.getByText("You love this!")).toBeInTheDocument();
    });
});
