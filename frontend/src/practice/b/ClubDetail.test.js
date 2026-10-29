import { render, screen } from "../../test-utils";
import ClubDetail from "./clubDetail";

const report = [
    { user: { id: 4, username: "reader_one" }, overdueLoans: 3 },
    { user: { id: 5, username: "reader_two" }, overdueLoans: 1 },
];

describe("Club overdue report", () => {
    test("shows one list item per entry", () => {
        render(<ClubDetail report={report} />);
        expect(screen.getByRole("list")).toBeInTheDocument();
        expect(screen.getAllByRole("listitem")).toHaveLength(2);
        expect(screen.getByText(/^\s*reader_one:\s3 overdue\s*$/)).toBeInTheDocument();
        expect(screen.getByText(/^\s*reader_two:\s1 overdue\s*$/)).toBeInTheDocument();
    });

    test("each element of the list has a different id", () => {
        render(<ClubDetail report={report} />);
        const ids = screen.getAllByRole("listitem").map((item) => item.getAttribute("id"));
        ids.forEach((id) => expect(id).toBeTruthy());
        expect(new Set(ids).size).toBe(ids.length);
    });

    test("does not show the message when there are entries", () => {
        render(<ClubDetail report={report} />);
        expect(screen.queryByText("No overdue loans")).not.toBeInTheDocument();
    });

    test("shows a paragraph and no list when the report is empty", () => {
        const { container } = render(<ClubDetail report={[]} />);
        expect(screen.queryByRole("list")).not.toBeInTheDocument();
        const message = screen.getByText("No overdue loans");
        expect(message.tagName).toBe("P");
        expect(container.querySelectorAll("li")).toHaveLength(0);
    });
});
