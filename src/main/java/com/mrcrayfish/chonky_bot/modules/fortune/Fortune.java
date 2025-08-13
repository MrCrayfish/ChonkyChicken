package com.mrcrayfish.chonky_bot.modules.fortune;

import com.mrcrayfish.chonky_bot.GuildConfig;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

import java.util.Random;

public final class Fortune
{
    private static final String[] RESPONSES = {
            "Bawk yes!",
            "Peckin' right it is",
            "Mmhmm *cluck*",
            "Yup, now toss me some seed",
            "That's a yes, featherbrain",
            "Do I lay eggs? Then yes.",
            "Affirm-a-cluck-tive",
            "Fine, yes, now let me nap",
            "You bet your beak",
            "For sure, unless the sky falls",
            "Even rooster agrees",
            "Yes! Now scram",
            "That's a golden egg of a yes",
            "My feathers say yes",
            "Yes. Now rub my comb",
            "Squawk yeah!",
            "Definitely. Cluckin' definitely",
            "100%... unless it's Tuesday",
            "Yes, but don't make it a habit",
            "Yes, yes, yes - ugh, so needy!",

            "BAWK no!",
            "Absolutely not, egghead",
            "Did you even think before asking?",
            "No. And now I'm cranky.",
            "I'd rather molt",
            "Not in this coop",
            "Noooope *peck peck*",
            "Do I look like I care? No.",
            "That's a solid *nope* from me",
            "If I had a seed for every bad question... still no",
            "No, and that's my final squawk",
            "You wish, two-legger",
            "HA. No.",
            "Not even if you bring seed",
            "Try again after I nap",
            "No way, feathery friend",
            "Not unless pigs fly... wait, wrong barn",
            "Cluck off",
            "Not today, not ever",
            "I reject your question and demand snack payment",
            "No, and don't ask again",
            "Reply hazy, try again",
            "Ask again later",
            "Do you really want to know?",

            "No, and don't look at me like that.",
            "That's a no so strong it laid an egg.",
            "Flap off with that question.",
            "I said no. My beak is sealed.",
            "Nope. Not today. Not tomorrow.",
            "I would laugh, but I'm too annoyed.",
            "No, and I'm filing a report to the moderators.",
            "Negative. Coop security has been alerted.",
            "I'd rather have no seed than say yes to that.",
            "No, and now I'm suspicious of your motives.",
            "That's the worst idea I've heard since electric eggs.",
            "No. Now drop a seed and walk away.",

            "Bawk... maybe?",
            "Hmm... gimme a minute... Zzz",
            "Ask again after my nap",
            "I'm too hungry to decide",
            "Depends... how many seeds you got?",
            "Could go either way like a rooster in a mirror",
            "The coop's divided",
            "Flip a feather",
            "I'm undecided and dramatic",
            "I'd tell you, but I forgot mid-cluck",
            "I dunno, I'm just a chicken",
            "You're asking ME?",
            "I'm molting - ask later",
            "Meh.",
            "Too foggy in the barn",
            "Why you always asking me the hard ones?",
            "50% cluck, 50% chance",
            "The egg's still boiling",
            "Could be, could not be - cluck science",
            "My instincts are scrambled",

            "Ask your mom",
            "I plead the 8th",
            "Try Google instead",
            "Another question?!",
            "You again?!",
            "Stop pecking my nerves",
            "Just throw seed and go away",
            "You woke me up for THIS?",
            "Next time bring seed, and maybe I'll answer",
            "Why are you like this?",
            "I'm one peck away from chaos",
            "No more questions unless it's snack-related",
            "You're stressing my feathers",
            "*aggressive clucking*",
            "You broke my vibe",
            "Who do you think you are?!",
            "I didn't sign up for this",
            "My patience is fried",
            "I'm fluffing my feathers in protest",
            "Stop poking me",
            "I don't work weekends",
            "I'll answer if you scratch my back",
            "Don't care... let me nap or feel my wrath",
            "I don't get enough seed to answer that question",
            "I've heard enough, and now it's time for a nap",
            "Ask your cat",
            "Consult your inner chicken",
            "Consult a toaster",
            "Only if you bring snacks",
            "That's classified",
            "Have you tried not asking?",

            "My beak says yes, my tail says no",
            "Bawkward...",
            "I had a vision during a dust bath, so maybe...",
            "Only if the moon is egg-shaped",
            "I've consulted the sacred seed, it's a no",
            "I clucked three times, and still nothing",
            "I talked to the goat. He says no.",
            "If you do a silly dance, then yes",
            "I'm a chicken, not a fortune teller!",
            "I'd answer, but the worm union said no",
            "I sense seed in your future",
            "I don't know, go ask the duck",
            "\uD83D\uDC14 *explodes in feathers*",

            "Did you bring seed first?",
            "I'm busy dust bathing",
            "Why is your question so weird?",
            "I could answer, but I won't",
            "You ask too many questions",
            "Eggs say no, but bacon says yes",
            "I dreamed of this. It ended badly.",
            "My tail feathers tingled, signs are good",
            "Ask the scarecrow, he's less annoyed",
            "I'm currently unavailable, please leave a message after the tone.",
            "You woke me up for *this* again?!",
            "I'm too fabulous to care right now",
            "Cluck me sideways, that's complicated",
            "Maybe. Maybe not. I lost count",
            "The winds of the coop are unclear",
            "My cousin the duck says yes",
            "Do you smell feathers burning? That's a sign",
            "My answer is: ehhhhhh",
            "Yes, but only if you stop poking me",
            "I pecked some dirt and saw your future",
            "You'll know when the egg cracks",
            "Absolutely... not telling",
            "You look like you need a nap",
            "I'm channeling barn energy... It says \"eh\"",
            "You should ask a goat next time",
            "Try offering me snacks first",
            "My egg sense is tingling",
            "Hmm. Squawk-squawk. Translation: unsure",
            "If yes means no and no means yes... then maybe",
            "I need at least 3 naps before answering that",
            "If I say yes, do I get seed?",
            "That question is unanswerable",
            "One more word and I flap at you",
            "Could you say that again slower? I'm just a bird",
            "Answer not found - chicken.exe crashed",
            "Ask again after molting season",
            "I see feathers... I see destiny... I see snacks",
            "Your question smells like carrots. Suspicious.",
            "I asked the duck. He just quacked.",
            "I was pecking when you spoke. What?",
            "A feather floated left. That's a maybe.",
            "I see an egg... and confusion",
            "I would answer, but I'm dramatic",
            "No thoughts, just fluff",
            "Flap your arms and try again",
            "I'm just a chonky chicken, not a miracle worker",
            "You're not my favorite human right now",
            "I already answered... in my head",
            "You've exhausted my wisdom supply",
            "I forgot what you asked. Nap time!"
    };

    private static final Random RANDOM = new Random();

    @SubscribeEvent
    private static void onMessageReceived(MessageReceivedEvent event)
    {
        if(!event.isFromGuild())
            return;

        GuildConfig config = GuildConfig.get(event.getGuild());
        if(!config.modules().fortune().enabled())
            return;

        if(event.getAuthor().isBot() || event.getAuthor().isSystem())
            return;

        Guild guild = event.getGuild();
        long channel = GuildConfig.get(guild).modules().fortune().channelId();
        if(event.getChannel().getIdLong() == channel)
        {
            event.getMessage().reply(RESPONSES[RANDOM.nextInt(0, RESPONSES.length)]).queue();
        }
    }
}
