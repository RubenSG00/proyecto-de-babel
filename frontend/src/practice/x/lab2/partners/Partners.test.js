import fs from "fs";
import path from "path";
import { rest } from "msw";
import { server } from "../../../../mocks/server";
import { render, screen, waitFor, within } from "../../../../test-utils";
import PartnerContainer from "./index";
import * as partnersApi from "../services/partners";

const partners = [
    { id: 1, name: "FIDE Academy", sector: "Education" },
    { id: 2, name: "Board Games Ltd", sector: "Retail" },
];

let authorizations;

beforeEach(() => {
    authorizations = [];
    localStorage.setItem("jwt", JSON.stringify("my-token"));
    server.use(
        rest.get("*/api/v1/partners", (req, res, ctx) => {
            authorizations.push(req.headers.get("Authorization"));
            return res(ctx.json(partners));
        }),
        rest.get("*/api/v1/partners/:id", (req, res, ctx) => {
            authorizations.push(req.headers.get("Authorization"));
            const partner = partners.find((p) => String(p.id) === req.params.id);
            return partner ? res(ctx.json(partner)) : res(ctx.status(404), ctx.json({ message: "Not found" }));
        })
    );
});

afterEach(() => localStorage.clear());

describe("partners service", () => {
    test("getAllPartners returns the data of GET /api/v1/partners", async () => {
        await expect(partnersApi.getAllPartners()).resolves.toEqual(partners);
    });

    test("getPartnerById returns the data of GET /api/v1/partners/{id}", async () => {
        await expect(partnersApi.getPartnerById(2)).resolves.toEqual(partners[1]);
    });

    test("the requests carry the JWT added by the API client", async () => {
        await partnersApi.getAllPartners();
        expect(authorizations).toEqual(["Bearer my-token"]);
    });
});

describe("PartnerContainer", () => {
    test("shows the partners loaded from the API", async () => {
        render(<PartnerContainer />);
        const table = screen.getByRole("table", { name: "partners" });
        await waitFor(() => expect(within(table).getByText("FIDE Academy")).toBeInTheDocument());
        expect(within(table).getByText("Board Games Ltd")).toBeInTheDocument();
    });

    test("uses useFetchState and the service, without building URLs or managing tokens", () => {
        const source = fs.readFileSync(path.join(__dirname, "index.jsx"), "utf8");
        expect(source).toMatch(/useFetchState/);
        expect(source).toMatch(/getAllPartners/);
        expect(source).not.toMatch(/fetch\(|axios|\/api\/|Bearer|getLocalAccessToken/);
    });
});
