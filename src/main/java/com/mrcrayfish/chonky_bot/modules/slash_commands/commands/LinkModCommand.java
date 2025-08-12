package com.mrcrayfish.chonky_bot.modules.slash_commands.commands;

import com.mrcrayfish.chonky_bot.Emoji;
import com.mrcrayfish.chonky_bot.GuildConfig;
import com.mrcrayfish.chonky_bot.modules.slash_commands.Response;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.components.thumbnail.Thumbnail;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LinkModCommand extends SlashCommand
{
    public LinkModCommand()
    {
        super(Commands.slash("link_mod", "Displays an information card about a Minecraft mod"));
        this.data.addOption(OptionType.STRING, "mod_id", "The id of the mod", true, true);
        this.data.setContexts(InteractionContextType.GUILD);
    }

    @Override
    public Response handleInteraction(SlashCommandInteractionEvent event)
    {
        String modId = event.getOption("mod_id", OptionMapping::getAsString);
        if(modId == null)
            return Response.fail("`mod_id` option is required");

        Guild guild = event.getGuild();
        if(guild == null)
            return Response.fail("Unknown guild");

        GuildConfig.MinecraftMod mod = GuildConfig.get(guild).minecraftMods().stream()
            .filter(m -> m.id().equals(modId))
            .findFirst().orElse(null);
        if(mod == null)
            return Response.fail("No mod found for the given `mod_id`");

        List<ContainerChildComponent> components = new ArrayList<>();
        components.add(Section.of(
            Thumbnail.fromUrl(mod.thumbnailUrl()),
            TextDisplay.of("# %s".formatted(mod.title())),
            TextDisplay.of(mod.description())
        ));
        components.add(Separator.create(true, Separator.Spacing.LARGE));
        components.add(this.buildDownloadDisplay(mod));
        event.getMessageChannel().sendMessageComponents(Container.of(components)).useComponentsV2().queue();
        return Response.success("Success");
    }

    private TextDisplay buildDownloadDisplay(GuildConfig.MinecraftMod mod)
    {
        List<String> downloads = new ArrayList<>();
        downloads.add("%s [%s](%s)".formatted(Emoji.OFFICIAL_DOWNLOAD, "Official Download", mod.primaryDownloadPage()));
        Optional.ofNullable(mod.curseforgeDownloadPage()).ifPresent(link -> downloads.add("%s [%s](%s)".formatted(Emoji.CURSEFORGE, "CurseForge", link)));
        Optional.ofNullable(mod.modrinthDownloadPage()).ifPresent(link -> downloads.add("%s [%s](%s)".formatted(Emoji.MODRINTH, "Modrinth", link)));
        return TextDisplay.of(String.join("  -  ", downloads));
    }

    @Override
    public void handleAutoComplete(CommandAutoCompleteInteractionEvent event)
    {
        // Need guild to access configs
        Guild guild = event.getGuild();
        if(guild == null)
            return;

        String option = event.getFocusedOption().getName();
        if(option.equals("mod_id"))
        {
            GuildConfig config = GuildConfig.get(guild);
            List<Command.Choice> choices = config.minecraftMods().stream()
                    .map(mod -> new Command.Choice(mod.title(), mod.id())).toList();
            event.replyChoices(choices).queue();
        }
    }
}
