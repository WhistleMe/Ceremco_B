package admin;

import helperCommands.SendMessage;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ContextException;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.ErrorResponse;
import variables.Variables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class AdminLists
{
    //method to add the very first Admin to the list, used on initialization only
    public static void addFirstAdmin(String userId)
    {
        Variables.adminList.add(User.fromId(userId));
    }

    //method for initialization only, add the keys to the blacklistbosses hashmap
    public static void addKeysBosses(String[] bosses)
    {
        for (String boss : bosses) {
            Variables.blackListBosses.put(boss, new HashMap<>());
        }
    }

    //used on initialization only
    public static void addkeysRoles(String boss, String[] roles)
    {
        for (String role : roles) {
            Variables.blackListBosses.get(boss).put(role, new ArrayList<>());
        }
    }

    //method to add a user as admin
    public static void addAdminList(GuildMessageReceivedEvent event, String userId)
    {
        if (!Variables.adminList.contains(User.fromId(userId))) {
            Variables.adminList.add(User.fromId(userId));
            SendMessage.sendChannel(event, "User: " + User.fromId(userId)
                    + " added as admin.");
        } else {
            SendMessage.sendChannel(event, "the User: " + User.fromId(userId)
                    + " is already an admin.");
        }
    }

    //method to delete a user as admin
    public static void delAdminList(GuildMessageReceivedEvent event, String userId)
    {
        if (Variables.adminList.contains(User.fromId(userId))) {
            Variables.adminList.remove(User.fromId(userId));
            SendMessage.sendChannel(event, "User: " + User.fromId(userId)
                    + " deleted as admin.");
        } else {
            SendMessage.sendChannel(event, "The user: " + User.fromId(userId)
                    + " is not an admin.");
        }
    }

    //method to add a user to the blacklist of a boss with a given role
    public static void addBlackListBosses(GuildMessageReceivedEvent event, String boss, String role, String userId)
    {
        if (!Variables.blackListBosses.get(boss).get(role).contains(User.fromId(userId))) {
            Variables.blackListBosses.get(boss).get(role).add(User.fromId(userId));
            SendMessage.sendChannel(event, "user: " + User.fromId(userId)
                    + " added to the blacklist of boss: " + boss + " with role: " + role);
        } else {
            SendMessage.sendChannel(event, "The user: " + User.fromId(userId)
                    + " is already part of the blacklist for the given role.");
        }
    }

    //method to delete a user of the blacklist of a boss with a given role
    public static void delBlackListBosses(GuildMessageReceivedEvent event, String boss, String role, String userId)
    {
        if (Variables.blackListBosses.get(boss).get(role).contains(User.fromId(userId))) {
            Variables.blackListBosses.get(boss).get(role).remove(User.fromId(userId));
            SendMessage.sendChannel(event, "user: " + User.fromId(userId)
                    + " deleted from the blacklist of boss: " + boss + " with role: " + role);
        } else {
            SendMessage.sendChannel(event, "The user: " + User.fromId(userId)
                    + " isn't on the blacklist for the given role");
        }
    }

    //method to check if the given user is an admin
    public static boolean isAdmin(String userId)
    {
        return Variables.adminList.contains(User.fromId(userId));
    }

    //method to check if the given user is blacklisted for the given boss/role.
    public static boolean isBlacklisted(String boss, String role, String userId)
    {
        return Variables.blackListBosses.get(boss).get(role).contains(User.fromId(userId));
    }

    //method to turn on/off the ability to chat in pvm sign up
    public static void switchPvmMessage(GuildMessageReceivedEvent event){
        Variables.togglePvmMessages = !Variables.togglePvmMessages;
        SendMessage.sendChannel(event, "can chat:" + !Variables.togglePvmMessages);
    }

    //method to print the lists in the discord as an Embed
    public static void printLists(GuildMessageReceivedEvent e)
    {
        //output the names of everyone in the adminList as String.
        StringBuilder output = new StringBuilder("Admins: ");
        for (User user : Variables.adminList.stream().toList()) {
            output.append(user).append(", ");
        }
        SendMessage.sendChannel(e, output.toString());
        //get each boss as key in the blacklistBosses hashmap
        Set<String> bosses = Variables.blackListBosses.keySet();
        for (String boss : bosses) {//for each key
            StringBuilder outputBoss = new StringBuilder(boss + ":");
            Set<String> roles = Variables.blackListBosses.get(boss).keySet();//get all the roles for the given boss
            for (String role : roles) {//for each role
                outputBoss.append("\n").append(role).append(": ");
                for (User user : Variables.blackListBosses.get(boss).get(role)) {
                    //get all the users for the given keys and add em to the stringbuilder
                    outputBoss.append(user).append(", ");
                }
            }
            SendMessage.sendChannel(e, outputBoss.toString());
        }
    }
}