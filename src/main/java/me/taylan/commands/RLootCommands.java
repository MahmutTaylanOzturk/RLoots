package me.taylan.commands;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.*;
import dev.triumphteam.cmd.core.message.MessageKey;
import dev.triumphteam.cmd.core.suggestion.SuggestionKey;
import me.taylan.RLoots;
import me.taylan.utils.FileManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

@Command(value = "RLoots", alias = {"rlootbox", "rganimet", "Remiellootbox"})
public class RLootCommands extends BaseCommand {


    private final RLoots plugin;
    private final FileManager fileManager;

    public RLootCommands(RLoots plugin) {
        this.plugin = plugin;
        this.fileManager = plugin.getFileManager();
        this.plugin.getManager().registerMessage(MessageKey.INVALID_ARGUMENT, (sender, context) -> {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[<gold>RLoots<gray>] Invalid argument."));
        });
        this.plugin.getManager().registerMessage(MessageKey.UNKNOWN_COMMAND, (sender, context) -> {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[<gold>RLoots<gray>] Unknown command."));
        });
        this.plugin.getManager().registerMessage(MessageKey.NOT_ENOUGH_ARGUMENTS, (sender, context) -> {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[<gold>RLoots<gray>] Not enough arguments."));
        });
        this.plugin.getManager().registerMessage(MessageKey.TOO_MANY_ARGUMENTS, (sender, context) -> {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[<gold>RLoots<gray>] Too many arguments."));
        });
        this.plugin.getManager().registerSuggestion(SuggestionKey.of("items"), (sender, context) -> {
            FileConfiguration configurationItem = YamlConfiguration.loadConfiguration(fileManager.getItemFile());
            return configurationItem.getConfigurationSection("Items").getKeys(false).stream().toList(); // suggest dates in a different format
        });
        this.plugin.getManager().registerCommand(this);
    }

    @Default
    public void executor(CommandSender sender) {
        if (sender instanceof Player player) {
            if (player.isOp()) {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[<gold>RLoots<gray>] * <aqua>Command Usages:"));
                player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>- <yellow>/RLoots items <gray>Returns the item list."));
                player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>- <yellow>/RLoots reload <gray>Reloads the plugin."));
                player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>- <yellow>/RLoots additem <itemname> <gray>Adds item to item file."));
                player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>- <yellow>/RLoots getitem <itemname> <amount> <gray>Gets item from item file."));
                player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>- <yellow>/RLoots createlootbox <lootboxid> <baseitemid> <item,item2...>"));
                player.sendMessage(MiniMessage.miniMessage().deserialize(" <gray>Create lootbox from strach."));
                player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>- <yellow>/RLoots setmobloot <item,item2...> <mobname>"));
                player.sendMessage(MiniMessage.miniMessage().deserialize(" <gray>Set the loot that will drop from the mob."));
            }
        }
    }

    @SubCommand(value = "reload", alias = {"r"})
    public void executor1(CommandSender sender) {
        if (sender instanceof Player player) {
            if (player.isOp()) {
                fileManager.reloadConfigs();
                player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[<gold>RLoots<gray>] <aqua>Configs Reloaded!"));
            }
        }
    }

    @SubCommand(value = "itemlist", alias = {"items"})
    public void executor3(CommandSender sender) {
        if (sender instanceof Player p) {
            if (p.isOp()) {
                ChestGui gui = new ChestGui(6, "Items", plugin);
                StaticPane pane = new StaticPane(0, 0, 9, 6);
                gui.setOnGlobalClick(event -> event.setCancelled(true));
                FileConfiguration configurationItem = YamlConfiguration.loadConfiguration(fileManager.getItemFile());
                configurationItem.getConfigurationSection("Items").getKeys(false).stream().forEach(s -> {
                    ItemStack item = configurationItem.getItemStack("Items." + s);
                    GuiItem guiItem = new GuiItem(item);
                    pane.addItem(guiItem, Slot.fromIndex(configurationItem.getConfigurationSection("Items").getKeys(false).stream().toList().indexOf(s)));
                });
                gui.addPane(pane);
                gui.show(p);
            }
        }
    }

