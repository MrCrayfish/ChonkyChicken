package com.mrcrayfish.chonkybot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public final class SlashCommands
{
    private static final Map<String, BotCommand> COMMANDS = new HashMap<>();

    private static void register(BotCommand command)
    {
        ChonkyBot.LOGGER.info("Registering slash command '/{}'", command.data().getName());
        COMMANDS.put(command.data().getName(), command);
    }

    public static Map<String, BotCommand> all()
    {
        return Collections.unmodifiableMap(COMMANDS);
    }

    static
    {
        register(new BotCommand(
            Commands.slash("print_rules", "Prints the embeds for the rules channel")
                .setGuildOnly(true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
            (bot, event) -> {
                if(event.getMessageChannel().getType() != ChannelType.TEXT)
                    return;

                List<MessageEmbed> embeds = new ArrayList<>();
                embeds.add(new EmbedBuilder()
                    .setColor(Constants.COLOUR_YELLOW)
                    .setUrl(Constants.WEBSITE_URL)
                    .setImage("https://i.imgur.com/TNvKRIi.png")
                    .build());
                embeds.add(new EmbedBuilder()
                    .setColor(Constants.COLOUR_YELLOW)
                    .setUrl(Constants.WEBSITE_URL)
                    .setImage("https://i.imgur.com/t1GJNnZ.png")
                    .build());

                var rules = """
                    **:family:  ` Keep It Safe `**
                    > This is a server of all ages, and to keep it safe environment we ask for all interactions, this includes but not limited to posting messages and images, to be appropriate and respectful. Derogatory language, hate speech, slurs, sensitive topics, and adult content is strictly prohibited and will be punished accordingly.
                                       
                    **:thumbs_up:  ` Use Common Sense `**
                    > We think this is pretty obvious but no one appreciates spam, typing in all caps, or blatant self promotion; keep that out of this server. There is also no need to ping members unnecessarily, this includes MrCrayfish and moderators.
                                       
                    **:lock:  ` Keep On Topic `**
                    > This server is a Minecraft community about modding, and this means conversations should be related to Minecraft and modding. We aren't going to do anything if conversation goes slightly off topic, but we do ask to take them elsewhere if they don't fit into any of our text channel topics.
                                       
                    **:man_construction_worker:️  ` Respect the Team `**
                    > The moderators of this server are here to help keep the server safe, they are not obligated to answer your questions related to Minecraft, be that game crashes or bugs. Pinging moderators should be reserved for serious rule violations.
                                       
                    **:notebook_with_decorative_cover:  ` Discord TOS `**
                    > We adhere by the Discord Terms of Service and Community Guidelines, as such we will take action against anyone who does not. You can read them here:
                    > *Terms of Service:* https://discord.com/terms
                    > *Community Guidelines:* https://discord.com/guidelines
                    """;
                embeds.add(new EmbedBuilder()
                    .setTitle("MrCrayfish's Discord Server Rules")
                    .setDescription(rules)
                    .setColor(Constants.COLOUR_YELLOW)
                    .build());

                event.getMessageChannel().sendMessageEmbeds(embeds).queue();
                event.reply("Success!").setEphemeral(true).queue();
            }
        ));
    }
}
