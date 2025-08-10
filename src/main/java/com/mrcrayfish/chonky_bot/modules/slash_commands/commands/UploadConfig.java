package com.mrcrayfish.chonky_bot.modules.slash_commands.commands;

import com.mrcrayfish.chonky_bot.GuildConfig;
import com.mrcrayfish.chonky_bot.modules.slash_commands.Response;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class UploadConfig extends SlashCommand
{
    public UploadConfig()
    {
        super(Commands.slash("upload_config", "Uploads a config file to the bot"));
        this.data.addOption(OptionType.ATTACHMENT, "file", "A YAML file", true);
        this.data.setContexts(InteractionContextType.GUILD);
        this.data.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }

    @Override
    public Response handle(SlashCommandInteractionEvent event)
    {
        Message.Attachment attachment = event.getOption("file", OptionMapping::getAsAttachment);
        if(attachment == null)
            return Response.fail("The config file is missing");

        String contentType = attachment.getContentType();
        if(contentType == null || !contentType.startsWith("text/plain"))
            return Response.fail("Only YAML files are accepted");

        if(!"yaml".equals(attachment.getFileExtension()) && !"yml".equals(attachment.getFileExtension()))
            return Response.fail("Only YAML files are accepted");

        Guild guild = event.getGuild();
        if(guild == null)
            return Response.fail("Unknown guild");

        if(!GuildConfig.load(guild, attachment.getUrl()))
            return Response.fail("Failed to process config");

        return Response.success("Successfully uploaded config");
    }
}
