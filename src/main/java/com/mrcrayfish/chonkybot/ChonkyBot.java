package com.mrcrayfish.chonkybot;

import com.mrcrayfish.chonkybot.modules.DeleteEveryoneAndHereMention;
import com.mrcrayfish.chonkybot.modules.SlashCommands;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

/**
 * Author: MrCrayfish
 */
public class ChonkyBot
{
    public static final Logger LOGGER = LoggerFactory.getLogger(ChonkyBot.class);

    public ChonkyBot(String token)
    {
        JDABuilder.createLight(token, Collections.emptyList())
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .enableCache(CacheFlag.MEMBER_OVERRIDES)
                .setEventManager(new AnnotatedEventManager())
                .addEventListeners(SlashCommands.class, DeleteEveryoneAndHereMention.class)
                .build();
    }
}
