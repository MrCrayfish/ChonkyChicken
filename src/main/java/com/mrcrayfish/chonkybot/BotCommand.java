package com.mrcrayfish.chonkybot;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.function.BiConsumer;

/**
 * Author: MrCrayfish
 */
public record BotCommand(SlashCommandData data, BiConsumer<ChonkyBot, SlashCommandInteractionEvent> handler)
{
}
