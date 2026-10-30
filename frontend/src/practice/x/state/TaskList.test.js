import userEvent from "@testing-library/user-event";
import { render, screen, within } from "../../../test-utils";
import TaskList from "./taskList";


describe("TaskList", () => {
    test("shows the initial tasks", () => {
        render(<TaskList initialTasks={["Warm up", "Stretch"]} />);
        expect(screen.getAllByRole("listitem")).toHaveLength(2);
        expect(screen.getByRole("button", { name: "Remove Warm up" })).toBeInTheDocument();
    });

    test("works without initial tasks", () => {
        render(<TaskList />);
        expect(screen.queryAllByRole("listitem")).toHaveLength(0);
    });

    test("adds the trimmed text at the end and empties the input", async () => {
        const user = userEvent.setup();
        render(<TaskList initialTasks={["Warm up"]} />);
        const input = screen.getByLabelText("New task");
        await user.type(input, "  Squats  ");
        await user.click(screen.getByRole("button", { name: "Add" }));
        const lis = screen.getAllByRole("listitem");
        expect(lis).toHaveLength(2);
        expect(lis[1]).toHaveTextContent("Squats");
        expect(screen.getByRole("button", { name: "Remove Squats" })).toBeInTheDocument();
        expect(input).toHaveValue("");
    });

    test("ignores blank texts", async () => {
        const user = userEvent.setup();
        render(<TaskList />);
        await user.type(screen.getByLabelText("New task"), "   ");
        await user.click(screen.getByRole("button", { name: "Add" }));
        expect(screen.queryAllByRole("listitem")).toHaveLength(0);
    });

    test("removes a task", async () => {
        const user = userEvent.setup();
        render(<TaskList initialTasks={["Warm up", "Stretch", "Run"]} />);
        await user.click(screen.getByRole("button", { name: "Remove Stretch" }));
        const lis = screen.getAllByRole("listitem");
        expect(lis).toHaveLength(2);
        expect(screen.queryByRole("button", { name: "Remove Stretch" })).not.toBeInTheDocument();
    });

    test("never modifies the initialTasks prop", async () => {
        const user = userEvent.setup();
        const initial = ["Warm up", "Stretch"];
        render(<TaskList initialTasks={initial} />);
        await user.type(screen.getByLabelText("New task"), "Run");
        await user.click(screen.getByRole("button", { name: "Add" }));
        await user.click(screen.getByRole("button", { name: "Remove Warm up" }));
        expect(initial).toEqual(["Warm up", "Stretch"]);
    });
});
