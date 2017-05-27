package red.man10.man10mysqlapi;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Man10MysqlAPI extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public String prefix = "§e§l[MySQLAPI]§f";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("mysqlapi")){
            if(sender instanceof Player == false){
                sender.sendMessage(prefix + "このコマンドはプレイヤーからのみしか実行できません");
                return false;
            }
            Player p = (Player) sender;
            p.sendMessage("§e==========[MySQLAPI]==========");
            p.sendMessage("§eCreated By §ctakatronix");
            p.sendMessage("§bTwitter https://twitter.com/takatronix");
            p.sendMessage("");
            p.sendMessage("§eAPI Made by §cSho0");
            p.sendMessage("§bTwitter https://twitter.com/Sho0_dev");
            p.sendMessage("§dVersion V1.2");
        }
        return true;
    }


}
