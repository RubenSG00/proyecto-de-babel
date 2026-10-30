import { useState } from "react";

export default function TaskList({ initialTasks = [] }) {
    // useState only uses the prop as the initial value; it is never modified
    const [tasks, setTasks] = useState(initialTasks);
    const [text, setText] = useState("");

    function add() {
        const task = text.trim();
        if (task === "") return;
        setTasks([...tasks, task]); // a new array, never push
        setText("");
    }

    function remove(task) {
        setTasks(tasks.filter((t) => t !== task));
    }

    return (
        <div>
            <label htmlFor="new-task">New task</label>
            <input id="new-task" value={text} onChange={(e) => setText(e.target.value)} />
            <button onClick={add}>Add</button>
            <ul>
                {tasks.map((task) => (
                    <li key={task}>
                        {task}{" "}
                        <button aria-label={`Remove ${task}`} onClick={() => remove(task)}>
                            ✕
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
}
