package com.mrcrayfish.chonky_bot;

import de.exlll.configlib.*;
import de.exlll.configlib.Configuration;
import net.dv8tion.jda.api.entities.Guild;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Configuration
public final class GuildConfig
{
    @Comment("A set of your Minecraft mods")
    private Set<MinecraftMod> minecraftMods = new HashSet<>();

    @Comment("Module related properties")
    private Modules modules = new Modules();

    public Set<MinecraftMod> minecraftMods()
    {
        return this.minecraftMods;
    }

    public Modules modules()
    {
        return this.modules;
    }

    @Configuration
    public static class MinecraftMod
    {
        @Comment("[Required] The id of the mod")
        private String id;

        @Comment("[Required] The title of the mod")
        private String title;

        @Comment("[Required] A short description of the mod")
        private String description;

        @Comment("[Required] The primary website page that hosts the downloads")
        private String primaryDownloadPage;

        @Comment("[Optional] The CurseForge page to download the mod")
        private String curseforgeDownloadPage;

        @Comment("[Optional] The Modrinth page to download the mod")
        private String modrinthDownloadPage;

        @Comment("[Required] A URL to an image to display in the container. Must be a square.")
        private String thumbnailUrl;

        public String id()
        {
            return this.id;
        }

        public String title()
        {
            return this.title;
        }

        public String description()
        {
            return this.description;
        }

        public String primaryDownloadPage()
        {
            return this.primaryDownloadPage;
        }

        public String curseforgeDownloadPage()
        {
            return this.curseforgeDownloadPage;
        }

        public String modrinthDownloadPage()
        {
            return this.modrinthDownloadPage;
        }

        public String thumbnailUrl()
        {
            return this.thumbnailUrl;
        }
    }

    @Configuration
    public static class Modules
    {
        private AutoDelete autoDelete = new AutoDelete();
        private Fortune fortune = new Fortune();
        private Introductions introductions = new Introductions();
        private ModSupport modSupport = new ModSupport();

        public AutoDelete autoDelete()
        {
            return this.autoDelete;
        }

        public Fortune fortune()
        {
            return this.fortune;
        }

        public Introductions introductions()
        {
            return this.introductions;
        }

        public ModSupport modSupport()
        {
            return this.modSupport;
        }

        @Configuration
        public static class AutoDelete
        {
            @Comment("If set to true, this module will be enabled")
            private Boolean enabled = false;

            public boolean enabled()
            {
                return this.enabled;
            }
        }

        @Configuration
        public static class Fortune
        {
            @Comment("If set to true, this module will be enabled")
            private Boolean enabled = false;

            @Comment("The ID of the text channel you want users to be able to ask their fortune")
            private Long channelId = -1L;

            public boolean enabled()
            {
                return this.enabled;
            }

            public long channelId()
            {
                return this.channelId;
            }
        }

        @Configuration
        public static class Introductions
        {
            @Comment("If set to true, this module will be enabled")
            private Boolean enabled = false;

            @Comment("The ID of the text channel where users post their introductions")
            private Long channelId = -1L;

            @Comment("The emoji to use to automatically react on new messages.")
            private String reactionEmoji = ":wave:";

            public boolean enabled()
            {
                return this.enabled;
            }

            public long channelId()
            {
                return this.channelId;
            }

            public String reactionEmoji()
            {
                return this.reactionEmoji;
            }
        }

        @Configuration
        public static class ModSupport
        {
            @Comment("If set to true, this module will be enabled")
            private Boolean enabled = false;

            @Comment("The ID of the forum channel users can post mod support tickets")
            private Long channelId = -1L;

            @Comment("The ID of the forum tag to indicate a thread ticket as solved")
            private Long solvedTagId = -1L;

            public boolean enabled()
            {
                return this.enabled;
            }

            public long channelId()
            {
                return this.channelId;
            }

            public Long solvedTagId()
            {
                return this.solvedTagId;
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
        Path path = Paths.get("configs", "%s.yaml".formatted(guildId));
        YamlConfigurationProperties properties = YamlConfigurationProperties.newBuilder().build();
        YamlConfigurationStore<GuildConfig> store = new YamlConfigurationStore<>(GuildConfig.class, properties);
        GuildConfig config = store.update(path);
        CONFIGS.put(guild.getIdLong(), config);
        return config;
    }

    public static Optional<String> load(Guild guild, String url)
    {
        try
        {
            URL u = new URI(url).toURL();
            if(!u.getProtocol().equals("https"))
            {
                return Optional.of("URL must be https");
            }

            try(InputStream is = u.openStream())
            {
                GuildConfig config = YamlConfigurations.read(is, GuildConfig.class);
                /* List<String> violations = validate(config);
                if(!violations.isEmpty())
                {
                    return Optional.of(String.join("\n", violations));
                }*/
                long guildId = guild.getIdLong();
                Path path = Paths.get("configs", "%s.yaml".formatted(guildId));
                YamlConfigurations.save(path, GuildConfig.class, config);
                CONFIGS.put(guildId, config);
                return Optional.empty();
            }
            catch(ConfigurationException e)
            {
                ChonkyBot.LOGGER.error("Failed to load config from URL", e);
                Throwable cause = e.getCause();
                if(cause != null)
                {
                    return Optional.of("Invalid YAML: %s".formatted(cause.getMessage()));
                }
                return Optional.of("Invalid YAML");
            }
            catch(RuntimeException e)
            {
                ChonkyBot.LOGGER.error("Failed to load config from URL", e);
                return Optional.of("Failed to load YAML");
            }
            catch(IOException e)
            {
                ChonkyBot.LOGGER.error("Failed to load config from URL", e);
                return Optional.of("Failed to read the attachment");
            }
        }
        catch(URISyntaxException | MalformedURLException e)
        {
            return Optional.of("Invalid attachment URL");
        }
    }

    public static Path path(Guild guild)
    {
        get(guild); // Ensure generated
        return Paths.get("configs", "%s.yaml".formatted(guild.getIdLong()));
    }

    // TODO validation
    private static List<String> validate(GuildConfig config)
    {
        return Collections.emptyList();
    }
}
