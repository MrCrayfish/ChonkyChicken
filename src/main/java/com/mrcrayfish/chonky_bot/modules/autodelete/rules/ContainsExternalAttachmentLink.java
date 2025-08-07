package com.mrcrayfish.chonky_bot.modules.autodelete.rules;

import com.mrcrayfish.chonky_bot.ChonkyBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContainsExternalAttachmentLink implements DeletionRule
{
    private static final Pattern PATTERN = Pattern.compile("(?:https?://)?(?:(cdn|media)\\.)?discord(?:app)?\\.(?:com|gg|net)/attachments/(?<channelid>\\d+)/.*");

    @Override
    public boolean test(Message msg)
    {
        Matcher matcher = PATTERN.matcher(msg.getContentRaw());
        while(matcher.find())
        {
            try
            {
                Guild guild = msg.getGuild();
                long channelId = Long.parseLong(matcher.group("channelid"));
                if(guild.getChannels(true).stream().mapToLong(ISnowflake::getIdLong).noneMatch(value -> value == channelId))
                {
                    return true;
                }
            }
            catch(Exception e)
            {
                ChonkyBot.LOGGER.error("Failed to delete external attachment", e);
            }
        }
        return false;
    }

    @Override
    public String reason()
    {
        return "External attachment links are not allowed";
    }

    @Override
    public int timeOutInSeconds()
    {
        return 60 * 5;
    }
}
