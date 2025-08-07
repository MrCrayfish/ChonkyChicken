package com.mrcrayfish.chonky_bot;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Author: MrCrayfish
 */
public class Main
{
    public static void main(String[] args)
    {
        Dotenv dotenv = Dotenv.load();
        String botToken = dotenv.get("BOT_TOKEN");
        new ChonkyBot(botToken);
    }
}
