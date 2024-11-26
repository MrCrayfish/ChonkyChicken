package com.mrcrayfish.chonkybot;

import com.google.common.collect.ImmutableMap;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Author: MrCrayfish
 */
public class ChonkyBot extends ListenerAdapter
{
    public static final Logger LOGGER = LoggerFactory.getLogger(ChonkyBot.class);

    private final JDA bot;
    private final Map<String, BotCommand> commands;

    public ChonkyBot(String token)
    {
        this.bot = JDABuilder.createLight(token, Collections.emptyList())
            .addEventListeners(this)
            .build();
        this.commands = this.buildCommands();
        this.finaliseSetup();
    }

    private void finaliseSetup()
    {
        // Register slash commands
        CommandListUpdateAction commands = this.bot.updateCommands();
        commands.addCommands(this.commands.values().stream().map(BotCommand::data).toList());
        commands.queue();
    }

    private Map<String, BotCommand> buildCommands()
    {
        return SlashCommands.all().values().stream()
            .collect(ImmutableMap.toImmutableMap(command -> command.data()
                .getName(), Function.identity()));
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event)
    {
        if(event.getGuild() == null)
            return;
        Optional.ofNullable(this.commands.get(event.getName())).ifPresentOrElse(command -> {
            LOGGER.info("'{}' executed the command '/{}'", event.getUser().getName(), event.getName());
            command.handler().accept(this, event);
        }, () -> {
            LOGGER.error("Unknown or unregistered command '{}'", event.getName());
        });
    }
}
