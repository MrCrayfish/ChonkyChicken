package com.mrcrayfish.chonky_bot.modules.slash_commands;

import org.jetbrains.annotations.Nullable;

public record Response(boolean success, String message)
{
    public static Response success(String message)
    {
        return new Response(true, message);
    }

    public static Response fail(String message)
    {
        return new Response(false, message);
    }

    public static Response fail()
    {
        return new Response(false, "An unknown error occurred");
    }
}
