package com.mrcrayfish.chonkybot;

import com.mrcrayfish.chonkybot.commands.SlashCommand;
import com.mrcrayfish.chonkybot.modules.SlashCommands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

/**
 * Author: MrCrayfish
 */
public class ChonkyBot
{
    public static final Logger LOGGER = LoggerFactory.getLogger(ChonkyBot.class);

    private final JDA bot;

    public ChonkyBot(String token)
    {
        this.bot = JDABuilder.createLight(token, Collections.emptyList())
                .setEventManager(new AnnotatedEventManager())
                .addEventListeners(this, SlashCommands.class)
                .build();
    }

    @SubscribeEvent
    private void onReady(ReadyEvent event)
    {
        // Update slash commands
        this.bot.updateCommands().addCommands(SlashCommands.all().values().stream().map(SlashCommand::data).toList()).queue();
    }
}
