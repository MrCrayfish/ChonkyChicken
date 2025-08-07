package com.mrcrayfish.chonky_bot.modules.slash_commands;

import com.mrcrayfish.chonky_bot.ChonkyBot;
import com.mrcrayfish.chonky_bot.modules.slash_commands.commands.PrintRulesCommand;
import com.mrcrayfish.chonky_bot.modules.slash_commands.commands.PruneCommand;
import com.mrcrayfish.chonky_bot.modules.slash_commands.commands.SlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    @SubscribeEvent
    private static void onReady(ReadyEvent event)
    {
        // Update slash commands
        event.getJDA().updateCommands().addCommands(SlashCommands.all().values().stream().map(SlashCommand::data).toList()).queue();
    }

    @SubscribeEvent
    private static void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event)
    {
        if(event.getGuild() == null)
            return;
        Optional.ofNullable(COMMANDS.get(event.getInteraction().getName())).ifPresentOrElse(command -> {
            ChonkyBot.LOGGER.info("'{}' executed the command '/{}'", event.getUser().getName(), event.getName());
            command.handle(event);
        }, () -> {
            ChonkyBot.LOGGER.error("Unknown or unregistered command '{}'", event.getName());
        });
    }

    static
    {
        register(new PrintRulesCommand());
        register(new PruneCommand());
    }
}
