package com.mrcrayfish.chonkybot;

import com.mrcrayfish.chonkybot.commands.PrintRulesCommand;
import com.mrcrayfish.chonkybot.commands.SlashCommand;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public final class SlashCommands
{
    private static final Map<String, SlashCommand> COMMANDS = new HashMap<>();

    private static void register(SlashCommand command)
    {
        ChonkyBot.LOGGER.info("Registering slash command '/{}'", command.data().getName());
        COMMANDS.put(command.data().getName(), command);
    }

    public static Map<String, SlashCommand> all()
    {
        return Collections.unmodifiableMap(COMMANDS);
    }

    static
    {
    }
}
