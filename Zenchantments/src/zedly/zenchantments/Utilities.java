package zedly.zenchantments;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import zedly.zenchantments.arrows.EnchantedArrow;

import java.util.*;

import static org.bukkit.GameMode.CREATIVE;
import static org.bukkit.Material.AIR;
import static org.bukkit.inventory.EquipmentSlot.HAND;

public class Utilities {

	// Returns true for main hand slots, false otherwise
    public static boolean isMainHand(EquipmentSlot preferred) {
        return preferred == HAND;
    }

    // Returns an ArrayList of ItemStacks of the player's held item and armor
    public static List<ItemStack> getArmorAndMainHandItems(Player player, boolean mainHand) {
        List<ItemStack> stk = new ArrayList<>(Arrays.asList(player.getInventory().getArmorContents()));
        stk.add(mainHand ? player.getInventory().getItemInMainHand() : player.getInventory().getItemInOffHand());
        stk.removeIf((ItemStack is) -> is == null || is.getType() == Material.AIR);
        return stk;
    }

    // Removes the given ItemStack's durability by the given 'damage' and then sets the item direction the given players hand.
    //      This also takes into account the unbreaking enchantment
    public static void damageTool(Player player, int damage, boolean handUsed) {
        if (!player.getGameMode().equals(CREATIVE)) {
            ItemStack hand = handUsed ? player.getInventory().getItemInMainHand() : player.getInventory().getItemInOffHand();
            for (int i = 0; i < damage; i++) {
                if (Storage.rnd.nextInt(100) <= (100 / (hand.getEnchantmentLevel(org.bukkit.enchantments.Enchantment.DURABILITY) + 1))) {
                    hand.setDurability((short) (hand.getDurability() + 1));
                }
            }
            if (handUsed) {
                player.getInventory().setItemInMainHand(hand.getDurability() > hand.getType().getMaxDurability() ? new ItemStack(AIR) : hand);
            } else {
                player.getInventory().setItemInOffHand(hand.getDurability() > hand.getType().getMaxDurability() ? new ItemStack(AIR) : hand);
            }
        }
    }

    // Displays a particle with the given data
    public static void display(Location loc, Particle particle, int amount, double speed, double xO, double yO, double zO) {
        loc.getWorld().spawnParticle(particle, loc.getX(), loc.getY(), loc.getZ(), amount, (float) xO, (float) yO, (float) zO, (float) speed);
    }

    // Removes the given ItemStack's durability by the given 'damage'
    //      This also takes into account the unbreaking enchantment
    public static void addUnbreaking(Player player, ItemStack is, int damage) {
        if (!player.getGameMode().equals(CREATIVE)) {
            for (int i = 0; i < damage; i++) {
                if (Storage.rnd.nextInt(100) <= (100 / (is.getEnchantmentLevel(org.bukkit.enchantments.Enchantment.DURABILITY) + 1))) {
                    is.setDurability((short) (is.getDurability() + 1));
                }
            }
        }
    }

    // Returns the item stack direction the player's main or off hand, determinted by 'handUsed'
    public static ItemStack usedStack(Player player, boolean handUsed) {
        return handUsed ? player.getInventory().getItemInMainHand() : player.getInventory().getItemInOffHand();
    }

    // Sets the hand the player to the given item stack, determined by 'handUsed'
    public static void setHand(Player player, ItemStack stk, boolean handUsed) {
        if (handUsed) {
            player.getInventory().setItemInMainHand(stk);
        } else {
            player.getInventory().setItemInOffHand(stk);
        }
    }

    // Removes a certain number of an item stack of the given description from the players inventory
    public static void removeItem(Player player, Material mat, short data, int amount) {
        if (!player.getGameMode().equals(CREATIVE)) {
            Inventory inv = player.getInventory();
            for (int i = 0; i < inv.getSize(); i++) {
                if (inv.getItem(i) != null && inv.getItem(i).getType() == mat && inv.getItem(i).getDurability() == data) {
                    if (inv.getItem(i).getAmount() > amount) {
                        int res = inv.getItem(i).getAmount() - amount;
                        ItemStack rest = inv.getItem(i);
                        rest.setAmount(res);
                        inv.setItem(i, rest);
                        return;
                    } else {
                        amount -= inv.getItem(i).getAmount();
                        inv.setItem(i, null);
                    }
                }
            }
        }
    }

    // Removes a certain number of an item stack of the given description from the players inventory
    public static void removeItem(Player player, Material mat, int amount) {
        removeItem(player, mat, (short) 0, amount);
    }

