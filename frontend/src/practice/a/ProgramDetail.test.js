import { render, screen } from "../../test-utils";
import ProgramDetail from "./programDetail";

const leaderboard = [
    { user: { id: 4, username: "alice", rating: 1500 }, points: 9 },
    { user: { id: 5, username: "bob", rating: 1400 }, points: 7 },
    { user: { id: 6, username: "carol", rating: 1300 }, points: 0 },
];

describe("Program leaderboard list", () => {
    test("shows the full leaderboard as a list with positions", () => {
        render(<ProgramDetail leaderboard={leaderboard} />);
        expect(screen.getByRole("list")).toBeInTheDocument();
        expect(screen.getAllByRole("listitem")).toHaveLength(3);
        expect(screen.getByText(/^\s*#1\s+alice\s+\(9 pts\)\s*$/i)).toBeInTheDocument();
        expect(screen.getByText(/^\s*#2\s+bob\s+\(7 pts\)\s*$/i)).toBeInTheDocument();
        expect(screen.getByText(/^\s*#3\s+carol\s+\(0 pts\)\s*$/i)).toBeInTheDocument();
    });

    test("keeps the order received in the props", () => {
        render(<ProgramDetail leaderboard={leaderboard} />);
        const items = screen.getAllByRole("listitem");
        expect(items[0]).toHaveTextContent("alice");
        expect(items[1]).toHaveTextContent("bob");
        expect(items[2]).toHaveTextContent("carol");
    });

    test("each element of the list has a different id", () => {
        render(<ProgramDetail leaderboard={leaderboard} />);
        const items = screen.getAllByRole("listitem");
        items.forEach((item) => expect(item).toHaveAttribute("id"));
        const ids = items.map((item) => item.getAttribute("id"));
        expect(new Set(ids).size).toBe(ids.length);
    });

    test("shows an empty list for an empty leaderboard", () => {
        render(<ProgramDetail leaderboard={[]} />);
        expect(screen.getByRole("list")).toBeInTheDocument();
        expect(screen.queryAllByRole("listitem")).toHaveLength(0);
    });
});
