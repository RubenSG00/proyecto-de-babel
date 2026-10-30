import { rest } from "msw";
import { server } from "../../../mocks/server";
import { render, renderHook, screen, waitFor } from "../../../test-utils";
import useMembers from "./useMembers";
import MemberList from "./memberList";
import MembersPage from "./membersPage";

const members = [
    { id: 4, username: "alice" },
    { id: 5, username: "bob" },
];

beforeEach(() => {
    server.use(
        rest.get("*/api/v1/gym/programs/:id/members", (req, res, ctx) =>
            res(ctx.json(req.params.id === "1" ? members : []))
        )
    );
});

describe("useMembers", () => {
    test("returns an empty list and then the members of the program", async () => {
        const { result } = renderHook(() => useMembers("1"));
        expect(result.current.members).toEqual([]);
        await waitFor(() => expect(result.current.members).toEqual(members));
    });
});

describe("MemberList", () => {
    test("shows one item per member with its id", () => {
        const { container } = render(<MemberList members={members} />);
        expect(screen.getAllByRole("listitem")).toHaveLength(2);
        expect(container.querySelector("#member-4")).toHaveTextContent("alice");
        expect(container.querySelector("#member-5")).toHaveTextContent("bob");
    });

    test("shows a message instead of an empty list", () => {
        render(<MemberList members={[]} />);
        expect(screen.queryByRole("list")).not.toBeInTheDocument();
        expect(screen.getByText("No members")).toBeInTheDocument();
    });
});

describe("MembersPage", () => {
    test("composes the hook and the list", async () => {
        render(<MembersPage programId="1" />);
        expect(screen.getByRole("heading", { level: 2, name: "Members" })).toBeInTheDocument();
        expect(await screen.findByText("alice")).toBeInTheDocument();
        expect(screen.getByText("bob")).toBeInTheDocument();
    });

    test("an empty program shows the message", async () => {
        render(<MembersPage programId="2" />);
        expect(await screen.findByText("No members")).toBeInTheDocument();
    });
});
