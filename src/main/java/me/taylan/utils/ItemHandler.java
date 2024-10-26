package me.taylan.utils;

import me.taylan.RLoots;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemHandler {
    private RLoots plugin;
    private FileManager fileManager;

    public ItemStack yellowlootbox,
            bluelootbox, ironIngot,
            goldIngot, cake, goldBlock, ironBlock, diamondBlock, chest;


    public ItemHandler(RLoots plugin) {
        this.plugin = plugin;
        this.fileManager = plugin.getFileManager();
    }

    public void init() {
        createCake();
        createGoldIngot();
        createironIngot();
        createIronBlock();
        createGoldBlock();
        createDiamondBlock();
        createChest();
        createBluelootbox();
        createYellowlootbox();
    }
    public void createChest() {
        ItemStack item = new ItemStack(Material.CHEST);
        item.setAmount(1);
        ItemMeta meta = item.getItemMeta();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, "lootbox");
        meta.getPersistentDataContainer().set(namespacedKey,
                PersistentDataType.STRING, "chest");
        item.setItemMeta(meta);
        this.chest = item;

        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(fileManager.getItemFile());
        fileConfiguration.set("Items.chest", item);
        fileManager.save(fileManager.getItemFile(), fileConfiguration);

    }

    public void createDiamondBlock() {
        ItemStack item = new ItemStack(Material.DIAMOND_BLOCK);
        item.setAmount(1);
        this.diamondBlock = item;

        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(fileManager.getItemFile());
        fileConfiguration.set("Items.diamondBlock", item);
        fileManager.save(fileManager.getItemFile(), fileConfiguration);

    }

    public void createIronBlock() {
        ItemStack item = new ItemStack(Material.IRON_BLOCK);
        item.setAmount(1);
        this.ironBlock = item;

        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(fileManager.getItemFile());
        fileConfiguration.set("Items.ironBlock", item);
        fileManager.save(fileManager.getItemFile(), fileConfiguration);

    }

    public void createGoldBlock() {
        ItemStack item = new ItemStack(Material.GOLD_BLOCK);
        item.setAmount(1);
        this.goldBlock = item;

        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(fileManager.getItemFile());
        fileConfiguration.set("Items.goldBlock", item);
        fileManager.save(fileManager.getItemFile(), fileConfiguration);

    }

    public void createGoldIngot() {
        ItemStack item = new ItemStack(Material.GOLD_INGOT);
        item.setAmount(1);
        this.goldIngot = item;

        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(fileManager.getItemFile());
        fileConfiguration.set("Items.goldIngot", item);
        fileManager.save(fileManager.getItemFile(), fileConfiguration);

    }

    public void createironIngot() {
        ItemStack item = new ItemStack(Material.IRON_INGOT);
        item.setAmount(1);
        this.ironIngot = item;

        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(fileManager.getItemFile());
        fileConfiguration.set("Items.ironIngot", item);
        fileManager.save(fileManager.getItemFile(), fileConfiguration);

    }

    public void createCake() {
        ItemStack item = new ItemStack(Material.CAKE);
        item.setAmount(1);
        this.cake = item;

        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(fileManager.getItemFile());
        fileConfiguration.set("Items.cake", item);
        fileManager.save(fileManager.getItemFile(), fileConfiguration);

    }



    public void createYellowlootbox() {
        ItemStack item = PlayerHeads.getSkull("https://textures.minecraft.net/texture/abd98792dd92d9719894341ac9012a584c4428558fd2c712f78e5f0d4da85470");
        item.setAmount(1);
        ItemMeta meta = item.getItemMeta();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, "lootbox");
        meta.getPersistentDataContainer().set(namespacedKey,
                PersistentDataType.STRING, "yellowgift");
        meta.displayName(MiniMessage.miniMessage().deserialize("<gradient:yellow:gold><i:false>Sarı Hediye Kutusu"));
        item.setItemMeta(meta);
        this.yellowlootbox = item;

        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(fileManager.getItemFile());
        fileConfiguration.set("Items.yellowlootbox", item);
        fileManager.save(fileManager.getItemFile(), fileConfiguration);

    }

    public void createBluelootbox() {
        ItemStack item = PlayerHeads.getSkull("https://textures.minecraft.net/texture/5b85e29f29ec9a90e482ea5b8391bcb4560bbae0dcd15d7ce1d86016b4356e98");
        item.setAmount(1);
        ItemMeta meta = item.getItemMeta();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, "lootbox");
        meta.getPersistentDataContainer().set(namespacedKey,
                PersistentDataType.STRING, "bluegift");
        meta.displayName(MiniMessage.miniMessage().deserialize("<gradient:blue:aqua><i:false>Mavi Hediye Kutusu"));
        item.setItemMeta(meta);
        this.bluelootbox = item;

        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(fileManager.getItemFile());
        fileConfiguration.set("Items.bluelootbox", item);
        fileManager.save(fileManager.getItemFile(), fileConfiguration);

    }
}
