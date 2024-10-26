package me.taylan.listeners;

import me.taylan.RLoots;
import me.taylan.utils.FileManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class LootListener implements Listener {

    private final RLoots plugin;
    private final FileManager fileManager;
    public static HashMap<Player, Integer> delay = new HashMap<>();
    private double alpha, jump = 0;

    public LootListener(RLoots plugin) {
        this.plugin = plugin;
        this.fileManager = plugin.getFileManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void lootboxPut(@NotNull BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Location location = event.getBlockPlaced().getLocation();
        FileConfiguration langFile = YamlConfiguration.loadConfiguration(fileManager.getLangFile());
        FileConfiguration lootboxFile = YamlConfiguration.loadConfiguration(fileManager.getLootBoxFile());
        if (event.getItemInHand() != null && event.getItemInHand().hasItemMeta()) {
            ItemMeta meta = event.getItemInHand().getItemMeta();
            NamespacedKey namespacedKey = new NamespacedKey(plugin, "lootbox");
            if (meta.getPersistentDataContainer().has(namespacedKey)) {
                String value = meta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
                AnimationType animation = AnimationType.valueOf(lootboxFile.getString("lootboxs." + value + ".animation"));
                event.setCancelled(true);
                if (!delay.containsKey(player)) {
                    delay.put(player, (plugin.getConfig().getInt("lootbox-open-delay") * 2));
                    spawnlootbox(location, langFile.getString("opening-lootbox"), event.getItemInHand(), player, animation);
                    event.getItemInHand().setAmount(event.getItemInHand().getAmount() - 1);
                    BukkitRunnable runnable = new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (delay.get(player) == 0) {
                                player.playSound(player.getLocation(), Sound.valueOf(plugin.getConfig().getString("lootbox-opening-sound")),
                                        plugin.getConfig().getInt("lootbox-opening-sound-volume"), plugin.getConfig().getInt("lootbox-opening-sound-pitch"));
                                delay.remove(player);
                                cancel();
                            } else {

                                NoteBlock noteBlock = (NoteBlock) Material.NOTE_BLOCK.createBlockData();
                                noteBlock.setInstrument(Instrument.PIANO);
                                noteBlock.setNote(Note.natural(0, Note.Tone.A));
                                noteBlock.setNote(Note.natural(0, Note.Tone.B));
                                noteBlock.setNote(Note.natural(0, Note.Tone.C));
                                noteBlock.setNote(Note.natural(0, Note.Tone.D));
                                noteBlock.setNote(Note.natural(0, Note.Tone.E));
                                noteBlock.setNote(Note.natural(0, Note.Tone.F));
                                noteBlock.setNote(Note.natural(0, Note.Tone.G));
                                noteBlock.setPowered(true);
                                player.playNote(player.getLocation(), noteBlock.getInstrument(), noteBlock.getNote());
                                delay.put(player, delay.get(player) - 1);
                            }
                        }
                    };
                    runnable.runTaskTimer(plugin, 0, 10);
                } else {
                    player.sendMessage(MiniMessage.miniMessage().deserialize(langFile.getString("already-opening-lootbox")));
                }
            }
        }
    }


    public void spawnlootbox(Location loc, String name, ItemStack lootbox, Player player, AnimationType animationType) {
        loc.getWorld().spawn(loc.subtract(0, 1.3, 0), ArmorStand.class, armorStand -> {
            armorStand.setVisible(false);
            armorStand.setGravity(true);
            armorStand.setSmall(false);
            armorStand.setInvulnerable(true);
            armorStand.setCustomNameVisible(true);
            armorStand.getEquipment().setHelmet(lootbox);
            armorStand.customName(MiniMessage.miniMessage().deserialize(name));
            plugin.getArmorStandHashMap().put(armorStand.getWorld(), armorStand);
            ItemStack itemlootbox = lootbox.clone();

            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    alpha += Math.PI / 32;

                    if (!delay.containsKey(player)) {
                        plugin.getArmorStandHashMap().remove(armorStand.getWorld());
                        Location loc = armorStand.getLocation().add(0, 1.3, 0);
                        armorStand.getWorld().spawnParticle(Particle.valueOf(plugin.getConfig().getString("lootbox-loot-particle")), loc,
                                1, 0, 0, 0, 0);
                        giveLoot(player, itemlootbox);
                        armorStand.remove();
                        cancel();
                    } else {
                        Location loc = armorStand.getLocation().add(0, 1.5, 0);
                        Location secondLocation = loc.clone().add(Math.cos(alpha), 0, Math.sin(alpha));
                        armorStand.getWorld().spawnParticle(Particle.valueOf(plugin.getConfig().getString("lootbox-open-particle")), secondLocation,
                                1, 0, 0, 0, 0);


                        if (animationType.equals(AnimationType.RANDOM)) {
                            armorStand.teleport(armorStand.getLocation().add(0, 0.008, 0));
                            armorStand.setRotation(armorStand.getLocation().getYaw() + 0.5f, 0);
                            armorStand.setHeadPose(new EulerAngle(armorStand.getHeadPose().getX() + 0.1,
                                    armorStand.getHeadPose().getY() + 0.1, armorStand.getHeadPose().getZ() + 0.1));
                        } else if (animationType.equals(AnimationType.SPIN)) {
                            armorStand.teleport(armorStand.getLocation().add(0, 0.01, 0));
                            armorStand.setRotation(armorStand.getLocation().getYaw() + 4f, 0);
                        } else if (animationType.equals(AnimationType.JUMP)) {
                            jump += 1;
                            if (jump > 20) {
                                jump = 0;
                                Vector velocity = armorStand.getVelocity().setY(0.3);
                                armorStand.setVelocity(velocity);
                            }
                        }
                    }
                }
            };
            runnable.runTaskTimer(plugin, 0, 1);
        });
    }

    enum AnimationType {
        SPIN, RANDOM, JUMP;
    }

    public void giveLoot(Player p, ItemStack lootbox) {
        if (lootbox.hasItemMeta()) {
            NamespacedKey namespacedKey = new NamespacedKey(plugin, "lootbox");
            ItemMeta itemMeta = lootbox.getItemMeta();
            String value = itemMeta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(fileManager.getLootBoxFile());
            FileConfiguration configurationItem = YamlConfiguration.loadConfiguration(fileManager.getItemFile());
            FileConfiguration langFile = YamlConfiguration.loadConfiguration(fileManager.getLangFile());
            for (String lootboxName : configuration.getConfigurationSection("lootboxs").getKeys(false)) {
                if (value.equals(lootboxName)) {
                    Set<String> keys = configuration.getConfigurationSection("lootboxs." + lootboxName + ".loots").getKeys(false);
                    int result = new Random().nextInt(keys.size());
                    String key = keys.stream().toList().get(result);
                    String itemRarity = configuration.getString("lootboxs." + lootboxName + ".loots." + key + ".rarity");
                    String item = configuration.getString("lootboxs." + lootboxName + ".loots." + key + ".item");
                    int itemAmount = plugin.parseAmount(configuration.getString("lootboxs." + lootboxName + ".loots." + key + ".amount"));

                    if (!item.equals("EMPTY")) {
                        if (configuration.getInt("lootboxs." + lootboxName + ".loots." + key + ".luck") == 1) {
                            ItemStack item2 = configurationItem.getItemStack("Items." + item);
                            assert item2 != null;
                            if (item2.hasItemMeta() && item2.getItemMeta().hasDisplayName()) {
                                p.sendMessage(MiniMessage.miniMessage().deserialize(itemRarity + " <white><bold:false>" + itemAmount + "x ").append(Objects.requireNonNull(item2.getItemMeta().displayName())));
                            } else {
                                p.sendMessage(MiniMessage.miniMessage().deserialize(itemRarity + " <white><bold:false>" + itemAmount + "x " + item2.getType()));
                            }
                            item2.setAmount(itemAmount);
                            p.getInventory().addItem(item2);
                            break;

                        } else {
                            int luck = ThreadLocalRandom.current().nextInt(configuration.getInt("lootboxs." + lootboxName + ".loots." + key + ".luck"));
                            if (luck > 5) {
                                ItemStack item2 = configurationItem.getItemStack("Items." + item);
                                assert item2 != null;
                                if (item2.hasItemMeta() && item2.getItemMeta().hasDisplayName()) {
                                    p.sendMessage(MiniMessage.miniMessage().deserialize(itemRarity + " <white><bold:false>" + itemAmount + "x ").append(Objects.requireNonNull(item2.getItemMeta().displayName())));
                                } else {
                                    p.sendMessage(MiniMessage.miniMessage().deserialize(itemRarity + " <white><bold:false>" + itemAmount + "x " + item2.getType()));
                                }
                                item2.setAmount(itemAmount);
                                p.getInventory().addItem(item2);
                                break;
                            } else {
                                p.sendMessage(MiniMessage.miniMessage().deserialize(Objects.requireNonNull(langFile.getString("empty-lootbox"))));
                                break;
                            }
                        }
                    } else {
                        p.sendMessage(MiniMessage.miniMessage().deserialize(Objects.requireNonNull(langFile.getString("empty-lootbox"))));
                        break;
                    }
                }
            }


        }

    }

    @EventHandler
    public void onInteractArmorStand(PlayerArmorStandManipulateEvent e) {
        if (plugin.getArmorStandHashMap().containsKey(e.getRightClicked().getWorld())) {
            if (e.getRightClicked().getUniqueId().equals(plugin.getArmorStandHashMap().get(e.getRightClicked().getWorld()).getUniqueId())) {
                e.setCancelled(true);
            }
        }
    }


}
