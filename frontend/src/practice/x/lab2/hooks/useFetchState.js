import { useEffect, useState } from "react";

/**
 * Copy of the useFetchState hook of the 2026-27 project template (Lab 2 p.8-9, p.16, p.21-22).
 *
 * Declares a state initialised with `initial` and fills it with the result of `fetchFn`
 * (a service function returning a promise), which is called when the component mounts and
 * again whenever a value of `deps` changes.
 *
 * @param {any} initial - initial (default) value of the state.
 * @param {function} fetchFn - service function to call, passed without parentheses
 *   (or wrapped in an arrow function when it needs arguments).
 * @param {Array} [deps=[]] - dependency list: the request is repeated when one of them changes.
 * @param {object} [options] - `skip`: if true, no request is made and the state keeps `initial`;
 *   `onError`: function called with the error message if the request fails (default: window.alert).
 * @returns {[any, function]} the state and its setter.
 */
export default function useFetchState(initial, fetchFn, deps = [], { skip = false, onError } = {}) {
    const [data, setData] = useState(initial);

    useEffect(() => {
        if (skip) return;
        let ignore = false;
        fetchFn()
            .then((result) => {
                if (!ignore) setData(result);
            })
            .catch((err) => {
                if (ignore) return;
                const message = err?.response?.data?.message ?? err?.message ?? "Failed to fetch data";
                if (onError) onError(message);
                else window.alert(message);
            });
        return () => {
            ignore = true;
        };
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [skip, ...deps]);

    return [data, setData];
}
