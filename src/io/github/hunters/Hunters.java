package io.github.hunters;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.Random;

public class Hunters extends JavaPlugin implements Listener {

    public GameStatus gameStatus;
    public ArrayList<Player> hunter;
    public ArrayList<Player> runner;
    public int runtime = -1;
    private Random random;
    GameRunnable game;

    @Override
    public void onLoad() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        this.random = new Random();
        this.Initialize();
        game.runTask(this);
    }

    public void startGame(){
        this.gameStatus = GameStatus.RUNNING;
        this.runtime = 18000;
        double huntx = 256.50+this.random.nextInt(32767);
        double huntz = 256.50+this.random.nextInt(32767);
        double runx = huntx + 128 + this.random.nextInt(64);
        double runz = huntz + 128 + this.random.nextInt(64);
        for (Player p: this.hunter){
            p.teleport(new Location(p.getWorld(), huntx, 256.50, huntz));
        }
        for (Player p: this.runner){
            p.teleport(new Location(p.getWorld(), runx, 256.50, runz));
        }
    }

    @EventHandler
    public void onDamageTaken(EntityDamageEvent event){
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL && this.runtime <= 6000) event.setCancelled(true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("hunters") && args.length >= 1){
            if (args[0] == "start"){
                this.gameStatus = GameStatus.WAITING;
                sender.sendMessage("Game started, now players are able to join!");
            }
            else if (args[0] == "join"){
                if (!(sender instanceof Player)){
                    sender.sendMessage("Only players can invoke!");
                    return true;
                }
                if (args.length != 2) {
                    sender.sendMessage("Arguments not matched!");
                    return true;
                }
                if (args[1].equals("hunters")){
                    if (this.hunter.size() < 2){
                        this.hunter.add((Player)sender);
                        sender.sendMessage("Successfully joined team!");
                        return true;
                    }
                }
                else if (args[1].equals("runners")){
                    if (this.hunter.size() < 2){
                        this.hunter.add((Player)sender);
                        sender.sendMessage("Successfully joined team!");
                        return true;
                    }
                }
                else{
                    sender.sendMessage("Arguments not resolved!");
                    return true;
                }
                sender.sendMessage("Team is full!");
            }
            else{
                sender.sendMessage("Command not resolved!");
            }
            return true;
        }
        return false;
    }
    public void Initialize(){
        this.gameStatus = GameStatus.STOPPED;
        this.runtime = -1;
        this.hunter.clear();
        this.runner.clear();
    }

    public void stopGame(){
        this.Initialize();
    }
}
