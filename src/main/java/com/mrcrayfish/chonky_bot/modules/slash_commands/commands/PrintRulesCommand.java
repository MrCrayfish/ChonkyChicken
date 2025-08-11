package com.mrcrayfish.chonky_bot.modules.slash_commands.commands;

import com.mrcrayfish.chonky_bot.Constants;
import com.mrcrayfish.chonky_bot.modules.slash_commands.Response;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.components.thumbnail.Thumbnail;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class PrintRulesCommand extends SlashCommand
{
    public PrintRulesCommand()
    {
        super(Commands.slash("print_rules", "Prints the embeds for the rules channel"));
        this.data.setContexts(InteractionContextType.GUILD);
        this.data.setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }

    @Override
    public Response handleInteraction(SlashCommandInteractionEvent event)
    {
        if(event.getChannelType() != ChannelType.TEXT)
            return Response.fail("Only text channels are allowed to run this command");

        Container container = Container.of(
            Section.of(
                Thumbnail.fromUrl("https://media.forgecdn.net/avatars/thumbnails/934/904/64/64/638411339753659456.png"),
                TextDisplay.of("# MrCrayfish's Discord Server Rules")
            ),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("**:family:  ` Keep It Safe `**"),
            TextDisplay.of("This is a server of all ages, and to keep it safe environment we ask for all interactions, this includes but not limited to posting messages and images, to be appropriate and respectful. Derogatory language, hate speech, slurs, sensitive topics, and adult content is strictly prohibited and will be punished accordingly."),
            Separator.createDivider(Separator.Spacing.SMALL),
            TextDisplay.of("**:thumbs_up:  ` Use Common Sense `**"),
            TextDisplay.of("We think this is pretty obvious but no one appreciates spam, typing in all caps, or blatant self promotion; keep that out of this server. There is also no need to ping members unnecessarily, this includes MrCrayfish and moderators."),
            Separator.createDivider(Separator.Spacing.SMALL),
            TextDisplay.of("**:lock:  ` Keep On Topic `**"),
            TextDisplay.of("This server is a Minecraft community about modding, and this means conversations should be related to Minecraft and modding. We aren't going to do anything if conversation goes slightly off topic, but we do ask to take them elsewhere if they don't fit into any of our text channel topics."),
            Separator.createDivider(Separator.Spacing.SMALL),
            TextDisplay.of("**:man_construction_worker:️  ` Respect the Team `**"),
            TextDisplay.of("The moderators of this server are here to help keep the server safe, they are not obligated to answer your questions related to Minecraft, be that game crashes or bugs. Pinging moderators should be reserved for serious rule violations."),
            Separator.createDivider(Separator.Spacing.SMALL),
            TextDisplay.of("**:notebook_with_decorative_cover:  ` Discord TOS `**"),
            TextDisplay.of("We adhere by the Discord Terms of Service and Community Guidelines, as such we will take action against anyone who does not."),
            TextDisplay.of("*Terms of Service:* https://discord.com/terms"),
            TextDisplay.of("*Community Guidelines:* https://discord.com/guidelines")
        ).withAccentColor(Constants.COLOUR_YELLOW);
        event.getMessageChannel().sendMessageComponents(container).useComponentsV2().queue();

        return Response.success("Rules printed successfully :sunglasses:");
    }
}
