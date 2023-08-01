package org.radium.guildsspigot.events;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.radium.guildsspigot.Core;
import org.radium.guildsspigot.manager.object.User;

public class FriendlyFireListener implements Listener {
    YamlConfiguration config = Core.getInstance().getSettingsConfiguration().getConfig();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (config.getBoolean("FriendlyFire.Enabled")) return;
        if (!(event.getEntity() instanceof Player)) return;
        Entity damager = event.getDamager();
        Player victim = (Player) event.getEntity();
        User userVictim = Core.getInstance().getUser(victim.getName());
        if (damager instanceof Arrow){
            if (config.getBoolean("FriendlyFire.AllowBow")) return;
            Arrow arrow = (Arrow) event.getDamager();
            Player shooter = (Player) arrow.getShooter();
            User userShooter = Core.getInstance().getUser(shooter.getName());
            if (userShooter.getGuildName() == null || userVictim.getGuildName() == null
            || userShooter.getGuildName().isEmpty() || userVictim.getGuildName().isEmpty()) return;
            if (userShooter.getGuildName().equals(userVictim.getGuildName())) {
                event.setCancelled(true);
            }
        }
        if (damager instanceof FishHook){
            if (config.getBoolean("FriendlyFire.AllowFishingRod")) return;
            FishHook hook = (FishHook) event.getDamager();
            Player shooter = (Player) hook.getShooter();
            User userShooter = Core.getInstance().getUser(shooter.getName());
            if (userShooter.getGuildName() == null || userVictim.getGuildName() == null
                    || userShooter.getGuildName().isEmpty() || userVictim.getGuildName().isEmpty()) return;
            if (userShooter.getGuildName().equals(userVictim.getGuildName())) {
                event.setCancelled(true);
            }
        }
        if (!(damager instanceof Player)) return;
        Player playerDamager = (Player) damager;
        User userDamager = Core.getInstance().getUser(playerDamager.getName());

        if (userDamager.getGuildName() == null || userVictim.getGuildName() == null
                || userDamager.getGuildName().isEmpty() || userVictim.getGuildName().isEmpty()) return;

        if (userDamager.getGuildName().equals(userVictim.getGuildName())) {
            event.setCancelled(true);
        }
    }

}
