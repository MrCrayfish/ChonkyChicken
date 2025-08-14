package com.mrcrayfish.chonky_bot.modules.introductions;

import com.mrcrayfish.chonky_bot.GuildConfig;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public final class Introductions
{
    @SubscribeEvent
    private static void onMessageReceived(MessageReceivedEvent event)
    {
        if(!event.isFromGuild())
            return;

        GuildConfig config = GuildConfig.get(event.getGuild());
        if(!config.modules().introductions().enabled())
            return;

        if(event.getAuthor().isBot() || event.getAuthor().isSystem())
            return;

        Guild guild = event.getGuild();
        long channel = GuildConfig.get(guild).modules().introductions().channelId();
        if(event.getChannel().getIdLong() == channel)
        {
            String emoji = GuildConfig.get(guild).modules().introductions().reactionEmoji();
            event.getMessage().addReaction(Emoji.fromFormatted(emoji)).queue();
        }
    }
}
