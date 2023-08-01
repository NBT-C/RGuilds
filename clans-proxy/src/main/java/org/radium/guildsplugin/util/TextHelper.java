package org.radium.guildsplugin.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.manager.LanguageManager;

public class TextHelper {
    public static String format(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    public static void sendMessage(ProxiedPlayer player, String message) {
        player.sendMessage(new TextComponent(format(message.replace("%prefix%", LanguageManager.getMsg("Prefix")))));
    }
    public static void sendPrefixedMessage(ProxiedPlayer player, String message) {
        player.sendMessage(new TextComponent(format(LanguageManager.getMsg("Prefix")+message)));
    }
    public static void sendPrefixedMessage(CommandSender commandSender, String message) {
        commandSender.sendMessage(new TextComponent(format(LanguageManager.getMsg("Prefix")+message)));
    }
    public static void sendMessage(CommandSender commandSender, String message) {
        commandSender.sendMessage(new TextComponent(format(message.replace("%prefix%", LanguageManager.getMsg("Prefix")))));
    }
    public static String formatClicked(String string){
        return ChatColor.translateAlternateColorCodes('&', LanguageManager.getMsg("Prefix")+string);
    }
}
