package me.taylan.utils;

import lombok.Getter;
import me.taylan.RLoots;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {

    private RLoots plugin;

    public FileManager(RLoots plugin) {
        this.plugin = plugin;

    }

    @Getter
    File itemFile = new File("plugins/RLoots", "Items.yml");
    @Getter
    File langFile = new File("plugins/RLoots", "Lang.yml");
    @Getter
    File mobDropFile = new File("plugins/RLoots", "Mobdrops.yml");

    @Getter
    File lootBoxFile = new File("plugins/RLoots", "Lootboxs.yml");


    public void save(File f, FileConfiguration fc) {
        try {
            fc.save(f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void createItemFile() {
        if (!itemFile.exists()) {
            try {
                itemFile.createNewFile();
                FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(itemFile);
                fileConfiguration.addDefault("Items", "");
                fileConfiguration.options().copyDefaults(true);
                save(itemFile, fileConfiguration);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void reloadConfigs() {
        plugin.reloadConfig();
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(itemFile);
        FileConfiguration fc2 = YamlConfiguration.loadConfiguration(mobDropFile);
        FileConfiguration fc3 = YamlConfiguration.loadConfiguration(lootBoxFile);
        FileConfiguration fc4 = YamlConfiguration.loadConfiguration(langFile);
    }

    public void createMobDropFile() {
        if (!mobDropFile.exists()) {
            try {
                mobDropFile.createNewFile();
                FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(mobDropFile);
                fileConfiguration.set("Mobs.Zindan Bekçisi.loots.0.item", "ironIngot");
                fileConfiguration.set("Mobs.Zindan Bekçisi.loots.0.rarity", "<gray><bold>COMMON LOOT!");
                fileConfiguration.set("Mobs.Zindan Bekçisi.loots.0.luck", 1.0);
                fileConfiguration.set("Mobs.Zindan Bekçisi.loots.0.amount", 1);
                fileConfiguration.set("Mobs.Zindan Bekçisi.loots.1.item", "yellowlootbox");
                fileConfiguration.set("Mobs.Zindan Bekçisi.loots.1.rarity", "<gray><bold>COMMON LOOT!");
                fileConfiguration.set("Mobs.Zindan Bekçisi.loots.1.luck", 0.5);
                fileConfiguration.set("Mobs.Zindan Bekçisi.loots.1.amount", 1);

                fileConfiguration.set("Mobs.Küçük Örümcek.loots.0.item", "bluelootbox");
                fileConfiguration.set("Mobs.Küçük Örümcek.loots.0.rarity", "<gray><bold>COMMON LOOT!");
                fileConfiguration.set("Mobs.Küçük Örümcek.loots.0.luck", 1.0);
                fileConfiguration.set("Mobs.Küçük Örümcek.loots.0.amount", 1);
                fileConfiguration.set("Mobs.Küçük Örümcek.loots.1.item", "yellowlootbox");
                fileConfiguration.set("Mobs.Küçük Örümcek.loots.1.rarity", "<gray><bold>COMMON LOOT!");
                fileConfiguration.set("Mobs.Küçük Örümcek.loots.1.luck", 0.3);
                fileConfiguration.set("Mobs.Küçük Örümcek.loots.1.amount", 1);
                fileConfiguration.set("Mobs.Küçük Örümcek.loots.2.item", "goldBlock");
                fileConfiguration.set("Mobs.Küçük Örümcek.loots.2.rarity", "<gray><bold>COMMON LOOT!");
                fileConfiguration.set("Mobs.Küçük Örümcek.loots.2.luck", 0.1);
                fileConfiguration.set("Mobs.Küçük Örümcek.loots.2.amount", 1);

                save(mobDropFile, fileConfiguration);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void createLangFile() {
        if (!langFile.exists()) {
            try {
                langFile.createNewFile();
                FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(langFile);
                fileConfiguration.set("empty-lootbox", "<red>This loot was empty!");
                fileConfiguration.set("opening-lootbox", "<gold>Loot is opening...");
                fileConfiguration.set("already-opening-lootbox", "<red>You're already opening a lootbox!");

                fileConfiguration.set("not-holding-item", "<gray>[<gold>RLoots<gray>] <red>You must hold a item while adding to config!");
                fileConfiguration.set("item-added-successfully", "<gray>[<gold>RLoots<gray>] <green>Item successfully added to config! You can add this item to lootboxs now.");
                fileConfiguration.set("lootbox-created-successfully", "<gray>[<gold>RLoots<gray>] <green>Lootbox successfully created! You can edit other settings from Lootboxs file.");
                fileConfiguration.set("lootbox-base-item-not-found", "<gray>[<gold>RLoots<gray>] <red>Item id is wrong or missing!");
                fileConfiguration.set("mobloot-created-successfully", "<gray>[<gold>RLoots<gray>] <green>Loots added to mob successfully! You can edit other settings from Mobdrops file.");

                save(langFile, fileConfiguration);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void createlootboxFile() {
        if (!lootBoxFile.exists()) {
            try {
                lootBoxFile.createNewFile();
                FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(lootBoxFile);
                fileConfiguration.set("lootboxs.chest.id", "lootbox");
                fileConfiguration.set("lootboxs.chest.animation", "RANDOM");
                fileConfiguration.set("lootboxs.chest.baseitem", "chest");
                fileConfiguration.set("lootboxs.chest.loots.0.rarity", "<gray><bold>COMMON LOOT!");
                fileConfiguration.set("lootboxs.chest.loots.0.item", "ironIngot");
                fileConfiguration.set("lootboxs.chest.loots.0.amount", 3);
                fileConfiguration.set("lootboxs.chest.loots.0.luck", 1);

                fileConfiguration.set("lootboxs.chest.loots.1.rarity", "<gray><bold>COMMON LOOT!");
                fileConfiguration.set("lootboxs.chest.loots.1.item", "goldIngot");
                fileConfiguration.set("lootboxs.chest.loots.1.amount", 3);
                fileConfiguration.set("lootboxs.chest.loots.1.luck", 1);

                fileConfiguration.set("lootboxs.chest.loots.2.rarity", "<gray><bold>COMMON LOOT!");
                fileConfiguration.set("lootboxs.chest.loots.2.item", "cake");
                fileConfiguration.set("lootboxs.chest.loots.2.amount", 1);
                fileConfiguration.set("lootboxs.chest.loots.2.luck", 1);

                fileConfiguration.set("lootboxs.chest.loots.3.rarity", "");
                fileConfiguration.set("lootboxs.chest.loots.3.item", "EMPTY");
                fileConfiguration.set("lootboxs.chest.loots.3.amount", 1);
                fileConfiguration.set("lootboxs.chest.loots.3.luck", 1);

                fileConfiguration.set("lootboxs.bluegift.id", "bluegift");
                fileConfiguration.set("lootboxs.bluegift.animation", "SPIN");
                fileConfiguration.set("lootboxs.bluegift.baseitem", "blueGift");
                fileConfiguration.set("lootboxs.bluegift.loots.0.rarity", "<blue><bold>RARE LOOT!");
                fileConfiguration.set("lootboxs.bluegift.loots.0.item", "ironBlock");
                fileConfiguration.set("lootboxs.bluegift.loots.0.amount", 14);
                fileConfiguration.set("lootboxs.bluegift.loots.0.luck", 1);

                fileConfiguration.set("lootboxs.bluegift.loots.1.rarity", "<blue><bold>RARE LOOT!");
                fileConfiguration.set("lootboxs.bluegift.loots.1.item", "goldBlock");
                fileConfiguration.set("lootboxs.bluegift.loots.1.amount", 14);
                fileConfiguration.set("lootboxs.bluegift.loots.1.luck", 1);

                fileConfiguration.set("lootboxs.bluegift.loots.2.rarity", "<gray><bold>COMMON LOOT!");
                fileConfiguration.set("lootboxs.bluegift.loots.2.item", "cake");
                fileConfiguration.set("lootboxs.bluegift.loots.2.amount", 1);
                fileConfiguration.set("lootboxs.bluegift.loots.2.luck", 1);

                fileConfiguration.set("lootboxs.yellowgift.id", "yellowgift");
                fileConfiguration.set("lootboxs.yellowgift.animation", "JUMP");
                fileConfiguration.set("lootboxs.yellowgift.baseitem", "yellowGift");
                fileConfiguration.set("lootboxs.yellowgift.loots.0.rarity", "<blue><bold>RARE LOOT!");
                fileConfiguration.set("lootboxs.yellowgift.loots.0.item", "ironBlock");
                fileConfiguration.set("lootboxs.yellowgift.loots.0.amount", 24);
                fileConfiguration.set("lootboxs.yellowgift.loots.0.luck", 1);

                fileConfiguration.set("lootboxs.yellowgift.loots.1.rarity", "<blue><bold>RARE LOOT!");
                fileConfiguration.set("lootboxs.yellowgift.loots.1.item", "goldBlock");
                fileConfiguration.set("lootboxs.yellowgift.loots.1.amount", 26);
                fileConfiguration.set("lootboxs.yellowgift.loots.1.luck", 1);

                fileConfiguration.set("lootboxs.yellowgift.loots.2.rarity", "<blue><bold>RARE LOOT!");
                fileConfiguration.set("lootboxs.yellowgift.loots.2.item", "diamondBlock");
                fileConfiguration.set("lootboxs.yellowgift.loots.2.amount", 23);
                fileConfiguration.set("lootboxs.yellowgift.loots.2.luck", 1);

                save(lootBoxFile, fileConfiguration);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