    // Removes a certain number of an item stack of the given description from the players inventory
    public static void removeItem(Player player, ItemStack is) {
        removeItem(player, is.getType(), is.getDurability(), is.getAmount());
    }

    // Removes a certain number of an item stack of the given description from the players inventory and returns true
    //      if the item stack was direction their inventory
    public static boolean removeItemCheck(Player player, Material mat, short data, int amount) {
        if (player.getGameMode().equals(CREATIVE)) {
            return true;
        }
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) != null && inv.getItem(i).getType() == mat && inv.getItem(i).getDurability() == data) {
                if (inv.getItem(i).getAmount() > amount) {
                    int res = inv.getItem(i).getAmount() - amount;
                    ItemStack rest = inv.getItem(i);
                    rest.setAmount(res);
                    inv.setItem(i, rest);
                    return true;
                } else {
                    inv.setItem(i, null);
                    return true;
                }
            }
        }
        return false;
    }

    // Returns a level for the enchant event given the XP level and the enchantments max level
    public static int getEnchantLevel(int maxlevel, int levels) {
        if (maxlevel == 1) {
            return 1;
        }
        int sectionsize = 32 / (maxlevel - 1);
        int position = levels / sectionsize;
        int mod = levels - position * sectionsize;
        if (Storage.rnd.nextInt(2 * sectionsize) >= mod) {
            return position + 1;
        } else {
            return position + 2;
        }
    }

    // Returns the english number representation of the given roman number string
    public static int getNumber(String numeral) {
        switch (numeral.toUpperCase()) {
            case "-":
                return 0;
            case "I":
                return 1;
            case "II":
                return 2;
            case "III":
                return 3;
            case "IV":
                return 4;
            case "V":
                return 5;
            case "VI":
                return 6;
            case "VII":
                return 7;
            case "VIII":
                return 8;
            case "IX":
                return 9;
            case "X":
                return 10;
            default:
                return 1;
        }
    }

    // Returns the roman number string representation of the given english number
    public static String getRomanString(int number) {
        switch (number) {
            case 0:
                return "-";
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "X";
            default:
                return "I";
        }
    }

    // Returns the roman number string representation of the given english number, capped at the int 'limit'
    public static String getRomanString(int number, int limit) {
        if (number > limit) {
            return getRomanString(limit);
        } else {
            return getRomanString(number);
        }
    }

    // Returns the exact center of a block of a given location
    public static Location getCenter(Location loc) {
        return getCenter(loc, false);
    }
    
    // Returns the exact center of a block of a given location
    public static Location getCenter(Location loc, boolean centerVertical) {
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        if (x >= 0) {
            x += .5;
        } else {
            x += .5;
        }
        if(centerVertical) {
            y = (int) y + .5;
        }
        if (z >= 0) {
            z += .5;
        } else {
            z += .5;
        }
        Location lo = loc.clone();
        lo.setX(x);
        lo.setY(y);
        lo.setZ(z);
        return lo;
    }

    // Returns the exact center of a block of a given block
    public static Location getCenter(Block blk) {
        return getCenter(blk.getLocation());
    }
    
    // Returns the exact center of a block of a given block
    public static Location getCenter(Block blk, boolean centerVertical) {
        return getCenter(blk.getLocation(), centerVertical);
    }

    // Returns the nearby entities at any loction within the given range
    // Returns a direction integer, 0-8, for the given player's pitch and yaw
    public static BlockFace getDirection(Player player) {
        float yaw = player.getLocation().getYaw();
        BlockFace direction = BlockFace.SELF;
        if (yaw < 0) {
            yaw += 360;
        }
        yaw %= 360;
        double i = (double) ((yaw + 8) / 18);
        if (i >= 19 || i < 1) {
            direction = BlockFace.SOUTH;
        } else if (i < 3) {
            direction = BlockFace.SOUTH_WEST;
        } else if (i < 6) {
            direction = BlockFace.WEST;
        } else if (i < 8) {
            direction = BlockFace.NORTH_WEST;
        } else if (i < 11) {

            direction = BlockFace.NORTH;
        } else if (i < 13) {
            direction = BlockFace.NORTH_EAST;
        } else if (i < 16) {
            direction = BlockFace.EAST;
        } else if (i < 18) {
            direction = BlockFace.SOUTH_EAST;
        }
        return direction;
    }

    // Returns a more simple direction integer, 0-6, for the given player's pitch and yaw
    public static BlockFace getCardinalDirection(float yaw, float pitch) {
        BlockFace direction;
        if (yaw < 0) {
            yaw += 360;
        }
        yaw %= 360;
        double i = (double) ((yaw + 8) / 18);
        if (i >= 18 || i < 3) {
            direction = BlockFace.SOUTH;
        } else if (i < 8) {
            direction = BlockFace.WEST;
        } else if (i < 13) {
            direction = BlockFace.NORTH;
        } else  {
            direction = BlockFace.EAST;
        }
        if (pitch < -50) {
            direction = BlockFace.UP;
        } else if (pitch > 50) {
            direction = BlockFace.DOWN;
        }
        return direction;
    }

	// Returns true if a player can use a certain enchantment at a certain time (permissions and cooldowns),
    //      otherwise false
    public static boolean canUse(Player player, int enchantmentID) {
        if (!player.hasPermission("zenchantments.enchant.use")) {
            return false;
        }
        if (EnchantPlayer.matchPlayer(player).getCooldown(enchantmentID) != 0) {
            return false;
        }
        return !EnchantPlayer.matchPlayer(player).isDisabled(enchantmentID);
    }

    // Adds a potion effect of given length and intensity to the given entity. 
    public static void addPotion(LivingEntity ent, PotionEffectType type, int length, int intensity) {
        for (PotionEffect eff : ent.getActivePotionEffects()) {
            if (eff.getType().equals(type)) {
                if (eff.getAmplifier() > intensity) {
                    return;
                } else if (eff.getDuration() > length) {
                    return;
                } else {
                    ent.removePotionEffect(type);
                }
            }
        }
        ent.addPotionEffect(new PotionEffect(type, length, intensity));
    }

	// Encodes a given string to be invisible to players surrounded by the escape sequence "\< \>"
	public static String toInvisibleString(String str) {
		str = "\\<" + str + "\\>" + ChatColor.COLOR_CHAR + 'F';
		StringBuilder builder = new StringBuilder();
		for (char c : str.toCharArray()) {
			builder.append(ChatColor.COLOR_CHAR);
			builder.append(c);
		}
		return builder.toString();
	}

	// Returns a map of strings to booleans, where the boolean represents visibility
	public static Map<String, Boolean> fromInvisibleString(String str) {
    	Map<String, Boolean> strs = new LinkedHashMap<>();

		int state = 0; // 0 = close, 1 = waiting for next to open, 2 = open, 3 = waiting for next to close
		StringBuilder builder = new StringBuilder();
		for (char c : str.toCharArray()) {
				switch (state) {
					case 0: // Visible, waiting for '§'
						if (c == ChatColor.COLOR_CHAR) {
							state = 1;
						} else {
							builder.append(c);
						}
						break;
					case 1: // Got a '§', waiting for '\'
						if (c == '\\') {
							state = 2;
						} else if (c == ChatColor.COLOR_CHAR) {
							builder.append(ChatColor.COLOR_CHAR);
						} else {
							builder.append(ChatColor.COLOR_CHAR);
							builder.append(c);
							state = 0;
						}
						break;
					case 2: // Got a '\', waiting for '§'
						if (c == ChatColor.COLOR_CHAR) {
							state = 3;
						} else {
							builder.append(ChatColor.COLOR_CHAR);
							builder.append('\\');
							builder.append(c);
							state = 0;
						}
						break;
					case 3: // Got a '§', waiting for '<'
						if (c == '<') {
							state = 4;
							if (builder.length() != 0) {
								strs.put(builder.toString(), true);
								builder = new StringBuilder();
							}
						} else if (c == ChatColor.COLOR_CHAR) {
							builder.append(ChatColor.COLOR_CHAR);
							builder.append('\\');
							state = 1;
						} else {
							builder.append(ChatColor.COLOR_CHAR);
							builder.append('\\');
							builder.append(ChatColor.COLOR_CHAR);
							builder.append(c);
							state = 0;
						}
						break;
					case 4: // Invisible, ignore '§'
						state = 5;
						break;
					case 5: // Invisible, waiting for '\'
						if (c == '\\') {
							state = 6;
						} else {
							builder.append(c);
							state = 4;
						}
						break;
					case 6: // Got '\', waiting for '§'
						if (c == ChatColor.COLOR_CHAR) {
							state = 7;
						} else {
							builder.append('\\');
							state = 5;
						}
						break;
					case 7: // Got '§', waiting for '>'
						if (c == '>') {
							state = 0;
							if (builder.length() != 0) {
								strs.put(builder.toString(), false);
								builder = new StringBuilder();
							}
						} else {
							builder.append('\\');
							builder.append(c);
							state = 4;
						}
						break;
			}
		}
		if (builder.length() != 0) {
			strs.put(builder.toString(), true);
		}
		return strs;
	}
}
