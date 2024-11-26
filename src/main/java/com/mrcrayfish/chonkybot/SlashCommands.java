package com.mrcrayfish.chonkybot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public final class SlashCommands
{
    private static final Map<String, BotCommand> COMMANDS = new HashMap<>();

    private static void register(BotCommand command)
    {
        ChonkyBot.LOGGER.info("Registering slash command '/{}'", command.data().getName());
        COMMANDS.put(command.data().getName(), command);
    }

    public static Map<String, BotCommand> all()
    {
        return Collections.unmodifiableMap(COMMANDS);
    }

    static
    {
        /*register(new BotCommand(
            Commands.slash("say", "Says the message you sent")
                .addOption(OptionType.STRING, "content", "The message", true),
            (bot, event) -> {
                event.reply(event.getOption("content").getAsString()).setEphemeral(true).queue();
            }
        ));*/
    }
}
