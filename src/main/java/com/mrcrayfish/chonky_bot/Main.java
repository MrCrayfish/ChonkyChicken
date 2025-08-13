package com.mrcrayfish.chonky_bot;

import io.github.cdimascio.dotenv.Dotenv;

import java.nio.file.Path;

/**
 * Author: MrCrayfish
 */
public class Main
{
    public static void main(String[] args)
    {
        Dotenv dotenv = Dotenv.load();
        String botToken = dotenv.get("BOT_TOKEN");
        if(botToken == null || botToken.isBlank())
            throw new IllegalArgumentException("BOT_TOKEN is not set in " + Path.of(".env").toAbsolutePath());
        new ChonkyBot(botToken);
    }
}
