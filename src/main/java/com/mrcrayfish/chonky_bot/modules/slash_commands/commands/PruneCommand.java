package com.mrcrayfish.chonky_bot.modules.slash_commands.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
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
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: MrCrayfish
 */
public class PruneCommand extends SlashCommand
{
    public PruneCommand()
    {
        super(Commands.slash("prune", "Prune messages for the given user"));
        this.data.addOption(OptionType.USER, "user", "A specific user to prune");
        this.data.addOption(OptionType.INTEGER, "limit_per_channel", "The maximum number of messages that can be removed per channel (Default: 10, Max: 100)");
        this.data.addOption(OptionType.INTEGER, "search_history_depth", "The maximum amount of messages to scan in a channel's history (Default: 10, Max: 500)");
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

        User user = event.getOption("user", OptionMapping::getAsUser);
        int limitPerChannel = Math.clamp(event.getOption("limit_per_channel", 100, OptionMapping::getAsInt), 1, 100);
        int maxSearchDepth = user == null ? 500 : limitPerChannel;
        int searchDepth = Math.clamp(event.getOption("search_history_depth", 200, OptionMapping::getAsInt), 1, maxSearchDepth);
        boolean allChannels = event.getOption("all_channels", false, OptionMapping::getAsBoolean);

        Optional.ofNullable(event.getGuild()).ifPresent(guild -> {
            List<TextChannel> channels = new ArrayList<>();
            if(allChannels) {
                channels.addAll(guild.getTextChannels());
            } else if(event.getChannel() instanceof TextChannel) {
                channels.add((TextChannel) event.getChannel());
            }

            int pruneCount = channels.stream().map(channel -> channel.getIterableHistory()
                .takeAsync(searchDepth)
                .thenApply(messages -> messages.stream()
                    .filter(m -> m.getAuthor().equals(user))
                    .limit(limitPerChannel)
                    .toList())
                .thenApplyAsync(messages -> {
                    AtomicInteger counter = new AtomicInteger();
                    CompletableFuture<?>[] tasks = channel.purgeMessages(messages).stream().map(future -> {
                        return future.whenComplete((v, e) -> {
                            if(e == null) counter.incrementAndGet();
                        });
                    }).toArray(CompletableFuture[]::new);
                    CompletableFuture.allOf(tasks).join();
                    return counter.get();
                })).map(CompletableFuture::join).reduce(Integer::sum).orElse(0);

            EmbedBuilder builder = new EmbedBuilder();
            if(user != null) {
                builder.setDescription("Pruned `%s` messages from user: `%s`".formatted(pruneCount, user.getEffectiveName()));
            } else {
                builder.setDescription("Pruned `%s` messages".formatted(pruneCount));
            }
            event.replyEmbeds(builder.build()).setEphemeral(true).queue();
        });
    }
}
