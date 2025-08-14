package com.mrcrayfish.chonky_bot.modules.slash_commands;

import com.mrcrayfish.chonky_bot.ChonkyBot;
import com.mrcrayfish.chonky_bot.modules.slash_commands.commands.*;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
            Response response = command.handleInteraction(event);
            if(!event.isAcknowledged()) {
                String title = response.success() ? "**:thumbsup: Success**" : "**:no_entry: Command Failed**";
                event.replyComponents(Container.of(
                        TextDisplay.of(title),
                        Separator.createDivider(Separator.Spacing.SMALL),
                        TextDisplay.of(response.message())
                )).useComponentsV2().setEphemeral(true).queue();
            }
        }, () -> {
            ChonkyBot.LOGGER.error("Unknown or unregistered command '{}'", event.getName());
        });
    }

    @SubscribeEvent
    private static void onAutoComplete(CommandAutoCompleteInteractionEvent event)
    {
        SlashCommand command = COMMANDS.get(event.getInteraction().getName());
        if(command != null)
        {
            command.handleAutoComplete(event);
        }
    }

    static
    {
        register(new PingCommand());
        register(new PrintRulesCommand());
        register(new PruneCommand());
        register(new UploadConfigCommand());
        register(new DownloadConfigCommand());
        register(new LinkModCommand());
        register(new DeviceModCommand());
    }
}
