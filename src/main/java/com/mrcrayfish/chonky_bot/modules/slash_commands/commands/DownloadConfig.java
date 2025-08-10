
package com.mrcrayfish.chonky_bot.modules.slash_commands.commands;

import com.mrcrayfish.chonky_bot.GuildConfig;
import com.mrcrayfish.chonky_bot.modules.slash_commands.Response;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.FileUpload;

import java.nio.file.Path;

public class DownloadConfig extends SlashCommand
{
    public DownloadConfig()
    {
        super(Commands.slash("download_config", "Download the config file for the bot"));
        this.data.setContexts(InteractionContextType.GUILD);
        this.data.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }

    @Override
    public Response handle(SlashCommandInteractionEvent event)
    {
        Guild guild = event.getGuild();
        if(guild == null)
            return Response.fail("Unknown guild");

        event.replyFiles(FileUpload.fromData(GuildConfig.path(guild), "config.yaml")).setEphemeral(true).queue();
        return Response.success("Successfully uploaded config");
    }
}
