package com.enderdevelopment.plugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {
    private final Main plugin;
    private final Map<UUID, Scoreboard> scoreboards = new HashMap<>();

    public ScoreboardManager(Main plugin) {
        this.plugin = plugin;
    }

    public void createScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("stamina", "dummy", "Stamina");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        UUID playerId = player.getUniqueId();
        scoreboards.put(playerId, scoreboard);
        player.setScoreboard(scoreboard);
    }

    public void updateScoreboard(Player player) {
        UUID playerId = player.getUniqueId();
        Scoreboard scoreboard = scoreboards.get(playerId);
        if (scoreboard != null) {
            Objective objective = scoreboard.getObjective("stamina");
            if (objective != null) {
                int stamina = plugin.getStamina(playerId);
                int kills = plugin.getKills(playerId);
                objective.getScore("Stamina: ").setScore(stamina);
                objective.getScore("Kills: ").setScore(kills);
            }
        }
    }

    public void removeScoreboard(Player player) {
        UUID playerId = player.getUniqueId();
        scoreboards.remove(playerId);
    }
}