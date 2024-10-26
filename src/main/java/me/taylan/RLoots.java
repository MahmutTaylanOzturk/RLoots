package me.taylan;

import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import lombok.Getter;
import me.taylan.commands.RLootCommands;
import me.taylan.listeners.LootListener;
import me.taylan.listeners.MobDeathListener;
import me.taylan.metrics.Metrics;
import me.taylan.utils.FileManager;
import me.taylan.utils.ItemHandler;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;


public final class RLoots extends JavaPlugin {
    @Getter
    public ItemHandler itemHandler;

    @Getter
    public FileManager fileManager;
    @Getter
    public BukkitCommandManager<CommandSender> manager;
    @Getter
    public HashMap<World, ArmorStand> armorStandHashMap = new HashMap<>();
    @Override
    public void onEnable() {
        manager = BukkitCommandManager.create(this);
        getConfig().options().copyDefaults(true);
        saveConfig();
        fileManager = new FileManager(this);
        fileManager.createLangFile();
        fileManager.createItemFile();
        fileManager.createMobDropFile();
        fileManager.createlootboxFile();
        itemHandler = new ItemHandler(this);
        itemHandler.init();
        new LootListener(this);
        new MobDeathListener(this);
        new RLootCommands(this);
        send("<gray>-----------------------------");
        send("<green>Lang file loaded!");
        send("<green>Item file loaded!");
        send("<green>MobDrop file loaded!");
        send("<green>LootBox file loaded!");
        send("<green>RLoots Enabled!");
        send("<gray>-----------------------------");
        int pluginId = 23417;
        new Metrics(this, pluginId);
    }

    @Override
    public void onDisable() {

        for (ArmorStand armorStand : armorStandHashMap.values()) {
            armorStand.remove();
        }
        armorStandHashMap.clear();
        send("<gray>-----------------------------");
        send("<yellow>RLoots Disabled!");
        send("<gray>-----------------------------");
    }

    public int parseAmount(String amountConfig) {
        String[] parts = amountConfig.trim().split("-");

        ThreadLocalRandom random = ThreadLocalRandom.current();

        if (parts.length == 1) {
            return Integer.parseInt(parts[0].trim());
        } else if (parts.length == 2) {
            int min = Integer.parseInt(parts[0].trim());
            int max = Integer.parseInt(parts[1].trim());
            return random.nextInt(max - min + 1) + min;
        }

        return 1;
    }
    public void send(String s) {
        getServer().getConsoleSender().sendMessage(MiniMessage.miniMessage().deserialize(s));
    }
}
