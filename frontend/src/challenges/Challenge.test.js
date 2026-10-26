import { render, screen } from "../test-utils";
import { waitFor } from '@testing-library/react'
import Challenge from "./challenge.js";

describe('Challenge info', () => {
    test('Headers are correct', async () => {
        render(<Challenge challengeId='1' />);

        // El título está presente:
        expect(
            screen.getByRole('heading', { level: 1, name: /challenge info/i })
        ).toBeInTheDocument();

        // El subtítulo está presente:
        expect(
            screen.getByRole('heading', { level: 3, name: /ranking/i })
        ).toBeInTheDocument();

    });

    test('Shows the leader of the ranking for challenge 1', async () => {
        render(<Challenge challengeId='1' />);

        // Espera a que se resuelva el fetch y se muestre el líder:
        await waitFor(() => {
            expect(
                screen.getByText(/leader:\s*john_doe\s*\(6 points\)/i)
            ).toBeInTheDocument();
        });
    });
});
