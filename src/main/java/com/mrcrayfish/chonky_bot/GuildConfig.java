package com.mrcrayfish.chonky_bot;

import net.dv8tion.jda.api.entities.Guild;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@ConfigSerializable
public class GuildConfig
{
    @Required
    private Modules modules = new Modules();

    public Modules modules()
    {
        return this.modules;
    }

    @ConfigSerializable
    public static class Modules
    {
        @Required
        private Fortune fortune = new Fortune();

        public Fortune fortune()
        {
            return this.fortune;
        }

        @ConfigSerializable
        public static class Fortune
        {
            @Required
            @Comment("The ID of the text channel you want users to be able to ask their fortune")
            private Long channelId = -1L;

            public long channelId()
            {
                return this.channelId;
            }
        }
    }

    private static final Map<Long, GuildConfig> CONFIGS = new HashMap<>();

    public static GuildConfig get(Guild guild)
    {
        GuildConfig config = CONFIGS.get(guild.getIdLong());
        if(config == null)
        {
            return load(guild);
        }
        return config;
    }

    private static GuildConfig load(Guild guild)
    {
        long guildId = guild.getIdLong();
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
            .indent(2).nodeStyle(NodeStyle.BLOCK)
            .path(Paths.get("configs", "%s.yaml".formatted(guildId)))
            .build();

        try
        {
            final CommentedConfigurationNode node = loader.load();
            GuildConfig config = node.get(GuildConfig.class, (Supplier<GuildConfig>) GuildConfig::new);
            CONFIGS.put(guildId, config);
            loader.save(node); // Save to update style/indent
            return config;
        }
        catch(ConfigurateException e)
        {
            GuildConfig config = new GuildConfig();
            CONFIGS.put(guildId, config);
            return config;
        }
    }

    public static boolean load(Guild guild, String url)
    {
        try
        {
            URL u = new URI(url).toURL();
            if(!u.getProtocol().equals("https"))
                return false;

            // Load the config from URL and map to Config object to filter out unused properties
            CommentedConfigurationNode node = YamlConfigurationLoader.builder().url(u).build().load();
            GuildConfig config = node.get(GuildConfig.class, (Supplier<GuildConfig>) GuildConfig::new);

            // Create an empty node and update it with the Config object
            CommentedConfigurationNode temp = CommentedConfigurationNode.root();
            temp.set(GuildConfig.class, config);

            // Finally save the config to file
            long guildId = guild.getIdLong();
            YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                    .indent(2).nodeStyle(NodeStyle.BLOCK)
                    .path(Paths.get("configs", "%s.yaml".formatted(guildId)))
                    .build();
            loader.save(temp);

            CONFIGS.put(guildId, config);
            return true;
        }
        catch(URISyntaxException | MalformedURLException | ConfigurateException e)
        {
            return false;
        }
    }
}
