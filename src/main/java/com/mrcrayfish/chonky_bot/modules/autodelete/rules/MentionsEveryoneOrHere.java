package com.mrcrayfish.chonky_bot.modules.autodelete.rules;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class MentionsEveryoneOrHere implements DeletionRule
{
    @Override
    public boolean test(Message msg)
    {
        if(msg.getContentRaw().contains("@everyone") || msg.getContentRaw().contains("@here"))
        {
            Member sender = msg.getMember();
            return sender != null && !sender.hasPermission(msg.getGuildChannel(), Permission.MESSAGE_MENTION_EVERYONE);
        }
        return false;
    }

    @Override
    public String reason()
    {
        return "@everyone and @here mention is not allowed";
    }

    @Override
    public int timeOutInSeconds()
    {
        return 60;
    }
}
