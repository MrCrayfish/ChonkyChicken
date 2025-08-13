package com.mrcrayfish.chonky_bot.modules.mod_support;

import com.mrcrayfish.chonky_bot.Emoji;
import com.mrcrayfish.chonky_bot.GuildConfig;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.forums.ForumTagSnowflake;
import net.dv8tion.jda.api.entities.channel.unions.IThreadContainerUnion;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

import java.util.ArrayList;

public class ModSupport
{
    @SubscribeEvent
    private static void onThreadCreated(ChannelCreateEvent event)
    {
        if(!event.isFromGuild())
            return;

        GuildConfig config = GuildConfig.get(event.getGuild());
        if(!config.modules().modSupport().enabled())
            return;

        if(!event.getChannelType().isThread())
            return;

        // Ignore if not a forum channel
        ThreadChannel thread = event.getChannel().asThreadChannel();
        IThreadContainerUnion parent = thread.getParentChannel();
        if(parent.getType() != ChannelType.FORUM)
            return;

        // Only allow tickets to happen in a specific forum channel
        Guild guild = event.getGuild();
        long channel = GuildConfig.get(guild).modules().modSupport().channelId();
        if(parent.getIdLong() != channel)
            return;

        // Finally display a container with ticket information and a button to mark as resolved
        Container container = Container.of(
            TextDisplay.of("### %s  %s".formatted(Emoji.INFO, "Thanks for submitting a ticket!")),
            TextDisplay.of("To get the best support, please update your post if it does not contain the **name** and **version** of the mod, the **modloader**, and the exact **Minecraft version** you are playing on. If you received a crash, please submit it to [mclo.gs](https://mclo.gs/), then copy and paste the link into your post or send it below."),
            Separator.createDivider(Separator.Spacing.LARGE),
            TextDisplay.of("Once your issue has been solved, please click the button to mark the ticket as :white_check_mark: `Solved`. This will help the community by letting them know a solution has been found (should someone else run into the same issue). **Thank you!**"),
            ActionRow.of(Button.of(ButtonStyle.SUCCESS, "mod_support_resolved", "Mark as Solved"))
        ).withAccentColor(0x5A82E2);
        thread.sendMessageComponents(container).useComponentsV2().queue();
    }

    @SubscribeEvent
    private static void onButtonInteraction(ButtonInteractionEvent event)
    {
        Guild guild = event.getGuild();
        if(guild == null)
            return;

        GuildConfig config = GuildConfig.get(guild);
        if(!config.modules().modSupport().enabled())
            return;

        if(event.getComponentId().startsWith("mod_support_resolved"))
        {
            if(!event.getChannelType().isThread())
            {
                event.replyComponents(responseContainer(false, "Invalid interaction")).useComponentsV2().setEphemeral(true).queue();
                return;
            }

            ThreadChannel thread = event.getChannel().asThreadChannel();
            IThreadContainerUnion parent = thread.getParentChannel();
            if(parent.getType() != ChannelType.FORUM)
            {
                event.replyComponents(responseContainer(false, "Invalid interaction")).useComponentsV2().setEphemeral(true).queue();
                return;
            }

            // Make sure the interaction came from the correct support forum channel
            long channel = config.modules().modSupport().channelId();
            if(parent.getIdLong() != channel)
            {
                event.replyComponents(responseContainer(false, "Invalid interaction")).useComponentsV2().setEphemeral(true).queue();
                return;
            }

            Member member = event.getMember();
            if(member == null)
            {
                event.replyComponents(responseContainer(false, "Invalid interaction")).useComponentsV2().setEphemeral(true).queue();
                return;
            }

            // Prevent other members marking ticket as resolved
            if(thread.getOwnerIdLong() != member.getIdLong() && !member.hasPermission(Permission.MANAGE_THREADS))
            {
                event.replyComponents(responseContainer(false, "You do not have the permission to perform this action")).useComponentsV2().setEphemeral(true).queue();
                return;
            }

            // Updates the forum tags of this thread to include the solved tag
            ArrayList<ForumTagSnowflake> tags = new ArrayList<>();
            tags.addFirst(ForumTagSnowflake.fromId(config.modules().modSupport().solvedTagId()));
            tags.addAll(thread.getAppliedTags());
            while(tags.size() > 5)
            {
                tags.removeLast();
            }
            thread.getManager().setAppliedTags(tags).queue();

            // Disable the button to prevent repeating the action
            event.getInteraction().editButton(event.getInteraction().getButton().asDisabled()).queue();

            // Finally send a container indicating the ticket is now marked as resolved
            thread.sendMessageComponents(Container.of(
                TextDisplay.of("### %s  %s".formatted(":white_check_mark:", "Issue Solved!")),
                TextDisplay.of("`%s` marked this ticket as resolved".formatted(event.getUser().getEffectiveName()))
            ).withAccentColor(0x77D100)).useComponentsV2().queue();
        }
    }

    private static Container responseContainer(boolean success, String message)
    {
        return Container.of(
            TextDisplay.of(success ? "### :thumbsup:  Success" : ":x:  Failed"),
            TextDisplay.of(message)
        );
    }
}
