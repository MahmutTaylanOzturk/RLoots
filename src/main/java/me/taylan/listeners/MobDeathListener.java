package me.taylan.listeners;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import me.taylan.RLoots;
import me.taylan.utils.FileManager;
import me.taylan.utils.ItemHandler;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class MobDeathListener implements Listener {

    private final RLoots plugin;
    private final ItemHandler itemHandler;
    private final FileManager fileManager;

    public MobDeathListener(RLoots plugin) {
        this.plugin = plugin;
        this.fileManager = plugin.getFileManager();
        this.itemHandler = plugin.getItemHandler();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void mobDeathListener(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        Entity entitykiller = event.getEntity().getKiller();
        if (!(entity instanceof LivingEntity && entitykiller instanceof Player)) return;
        Player player = (Player) entitykiller;
        String name = entity.getName();
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(fileManager.getMobDropFile());
        FileConfiguration itemConfiguration = YamlConfiguration.loadConfiguration(fileManager.getItemFile());
        for (String mobName : configuration.getConfigurationSection("Mobs").getKeys(false)) {
            if (name.contains(mobName)) {
                for (String lootID : configuration.getConfigurationSection("Mobs." + mobName + ".loots").getKeys(false)) {
                    String item = configuration.getString("Mobs." + mobName + ".loots." + lootID + ".item");
                    double lootSharePlayerChance = configuration.getDouble("Mobs." + mobName + ".loots." + lootID + ".luck");
                    String itemRarity = configuration.getString("Mobs." + mobName + ".loots." + lootID + ".rarity");
                    double random = ThreadLocalRandom.current().nextDouble();
                    if (random < lootSharePlayerChance) {
                        int amount = plugin.parseAmount(configuration.getString("Mobs." + mobName + ".loots." + lootID + ".amount"));
                        ItemStack item2 = itemConfiguration.getItemStack("Items." + item);
                        item2.setAmount(amount);
                        if (item2.hasItemMeta() && item2.getItemMeta().hasDisplayName()) {
                            player.sendMessage(MiniMessage.miniMessage().deserialize(itemRarity + " <white><bold:false>" + amount + "x ").append(Objects.requireNonNull(item2.getItemMeta().displayName())));
                        } else {
                            player.sendMessage(MiniMessage.miniMessage().deserialize(itemRarity + " <white><bold:false>" + amount + "x " + item2.getType()));
                        }
                        if (player.getInventory().firstEmpty() == -1) {
                            Item dropItem = entity.getWorld().dropItemNaturally(entity.getLocation(), item2);
                            if (item2.hasItemMeta() && item2.getItemMeta().hasDisplayName()) {
                                dropItem.customName(MiniMessage.miniMessage().deserialize("<white><i:false>" + item2.getAmount() + "x ").append(item2.getItemMeta().displayName().append(MiniMessage.miniMessage().deserialize(" <gold>[<yellow>" + player.getName() + "<gold>]"))));
                                dropItem.setCustomNameVisible(true);
                            }
                            dropItem.setOwner(player.getUniqueId());
                        } else {
                            player.getInventory().addItem(item2);
                        }

                    }
                }
            }
        }


    }
}



