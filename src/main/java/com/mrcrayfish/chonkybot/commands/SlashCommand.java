package com.mrcrayfish.chonkybot.commands;

import com.mrcrayfish.chonkybot.ChonkyBot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.function.BiConsumer;

/**
 * Author: MrCrayfish
 */
public abstract class SlashCommand
{
    protected final SlashCommandData data;

    public SlashCommand(SlashCommandData data)
    {
        this.data = data;
    }

    public final SlashCommandData data()
    {
        return this.data;
    }

    public abstract void handle(ChonkyBot bot, SlashCommandInteractionEvent event);
}
