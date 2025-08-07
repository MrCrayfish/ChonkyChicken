package com.mrcrayfish.chonkybot.modules;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

import java.util.concurrent.TimeUnit;

public class DeleteEveryoneAndHereMention
{
    @SubscribeEvent
    private static void onMessageReceived(MessageReceivedEvent event)
    {
        runMentionCheck(event.getMessage(), event.getMember());
    }

    @SubscribeEvent
    private static void onMessageUpdated(MessageUpdateEvent event)
    {
        runMentionCheck(event.getMessage(), event.getMember());
    }

    private static void runMentionCheck(Message msg, Member sender)
    {
        if(msg.getContentRaw().contains("@everyone") || msg.getContentRaw().contains("@here"))
        {
            if(sender != null && !sender.hasPermission(msg.getGuildChannel(), Permission.MESSAGE_MENTION_EVERYONE))
            {
                // TODO instead send message to a moderation channel with special buttons to ban, kick, timeout, etc
                msg.delete().reason("@everyone and @here mention is not allowed").queue();
                sender.timeoutFor(60, TimeUnit.SECONDS).queue();
            }
        }
    }
}
