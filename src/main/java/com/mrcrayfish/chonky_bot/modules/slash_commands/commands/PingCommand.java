package com.mrcrayfish.chonky_bot.modules.slash_commands.commands;

import com.mrcrayfish.chonky_bot.modules.slash_commands.Response;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Random;

public class PingCommand extends SlashCommand
{
    private final Random random = new Random();
    private final String[] responses = {
        "You're lucky I wasn't napping when you sent that",
        "I'm awake...",
        "Yeah, what do you want egghead?",
        "Chonky is awake but is in the middle of a dust bath"
    };

    public PingCommand()
    {
        super(Commands.slash("ping", "Pings to test if Chonky is not taking a nap!"));
        this.data.setContexts(InteractionContextType.GUILD);
        this.data.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }

    @Override
    public Response handle(SlashCommandInteractionEvent event)
    {
        event.reply(this.responses[this.random.nextInt(0, this.responses.length)]).setEphemeral(true).queue();
        return Response.success("");
    }
}
