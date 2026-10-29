// Builds an unsigned JWT whose payload has the given authorities. jwt-decode only decodes the
// payload (it does not check the signature), which is all the frontend does with the token.
function base64Url(object) {
    return Buffer.from(JSON.stringify(object)).toString("base64").replace(/=+$/, "").replace(/\+/g, "-").replace(/\//g, "_");
}

export function loginWith(...authorities) {
    const token = `${base64Url({ alg: "none", typ: "JWT" })}.${base64Url({ sub: "someone", authorities })}.signature`;
    localStorage.setItem("jwt", JSON.stringify(token));
}
