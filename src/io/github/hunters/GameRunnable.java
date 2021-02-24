package io.github.hunters;

import net.minecraft.server.v1_12_R1.ChatMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class GameRunnable extends BukkitRunnable {

    public Hunters plugin;
    public GameRunnable(JavaPlugin plugin){
        if (!(plugin instanceof Hunters)){
            throw new RuntimeException("Plugin Error: ONLY HUNTERS(EXTENDS JAVAPLUGIN) CAN USE THIS RUNNABLE!");
        }
        this.plugin = (Hunters)plugin;
    }

    @Override
    public void run() {
        if (this.plugin.gameStatus == GameStatus.STOPPED){
            return;
        }
        else if (this.plugin.gameStatus == GameStatus.RUNNING){
            this.plugin.runtime -= 1;
            for (Player p: this.plugin.hunter) {
                if (!p.isOnline()) this.plugin.hunter.remove(p);
                if (p.isDead()) this.plugin.hunter.remove(p);
            }
            for (Player p: this.plugin.runner){
                if (!p.isOnline()) this.plugin.hunter.remove(p);
                if (p.isDead()) this.plugin.hunter.remove(p);
            }
            if (this.plugin.runner.isEmpty()) {
                for (Player p: this.plugin.hunter) {
                    p.sendMessage(ChatColor.GOLD+"Hunters win!");
                }
                for (Player p: this.plugin.runner){
                    p.sendMessage(ChatColor.GOLD+"Hunters win!");
                }
                this.plugin.stopGame();
            }
            else if (this.plugin.hunter.isEmpty()) {
                for (Player p: this.plugin.hunter) {
                    p.sendMessage(ChatColor.GOLD+"Runners win!");
                }
                for (Player p: this.plugin.runner){
                    p.sendMessage(ChatColor.GOLD+"Runners win!");
                }
                this.plugin.stopGame();
            }
        }
        else if (this.plugin.gameStatus == GameStatus.WAITING){
            if (this.plugin.runner.size() == 2 && this.plugin.hunter.size() == 2){
                this.plugin.startGame();
            }
        }
    }
}
