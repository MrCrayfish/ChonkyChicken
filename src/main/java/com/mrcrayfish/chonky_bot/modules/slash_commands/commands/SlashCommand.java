package com.mrcrayfish.chonky_bot.modules.slash_commands.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

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

    public abstract void handle(SlashCommandInteractionEvent event);
}
