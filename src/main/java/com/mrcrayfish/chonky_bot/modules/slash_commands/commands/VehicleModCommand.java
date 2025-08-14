package com.mrcrayfish.chonky_bot.modules.slash_commands.commands;

import com.mrcrayfish.chonky_bot.Emoji;
import com.mrcrayfish.chonky_bot.modules.slash_commands.Response;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class VehicleModCommand extends SlashCommand
{
    public VehicleModCommand()
    {
        super(Commands.slash("vehicle_mod", "Shows a message about the current status of the Vehicle Mod"));
        this.data.setContexts(InteractionContextType.GUILD);
    }

    @Override
    public Response handleInteraction(SlashCommandInteractionEvent event)
    {
        Container container = Container.of(
            TextDisplay.of("## %s  %s".formatted(Emoji.INFO, "Vehicle Mod development is on halt")),
            TextDisplay.of("https://discord.com/channels/336389026586165261/490397751616733194/1393975254250033273"),
            TextDisplay.of("> The vehicle mod needs serious reworks. It kept growing in features, which ultimately doomed ever seeing the update. It also happened to be that I was also updating the furniture mod (legacy version) at the time, but then I decided to do a full rewrite of it instead, which then the rewrite became a new mod (refurbished). It also came at a time where I wanted to relicense all my mods to a better license (and that required contacting everyone who made contributions). The mod is not dead, it needs to be rethought and simplified, and that will take time. I have other mods that need attention too. - MrCrayfish")
        ).withAccentColor(0x5A82E2);
        event.getMessageChannel().sendMessageComponents(
            container,
            TextDisplay.of("-# *Command executed by `%s` - %s*".formatted(event.getUser().getEffectiveName(), event.getUser().getIdLong()))
        ).useComponentsV2().queue();
        return Response.success("Success");
    }
}
