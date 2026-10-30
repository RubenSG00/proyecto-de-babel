import userEvent from "@testing-library/user-event";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import { render, screen, waitFor } from "@testing-library/react";
import { rest } from "msw";
import { server } from "../../../../mocks/server";
import RefereeEdit from "./RefereeEdit";

let requests;

function renderAt(url) {
    render(
        <MemoryRouter initialEntries={[url]}>
            <Routes>
                <Route path="/referees/:id" element={<RefereeEdit />} />
                <Route path="/referees" element={<h1>Referees list</h1>} />
            </Routes>
        </MemoryRouter>
    );
}

beforeEach(() => {
    requests = [];
    server.use(
        rest.get("*/api/v1/referees/:id", (req, res, ctx) => {
            requests.push(`GET ${req.params.id}`);
            if (req.params.id === "3")
                return res(ctx.json({ id: 3, name: "Pedro Ruiz", email: "pedro@fide.org", level: "NATIONAL" }));
            return res(ctx.status(404), ctx.json({ message: "Referee not found" }));
        }),
        rest.post("*/api/v1/referees", async (req, res, ctx) => {
            requests.push({ method: "POST", body: await req.json() });
            return res(ctx.status(201), ctx.json({ id: 100 }));
        }),
        rest.put("*/api/v1/referees/:id", async (req, res, ctx) => {
            const body = await req.json();
            requests.push({ method: "PUT", id: req.params.id, body });
            if (body.email === "wrong@fide.org") return res(ctx.status(400), ctx.json({ message: "Invalid e-mail" }));
            if (body.email === "silent@fide.org") return res(ctx.status(500));
            return res(ctx.json(body));
        })
    );
});

describe("RefereeEdit", () => {
    test("new: empty form, no request, POST and back to the list", async () => {
        const user = userEvent.setup();
        renderAt("/referees/new");
        expect(screen.getByLabelText("Name")).toHaveValue("");
        expect(screen.getByLabelText("Level")).toHaveValue("REGIONAL");
        await user.type(screen.getByLabelText("Name"), "Lucia Gil");
        await user.type(screen.getByLabelText("E-mail"), "lucia@fide.org");
        await user.selectOptions(screen.getByLabelText("Level"), "INTERNATIONAL");
        await user.click(screen.getByRole("button", { name: "Save" }));
        expect(await screen.findByRole("heading", { name: "Referees list" })).toBeInTheDocument();
        expect(requests.filter((r) => typeof r === "string")).toEqual([]);
        expect(requests).toEqual([{ method: "POST", body: { id: null, name: "Lucia Gil", email: "lucia@fide.org", level: "INTERNATIONAL" } }]);
    });

    test("edit: loads the referee, PUT with the changes and back to the list", async () => {
        const user = userEvent.setup();
        renderAt("/referees/3");
        await waitFor(() => expect(screen.getByLabelText("Name")).toHaveValue("Pedro Ruiz"));
        await user.clear(screen.getByLabelText("Name"));
        await user.type(screen.getByLabelText("Name"), "Pedro R.");
        await user.click(screen.getByRole("button", { name: "Save" }));
        expect(await screen.findByRole("heading", { name: "Referees list" })).toBeInTheDocument();
        const put = requests.find((r) => r.method === "PUT");
        expect(put.id).toBe("3");
        expect(put.body).toEqual({ id: 3, name: "Pedro R.", email: "pedro@fide.org", level: "NATIONAL" });
    });

    test("an error while loading is shown in the modal", async () => {
        renderAt("/referees/9");
        expect(await screen.findByText("Referee not found")).toBeInTheDocument();
    });

    test("an error while saving shows the message of the backend and stays", async () => {
        const user = userEvent.setup();
        renderAt("/referees/3");
        await waitFor(() => expect(screen.getByLabelText("E-mail")).toHaveValue("pedro@fide.org"));
        await user.clear(screen.getByLabelText("E-mail"));
        await user.type(screen.getByLabelText("E-mail"), "wrong@fide.org");
        await user.click(screen.getByRole("button", { name: "Save" }));
        expect(await screen.findByText("Invalid e-mail")).toBeInTheDocument();
        expect(screen.queryByRole("heading", { name: "Referees list" })).not.toBeInTheDocument();
    });

    test("an error without message shows the default text", async () => {
        const user = userEvent.setup();
        renderAt("/referees/3");
        await waitFor(() => expect(screen.getByLabelText("E-mail")).toHaveValue("pedro@fide.org"));
        await user.clear(screen.getByLabelText("E-mail"));
        await user.type(screen.getByLabelText("E-mail"), "silent@fide.org");
        await user.click(screen.getByRole("button", { name: "Save" }));
        expect(await screen.findByText("Error saving referee")).toBeInTheDocument();
    });
});
