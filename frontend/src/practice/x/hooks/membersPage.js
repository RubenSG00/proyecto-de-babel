import useMembers from "./useMembers";
import MemberList from "./memberList";

// The page only composes the hook (data) and the presentation component (rendering)
export default function MembersPage({ programId }) {
    const { members } = useMembers(programId);
    return (
        <div>
            <h2>Members</h2>
            <MemberList members={members} />
        </div>
    );
}