    @SubCommand(value = "createlootbox")
    public void executor7(CommandSender sender, String lootboxid, @Suggestion("items") String baseitemid, @Suggestion("items") @Split(",") List<String> items) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.isOp()) {
                FileConfiguration langFile = YamlConfiguration.loadConfiguration(fileManager.getLangFile());
                FileConfiguration configurationItem = YamlConfiguration.loadConfiguration(fileManager.getItemFile());
                if (configurationItem.getConfigurationSection("Items").getKeys(false).contains(baseitemid)) {
                    FileConfiguration lootConfiguration = YamlConfiguration.loadConfiguration(fileManager.getLootBoxFile());
                    lootConfiguration.set("lootboxs." + lootboxid + ".id", lootboxid);
                    lootConfiguration.set("lootboxs." + lootboxid + ".animation", "RANDOM");
                    lootConfiguration.set("lootboxs." + lootboxid + ".baseitem", baseitemid);
                    ItemStack baseitem = configurationItem.getItemStack("Items." + baseitemid);
                    NamespacedKey namespacedKey = new NamespacedKey(plugin, "lootbox");
                    ItemMeta itemMeta = baseitem.getItemMeta();
                    itemMeta.getPersistentDataContainer().set(namespacedKey,
                            PersistentDataType.STRING, lootboxid);
                    baseitem.setItemMeta(itemMeta);
                    configurationItem.set("Items." + baseitemid, baseitem);
                    fileManager.save(fileManager.getItemFile(), configurationItem);
                    items.forEach(s -> {
                        if (configurationItem.getConfigurationSection("Items").getKeys(false).contains(s)) {
                            lootConfiguration.set("lootboxs." + lootboxid + ".loots." + items.indexOf(s) + ".rarity", "<gray><bold>COMMON LOOT!");
                            lootConfiguration.set("lootboxs." + lootboxid + ".loots." + items.indexOf(s) + ".item", s);
                            lootConfiguration.set("lootboxs." + lootboxid + ".loots." + items.indexOf(s) + ".amount", 1);
                            lootConfiguration.set("lootboxs." + lootboxid + ".loots." + items.indexOf(s) + ".luck", 1);
                        } else {
                            p.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[<gold>RLoots<gray>] <red>No item named <gray>" + s + "<red> was found!"));
                        }
                    });

                    fileManager.save(fileManager.getLootBoxFile(), lootConfiguration);
                    p.sendMessage(MiniMessage.miniMessage().deserialize(langFile.getString("lootbox-created-successfully")));

                } else {
                    p.sendMessage(MiniMessage.miniMessage().deserialize(langFile.getString("lootbox-base-item-not-found")));
                }
            }
        }
    }

    @SubCommand(value = "setmobloot")
    public void executor12(CommandSender sender, @Suggestion("items") @Split(",") List<String> items, @Join(" ") String mobname) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.isOp()) {
                FileConfiguration langFile = YamlConfiguration.loadConfiguration(fileManager.getLangFile());
                FileConfiguration configurationItem = YamlConfiguration.loadConfiguration(fileManager.getItemFile());
                FileConfiguration lootConfiguration = YamlConfiguration.loadConfiguration(fileManager.getMobDropFile());
                items.forEach(s -> {
                    if (configurationItem.getConfigurationSection("Items").getKeys(false).contains(s)) {
                        lootConfiguration.set("Mobs." + mobname + ".loots." + items.indexOf(s) + ".rarity", "<gray><bold>COMMON LOOT!");
                        lootConfiguration.set("Mobs." + mobname + ".loots." + items.indexOf(s) + ".item", s);
                        lootConfiguration.set("Mobs." + mobname + ".loots." + items.indexOf(s) + ".amount", 1);
                        lootConfiguration.set("Mobs." + mobname + ".loots." + items.indexOf(s) + ".luck", 1.0);
                    } else {
                        p.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[<gold>RLoots<gray>] <red>No item named <gray>" + s + "<red> was found!"));
                    }
                });

                fileManager.save(fileManager.getMobDropFile(), lootConfiguration);
                p.sendMessage(MiniMessage.miniMessage().deserialize(langFile.getString("mobloot-created-successfully")));


            }
        }
    }

    @SubCommand(value = "additem", alias = {"import"})
    public void executor5(CommandSender sender, String item) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.isOp()) {
                FileConfiguration langFile = YamlConfiguration.loadConfiguration(fileManager.getLangFile());
                if (p.getInventory().getItemInMainHand() != null) {
                    ItemStack itemHand = p.getInventory().getItemInMainHand();
                    FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(fileManager.getItemFile());
                    fileConfiguration.set("Items." + item, itemHand);
                    fileManager.save(fileManager.getItemFile(), fileConfiguration);
                    p.sendMessage(MiniMessage.miniMessage().deserialize(langFile.getString("item-added-successfully")));
                } else {
                    p.sendMessage(MiniMessage.miniMessage().deserialize(langFile.getString("not-holding-item")));
                }
            }
        }
    }

    @SubCommand(value = "getitem", alias = {"itemal"})
    public void executor2(CommandSender sender, @Suggestion("items") String item, int amount) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.isOp()) {
                FileConfiguration configurationItem = YamlConfiguration.loadConfiguration(fileManager.getItemFile());
                if (configurationItem.getConfigurationSection("Items").getKeys(false).contains(item)) {
                    ItemStack giveitem = configurationItem.getItemStack("Items." + item);
                    assert giveitem != null;
                    giveitem.setAmount(amount);
                    p.getInventory().addItem(giveitem);
                } else {
                    p.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[<gold>RLoots<gray>] <red>No item named <gray>" + item + "<red> was found!"));
                }
            }
        }
    }
}