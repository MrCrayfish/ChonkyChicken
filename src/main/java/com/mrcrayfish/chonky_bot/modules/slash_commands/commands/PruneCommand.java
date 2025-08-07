package com.mrcrayfish.chonky_bot.modules.slash_commands.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Author: MrCrayfish
 */
public class PruneCommand extends SlashCommand
{
    public PruneCommand()
    {
        super(Commands.slash("prune", "Prune messages for the given user"));
        this.data.addOption(OptionType.USER, "user", "The user to prune", true);
        this.data.addOption(OptionType.INTEGER, "message_count", "The maximum number of messages to remove (Default: 100, Max: 100)");
        this.data.addOption(OptionType.INTEGER, "search_depth", "The maximum amount of messages to scan (Default: 200, Max: 500)");
        this.data.addOption(OptionType.BOOLEAN, "all_channels", "Prune in every channel (Default: false)");
        this.data.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE));
        this.data.setContexts(InteractionContextType.GUILD);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event)
    {
        // Only allow from text channels
        if(event.getChannelType() != ChannelType.TEXT)
            return;

        User user = event.getOption("user").getAsUser();
        int messageCount = Math.clamp(event.getOption("message_count", 100, OptionMapping::getAsInt), 1, 100);
        int searchDepth = Math.clamp(event.getOption("search_depth", 200, OptionMapping::getAsInt), 1, 500);
        boolean allChannels = event.getOption("all_channels", false, OptionMapping::getAsBoolean);

        event.reply("Beginning prune...").setEphemeral(true).queue();

        // Get the channels to prune
        List<MessageChannel> channels = new ArrayList<>(List.of(event.getMessageChannel()));
        if(allChannels) {
            channels.clear();
            Objects.requireNonNull(event.getGuild()).getChannels().stream()
                .filter(TextChannel.class::isInstance)
                .map(TextChannel.class::cast)
                .forEach(channel -> {

                    MessageChannel textChannel = (MessageChannel) channel;
                    channels.add(textChannel);
                });
        }

        /*channels.forEach(c -> {
            c.getIterableHistory()
                .takeAsync(searchDepth)
                .thenApply(messages -> {
                    return messages.stream().filter(m -> m.getAuthor().equals(user)).toList();
                }).thenAccept(messages -> {
                    textChannel.purgeMessages(messages).forEach(future -> {
                        future.thenRun(() -> {

                        })
                    });
                    CompletableFuture.allOf(textChannel.purgeMessages(messages).toArray(CompletableFuture[]::new)).join();
                });
        });*/


        //event.reply("Purging messages for ");
    }
}
