package com.mrcrayfish.chonky_bot.modules.slash_commands.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
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
        this.data.addOption(OptionType.INTEGER, "count", "The maximum number of messages to be removed per channel (Min: 1, Max: 100)", true);
        this.data.addOption(OptionType.USER, "user", "A specific user to prune");
        this.data.addOption(OptionType.INTEGER, "search_depth", "The amount of messages to scan in a channel's history (Default: 100, Max: 500)");
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

        int limitPerChannel = event.getOption("count", 1, mapping -> Math.clamp(mapping.getAsInt(), 1, 100));
        User targetUser = event.getOption("user", OptionMapping::getAsUser);
        int maxSearchDepth = targetUser == null ? 500 : limitPerChannel;
        int searchDepth = Math.clamp(event.getOption("search_depth", 100, OptionMapping::getAsInt), 1, maxSearchDepth);
        boolean allChannels = event.getOption("all_channels", false, OptionMapping::getAsBoolean);

        Member member = event.getMember();
        if(member == null)
            return;

        if(allChannels && !member.isOwner())
            return;

        Guild guild = event.getGuild();
        if(guild == null)
            return;

        List<TextChannel> channels = new ArrayList<>();
        if(allChannels)
        {
            channels.addAll(guild.getTextChannels());
        }
        else if(event.getChannel() instanceof TextChannel)
        {
            channels.add((TextChannel) event.getChannel());
        }

        int pruneCount = channels.stream().map(channel -> channel.getIterableHistory()
            .takeAsync(searchDepth)
            .thenApply(messages -> messages.stream()
                .filter(msg -> targetUser == null || msg.getAuthor().equals(targetUser))
                .limit(limitPerChannel)
                .toList())
            .thenApplyAsync(messages -> {
                AtomicInteger counter = new AtomicInteger();
                List<CompletableFuture<Void>> purgeFutures = channel.purgeMessages(messages);
                CompletableFuture<?>[] tasks = purgeFutures.stream().map(future -> {
                    return future.whenComplete((v, e) -> {
                        if(e == null) counter.addAndGet(messages.size());
                    });
                }).toArray(CompletableFuture[]::new);
                CompletableFuture.allOf(tasks).join();
                return counter.get();
            }).join()).reduce(Integer::sum).orElse(0);

        this.sendResponse(event, targetUser != null
                ? "Pruned `%s` messages from user: `%s`".formatted(pruneCount, targetUser.getEffectiveName())
                : "Pruned `%s` messages".formatted(pruneCount));
    }

    private void sendResponse(IReplyCallback callback, String message)
    {
        Container container = Container.of(
                TextDisplay.of("**:thumbsup: Job Complete**"),
                Separator.createDivider(Separator.Spacing.SMALL),
                TextDisplay.of(message)
        );
        callback.replyComponents(container).useComponentsV2().setEphemeral(true).queue();
    }
}
