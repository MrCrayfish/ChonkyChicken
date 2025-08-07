package com.mrcrayfish.chonkybot.modules;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class DeleteEveryoneAndHereMention
{
    @SubscribeEvent
    public static void onReceivedMessage(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        if(msg.getContentRaw().contains("@everyone") || msg.getContentRaw().contains("@here"))
        {
            Member member = event.getMember();
            if(member != null && !member.hasPermission(msg.getGuildChannel(), Permission.MESSAGE_MENTION_EVERYONE))
            {
                // TODO instead send message to a moderation channel with special buttons to ban, kick, timeout, etc
                msg.delete().reason("@everyone and @here mention is not allowed").queue();
            }
        }
    }
}
