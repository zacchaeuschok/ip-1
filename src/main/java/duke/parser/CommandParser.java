package duke.parser;

import duke.exceptions.DukeException;
import duke.exceptions.InvalidCommandException;
import duke.tasks.TaskList;

import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser {
    enum Command {
        LIST,
        DELETE,
        DONE,
        BEFORE,
        FIND,
        TODO,
        EVENT,
        DEADLINE
    }

    private static final Pattern COMMAND_PATTERN =
            Pattern.compile("^([a-zA-Z]+)(?: ([^/]*))?(?: /([a-zA-Z]+))?(?: (.*))?$");
    private final TaskList taskList;

    public CommandParser(TaskList tasks) {
        this.taskList = tasks;
    }

    private Command generator(String action) throws InvalidCommandException {
        try {
            return Command.valueOf(action.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException();
        }
    }

    public void handle(String command) {
        try {
            // Identify groups based on command pattern
            Matcher matcher = COMMAND_PATTERN.matcher(command);
            matcher.find();
            String action = matcher.group(1);
            String desc = matcher.group(2);
            String time = matcher.group(4);
            Command cmd = generator(action);

            // Call methods according to command
            switch (cmd) {
            case LIST:
                taskList.printList();
                break;
            case DONE:
                taskList.markDone(desc);
                break;
            case BEFORE:
                taskList.printDeadline(time);
                break;
            case FIND:
                taskList.find(desc);
                break;
            case DELETE:
                taskList.delete(desc);
                break;
            case TODO:
                taskList.addToDo(desc);
                break;
            case EVENT:
                taskList.addEvent(desc, time);
                break;
            case DEADLINE:
                taskList.addDeadline(desc, time);
                break;
            default:
                throw new InvalidCommandException();
            }
        } catch (DukeException e) {
            System.out.println(e);
        } catch (DateTimeParseException e) {
            System.out.println("Please enter the correct due date format d/mm/yyyy [HHmm]");
        }
    }
}
