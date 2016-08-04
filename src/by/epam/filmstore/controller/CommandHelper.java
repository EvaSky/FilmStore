package by.epam.filmstore.controller;



import by.epam.filmstore.command.Command;
import by.epam.filmstore.command.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Olga Shahray on 23.07.2016.
 */
public class CommandHelper {

    private static final CommandHelper instance = new CommandHelper();

    private Map<CommandName, Command> commands = new HashMap<>();

    CommandHelper(){

        commands.put(CommandName.USER_AUTHORIZATION, new LoginationCommand());
        commands.put(CommandName.USER_REGISTRATION, new SaveNewUserCommand());
        commands.put(CommandName.CHANGE_LANGUAGE, new ChangeLanguageCommand());
        commands.put(CommandName.GET_FILMS_BY_YEAR, new GetFilmsByYearCommand());
        commands.put(CommandName.SAVE_NEW_FILM, new SaveNewFilmCommand());

    }

    public Command getCommand(String name) {
        name = name.replace('-', '_');
        CommandName commandName = CommandName.valueOf(name.toUpperCase());

        return commands.get(commandName);
    }

    public static CommandHelper getInstance() {
        return instance;
    }
}
