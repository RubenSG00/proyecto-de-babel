import { render, screen, within } from "../../../test-utils";
import ScoreBoard from "./scoreBoard";

const players = [
    { username: "magnus", points: 9 },
    { username: "hikaru", points: 8 },
    { username: "fabiano", points: 7 },
    { username: "ian", points: 6 },
];

// Render, (interact) and assert on what the user sees: query by role first, by text second
describe("ScoreBoard", () => {
    test("shows the title as a level-2 heading", () => {
        render(<ScoreBoard title="Top players" players={players} />);
        expect(screen.getByRole("heading", { level: 2, name: "Top players" })).toBeInTheDocument();
    });

    test("shows one item per player, all of them, with username and points", () => {
        render(<ScoreBoard title="Top players" players={players} />);
        const items = within(screen.getByRole("list")).getAllByRole("listitem");
        expect(items).toHaveLength(4);
        expect(items[0]).toHaveTextContent("magnus: 9");
        expect(items[3]).toHaveTextContent("ian: 6");
        expect(screen.queryByText("No players yet")).not.toBeInTheDocument();
    });

    test("without players: a message and no list", () => {
        render(<ScoreBoard title="Top players" players={[]} />);
        expect(screen.getByText("No players yet")).toBeInTheDocument();
        expect(screen.queryByRole("list")).not.toBeInTheDocument();
    });
});
