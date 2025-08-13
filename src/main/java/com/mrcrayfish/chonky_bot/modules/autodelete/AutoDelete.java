package com.mrcrayfish.chonky_bot.modules.autodelete;

import com.mrcrayfish.chonky_bot.ChonkyBot;
import com.mrcrayfish.chonky_bot.GuildConfig;
import com.mrcrayfish.chonky_bot.Util;
import com.mrcrayfish.chonky_bot.modules.autodelete.rules.DeletionRule;
import com.mrcrayfish.chonky_bot.modules.autodelete.rules.ContainsExternalAttachmentLink;
import com.mrcrayfish.chonky_bot.modules.autodelete.rules.MentionsEveryoneOrHere;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public final class AutoDelete
{
    private static final List<DeletionRule> RULES = List.of(
        new MentionsEveryoneOrHere(),
        new ContainsExternalAttachmentLink()
    );

    @Nullable
    private static List<DeletionRule> findMatchingRules(Message message)
    {
        List<DeletionRule> matches = null;
        for(DeletionRule rule : RULES)
        {
            if(rule.test(message))
            {
                if(matches == null)
                {
                    matches = new ArrayList<>();
                }
                matches.add(rule);
            }
        }
        return matches;
    }

    private static void deleteMessageIfMatchesRule(Message message)
    {
        if(Util.isPrivilegedMember(message.getAuthor(), message.getMember()))
            return;

        List<DeletionRule> matches = findMatchingRules(message);
        if(matches == null)
            return;

        // Timeout the user if any of the matching rules have a valid timeout length
        Optional.ofNullable(message.getMember()).ifPresent(member -> {
            int maxSeconds = matches.stream()
                    .max(Comparator.comparing(DeletionRule::timeOutInSeconds))
                    .map(DeletionRule::timeOutInSeconds)
                    .orElse(0);
            if(maxSeconds > 0) {
                member.timeoutFor(maxSeconds, TimeUnit.SECONDS).queue();
            }
        });

        // Construct a reason based on matching rules and delete the message
        String reason = String.join(", ", matches.stream().map(DeletionRule::reason).toList());
        message.delete().reason(reason).queue(unused -> {
            ChonkyBot.LOGGER.info("Deleted message: {}", message.getId());
        }, throwable -> {
            ChonkyBot.LOGGER.info("Failed to delete message: {}", message.getId(), throwable);
        });
    }

    @SubscribeEvent
    private static void onMessageReceived(MessageReceivedEvent event)
    {
        if(!event.isFromGuild())
            return;

        GuildConfig config = GuildConfig.get(event.getGuild());
        if(!config.modules().autoDelete().enabled())
            return;

        deleteMessageIfMatchesRule(event.getMessage());
    }

    @SubscribeEvent
    private static void onMessageUpdated(MessageUpdateEvent event)
    {
        if(!event.isFromGuild())
            return;

        GuildConfig config = GuildConfig.get(event.getGuild());
        if(!config.modules().autoDelete().enabled())
            return;

        deleteMessageIfMatchesRule(event.getMessage());
    }
}
