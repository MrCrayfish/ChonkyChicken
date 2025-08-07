package com.mrcrayfish.chonky_bot;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.Nullable;

public class Util
{
    public static boolean isPrivilegedMember(User author, @Nullable Member member)
    {
        return author.isBot() || author.isSystem() || member != null && member.isOwner();
    }
}
