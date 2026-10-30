import userEvent from "@testing-library/user-event";
import { render, screen } from "../../../test-utils";
import VoteBoard, { VoteButton } from "./voteBoard";

describe("VoteButton", () => {
    test("shows the label and the count and reports the clicks", async () => {
        const onVote = jest.fn();
        render(<VoteButton label="Yoga" count={2} onVote={onVote} />);
        const button = screen.getByRole("button", { name: "Yoga (2)" });
        await userEvent.setup().click(button);
        expect(onVote).toHaveBeenCalledTimes(1);
        // No state: the count does not change by itself
        expect(screen.getByRole("button", { name: "Yoga (2)" })).toBeInTheDocument();
    });
});

describe("VoteBoard", () => {
    test("starts with no votes", () => {
        render(<VoteBoard />);
        expect(screen.getByRole("heading", { level: 2, name: "Vote for the next session" })).toBeInTheDocument();
        expect(screen.getByRole("button", { name: "Yoga (0)" })).toBeInTheDocument();
        expect(screen.getByRole("button", { name: "Pilates (0)" })).toBeInTheDocument();
        expect(screen.getByText("Total votes: 0")).toBeInTheDocument();
    });

    test("counts the votes of each option and the total", async () => {
        const user = userEvent.setup();
        render(<VoteBoard />);
        await user.click(screen.getByRole("button", { name: "Yoga (0)" }));
        await user.click(screen.getByRole("button", { name: "Yoga (1)" }));
        await user.click(screen.getByRole("button", { name: "Pilates (0)" }));
        expect(screen.getByRole("button", { name: "Yoga (2)" })).toBeInTheDocument();
        expect(screen.getByRole("button", { name: "Pilates (1)" })).toBeInTheDocument();
        expect(screen.getByText("Total votes: 3")).toBeInTheDocument();
    });
});
