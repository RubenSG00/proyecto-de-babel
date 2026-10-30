import { render, renderHook, screen, waitFor } from "../../../test-utils";
import { fetchRanking } from "./rankingClient";
import useRanking from "./useRanking";
import RankingSummary from "./rankingSummary";

// The network client is replaced by a fake: if the hook does not use it, nothing is shown
jest.mock("./rankingClient", () => ({ fetchRanking: jest.fn() }));

const apiRanking = [
    { user: { id: 4, username: "john_doe", rating: 1500 }, points: 6 },
    { user: { id: 5, username: "jane_smith", rating: 1600 }, points: 4 },
];

beforeEach(() => {
    fetchRanking.mockReset();
    fetchRanking.mockImplementation((id) => Promise.resolve(id === "1" ? apiRanking : []));
});

describe("useRanking", () => {
    test("obtains the ranking through fetchRanking and converts it", async () => {
        const { result } = renderHook(() => useRanking("1"));
        await waitFor(() =>
            expect(result.current.ranking).toEqual([
                { username: "john_doe", points: 6 },
                { username: "jane_smith", points: 4 },
            ])
        );
        expect(fetchRanking).toHaveBeenCalledWith("1");
    });
});

describe("RankingSummary", () => {
    test("shows the number of participants and the leader", async () => {
        render(<RankingSummary challengeId="1" />);
        expect(await screen.findByText("2 participants")).toBeInTheDocument();
        expect(screen.getByText("Leader: john_doe")).toBeInTheDocument();
    });

    test("an empty ranking shows a message instead", async () => {
        render(<RankingSummary challengeId="2" />);
        expect(await screen.findByText("No participants yet")).toBeInTheDocument();
        expect(screen.queryByText(/leader/i)).not.toBeInTheDocument();
        expect(screen.queryByText(/participants$/)).not.toBeInTheDocument();
    });
});
