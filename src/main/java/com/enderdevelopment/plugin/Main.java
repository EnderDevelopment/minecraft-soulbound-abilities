package com.enderdevelopment.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {
    private ScoreboardManager scoreboardManager;
    private Map<UUID, Integer> staminaMap = new HashMap<>();
    private Map<UUID, Integer> killsMap = new HashMap<>();
    private Map<UUID, Boolean> ability1Unlocked = new HashMap<>();
    private Map<UUID, Boolean> ability2Unlocked = new HashMap<>();
    private Map<UUID, Boolean> ability3Unlocked = new HashMap<>();
    private Map<UUID, Boolean> ability4Unlocked = new HashMap<>();

    @Override
    public void onEnable() {
        scoreboardManager = new ScoreboardManager(this);
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("StaminaPlugin has been enabled!");
    }

    private Map<UUID, Integer> staminaMap = new HashMap<>();
    private Map<UUID, Integer> killsMap = new HashMap<>();
    private Map<UUID, Boolean> ability1Unlocked = new HashMap<>();
    private Map<UUID, Boolean> ability2Unlocked = new HashMap<>();
    private Map<UUID, Boolean> ability3Unlocked = new HashMap<>();
    private Map<UUID, Boolean> ability4Unlocked = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("StaminaPlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("StaminaPlugin has been disabled!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (!staminaMap.containsKey(playerId)) {
            staminaMap.put(playerId, 5);
            killsMap.put(playerId, 0);
            ability1Unlocked.put(playerId, false);
            ability2Unlocked.put(playerId, false);
            ability3Unlocked.put(playerId, false);
            ability4Unlocked.put(playerId, false);
        }
        scoreboardManager.createScoreboard(player);
        scoreboardManager.updateScoreboard(player);
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (!staminaMap.containsKey(playerId)) {
            staminaMap.put(playerId, 5);
            killsMap.put(playerId, 0);
            ability1Unlocked.put(playerId, false);
            ability2Unlocked.put(playerId, false);
            ability3Unlocked.put(playerId, false);
            ability4Unlocked.put(playerId, false);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        scoreboardManager.removeScoreboard(player);
        staminaMap.remove(playerId);
        killsMap.remove(playerId);
        ability1Unlocked.remove(playerId);
        ability2Unlocked.remove(playerId);
        ability3Unlocked.remove(playerId);
        ability4Unlocked.remove(playerId);
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        staminaMap.remove(playerId);
        killsMap.remove(playerId);
        ability1Unlocked.remove(playerId);
        ability2Unlocked.remove(playerId);
        ability3Unlocked.remove(playerId);
        ability4Unlocked.remove(playerId);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID playerId = player.getUniqueId();
        staminaMap.put(playerId, 5);
        killsMap.put(playerId, 0);
        ability1Unlocked.put(playerId, false);
        ability2Unlocked.put(playerId, false);
        ability3Unlocked.put(playerId, false);
        ability4Unlocked.put(playerId, false);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player) {
            Player player = (Player) event.getEntity().getKiller();
            UUID playerId = player.getUniqueId();
            int kills = killsMap.getOrDefault(playerId, 0) + 1;
            killsMap.put(playerId, kills);
            scoreboardManager.updateScoreboard(player);
            int stamina = staminaMap.getOrDefault(playerId, 5) + 1;
            staminaMap.put(playerId, stamina);
            scoreboardManager.updateScoreboard(player);
            checkAbilities(player);
        }
    }

    public int getStamina(UUID playerId) {
        return staminaMap.getOrDefault(playerId, 5);
    }

    public int getKills(UUID playerId) {
        return killsMap.getOrDefault(playerId, 0);
    }

    private void checkAbilities(Player player) {
        UUID playerId = player.getUniqueId();
        int stamina = staminaMap.getOrDefault(playerId, 5);
        int kills = killsMap.getOrDefault(playerId, 0);

        if (stamina >= 15 && !ability1Unlocked.getOrDefault(playerId, false)) {
            ability1Unlocked.put(playerId, true);
            player.sendMessage("You have unlocked Feel the Power!");
        }

        if (kills >= 50 && !ability2Unlocked.getOrDefault(playerId, false)) {
            ability2Unlocked.put(playerId, true);
            player.sendMessage("You have unlocked Supersonic!");
        }

        if (stamina >= 30 && !ability3Unlocked.getOrDefault(playerId, false)) {
            ability3Unlocked.put(playerId, true);
            player.sendMessage("You have unlocked Hyper Generate!");
        }

        if (kills >= 100 && !ability4Unlocked.getOrDefault(playerId, false)) {
            ability4Unlocked.put(playerId, true);
            player.sendMessage("You have unlocked Ultimate Power!");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        UUID playerId = player.getUniqueId();

        if (command.getName().equalsIgnoreCase("stamina")) {
            int stamina = staminaMap.getOrDefault(playerId, 5);
            player.sendMessage("Your current stamina: " + stamina);
            return true;
        }

        if (command.getName().equalsIgnoreCase("abilities")) {
            player.sendMessage("Available abilities:");
            if (ability1Unlocked.getOrDefault(playerId, false)) {
                player.sendMessage("1. Feel the Power");
            }
            if (ability2Unlocked.getOrDefault(playerId, false)) {
                player.sendMessage("2. Supersonic");
            }
            if (ability3Unlocked.getOrDefault(playerId, false)) {
                player.sendMessage("3. Hyper Generate");
            }
            if (ability4Unlocked.getOrDefault(playerId, false)) {
                player.sendMessage("4. Ultimate Power");
            }
            return true;
        }

        return false;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.BONE && ability1Unlocked.getOrDefault(playerId, false)) {
            // Feel the Power ability
            player.sendMessage("Using Feel the Power!");
            // Add logic to create hearts or add damage to attacks
        } else if (item.getType() == Material.FEATHER && ability2Unlocked.getOrDefault(playerId, false)) {
            // Supersonic ability
            player.sendMessage("Using Supersonic!");
            player.setAllowFlight(true);
            player.setFlying(true);
            // Add logic to smash down after some time
        } else if (item.getType() == Material.BLAZE_ROD && ability3Unlocked.getOrDefault(playerId, false)) {
            // Hyper Generate ability
            player.sendMessage("Using Hyper Generate!");
            Fireball fireball = player.launchProjectile(Fireball.class);
            fireball.setYield(3.0f);
            fireball.setIsIncendiary(true);
            // Add logic to create a huge flame ball with light
        } else if (item.getType() == Material.NETHER_STAR && ability4Unlocked.getOrDefault(playerId, false)) {
            // Ultimate Power ability
            player.sendMessage("Using Ultimate Power!");
            // Add logic to use all abilities at once
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            UUID playerId = player.getUniqueId();
            ItemStack item = player.getInventory().getItemInMainHand();

            if (item.getType() == Material.BONE && ability1Unlocked.getOrDefault(playerId, false)) {
                // Add 5 damage to attacks with Feel the Power
                event.setDamage(event.getDamage() + 5);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (ability2Unlocked.getOrDefault(playerId, false) && player.isFlying()) {
            // Add logic to smash down after some time
            // For example, after 5 seconds of flying
            // You can use a scheduler to check the time and then smash down
        }
    }
}