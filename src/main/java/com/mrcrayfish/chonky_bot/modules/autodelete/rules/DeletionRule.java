package com.mrcrayfish.chonky_bot.modules.autodelete.rules;

import net.dv8tion.jda.api.entities.Message;

public interface DeletionRule
{
    boolean test(Message msg);

    String reason();

    int timeOutInSeconds();
}
