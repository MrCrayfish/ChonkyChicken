package com.mrcrayfish.chonky_bot.modules.slash_commands.commands;

import com.mrcrayfish.chonky_bot.Emoji;
import com.mrcrayfish.chonky_bot.modules.slash_commands.Response;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class DeviceModCommand extends SlashCommand
{
    public DeviceModCommand()
    {
        super(Commands.slash("device_mod", "Shows a disclaimer about the Device Mod being discontinued"));
        this.data.setContexts(InteractionContextType.GUILD);
    }

    @Override
    public Response handleInteraction(SlashCommandInteractionEvent event)
    {
        Container container = Container.of(
            TextDisplay.of("## %s  %s".formatted(Emoji.INFO, "The Device Mod has been Archived")),
            TextDisplay.of("The Device Mod has been discontinued. This means that the mod will no longer be updated and support will not be provided. MrCrayfish no longer enjoys developing the mod and would rather focus on other projects. Thank you for understanding :thumbsup:")
        ).withAccentColor(0x5A82E2);
        event.getMessageChannel().sendMessageComponents(container).useComponentsV2().queue();
        return Response.success("Success");
    }
}
