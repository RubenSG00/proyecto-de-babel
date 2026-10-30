import { rest } from "msw";
import { server } from "../../../mocks/server";
import { fetchRanking } from "./rankingClient";

test("fetchRanking requests the ranking of the challenge and returns the parsed JSON", async () => {
    const requested = [];
    server.use(
        rest.get("*/api/v1/challenges/:id/ranking", (req, res, ctx) => {
            requested.push(req.params.id);
            return res(ctx.json([{ user: { id: 9, username: "carol" }, points: 1 }]));
        })
    );
    await expect(fetchRanking("3")).resolves.toEqual([{ user: { id: 9, username: "carol" }, points: 1 }]);
    expect(requested).toEqual(["3"]);
});
