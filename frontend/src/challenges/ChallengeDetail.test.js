import { render, screen } from "../test-utils";
import ChallengeDetail from './challengeDetail'

const ranking1 = [
  { "user": { "id": 4, "username": "john_doe", "rating": 1500 }, "points": 6 },
  { "user": { "id": 5, "username": "jane_smith", "rating": 1600 }, "points": 4 }
];

describe('Challenge ranking list', () => {
  test('shows the full ranking as a list', () => {
    render(<ChallengeDetail ranking={ranking1} />);

    // Debe existir una lista <ul>
    const list = screen.getByRole('list');
    expect(list).toBeInTheDocument();

    // Debe haber dos elementos <li> (uno por participante)
    const items = screen.getAllByRole('listitem');
    expect(items).toHaveLength(2);

    expect(screen.getByText(/^\s*john_doe\s*-\s*6\s*$/i)).toBeInTheDocument();
    expect(screen.getByText(/^\s*jane_smith\s*-\s*4\s*$/i)).toBeInTheDocument();
  });

  test('each element of the list should have a different id', () => {
    render(<ChallengeDetail ranking={ranking1} />);

    const items = screen.getAllByRole('listitem');

    /* ------------------------------------------------------------------
     *  Verify that every <li> has an id attribute
     * ------------------------------------------------------------------ */
    items.forEach((item) => {
      expect(item).toHaveAttribute('id');
    });

    /* ------------------------------------------------------------------
     *  Collect all id values and assert that they are unique
     * ------------------------------------------------------------------ */
    const ids = items.map((item) => item.getAttribute('id')); // non‑null by contract
    expect(new Set(ids).size).toBe(ids.length);  // unique ids    
  })
});
