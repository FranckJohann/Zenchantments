package zedly.zenchantmentsmain;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiPredicate;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.*;
import static org.bukkit.GameMode.CREATIVE;
import static org.bukkit.Material.*;
import org.bukkit.block.Biome;
import static org.bukkit.block.Biome.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import static org.bukkit.block.BlockFace.*;
import org.bukkit.enchantments.Enchantment;
import static org.bukkit.entity.EntityType.*;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import static org.bukkit.event.block.Action.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import static org.bukkit.potion.PotionEffectType.*;
import org.bukkit.util.Vector;

import static zedly.zenchantmentsmain.Tool.*;
import zedly.zenchantments.compatibility.CompatibilityAdapter;

// CustomEnchantment is the defualt structure for any enchantment. Each enchantment below it will extend this class
//      and will override any methods as neccecary in its behavior
public class CustomEnchantment {

    protected static final CompatibilityAdapter ADAPTER = Storage.COMPATIBILITY_ADAPTER;

    public static void applyForTool(Player player, ItemStack tool, BiPredicate<CustomEnchantment, Integer> action) {
        Config.get(player.getWorld()).getEnchants(tool).forEach((CustomEnchantment ench, Integer level) -> {
            if (!ench.used && Utilities.canUse(player, ench.getEnchantmentId())) {
                try {
                    ench.used = true;
                    if (action.test(ench, level)) {
                        EnchantPlayer.matchPlayer(player).setCooldown(ench.getEnchantmentId(), ench.cooldown);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                ench.used = false;
            }
        });
    }
    protected int maxLevel;         // Max level the given enchant can naturally obtain
    protected String loreName;      // Name the given enchantment will appear as; with &7 (Gray) color
    protected float probability;    // Relative probability of obtaining the given enchantment
    protected Tool[] enchantable;   // Enums that represent tools that can receive and work with given enchantment
    protected Class[] conflicting;  // Classes of enchantments that don't work with given enchantment
    protected String description;   // Description of what the enchantment does
    protected int cooldown;         // Cooldown for given enchantment given in ticks; Default is 0
    protected double power;         // Power multiplier for the enchantment's effects; Default is 0; -1 means no effect
    protected int handUse;          // Which hands an enchantment has actiosn for; 0 = none, 1 = left, 2 = right, 3 = both
    private boolean used;       // Indicates that an enchantment has already been applied to an event, avoiding infinite regress

    public int getEnchantmentId() {
        return -1;
    }

    public void loadData() {

    }

    // Returns true if the given material (tool) is compatible with the enchantment, otherwise false
    public boolean validMaterial(Material m) {
        for (Tool t : enchantable) {
            if (t.contains(m)) {
                return true;
            }
        }
        return false;
    }

    // Returns true if the given item stack (tool) is compatible with the enchantment, otherwise false
    public boolean validMaterial(ItemStack m) {
        return validMaterial(m.getType());
    }

    //Empty Methods for Events and Scanning Tasks: These are empty by default so that the WatcherEnchant can call these 
    //      for any enchantment without performing any checks. Each enchantment will override them as neccecary
    public boolean onBlockBreak(BlockBreakEvent evt, int level, boolean usedHand) {
        return false;
    }

    public boolean onBlockInteract(PlayerInteractEvent evt, int level, boolean usedHand) {
        return false;
    }

    public boolean onEntityInteract(PlayerInteractEntityEvent evt, int level, boolean usedHand) {
        return false;
    }

    public boolean onEntityKill(EntityDeathEvent evt, int level, boolean usedHand) {
        return false;
    }

    public boolean onEntityHit(EntityDamageByEntityEvent evt, int level, boolean usedHand) {
        return false;
    }

    public boolean onBeingHit(EntityDamageByEntityEvent evt, int level, boolean usedHand) {
        return false;
    }

    public boolean onEntityDamage(EntityDamageEvent evt, int level, boolean usedHand) {
        return false;
    }

    public boolean onPlayerFish(PlayerFishEvent evt, int level, boolean usedHand) {
        return false;
    }

    public boolean onHungerChange(FoodLevelChangeEvent evt, int level, boolean usedHand) {
        return false;
    }

    public boolean onShear(PlayerShearEntityEvent evt, int level, boolean usedHand) {
        return false;
    }

    public boolean onEntityShootBow(EntityShootBowEvent evt, int level, boolean usedHand) {
        return false;
    }

    public boolean onPotionSplash(PotionSplashEvent evt, int level, boolean usedHand) {
        return false;
    }

    public boolean onProjectileLaunch(ProjectileLaunchEvent evt, int level, boolean usedHand) {
        return false;
    }

    public boolean onPlayerDeath(PlayerDeathEvent evt, int level, boolean usedHand) {
        return false;
    }

    public boolean onScan(Player player, int level, boolean usedHand) {
        return false;
    }

    public boolean onScanHands(Player player, int level, boolean usedHand) {
        return false;
    }

    public boolean onCombust(EntityCombustByEntityEvent evt, int level, boolean usedHand) {
        return false;
    }

    public boolean onFastScan(Player player, int level, boolean usedHand) {
        return false;
    }

    public boolean onFastScanHands(Player player, int level, boolean usedHand) {
        return false;
    }

    public static class Anthropomorphism extends CustomEnchantment {

        public Anthropomorphism() {
            maxLevel = 1;
            loreName = "Anthropomorphism";
            probability = 0;
            enchantable = new Tool[]{PICKAXE};
            conflicting = new Class[]{Pierce.class, Switch.class};
            description = "Spawns blocks to protect you when right sneak clicking, and attacks entities when left clicking";
            cooldown = 0;
            power = 1.0;
            handUse = 3;

        }

        public int getEnchantmentId() {
            return 1;
        }

        @Override
        public boolean onBlockInteract(PlayerInteractEvent evt, int level, boolean usedHand) {
            Player player = evt.getPlayer();
            ItemStack hand = Utilities.usedStack(player, usedHand);
            if (evt.getAction() == RIGHT_CLICK_AIR || evt.getAction() == RIGHT_CLICK_BLOCK) {
                if (player.isSneaking()) {
                    if (!Storage.anthVortex.contains(player)) {
                        Storage.anthVortex.add(player);
                    }
                    int counter = 0;
                    for (Entity p : Storage.idleBlocks.values()) {
                        if (p.equals(player)) {
                            counter++;
                        }
                    }
                    if (counter < 64 && player.getInventory().contains(COBBLESTONE)) {
                        Utilities.removeItem(player, COBBLESTONE, 1);
                        Utilities.damageTool(player, 2, usedHand);
                        player.updateInventory();
                        Location loc = player.getLocation();
                        Material mat[] = new Material[]{STONE, GRAVEL, DIRT, GRASS};
                        FallingBlock blockEntity = loc.getWorld().spawnFallingBlock(loc, mat[Storage.rnd.nextInt(4)], (byte) 0x0);
                        blockEntity.setDropItem(false);
                        blockEntity.setGravity(false);
                        blockEntity.setMetadata("ze.anthrothrower", new FixedMetadataValue(Storage.zenchantments, player));
                        Storage.idleBlocks.put(blockEntity, player);
                        return true;
                    }
                }
                return false;
            } else if ((evt.getAction() == LEFT_CLICK_AIR || evt.getAction() == LEFT_CLICK_BLOCK)
                    || hand.getType() == AIR) {
                Storage.anthVortex.remove(player);
                List<FallingBlock> toRemove = new ArrayList<>();
                for (FallingBlock blk : Storage.idleBlocks.keySet()) {
                    if (Storage.idleBlocks.get(blk).equals(player)) {
                        Storage.attackBlocks.put(blk, power);
                        toRemove.add(blk);
                        blk.setVelocity(player.getTargetBlock((HashSet<Material>) null, 7)
                                .getLocation().subtract(player.getLocation()).toVector().multiply(.25));
                    }
                }
                for (FallingBlock blk : toRemove) {
                    Storage.idleBlocks.remove(blk);
                    blk.setGravity(true);
                    blk.setGlowing(true);
                }
            }
            return false;
        }
    }

    public static class Arborist extends CustomEnchantment {

        public Arborist() {
            maxLevel = 3;
            loreName = "Arborist";
            probability = 0;
            enchantable = new Tool[]{AXE};
            conflicting = new Class[]{};
            description = "Drops more apples, sticks, and saplings when used on leaves and wood";
            cooldown = 0;
            power = 1.0;
            handUse = 1;

        }

        public int getEnchantmentId() {
            return 2;
        }

        @Override
        public boolean onBlockBreak(BlockBreakEvent evt, int level, boolean usedHand) {
            Block blk = evt.getBlock();
            if (blk.getType() == LOG || blk.getType() == LOG_2 || blk.getType() == LEAVES_2 || blk.getType() == LEAVES) {
                short s = (short) blk.getData();
                if (s >= 8) {
                    s -= 8;
                }
                ItemStack stk;
                if (blk.getType() == LOG_2 || blk.getType() == LEAVES_2) {
                    stk = new ItemStack(SAPLING, 1, (short) (s + 4));
                } else {
                    stk = new ItemStack(SAPLING, 1, s);
                }
                if (Storage.rnd.nextInt(10) >= (9 - level) / (power + .001)) {
                    if (Storage.rnd.nextInt(3) % 3 == 0) {
                        evt.getBlock().getWorld()
                                .dropItemNaturally(Utilities.getCenter(evt.getBlock()), stk);
                    }
                    if (Storage.rnd.nextInt(3) % 3 == 0) {
                        evt.getBlock().getWorld()
                                .dropItemNaturally(Utilities.getCenter(evt.getBlock()), new ItemStack(STICK, 1));
                    }
                    if (Storage.rnd.nextInt(3) % 3 == 0) {
                        evt.getBlock().getWorld()
                                .dropItemNaturally(Utilities.getCenter(evt.getBlock()), new ItemStack(APPLE, 1));
                    }
                    if (Storage.rnd.nextInt(65) == 25) {
                        evt.getBlock().getWorld()
                                .dropItemNaturally(Utilities.getCenter(evt.getBlock()), new ItemStack(GOLDEN_APPLE, 1));
                    }
                    return true;
                }
            }
            return false;
        }
    }

    public static class Bind extends CustomEnchantment {

        public Bind() {
            maxLevel = 1;
            loreName = "Bind";
            probability = 0;
            enchantable = new Tool[]{ALL};
            conflicting = new Class[]{};
            description = "Keeps items with this enchantment in your inventory after death";
            cooldown = 0;
            power = -1.0;
            handUse = 0;

        }

        public int getEnchantmentId() {
            return 4;
        }

        @Override
        public boolean onPlayerDeath(final PlayerDeathEvent evt, int level, boolean usedHand) {
            if (evt.getKeepInventory()) {
                return false;
            }
            final Player player = evt.getEntity();
            Config config = Config.get(player.getWorld());
            final ItemStack[] contents = player.getInventory().getContents().clone();
            final List<ItemStack> removed = new ArrayList<>();
            for (int i = 0; i < contents.length; i++) {
                if (!config.getEnchants(contents[i]).containsKey(this)) {
                    contents[i] = null;
                } else {
                    removed.add(contents[i]);
                    evt.getDrops().remove(contents[i]);
                }
            }
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Storage.zenchantments, () -> {
                if (evt.getKeepInventory()) {
                    evt.getDrops().addAll(removed);
                } else {
                    player.getInventory().setContents(contents);
                }
            }, 1);
            return true;
        }
    }

    public static class BlazesCurse extends CustomEnchantment {

        private static final Biome[] noRainBiomes = new Biome[]{DESERT, FROZEN_OCEAN, FROZEN_RIVER, ICE_FLATS,
            ICE_MOUNTAINS, DESERT_HILLS, COLD_BEACH, TAIGA_COLD, TAIGA_COLD_HILLS,
            SAVANNA, SAVANNA_ROCK, MESA, MESA_ROCK, MESA_CLEAR_ROCK, MUTATED_DESERT,
            MUTATED_ICE_FLATS, MUTATED_TAIGA_COLD, MUTATED_SAVANNA, MUTATED_SAVANNA_ROCK,
            MUTATED_MESA, MUTATED_MESA_ROCK, MUTATED_MESA_CLEAR_ROCK};
        private static final float submergeDamage = 1.5f;
        private static final float rainDamage = .5f;

        public BlazesCurse() {
            maxLevel = 1;
            loreName = "Blaze's Curse";
            probability = 0;
            enchantable = new Tool[]{CHESTPLATE};
            conflicting = new Class[]{};
            description = "Causes the player to be unharmed in lava and fire, but damages them in water and rain";
            cooldown = 0;
            power = -1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 5;
        }

        @Override
        public boolean onEntityDamage(EntityDamageEvent evt, int level, boolean usedHand) {
            if (evt.getCause() == EntityDamageEvent.DamageCause.HOT_FLOOR || evt.getCause() == EntityDamageEvent.DamageCause.LAVA || evt.getCause() == EntityDamageEvent.DamageCause.FIRE || evt.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                evt.setCancelled(true);
                return true;
            }
            return false;
        }

        @Override
        public boolean onBeingHit(EntityDamageByEntityEvent evt, int level, boolean usedHand) {
            if (evt.getDamager().getType() == EntityType.FIREBALL
                    || evt.getDamager().getType() == EntityType.SMALL_FIREBALL) {
                evt.setDamage(0);
                return true;
            }
            return false;
        }

        @Override
        public boolean onScan(Player player, int level, boolean usedHand) {
            Material mat = player.getLocation().getBlock().getType();
            if (mat == STATIONARY_WATER || mat == WATER) {
                ADAPTER.damagePlayer(player, submergeDamage, DamageCause.DROWNING);
                return true;
            }
            mat = player.getLocation().getBlock().getRelative(DOWN).getType();
            if (mat == ICE || mat == FROSTED_ICE) {
                ADAPTER.damagePlayer(player, rainDamage, DamageCause.MELTING);
                return true;
            }
            if (player.getWorld().hasStorm() == true
                    && !ArrayUtils.contains(noRainBiomes, player.getLocation().getBlock().getBiome())
                    && player.getLocation().getY() >= player.getWorld().getHighestBlockYAt(player.getLocation())) {
                ADAPTER.damagePlayer(player, rainDamage, DamageCause.CUSTOM);
            }
            player.setFireTicks(0);
            return true;
        }

    }

    public static class Blizzard extends CustomEnchantment {

        public Blizzard() {
            maxLevel = 3;
            loreName = "Blizzard";
            probability = 0;
            enchantable = new Tool[]{BOW_};
            conflicting = new Class[]{Firestorm.class};
            description = "Spawns a blizzard where the arrow strikes freezing nearby entities";
            cooldown = 0;
            power = 1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 6;
        }

        @Override
        public boolean onEntityShootBow(EntityShootBowEvent evt, int level, boolean usedHand) {
            EnchantArrow.ArrowEnchantBlizzard arrow = new EnchantArrow.ArrowEnchantBlizzard((Projectile) evt.getProjectile(), level, power);
            Utilities.putArrow(evt.getProjectile(), arrow, (Player) evt.getEntity());
            return true;
        }
    }

    public static class Bounce extends CustomEnchantment {

        public Bounce() {
            maxLevel = 5;
            loreName = "Bounce";
            probability = 0;
            enchantable = new Tool[]{BOOTS};
            conflicting = new Class[]{};
            description = "Preserves momentum when on slime blocks";
            cooldown = 0;
            power = 1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 7;
        }

        @Override
        public boolean onFastScan(Player player, int level, boolean usedHand) {
            if (player.getVelocity().getY() < 0 && (player.getLocation().getBlock().getRelative(0, -1, 0).getType() == SLIME_BLOCK
                    || player.getLocation().getBlock().getType() == SLIME_BLOCK
                    || (player.getLocation().getBlock().getRelative(0, -2, 0).getType() == SLIME_BLOCK) && (level * power) > 2.0)) {
                if (!player.isSneaking()) {
                    player.setVelocity(player.getVelocity().setY(.56 * level * power));
                    return true;
                }
                player.setFallDistance(0);
            }
            return false;
        }
    }

    public static class Burst extends CustomEnchantment {

        public Burst() {
            maxLevel = 3;
            loreName = "Burst";
            probability = 0;
            enchantable = new Tool[]{BOW_};
            conflicting = new Class[]{Spread.class};
            description = "Rapidly fires arrows in series";
            cooldown = 0;
            power = 1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 8;
        }

        @Override
        public boolean onBlockInteract(final PlayerInteractEvent evt, int level, boolean usedHand) {
            final Player player = evt.getPlayer();
            final ItemStack hand = Utilities.usedStack(player, usedHand);
            if (evt.getAction().equals(RIGHT_CLICK_AIR) || evt.getAction().equals(RIGHT_CLICK_BLOCK)) {
                for (int i = 0; i <= (int) Math.round((power * level) + 1); i++) {
                    if (hand.containsEnchantment(Enchantment.ARROW_INFINITE) || Utilities.removeItemCheck(player, Material.ARROW, (short) 0, 1)) {
                        Utilities.setHand(player, hand, usedHand);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Storage.zenchantments, () -> {
                            Arrow arrow = player.getWorld().spawnArrow(player.getEyeLocation(), player.getLocation().getDirection(), 1, 0);
                            arrow.setShooter(player);
                            arrow.setVelocity(player.getLocation().getDirection().normalize().multiply(1.7));
                            EntityShootBowEvent shootEvent = new EntityShootBowEvent(player, hand, arrow, 1f);
                            ProjectileLaunchEvent launchEvent = new ProjectileLaunchEvent(arrow);
                            Bukkit.getPluginManager().callEvent(shootEvent);
                            Bukkit.getPluginManager().callEvent(launchEvent);
                            if (shootEvent.isCancelled() || launchEvent.isCancelled()) {
                                arrow.remove();
                            } else {
                                arrow.setMetadata("ze.arrow", new FixedMetadataValue(Storage.zenchantments, null));
                                arrow.setCritical(true);
                                Utilities.putArrow(arrow, new EnchantArrow.ArrowGenericMulitple(arrow), player);
                                Utilities.damageTool(player, 1, usedHand);
                            }
                        }, i * 2);
                    }
                    return true;
                }
            }
            return false;
        }

    }

    public static class Combustion extends CustomEnchantment {

        public Combustion() {
            maxLevel = 4;
            loreName = "Combustion";
            probability = 0;
            enchantable = new Tool[]{CHESTPLATE};
            conflicting = new Class[]{};
            description = "Lights attacking entities on fire when player is attacked";
            cooldown = 0;
            power = 1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 9;
        }

        @Override
        public boolean onBeingHit(EntityDamageByEntityEvent evt, int level, boolean usedHand) {
            Entity ent;
            if (evt.getDamager().getType() == EntityType.ARROW) {
                Arrow arrow = (Arrow) evt.getDamager();
                if (arrow.getShooter() instanceof LivingEntity) {
                    ent = (Entity) arrow.getShooter();
                } else {
                    return false;
                }
            } else {
                ent = evt.getDamager();
            }
            return ADAPTER.igniteEntity(ent, (Player) evt.getEntity(), (int) (50 * level * power));
        }

        public boolean onCombust(EntityCombustByEntityEvent evt, int level, boolean usedHand) {
            if (ADAPTER.isZombie(evt.getCombuster())) {
                evt.setDuration(0);
            }
            return false;
        }
    }

    public static class Conversion extends CustomEnchantment {

        public Conversion() {
            maxLevel = 4;
            loreName = "Conversion";
            probability = 0;
            enchantable = new Tool[]{SWORD};
            conflicting = new Class[]{};
            description = "Converts XP to health when right clicking and sneaking";
            cooldown = 0;
            power = 1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 10;
        }

        @Override
        public boolean onBlockInteract(PlayerInteractEvent evt, int level, boolean usedHand) {
            if (evt.getAction() == Action.RIGHT_CLICK_AIR || evt.getAction() == Action.RIGHT_CLICK_BLOCK) {
                final Player player = (Player) evt.getPlayer();
                if (player.isSneaking()) {
                    if (player.getLevel() > 1) {
                        if (player.getHealth() < 20) {
                            player.setLevel((int) (player.getLevel() - 1));
                            player.setHealth(Math.min(20, player.getHealth() + 2 * power * level));
                            for (int i = 0; i < 3; i++) {
                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Storage.zenchantments, () -> {
                                    Utilities.display(Utilities.getCenter(player.getLocation()), Particle.HEART, 10, .1f, .5f, .5f, .5f);
                                }, ((i * 5) + 1));
                            }
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    public static class Decapitation extends CustomEnchantment {

        private static final int BASE_PLAYER_DROP_CHANCE = 150;
        private static final int BASE_MOB_DROP_CHANCE = 150;

        public Decapitation() {
            maxLevel = 4;
            loreName = "Decapitation";
            probability = 0;
            enchantable = new Tool[]{SWORD};
            conflicting = new Class[]{};
            description = "Increases the chance for dropping the enemies head on death";
            cooldown = 0;
            power = 1.0;
            handUse = 1;
        }

        public int getEnchantmentId() {
            return 11;
        }

        @Override
        public boolean onEntityKill(EntityDeathEvent evt, int level, boolean usedHand) {
            EntityType[] t = new EntityType[]{SKELETON, WITHER_SKULL, ZOMBIE, PLAYER, CREEPER};
            short id = (short) ArrayUtils.indexOf(t, evt.getEntityType());
            if (id == -1) {
                return false;
            }
            if (id == 3) {
                if (Storage.rnd.nextInt((int) Math.round(BASE_PLAYER_DROP_CHANCE / (level * power))) == 0) {
                    ItemStack stk = new ItemStack(Material.SKULL_ITEM, 1, id);
                    SkullMeta meta = (SkullMeta) stk.getItemMeta();
                    meta.setOwner(evt.getEntity().getName());
                    stk.setItemMeta(meta);
                    evt.getEntity().getWorld().dropItemNaturally(evt.getEntity().getLocation(), stk);
                }
            } else if (Storage.rnd.nextInt((int) Math.round(BASE_MOB_DROP_CHANCE / level * power)) == 0) {
                ItemStack stk = new ItemStack(Material.SKULL_ITEM, 1, id);
                evt.getEntity().getWorld().dropItemNaturally(evt.getEntity().getLocation(), stk);
            }
            return false;
        }
    }

    public static class Extraction extends CustomEnchantment {

        public Extraction() {
            maxLevel = 3;
            loreName = "Extraction";
            probability = 0;
            enchantable = new Tool[]{PICKAXE};
            conflicting = new Class[]{Switch.class};
            description = "Smelts and yields more product from ores";
            cooldown = 0;
            power = 1.0;
            handUse = 1;
        }

        public int getEnchantmentId() {
            return 12;
        }

        @Override
        public boolean onBlockBreak(BlockBreakEvent evt, final int level, boolean usedHand) {
            if (evt.getBlock().getType() == GOLD_ORE || evt.getBlock().getType() == IRON_ORE) {
                Utilities.damageTool(evt.getPlayer(), 1, usedHand);
                for (int x = 0; x < Storage.rnd.nextInt((int) Math.round(power * level + 1)) + 1; x++) {
                    evt.getBlock().getWorld().dropItemNaturally(Utilities.getCenter(evt.getBlock()),
                            new ItemStack(evt.getBlock().getType() == GOLD_ORE ? GOLD_INGOT : IRON_INGOT));
                }
                ExperienceOrb o = (ExperienceOrb) evt.getBlock().getWorld().spawnEntity(Utilities.getCenter(evt.getBlock()), EXPERIENCE_ORB);
                o.setExperience(evt.getBlock().getType() == IRON_ORE ? Storage.rnd.nextInt(5) + 1 : Storage.rnd.nextInt(5) + 3);
                evt.getBlock().setType(AIR);
                Utilities.display(Utilities.getCenter(evt.getBlock()), Particle.FLAME, 10, .1f, .5f, .5f, .5f);
                return true;
            }
            return false;
        }
    }

    public static class Fire extends CustomEnchantment {

        public Fire() {
            maxLevel = 1;
            loreName = "Fire";
            probability = 0;
            enchantable = new Tool[]{PICKAXE, AXE, SHOVEL};
            conflicting = new Class[]{Switch.class, Variety.class};
            description = "Drops the smelted version of the block broken";
            cooldown = 0;
            power = -1.0;
            handUse = 1;
        }

        public int getEnchantmentId() {
            return 13;
        }

        @Override
        public boolean onBlockBreak(BlockBreakEvent evt, int level, boolean usedHand) {
            ItemStack hand = Utilities.usedStack(evt.getPlayer(), usedHand);
            Material mat = AIR;
            short itemInfo = 0;
            short s = evt.getBlock().getData();
            if (Tool.PICKAXE.contains(hand)) {
                if (evt.getBlock().getType() == STONE) {
                    if (s == 1 || s == 3 || s == 5) {
                        s++;
                        mat = STONE;
                        itemInfo = s;
                    } else if (s == 2 || s == 4 || s == 6) {
                        return false;
                    } else {
                        mat = SMOOTH_BRICK;
                    }
                } else if (evt.getBlock().getType() == IRON_ORE) {
                    mat = IRON_INGOT;
                } else if (evt.getBlock().getType() == GOLD_ORE) {
                    mat = GOLD_INGOT;
                } else if (evt.getBlock().getType() == COBBLESTONE) {
                    mat = STONE;
                } else if (evt.getBlock().getType() == SPONGE && s == 1) {
                    mat = SPONGE;
                } else if (evt.getBlock().getType() == MOSSY_COBBLESTONE) {
                    mat = SMOOTH_BRICK;
                    itemInfo = 1;
                } else if (evt.getBlock().getType() == NETHERRACK) {
                    mat = NETHER_BRICK_ITEM;
                } else if (evt.getBlock().getType() == SMOOTH_BRICK && evt.getBlock().getData() == 0) {
                    mat = SMOOTH_BRICK;
                    itemInfo = 2;
                }
            }
            if (evt.getBlock().getType() == GOLD_ORE || evt.getBlock().getType() == IRON_ORE) {
                ExperienceOrb o = (ExperienceOrb) evt.getBlock().getWorld().spawnEntity(Utilities.getCenter(evt.getBlock()), EXPERIENCE_ORB);
                o.setExperience(evt.getBlock().getType() == IRON_ORE ? Storage.rnd.nextInt(5) + 1 : Storage.rnd.nextInt(5) + 3);
            }
            if (evt.getBlock().getType() == SAND) {
                mat = GLASS;
            } else if (evt.getBlock().getType() == LOG || evt.getBlock().getType() == LOG_2) {
                mat = COAL;
                itemInfo = 1;
            } else if (evt.getBlock().getType() == CLAY) {
                Utilities.display(Utilities.getCenter(evt.getBlock()), Particle.FLAME, 10, .1f, .5f, .5f, .5f);
                for (int x = 0; x < 4; x++) {
                    evt.getBlock().getWorld().dropItemNaturally(Utilities.getCenter(evt.getBlock()), new ItemStack(CLAY_BRICK));
                }

                Block affectedBlock = evt.getBlock();
                Storage.fireDropLocs.add(affectedBlock);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Storage.zenchantments, () -> {
                    Storage.fireDropLocs.remove(affectedBlock);
                }, 5);

                return true;
            } else if (evt.getBlock().getType() == CACTUS) {
                Location location = evt.getBlock().getLocation().clone();
                double height = location.getY();
                for (double i = location.getY(); i <= 256; i++) {
                    location.setY(i);
                    if (location.getBlock().getType() == CACTUS) {
                        height++;
                    } else {
                        break;
                    }
                }
                for (double i = height - 1; i >= evt.getBlock().getLocation().getY(); i--) {
                    location.setY(i);
                    Utilities.display(Utilities.getCenter(location.getBlock()), Particle.FLAME, 10, .1f, .5f, .5f, .5f);

                    evt.getBlock().getWorld().dropItemNaturally(Utilities.getCenter(location), new ItemStack(INK_SACK, 1, (short) 2));

                    Block affectedBlock = location.getBlock();
                    Storage.fireDropLocs.add(affectedBlock);
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Storage.zenchantments, () -> {
                        Storage.fireDropLocs.remove(affectedBlock);
                    }, 5);

                    
                }
                return true;
            }
            if (mat != AIR) {

                evt.getBlock().getWorld().dropItemNaturally(Utilities.getCenter(evt.getBlock()), new ItemStack((mat), 1, itemInfo));
                Utilities.display(Utilities.getCenter(evt.getBlock()), Particle.FLAME, 10, .1f, .5f, .5f, .5f);

                Block affectedBlock = evt.getBlock();
                Storage.fireDropLocs.add(affectedBlock);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Storage.zenchantments, () -> {
                    Storage.fireDropLocs.remove(affectedBlock);
                }, 5);

                return true;
            }
            return false;
        }
    }

    public static class Firestorm extends CustomEnchantment {

        public Firestorm() {
            maxLevel = 3;
            loreName = "Firestorm";
            probability = 0;
            enchantable = new Tool[]{BOW_};
            conflicting = new Class[]{Blizzard.class};
            description = "Spawns a firestorm where the arrow strikes burning nearby entities";
            cooldown = 0;
            power = 1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 14;
        }

        @Override
        public boolean onEntityShootBow(EntityShootBowEvent evt, int level, boolean usedHand) {
            EnchantArrow.ArrowEnchantFirestorm arrow = new EnchantArrow.ArrowEnchantFirestorm((Projectile) evt.getProjectile(), level, power);
            Utilities.putArrow(evt.getProjectile(), arrow, (Player) evt.getEntity());
            return true;
        }

    }

    public static class Fireworks extends CustomEnchantment {

        public Fireworks() {
            maxLevel = 4;
            loreName = "Fireworks";
            probability = 0;
            enchantable = new Tool[]{BOW_};
            conflicting = new Class[]{};
            description = "Shoots arrows that burst into fireworks upon impact";
            cooldown = 0;
            power = -1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 15;
        }

        @Override
        public boolean onEntityShootBow(EntityShootBowEvent evt, int level, boolean usedHand) {
            EnchantArrow.ArrowEnchantFirework arrow = new EnchantArrow.ArrowEnchantFirework((Projectile) evt.getProjectile(), level);
            Utilities.putArrow(evt.getProjectile(), arrow, (Player) evt.getEntity());
            return true;
        }

    }

    public static class Force extends CustomEnchantment {

        public Force() {
            maxLevel = 3;
            loreName = "Force";
            probability = 0;
            enchantable = new Tool[]{SWORD};
            conflicting = new Class[]{RainbowSlam.class, Gust.class};
            description = "Pushes and pulls nearby mobs, configurable through shift clicking";
            cooldown = 0;
            power = 1.0;
            handUse = 3;
        }

        public int getEnchantmentId() {
            return 16;
        }

        @Override
        public boolean onBlockInteract(PlayerInteractEvent evt, int level, boolean usedHand) {
            Player player = evt.getPlayer();
            if (!evt.getPlayer().hasMetadata("ze.force.direction")) {
                player.setMetadata("ze.force.direction", new FixedMetadataValue(Storage.zenchantments, true));
            }
            if (player.isSneaking() && (evt.getAction() == RIGHT_CLICK_AIR || evt.getAction() == RIGHT_CLICK_BLOCK)) {
                boolean b = !player.getMetadata("ze.force.direction").get(0).asBoolean();
                player.setMetadata("ze.force.direction", new FixedMetadataValue(Storage.zenchantments, b));
                player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + (b ? "Push Mode" : "Pull Mode"));
                return false;
            }
            boolean mode = player.getMetadata("ze.force.direction").get(0).asBoolean();
            if (evt.getAction() == Action.RIGHT_CLICK_AIR || evt.getAction() == Action.RIGHT_CLICK_BLOCK) {
                List<Entity> nearEnts = player.getNearbyEntities(5, 5, 5);
                if (!nearEnts.isEmpty()) {
                    if (player.getFoodLevel() >= 2) {
                        if (Storage.rnd.nextInt(10) == 5) {
                            FoodLevelChangeEvent event = new FoodLevelChangeEvent(player, 2);
                            Bukkit.getServer().getPluginManager().callEvent(event);
                            if (!event.isCancelled()) {
                                player.setFoodLevel(player.getFoodLevel() - 2);
                            }
                        }
                        for (Entity ent : nearEnts) {
                            Location playLoc = player.getLocation();
                            Location entLoc = ent.getLocation();
                            Location total = mode ? entLoc.subtract(playLoc) : playLoc.subtract(entLoc);
                            Vector vect = new Vector(total.getX(), total.getY(), total.getZ()).multiply((.1f + (power * level * .2f)));
                            vect.setY(vect.getY() > 1 ? 1 : -1);
                            if (ent instanceof LivingEntity && ADAPTER.attackEntity((LivingEntity) ent, player, 0)) {
                                ent.setVelocity(vect);
                            }
                        }
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static class FrozenStep extends CustomEnchantment {

        public FrozenStep() {
            maxLevel = 3;
            loreName = "Frozen Step";
            probability = 0;
            enchantable = new Tool[]{BOOTS};
            conflicting = new Class[]{NetherStep.class};
            description = "Allows the player to walk on water and safely emerge from it when sneaking";
            cooldown = 0;
            power = 1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 17;
        }

        public boolean onScan(Player player, int level, boolean usedHand) {
            if (player.isSneaking() && player.getLocation().getBlock().getType() == STATIONARY_WATER && !player.isFlying()) {
                player.setVelocity(player.getVelocity().setY(.4));
            }
            Block block = (Block) player.getLocation().getBlock();
            int radius = (int) Math.round(power * level + 2);
            for (int x = -(radius); x <= radius; x++) {
                for (int z = -(radius); z <= radius; z++) {
                    Block possiblePlatformBlock = block.getRelative(x, -1, z);
                    Location possiblePlatformLoc = possiblePlatformBlock.getLocation();
                    if (possiblePlatformLoc.distanceSquared(block.getLocation()) < radius * radius - 2) {
                        if (Storage.waterLocs.containsKey(possiblePlatformLoc)) {
                            Storage.waterLocs.put(possiblePlatformLoc, System.nanoTime());
                        } else if (possiblePlatformBlock.getType() == STATIONARY_WATER
                                && possiblePlatformBlock.getData() == 0
                                && possiblePlatformBlock.getRelative(0, 1, 0).getType() == AIR) {
                            if (ADAPTER.formBlock(possiblePlatformBlock, PACKED_ICE, (byte) 0, player)) {
                                Storage.waterLocs.put(possiblePlatformLoc, System.nanoTime());
                            }
                        }
                    }
                }
            }
            return true;
        }
    }

    public static class Fuse extends CustomEnchantment {

        public Fuse() {
            maxLevel = 1;
            loreName = "Fuse";
            probability = 0;
            enchantable = new Tool[]{BOW_};
            conflicting = new Class[]{};
            description = "Instantly ignites anything explosive";
            cooldown = 0;
            power = -1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 18;
        }

        @Override
        public boolean onEntityShootBow(EntityShootBowEvent evt, int level, boolean usedHand) {
            EnchantArrow.ArrowEnchantFuse arrow = new EnchantArrow.ArrowEnchantFuse((Projectile) evt.getProjectile());
            Utilities.putArrow(evt.getProjectile(), arrow, (Player) evt.getEntity());
            return true;
        }

    }

    public static class Germination extends CustomEnchantment {

        private static final ItemStack BONE_MEAL = new ItemStack(Material.INK_SACK, 1, (short) 15);

        public Germination() {
            maxLevel = 3;
            loreName = "Germination";
            probability = 0;
            enchantable = new Tool[]{HOE};
            conflicting = new Class[]{};
            description = "Uses bonemeal from the player's inventory to grow nearby plants";
            cooldown = 0;
            power = 1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 19;
        }

        @Override
        public boolean onBlockInteract(PlayerInteractEvent evt, int level, boolean usedHand) {
            if (evt.getAction() != RIGHT_CLICK_BLOCK) {
                return false;
            }
            Player player = evt.getPlayer();
            Location loc = evt.getClickedBlock().getLocation();
            Block clickedBlock = evt.getClickedBlock();
            int radiusXZ = (int) Math.round(power * level + 2);
            int radiusY = 2;
            boolean applied = false;
            for (int x = -(radiusXZ); x <= radiusXZ; x++) {
                for (int y = -(radiusY) - 1; y <= radiusY - 1; y++) {
                    for (int z = -(radiusXZ); z <= radiusXZ; z++) {
                        Block relativeBlock = clickedBlock.getRelative(x, y, z);
                        if (relativeBlock.getLocation().distanceSquared(loc) < radiusXZ * radiusXZ
                                && player.getInventory().containsAtLeast(BONE_MEAL, 1)
                                && ADAPTER.grow(relativeBlock, player)) {
                            applied = true;
                            if (Storage.rnd.nextBoolean()) {
                                ADAPTER.grow(relativeBlock, player);
                            }
                            Utilities.display(Utilities.getCenter(relativeBlock), Particle.VILLAGER_HAPPY, 30, 1f, .3f, .3f, .3f);
                            if (Storage.rnd.nextInt(10) <= 3) {
                                Utilities.damageTool(player, 1, usedHand);
                            }
                            if (!player.getGameMode().equals(CREATIVE)) {
                                Utilities.removeItem(player, INK_SACK, (short) 15, 1);
                            }
                        }
                    }
                }
            }
            return applied;
        }
    }

    public static class Glide extends CustomEnchantment {

        public Glide() {
            maxLevel = 3;
            loreName = "Glide";
            probability = 0;
            enchantable = new Tool[]{LEGGINGS};
            conflicting = new Class[]{};
            description = "Gently brings the player back to the ground when sneaking";
            cooldown = 0;
            power = 1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 20;
        }

        @Override
        public boolean onFastScan(Player player, int level, boolean usedHand) {
            if (!Storage.sneakGlide.containsKey(player)) {
                Storage.sneakGlide.put(player, player.getLocation().getY());
            }
            if (!player.isSneaking() || Storage.sneakGlide.get(player) == player.getLocation().getY()) {
                return false;
            }
            boolean b = false;
            for (int i = -5; i < 0; i++) {
                if (player.getLocation().getBlock().getRelative(0, i, 0).getType() != AIR) {
                    b = true;
                }
            }
            if (player.getVelocity().getY() > -0.5) {
                b = true;
            }
            if (!b) {
                double sinPitch = Math.sin(Math.toRadians(player.getLocation().getPitch()));
                double cosPitch = Math.cos(Math.toRadians(player.getLocation().getPitch()));
                double sinYaw = Math.sin(Math.toRadians(player.getLocation().getYaw()));
                double cosYaw = Math.cos(Math.toRadians(player.getLocation().getYaw()));
                double y = -1 * (sinPitch);
                Vector v = new Vector(-cosPitch * sinYaw, 0, -1 * (-cosPitch * cosYaw));
                v.multiply(level * power / 2);
                v.setY(-1);
                player.setVelocity(v);
                player.setFallDistance((float) (6 - level * power) - 4);
                Location l = player.getLocation().clone();
                l.setY(l.getY() - 3);
                Utilities.display(l, Particle.CLOUD, 1, .1f, 0, 0, 0);
            }
            if (Storage.rnd.nextInt(5 * level) == 5) { // Slowly damage all armor
                ItemStack[] s = player.getInventory().getArmorContents();
                for (int i = 0; i < 4; i++) {
                    if (s[i] != null) {
                        Map<CustomEnchantment, Integer> map = Config.get(player.getWorld()).getEnchants(s[i]);
                        if (map.containsKey(this)) {
                            Utilities.addUnbreaking(player, s[i], 1);
                        }
                        if (s[i].getDurability() > s[i].getType().getMaxDurability()) {
                            s[i] = null;
                        }
                    }
                }
                player.getInventory().setArmorContents(s);
            }
            Storage.sneakGlide.put(player, player.getLocation().getY());
            return true;
        }

    }

    public static class Gluttony extends CustomEnchantment {

        private static final int[] FOOD_LEVELS = {10, 8, 8, 8, 6, 6, 6, 6, 6, 6, 5, 5, 4, 3, 2, 2, 1};
        private static final double[] SATURATIONS = {12.0, 12.8, 4.8, 12.8, 6.0, 7.2, 7.2, 9.6, 7.2, 6.0, 9.6, 6.0, 2.4, 3.6, 0.4, 1.2, 1.2};
        private static final Material[] FOOD_ITEMS = new Material[]{RABBIT_STEW, COOKED_BEEF, PUMPKIN_PIE,
            GRILLED_PORK, BAKED_POTATO, BEETROOT_SOUP, COOKED_CHICKEN, COOKED_MUTTON,
            MUSHROOM_SOUP, COOKED_FISH, COOKED_FISH, BREAD, APPLE, CARROT_ITEM, COOKIE,
            MELON, BEETROOT};

        public Gluttony() {
            maxLevel = 1;
            loreName = "Gluttony";
            probability = 0;
            enchantable = new Tool[]{HELMET};
            conflicting = new Class[]{};
            description = "Automatically eats for the player";
            cooldown = 0;
            power = -1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 21;
        }

        @Override
        public boolean onScan(Player player, int level, boolean usedHand) {
            int check = 0;
            for (int i = 0; i < FOOD_ITEMS.length; i++) {
                if (FOOD_ITEMS[i] == COOKED_FISH) {
                    check = (check + 1) % 2;
                }
                if (player.getInventory().containsAtLeast(new ItemStack(FOOD_ITEMS[i], 1, (short) check), 1)
                        && player.getFoodLevel() <= 20 - FOOD_LEVELS[i]) {
                    Utilities.removeItem(player, FOOD_ITEMS[i], (short) check, 1);
                    player.setFoodLevel(player.getFoodLevel() + FOOD_LEVELS[i]);
                    player.setSaturation((float) (player.getSaturation() + SATURATIONS[i]));
                    if (FOOD_ITEMS[i] == RABBIT_STEW || FOOD_ITEMS[i] == MUSHROOM_SOUP) {
                        player.getInventory().addItem(new ItemStack(BOWL));
                    }
                }
            }
            return true;
        }
    }

    public static class GoldRush extends CustomEnchantment {

        public GoldRush() {
            maxLevel = 3;
            loreName = "Gold Rush";
            probability = 0;
            enchantable = new Tool[]{SHOVEL};
            conflicting = new Class[]{};
            description = "Randomly drops gold nuggets when mining sand";
            cooldown = 0;
            power = 1.0;
            handUse = 1;
        }

        public int getEnchantmentId() {
            return 22;
        }

        @Override
        public boolean onBlockBreak(BlockBreakEvent evt, int level, boolean usedHand) {
            if (evt.getBlock().getType() == SAND && Storage.rnd.nextInt(100) >= (100 - (level * power * 3))) {
                evt.getBlock().getWorld().dropItemNaturally(Utilities.getCenter(evt.getBlock()), new ItemStack(GOLD_NUGGET));
                return true;
            }
            return false;
        }
    }

    public static class Grab extends CustomEnchantment {

        public Grab() {
            maxLevel = 1;
            loreName = "Grab";
            probability = 0;
            enchantable = new Tool[]{PICKAXE, SHOVEL, AXE};
            conflicting = new Class[]{};
            description = "Teleports mined items and XP directly to the player";
            cooldown = 0;
            power = -1.0;
            handUse = 1;
        }

        public int getEnchantmentId() {
            return 23;
        }

        @Override
        public boolean onBlockBreak(final BlockBreakEvent evt, int level, boolean usedHand) {
            Storage.grabLocs.put(evt.getBlock(), evt.getPlayer().getLocation());
            final Block block = evt.getBlock();
            ADAPTER.breakBlockNMS(evt.getBlock(), evt.getPlayer());
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Storage.zenchantments, () -> {
                Storage.grabLocs.remove(block);
            }, 15);
            return true;
        }
    }

    public static class GreenThumb extends CustomEnchantment {

        public GreenThumb() {
            maxLevel = 3;
            loreName = "Green Thumb";
            probability = 0;
            enchantable = new Tool[]{LEGGINGS};
            conflicting = new Class[]{};
            description = "Grows the foliage around the player";
            cooldown = 0;
            power = 1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 24;
        }

        @Override
        public boolean onScan(Player player, int level, boolean usedHand) {
            Location loc = player.getLocation().clone();
            Block centerBlock = (Block) loc.getBlock();
            int radius = (int) Math.round(power * level + 2);
            for (int x = -(radius); x <= radius; x++) {
                for (int y = -(radius) - 1; y <= radius - 1; y++) {
                    for (int z = -(radius); z <= radius; z++) {
                        Block relativeBlock = centerBlock.getRelative(x, y, z);
                        if (relativeBlock.getLocation().distance(loc) < radius) {
                            if (level != 10 && Storage.rnd.nextInt((int) (300 / (power * level / 2))) != 0) {
                                continue;
                            }
                            boolean applied = false;
                            switch (relativeBlock.getType()) {
                                case DIRT:
                                    if (relativeBlock.getData() != 2 && relativeBlock.getData() != 1
                                            && relativeBlock.getRelative(0, 1, 0).getType() == AIR) {
                                        byte data = 0;
                                        Material mat;
                                        switch (centerBlock.getBiome()) {
                                            case MUSHROOM_ISLAND:
                                            case MUSHROOM_ISLAND_SHORE:
                                                mat = MYCEL;
                                                break;
                                            case REDWOOD_TAIGA:
                                            case REDWOOD_TAIGA_HILLS:
                                            case TAIGA_COLD:
                                            case TAIGA_COLD_HILLS:
                                                data = (byte) 2;
                                            default:
                                                mat = GRASS;
                                        }
                                        applied = ADAPTER.placeBlock(relativeBlock, player, mat, data);
                                    }
                                    break;
                                default:
                                    applied = ADAPTER.grow(centerBlock.getRelative(x, y, z), player);
                                    break;
                            }
                            if (applied) { // Display particles and damage armor
                                Utilities.display(Utilities.getCenter(centerBlock.getRelative(x, y + 1, z)), Particle.VILLAGER_HAPPY, 20, 1f, .3f, .3f, .3f);
                                int chc = Storage.rnd.nextInt(50);
                                if (chc > 42 && level != 10) {
                                    ItemStack[] s = player.getInventory().getArmorContents();
                                    for (int i = 0; i < 4; i++) {
                                        if (s[i] != null) {
                                            Map<CustomEnchantment, Integer> map = Config.get(player.getWorld()).getEnchants(s[i]);
                                            if (map.containsKey(this)) {
                                                Utilities.addUnbreaking(player, s[i], 1);
                                            }
                                            if (s[i].getDurability() > s[i].getType().getMaxDurability()) {
                                                s[i] = null;
                                            }
                                        }
                                    }
                                    player.getInventory().setArmorContents(s);
                                }
                            }
                        }
                    }
                }
            }

            return true;
        }
    }

    public static class Gust extends CustomEnchantment {

        public Gust() {
            maxLevel = 1;
            loreName = "Gust";
            probability = 0;
            enchantable = new Tool[]{SWORD};
            conflicting = new Class[]{Force.class, RainbowSlam.class};
            description = "Pushes the user through the air at the cost of their health";
            cooldown = 0;
            power = 1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 25;
        }

        @Override
        public boolean onBlockInteract(final PlayerInteractEvent evt, int level, final boolean usedHand) {
            final Player player = evt.getPlayer();
            if (evt.getAction().equals(RIGHT_CLICK_AIR) || evt.getAction().equals(RIGHT_CLICK_BLOCK)) {
                if (player.getHealth() > 2 && (evt.getClickedBlock() == null || evt.getClickedBlock().getLocation().distance(player.getLocation()) > 2)) {
                    final Block blk = player.getTargetBlock((HashSet<Material>) null, 10);
                    player.setVelocity(blk.getLocation().toVector().subtract(player.getLocation().toVector()).multiply(.25 * power));
                    player.setFallDistance(-40);
                    ADAPTER.damagePlayer(player, 3, DamageCause.MAGIC);
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Storage.zenchantments, () -> {
                        Utilities.damageTool(evt.getPlayer(), 1, usedHand);
                    }, 1);
                    return true;
                }
            }
            return false;
        }
    }

    public static class Harvest extends CustomEnchantment {

        private static final Material[] CROP_BLOCKS = {CROPS, POTATO, CARROT, MELON_BLOCK, PUMPKIN, COCOA, BEETROOT_BLOCK, NETHER_WARTS};

        public Harvest() {
            maxLevel = 3;
            loreName = "Harvest";
            probability = 0;
            enchantable = new Tool[]{HOE};
            conflicting = new Class[]{};
            description = "Harvests fully grown crops within a radius when clicked";
            cooldown = 0;
            power = 1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 26;
        }

        @Override
        public boolean onBlockInteract(PlayerInteractEvent evt, int level, boolean usedHand) {
            if (evt.getAction() == RIGHT_CLICK_BLOCK) {
                Location loc = evt.getClickedBlock().getLocation();
                int radiusXZ = (int) Math.round(power * level + 2);
                int radiusY = 1;
                Player player = evt.getPlayer();
                if (ArrayUtils.contains(CROP_BLOCKS, evt.getClickedBlock().getType())) {
                    for (int x = -(radiusXZ); x <= radiusXZ; x++) {
                        for (int y = -(radiusY) - 1; y <= radiusY - 1; y++) {
                            for (int z = -(radiusXZ); z <= radiusXZ; z++) {
                                Block block = (Block) loc.getBlock();
                                if (block.getRelative(x, y, z).getLocation().distanceSquared(loc) < radiusXZ * radiusXZ) {
                                    if ((block.getRelative(x, y + 1, z).getType() == MELON_BLOCK || block.getRelative(x, y + 1, z).getType() == PUMPKIN)
                                            || ((block.getRelative(x, y + 1, z).getType() == NETHER_WARTS || block.getRelative(x, y + 1, z).getType() == BEETROOT_BLOCK) && block.getRelative(x, y + 1, z).getData() == 3)
                                            || ((block.getRelative(x, y + 1, z).getType() == CROPS || block.getRelative(x, y + 1, z).getType() == POTATO
                                            || (block.getRelative(x, y + 1, z).getType() == CARROT)) && block.getRelative(x, y + 1, z).getData() == 7)) {
                                        final Block blk = block.getRelative(x, y + 1, z);
                                        if (ADAPTER.breakBlockNMS(block.getRelative(x, y + 1, z), evt.getPlayer())) {
                                            Utilities.damageTool(player, 1, usedHand);
                                            Storage.grabLocs.put(block.getRelative(x, y + 1, z), evt.getPlayer().getLocation());
                                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Storage.zenchantments, () -> {
                                                Storage.grabLocs.remove(blk);
                                            }, 3);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    return true;
                }
            }
            return false;
        }
    }

    public static class Haste extends CustomEnchantment {

        public Haste() {
            maxLevel = 4;
            loreName = "Haste";
            probability = 0;
            enchantable = new Tool[]{PICKAXE, AXE, SHOVEL};
            conflicting = new Class[]{};
            description = "Gives the player a mining boost";
            cooldown = 0;
            power = 1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 27;
        }

        @Override
        public boolean onScanHands(Player player, int level, boolean usedHand) {
            Utilities.addPotion(player, FAST_DIGGING, 610, (int) Math.round(level * power));
            player.setMetadata("ze.haste", new FixedMetadataValue(Storage.zenchantments, null));
            return false;
        }

    }

    public static class IceAspect extends CustomEnchantment {

        public IceAspect() {
            maxLevel = 2;
            loreName = "Ice Aspect";
            probability = 0;
            enchantable = new Tool[]{SWORD};
            conflicting = new Class[]{};
            description = "Temporarily freezes the target";
            cooldown = 0;
            power = 1.0;
            handUse = 1;
        }

        public int getEnchantmentId() {
            return 29;
        }

        @Override
        public boolean onEntityHit(EntityDamageByEntityEvent evt, int level, boolean usedHand) {
            Utilities.addPotion((LivingEntity) evt.getEntity(), SLOW,
                    (int) Math.round(40 + level * power * 40), (int) Math.round(power * level * 2));
            Utilities.display(Utilities.getCenter(evt.getEntity().getLocation()), Particle.CLOUD, 10, .1f, 1f, 2f, 1f);
            return true;
        }
    }

    public static class Jump extends CustomEnchantment {

        public Jump() {
            maxLevel = 4;
            loreName = "Jump";
            probability = 0;
            enchantable = new Tool[]{BOOTS};
            conflicting = new Class[]{};
            description = "Gives the player a jump boost";
            cooldown = 0;
            power = 1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 30;
        }

        @Override
        public boolean onScan(Player player, int level, boolean usedHand) {
            Utilities.addPotion(player, JUMP, 610, (int) Math.round(level * power));
            return true;
        }
    }

    public static class Laser extends CustomEnchantment {

        public Laser() {
            maxLevel = 3;
            loreName = "Laser";
            probability = 0;
            enchantable = new Tool[]{PICKAXE, AXE};
            conflicting = new Class[]{};
            description = "Breaks blocks and damages mobs using a powerful beam of light";
            cooldown = 0;
            power = 1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 31;
        }

        public void shoot(Player player, int level, boolean usedHand) {
            EnchantPlayer.matchPlayer(player).setCooldown(33, 5); // Avoid recursing into Lumber enchant
            Block blk = player.getTargetBlock((HashSet<Material>) null, 6
                    + (int) Math.round(level * power * 3));
            Location playLoc = player.getLocation();
            Location target = Utilities.getCenter(blk.getLocation());
            target.setY(target.getY() + .5);
            Location c = playLoc;
            c.setY(c.getY() + 1.1);
            double d = target.distance(c);
            for (int i = 0; i < (int) d * 5; i++) {
                Location tempLoc = target.clone();
                tempLoc.setX(c.getX() + (i * ((target.getX() - c.getX()) / (d * 5))));
                tempLoc.setY(c.getY() + (i * ((target.getY() - c.getY()) / (d * 5))));
                tempLoc.setZ(c.getZ() + (i * ((target.getZ() - c.getZ()) / (d * 5))));
                player.getWorld().spawnParticle(Particle.REDSTONE, tempLoc.getX(), tempLoc.getY(), tempLoc.getZ(), 0, 255, 0, 0, 0);
                for (Entity ent : Bukkit.getWorld(playLoc.getWorld().getName()).getNearbyEntities(tempLoc, .3, .3, .3)) {
                    if (ent instanceof LivingEntity && ent != player) {
                        LivingEntity e = (LivingEntity) ent;
                        ADAPTER.attackEntity(e, player, 1 + (level + power * 2));
                    }
                }
            }
            if (ADAPTER.isBlockSafeToBreak(blk)) {
                ADAPTER.breakBlockNMS(blk, player);
            }
        }

        @Override
        public boolean onEntityInteract(PlayerInteractEntityEvent evt, int level, boolean usedHand) {
            if (usedHand && !evt.getPlayer().isSneaking()) {
                shoot(evt.getPlayer(), level, usedHand);
                return true;
            }
            return false;
        }

        @Override
        public boolean onBlockInteract(final PlayerInteractEvent evt, int level, boolean usedHand) {
            if (usedHand && !evt.getPlayer().isSneaking() && (evt.getAction() == RIGHT_CLICK_AIR || evt.getAction() == RIGHT_CLICK_BLOCK)) {
                shoot(evt.getPlayer(), level, usedHand);
                return true;
            }
            return false;
        }
    }

    public static class Level extends CustomEnchantment {

        public Level() {
            maxLevel = 3;
            loreName = "Level";
            probability = 0;
            enchantable = new Tool[]{PICKAXE, SWORD, BOW_};
            conflicting = new Class[]{};
            description = "Drops more XP when killing mobs or mining ores";
            cooldown = 0;
            power = 1.0;
            handUse = 3;
        }

        public int getEnchantmentId() {
            return 32;
        }

        @Override
        public boolean onEntityKill(EntityDeathEvent evt, int level, boolean usedHand) {
            if (Storage.rnd.nextBoolean()) {
                evt.setDroppedExp((int) (evt.getDroppedExp() * (1.3 + (level * power * .5))));
                return true;
            }
            return false;
        }

        @Override
        public boolean onBlockBreak(BlockBreakEvent evt, int level, boolean usedHand) {
            if (Storage.rnd.nextBoolean()) {
                evt.setExpToDrop((int) (evt.getExpToDrop() * (1.3 + (level * power * .5))));
                return true;
            }
            return false;
        }

        @Override
        public boolean onEntityShootBow(EntityShootBowEvent evt, int level, boolean usedHand) {
            if (Storage.rnd.nextBoolean()) {
                EnchantArrow.ArrowEnchantLevel arrow = new EnchantArrow.ArrowEnchantLevel((Projectile) evt.getProjectile(), level, power);
                Utilities.putArrow(evt.getProjectile(), arrow, (Player) evt.getEntity());
                return true;
            }
            return false;
        }

    }

    public static class LongCast extends CustomEnchantment {

        public LongCast() {
            maxLevel = 2;
            loreName = "Long Cast";
            probability = 0;
            enchantable = new Tool[]{ROD};
            conflicting = new Class[]{ShortCast.class};
            description = "Launches fishing hooks farther out when casting";
            cooldown = 0;
            power = 1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 33;
        }

        @Override
        public boolean onProjectileLaunch(ProjectileLaunchEvent evt, int level, boolean usedHand) {
            if (evt.getEntity().getType() == EntityType.FISHING_HOOK) {
                evt.getEntity().setVelocity(evt.getEntity().getVelocity().normalize().multiply(Math.min(1.9 + (power * level - 1.2), 2.7)));
            }
            return true;
        }
    }

    public static class Lumber extends CustomEnchantment {

        private static final int MAX_BLOCKS = 160;

        private static final Material[] ACCEPTED_NEARBY_BLOCKS = {
            LOG, LOG_2, LEAVES, LEAVES_2, DIRT, GRASS, VINE, SNOW, COCOA, AIR, RED_ROSE, YELLOW_FLOWER, LONG_GRASS, GRAVEL, STONE, DOUBLE_PLANT, WATER, STATIONARY_WATER,
            SAND, SAPLING, BROWN_MUSHROOM, RED_MUSHROOM, MOSSY_COBBLESTONE, CLAY, HUGE_MUSHROOM_1, HUGE_MUSHROOM_2, SUGAR_CANE_BLOCK, MYCEL, TORCH
        };

        private static final Material[] TRUNK_BLOCKS = {
            LOG, LOG_2, HUGE_MUSHROOM_1, HUGE_MUSHROOM_2
        };

        public Lumber() {
            maxLevel = 1;
            loreName = "Lumber";
            probability = 0;
            enchantable = new Tool[]{AXE};
            conflicting = new Class[]{};
            description = "Breaks the entire tree at once";
            cooldown = 0;
            power = -1.0;
            handUse = 1;
        }

        public int getEnchantmentId() {
            return 34;
        }

        @Override
        public boolean onBlockBreak(BlockBreakEvent evt, int level, boolean usedHand) {
            if (!evt.getPlayer().isSneaking()) {
                return false;
            }
            Block startBlock = evt.getBlock();
            if (!ArrayUtils.contains(TRUNK_BLOCKS, startBlock.getType())) {
                return false;
            }
            // BFS through the trunk, cancel if forbidden blocks are adjacent or search body becomes too large
            LinkedHashSet<Block> trunk = new LinkedHashSet<>();
            LinkedHashSet<Block> searchBody = new LinkedHashSet<>();
            List<Block> searchPerimeter = new ArrayList<>();
            searchBody.add(startBlock);
            searchPerimeter.add(startBlock);

            while (!searchPerimeter.isEmpty()) {
                Block searchBlock = searchPerimeter.remove(0);

                // If block is a trunk, add all adjacent blocks to search perimeter
                if (ArrayUtils.contains(TRUNK_BLOCKS, searchBlock.getType())) {
                    trunk.add(searchBlock);
                    for (int y = -1; y <= 1; y++) {
                        for (int x = -1; x <= 1; x++) {
                            for (int z = -1; z <= 1; z++) {
                                Block testBlock = searchBlock.getRelative(x, y, z);
                                if (!searchBody.contains(testBlock)) {
                                    if (ArrayUtils.contains(ACCEPTED_NEARBY_BLOCKS, searchBlock.getType())) {
                                        searchPerimeter.add(testBlock);
                                        searchBody.add(testBlock);
                                    } else {
                                        // Trunk is adjacent to a forbidden block. Nothing to do here
                                        return false;
                                    }
                                }
                            }
                        }
                    }

                    // If block is not a trunk, add only any nearby trunk blocks to search perimeter
                } else {
                    for (int y = -1; y <= 1; y++) {
                        for (int x = -1; x <= 1; x++) {
                            for (int z = -1; z <= 1; z++) {
                                Block testBlock = searchBlock.getRelative(x, y, z);
                                if (!searchBody.contains(testBlock) && ArrayUtils.contains(TRUNK_BLOCKS, searchBlock.getType())) {
                                    searchPerimeter.add(testBlock);
                                    searchBody.add(testBlock);
                                } else if (!ArrayUtils.contains(ACCEPTED_NEARBY_BLOCKS, testBlock.getType())) {
                                    // Trunk is adjacent to a forbidden block. Nothing to do here
                                    return false;
                                }
                            }
                        }
                    }
                }
                if (trunk.size() > MAX_BLOCKS) {
                    // Allowed trunk size exceeded
                    return false;
                }
            }
            for (Block b : trunk) {
                ADAPTER.breakBlockNMS(b, evt.getPlayer());
            }
            return true;
        }
    }

    public static class SonicShock extends CustomEnchantment {

        public SonicShock() {
            maxLevel = 3;
            loreName = "Sonic Shock";
            probability = 0;
            enchantable = new Tool[]{WINGS};
            conflicting = new Class[]{};
            description = "Damages mobs when flying past at high speed";
            cooldown = 0;
            power = 1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 56;
        }

        @Override
        public boolean onFastScan(Player player, int level, boolean usedHand) {
            if (player.isGliding() && player.getVelocity().length() >= 1) {
                for (Entity e : player.getNearbyEntities(2 + level, 3, 2 + level)) {
                    double damage = player.getVelocity().length() * 1.5 * level;
                    if (e instanceof Monster) {
                        ADAPTER.attackEntity((LivingEntity) e, player, power * damage);
                    }
                }
            }
            return true;
        }
    }

    public static class Magnetism extends CustomEnchantment {

        public Magnetism() {
            maxLevel = 3;
            loreName = "Magnetism";
            probability = 0;
            enchantable = new Tool[]{LEGGINGS};
            conflicting = new Class[]{};
            description = "Slowly attracts nearby items to the players inventory";
            cooldown = 0;
            power = 1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 35;
        }

        @Override
        public boolean onFastScan(Player player, int level, boolean usedHand) {
            int radius = (int) Math.round(power * level * 2 + 3);
            for (Entity e : player.getNearbyEntities(radius, radius, radius)) {
                if (e.getType().equals(DROPPED_ITEM) && e.getTicksLived() > 160) {
                    e.setVelocity(player.getLocation().toVector().subtract(e.getLocation().toVector()).multiply(.05));
                }
            }
            return true;
        }
    }

    public static class Meador extends CustomEnchantment {

        public Meador() {
            maxLevel = 1;
            loreName = "Meador";
            probability = 0;
            enchantable = new Tool[]{BOOTS};
            conflicting = new Class[]{Weight.class, Speed.class, Jump.class};
            description = "Gives the player both a speed and jump boost";
            cooldown = 0;
            power = 1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 36;
        }

        @Override
        public boolean onScan(Player player, int level, boolean usedHand) {
            player.setWalkSpeed((float) Math.min(.5f + level * power * .05f, 1));
            player.setFlySpeed((float) Math.min(.5f + level * power * .05f, 1));
            player.setMetadata("ze.speed", new FixedMetadataValue(Storage.zenchantments, true));
            Utilities.addPotion(player, JUMP, 610, (int) Math.round(power * level + 2));
            return true;
        }
    }

    public static class Mow extends CustomEnchantment {

        public Mow() {
            maxLevel = 3;
            loreName = "Mow";
            probability = 0;
            enchantable = new Tool[]{SHEAR};
            conflicting = new Class[]{};
            description = "Shears all nearby sheep";
            cooldown = 0;
            power = 1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 37;
        }

        private boolean shear(PlayerEvent evt, int level, boolean usedHand) {
            boolean hasSheep = false;
            int radius = (int) Math.round(level * power + 2);
            Player player = evt.getPlayer();
            for (Entity ent : evt.getPlayer().getNearbyEntities(radius, radius, radius)) {
                if (ent instanceof Sheep) {
                    Sheep sheep = (Sheep) ent;
                    if (sheep.isAdult()) {
                        ADAPTER.shearEntityNMS(sheep, player, usedHand);
                    }
                } else if (ent instanceof MushroomCow) {
                    MushroomCow mCow = (MushroomCow) ent;
                    if (mCow.isAdult()) {
                        ADAPTER.shearEntityNMS(mCow, player, usedHand);
                    }
                }
            }
            return hasSheep;
        }

        @Override
        public boolean onBlockInteract(PlayerInteractEvent evt, int level, boolean usedHand) {
            if (evt.getAction() == RIGHT_CLICK_AIR || evt.getAction() == RIGHT_CLICK_BLOCK) {
                shear(evt, level, usedHand);
            }
            return false;
        }

        @Override
        public boolean onShear(PlayerShearEntityEvent evt, int level, boolean usedHand) {
            shear(evt, level, usedHand);
            return false;
        }
    }

    public static class MysteryFish extends CustomEnchantment {

        public MysteryFish() {
            maxLevel = 3;
            loreName = "Mystery Fish";
            probability = 0;
            enchantable = new Tool[]{ROD};
            conflicting = new Class[]{};
            description = "Catches water mobs like Squid and Guardians";
            cooldown = 0;
            power = 1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 38;
        }

        @Override
        public boolean onPlayerFish(final PlayerFishEvent evt, int level, boolean usedHand) {
            if (Storage.rnd.nextInt(10) < level * power) {
                if (evt.getCaught() != null) {
                    Location location = evt.getCaught().getLocation();
                    final Entity ent;
                    if (Storage.rnd.nextBoolean()) {
                        ent = evt.getPlayer().getWorld().spawnEntity(location, SQUID);
                    } else {
                        Entity g = Storage.COMPATIBILITY_ADAPTER.spawnGuardian(location, Storage.rnd.nextBoolean());
                        Storage.guardianMove.put(g, evt.getPlayer());
                        ent = g;
                    }
                    evt.getCaught().setPassenger(ent);
                }
            }
            return true;
        }
    }

    public static class NetherStep extends CustomEnchantment {

        public NetherStep() {
            maxLevel = 3;
            loreName = "Nether Step";
            probability = 0;
            enchantable = new Tool[]{BOOTS};
            conflicting = new Class[]{FrozenStep.class};
            description = "Allows the player to slowly but safely walk on lava";
            cooldown = 0;
            power = 1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 39;
        }

        @Override
        public boolean onScan(Player player, int level, boolean usedHand) {
            if (player.isSneaking() && player.getLocation().getBlock().getType() == STATIONARY_LAVA && !player.isFlying()) {
                player.setVelocity(player.getVelocity().setY(.4));
            }
            Block block = (Block) player.getLocation().add(0, 0.2, 0).getBlock();
            int radius = (int) Math.round(power * level + 2);
            for (int x = -(radius); x <= radius; x++) {
                for (int z = -(radius); z <= radius; z++) {
                    Block possiblePlatformBlock = block.getRelative(x, -1, z);
                    Location possiblePlatformLoc = possiblePlatformBlock.getLocation();
                    if (possiblePlatformLoc.distanceSquared(block.getLocation()) < radius * radius - 2) {
                        if (Storage.fireLocs.containsKey(possiblePlatformLoc)) {
                            Storage.fireLocs.put(possiblePlatformLoc, System.nanoTime());
                        } else if (possiblePlatformBlock.getType() == STATIONARY_LAVA
                                && possiblePlatformBlock.getData() == 0
                                && possiblePlatformBlock.getRelative(0, 1, 0).getType() == AIR) {
                            if (ADAPTER.formBlock(possiblePlatformBlock, SOUL_SAND, (byte) 0, player)) {
                                Storage.fireLocs.put(possiblePlatformLoc, System.nanoTime());
                            }
                        }
                    }
                }
            }
            return true;
        }
    }

    public static class NightVision extends CustomEnchantment {

        public NightVision() {
            maxLevel = 1;
            loreName = "Night Vision";
            probability = 0;
            enchantable = new Tool[]{HELMET};
            conflicting = new Class[]{};
            description = "Lets the player see in the darkness";
            cooldown = 0;
            power = -1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 40;
        }

        @Override
        public boolean onScan(Player player, int level, boolean usedHand) {
            Utilities.addPotion(player, NIGHT_VISION, 610, 5);
            return true;
        }
    }

    public static class Persephone extends CustomEnchantment {

        private static final Material[] CROP_BLOCKS = {CROPS, POTATO, CARROT, BEETROOT_BLOCK, NETHER_WARTS, SOUL_SAND, SOIL};

        public Persephone() {
            maxLevel = 3;
            loreName = "Persephone";
            probability = 0;
            enchantable = new Tool[]{HOE};
            conflicting = new Class[]{};
            description = "Plants seeds from the player's inventory around them";
            cooldown = 0;
            power = 1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 41;
        }

        @Override
        public boolean onBlockInteract(PlayerInteractEvent evt, int level, boolean usedHand) {
            if (evt.getAction() == RIGHT_CLICK_BLOCK) {
                Player player = evt.getPlayer();
                Location loc = evt.getClickedBlock().getLocation();
                int radiusXZ = (int) Math.round(power * level + 2);

                if (ArrayUtils.contains(CROP_BLOCKS, evt.getClickedBlock().getType())) {
                    Block block = (Block) loc.getBlock();
                    for (int x = -(radiusXZ); x <= radiusXZ; x++) {
                        for (int y = -2; y <= 0; y++) {
                            for (int z = -(radiusXZ); z <= radiusXZ; z++) {

                                if (block.getRelative(x, y, z).getLocation().distanceSquared(loc) < radiusXZ * radiusXZ) {
                                    if (block.getRelative(x, y, z).getType() == SOIL
                                            && block.getRelative(x, y + 1, z).getType() == AIR) {
                                        if (evt.getPlayer().getInventory().contains(CARROT_ITEM)) {
                                            if (ADAPTER.placeBlock(block.getRelative(x, y + 1, z), player, CARROT, 0)) {
                                                Utilities.removeItem(player, CARROT_ITEM, (short) 0, 1);
                                            }
                                        }
                                        if (evt.getPlayer().getInventory().contains(POTATO_ITEM)) {
                                            if (ADAPTER.placeBlock(block.getRelative(x, y + 1, z), player, POTATO, 0)) {
                                                Utilities.removeItem(player, POTATO_ITEM, (short) 0, 1);
                                            }
                                        }
                                        if (evt.getPlayer().getInventory().contains(SEEDS)) {
                                            if (ADAPTER.placeBlock(block.getRelative(x, y + 1, z), player, CROPS, 0)) {
                                                Utilities.removeItem(player, SEEDS, (short) 0, 1);
                                            }
                                        }
                                        if (evt.getPlayer().getInventory().contains(BEETROOT_SEEDS)) {
                                            if (ADAPTER.placeBlock(block.getRelative(x, y + 1, z), player, Material.BEETROOT_BLOCK, 0)) {
                                                Utilities.removeItem(player, BEETROOT_SEEDS, (short) 0, 1);
                                            }
                                        }
                                    } else if (block.getRelative(x, y, z).getType() == SOUL_SAND
                                            && block.getRelative(x, y + 1, z).getType() == AIR) {
                                        if (evt.getPlayer().getInventory().contains(NETHER_STALK)) {
                                            if (ADAPTER.placeBlock(block.getRelative(x, y + 1, z), player, NETHER_WARTS, 0)) {
                                                Utilities.removeItem(player, NETHER_STALK, (short) 0, 1);
                                            }
                                        }
                                    } else {
                                        continue;
                                    }
                                    if (Storage.rnd.nextBoolean()) {
                                        Utilities.damageTool(evt.getPlayer(), 1, usedHand);
                                    }
                                }
                            }
                        }
                    }
                    return true;
                }
            }
            return false;
        }
    }

    public static class Pierce extends CustomEnchantment {

        public Pierce() {
            maxLevel = 1;
            loreName = "Pierce";
            probability = 0;
            enchantable = new Tool[]{PICKAXE};
            conflicting = new Class[]{Anthropomorphism.class, Switch.class, Shred.class};
            description = "Lets the player mine in several modes which can be changed through shift clicking";
            cooldown = 0;
            power = -1.0;
            handUse = 3;
        }

        public int getEnchantmentId() {
            return 42;
        }

        @Override
        public boolean onBlockInteract(PlayerInteractEvent evt, int level, boolean usedHand) {
            Player player = evt.getPlayer();
            if (!evt.getPlayer().hasMetadata("ze.pierce.mode")) {
                player.setMetadata("ze.pierce.mode", new FixedMetadataValue(Storage.zenchantments, 1));
            }
            if (player.isSneaking() && (evt.getAction() == RIGHT_CLICK_AIR || evt.getAction() == RIGHT_CLICK_BLOCK)) {
                int b = player.getMetadata("ze.pierce.mode").get(0).asInt();
                b = b == 5 ? 1 : b + 1;
                player.setMetadata("ze.pierce.mode", new FixedMetadataValue(Storage.zenchantments, b));
                switch (b) {
                    case 1:
                        player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "1x Normal Mode");
                        break;
                    case 2:
                        player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "3x Wide Mode");
                        break;
                    case 3:
                        player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "3x Long Mode");
                        break;
                    case 4:
                        player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "3x Tall Mode");
                        break;
                    case 5:
                        player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Ore Mode");
                        break;
                }
            }
            return false;
        }

        @Override
        public boolean onBlockBreak(final BlockBreakEvent evt, int level, boolean usedHand) {
            //1 = normal; 2 = wide; 3 = deep; 4 = tall; 5 = ore
            Player player = evt.getPlayer();
            if (!evt.getPlayer().hasMetadata("ze.pierce.mode")) {
                player.setMetadata("ze.pierce.mode", new FixedMetadataValue(Storage.zenchantments, 1));
            }
            final int mode = player.getMetadata("ze.pierce.mode").get(0).asInt();
            List<Block> total = new ArrayList<>();
            final Location blkLoc = evt.getBlock().getLocation();
            if (mode != 1 && mode != 5) {
                int add = -1;
                boolean b = false;
                int[][] ints = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
                switch (Utilities.getCardinalDirection(evt.getPlayer().getLocation().getYaw(), 0)) {
                    case SOUTH:
                        ints = new int[][]{{1, 0, 0}, {0, 0, 1}, {0, 1, 0}};
                        add = 1;
                        b = true;
                        break;
                    case WEST:
                        ints = new int[][]{{0, 0, 1}, {1, 0, 0}, {0, 1, 0}};
                        break;
                    case NORTH:
                        ints = new int[][]{{1, 0, 0}, {0, 0, 1}, {0, 1, 0}};
                        b = true;
                        break;
                    case EAST:
                        ints = new int[][]{{0, 0, 1}, {1, 0, 0}, {0, 1, 0}};
                        add = 1;
                        break;
                }
                int[] rads = ints[mode - 2];
                if (mode == 3) {
                    if (b) {
                        blkLoc.setZ(blkLoc.getZ() + add);
                    } else {
                        blkLoc.setX(blkLoc.getX() + add);
                    }
                }
                if (mode == 4) {
                    if (evt.getPlayer().getLocation().getPitch() > 65) {
                        blkLoc.setY(blkLoc.getY() - 1);
                    } else if (evt.getPlayer().getLocation().getPitch() < -65) {
                        blkLoc.setY(blkLoc.getY() + 1);
                    }
                }
                for (int x = -(rads[0]); x <= rads[0]; x++) {
                    for (int y = -(rads[1]); y <= rads[1]; y++) {
                        for (int z = -(rads[2]); z <= rads[2]; z++) {
                            total.add(blkLoc.getBlock().getRelative(x, y, z));
                        }
                    }
                }
            } else if (mode == 5) {
                List<Block> used = new ArrayList<>();
                if (ArrayUtils.contains(Storage.ORES, evt.getBlock().getType())) {
                    Material mat[];
                    if (evt.getBlock().getType() != REDSTONE_ORE && evt.getBlock().getType() != GLOWING_REDSTONE_ORE) {
                        mat = new Material[]{evt.getBlock().getType()};
                    } else {
                        mat = new Material[]{REDSTONE_ORE, GLOWING_REDSTONE_ORE};
                    }
                    oreBFS(evt.getBlock(), used, total, mat, 0);
                } else {
                    return false;
                }
            }
            if (total.size() < 128) {
                for (Block b : total) {
                    if (ADAPTER.isBlockSafeToBreak(b)) {
                        ADAPTER.breakBlockNMS(b, evt.getPlayer());
                    }
                }
            }
            return true;
        }

        private void oreBFS(Block blk, List<Block> bks, List<Block> total, Material[] mat, int i) {
            i++;
            if (i >= 128 || total.size() >= 128) {
                return;
            }
            bks.add(blk);
            Location loc = blk.getLocation().clone();
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        Location loc2 = loc.clone();
                        loc2.setX(loc2.getX() + x);
                        loc2.setY(loc2.getY() + y);
                        loc2.setZ(loc2.getZ() + z);
                        if (!bks.contains(loc2.getBlock()) && (ArrayUtils.contains(mat, loc2.getBlock().getType()))) {
                            oreBFS(loc2.getBlock(), bks, total, mat, i);
                            total.add(loc2.getBlock());
                        }
                    }
                }
            }
        }
    }

    public static class Plough extends CustomEnchantment {

        public Plough() {
            maxLevel = 3;
            loreName = "Plough";
            probability = 0;
            enchantable = new Tool[]{HOE};
            conflicting = new Class[]{};
            description = "Tills all soil within a radius";
            cooldown = 0;
            power = 1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 43;
        }

        @Override
        public boolean onBlockInteract(PlayerInteractEvent evt, int level, boolean usedHand) {
            if (evt.getAction() == RIGHT_CLICK_BLOCK) {
                Location loc = evt.getClickedBlock().getLocation();
                int radiusXZ = (int) Math.round(power * level + 2);
                int radiusY = 1;
                for (int x = -(radiusXZ); x <= radiusXZ; x++) {
                    for (int y = -(radiusY) - 1; y <= radiusY - 1; y++) {
                        for (int z = -(radiusXZ); z <= radiusXZ; z++) {
                            Block block = (Block) loc.getBlock();
                            if (block.getRelative(x, y, z).getLocation().distanceSquared(loc) < radiusXZ * radiusXZ) {
                                if (((block.getRelative(x, y, z).getType() == DIRT
                                        || block.getRelative(x, y, z).getType() == GRASS
                                        || block.getRelative(x, y, z).getType() == MYCEL))
                                        && block.getRelative(x, y + 1, z).getType() == AIR) {
                                    ADAPTER.placeBlock(block.getRelative(x, y, z), evt.getPlayer(), SOIL, 0);
                                    if (Storage.rnd.nextBoolean()) {
                                        Utilities.damageTool(evt.getPlayer(), 1, usedHand);
                                    }
                                }
                            }
                        }
                    }
                }
                return true;
            }
            return false;
        }
    }

    public static class Potion extends CustomEnchantment {

        PotionEffectType[] potions;

        public Potion() {
            maxLevel = 3;
            loreName = "Potion";
            probability = 0;
            enchantable = new Tool[]{BOW_};
            conflicting = new Class[]{};
            description = "Gives the shooter random positive potion effects when attacking";
            cooldown = 0;
            power = 1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 44;
        }

        @Override
        public boolean onEntityShootBow(EntityShootBowEvent evt, int level, boolean usedHand) {
            EnchantArrow.ArrowEnchantPotion arrow = new EnchantArrow.ArrowEnchantPotion((Projectile) evt.getProjectile(), level, power);
            Utilities.putArrow(evt.getProjectile(), arrow, (Player) evt.getEntity());
            return true;
        }
    }

    public static class PotionResistance extends CustomEnchantment {

        public PotionResistance() {
            maxLevel = 4;
            loreName = "Potion Resistance";
            probability = 0;
            enchantable = new Tool[]{HELMET, CHESTPLATE, LEGGINGS, BOOTS};
            conflicting = new Class[]{};
            description = "Lessens the effects of all potions on players";
            cooldown = 0;
            power = 1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 45;
        }

        @Override
        public boolean onPotionSplash(PotionSplashEvent evt, int level, boolean usedHand) {
            for (LivingEntity ent : evt.getAffectedEntities()) {
                if (ent instanceof Player) {
                    int effect = 0;
                    for (ItemStack stk : ((Player) ent).getInventory().getArmorContents()) {
                        Map<CustomEnchantment, Integer> map = Config.get(ent.getWorld()).getEnchants(stk);
                        for (CustomEnchantment e : map.keySet()) {
                            if (e.equals(this)) {
                                effect += map.get(e);
                            }
                        }
                    }
                    evt.setIntensity(ent, evt.getIntensity(ent) / ((effect * power + 1.3) / 2));
                }
            }
            return true;
        }
    }

    public static class QuickShot extends CustomEnchantment {

        public QuickShot() {
            maxLevel = 1;
            loreName = "Quick Shot";
            probability = 0;
            enchantable = new Tool[]{BOW_};
            conflicting = new Class[]{};
            description = "Shoots arrows at full speed, instantly";
            cooldown = 0;
            power = 1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 46;
        }

        @Override
        public boolean onEntityShootBow(EntityShootBowEvent evt, int level, boolean usedHand) {
            EnchantArrow.ArrowEnchantQuickShot arrow = new EnchantArrow.ArrowEnchantQuickShot((Projectile) evt.getProjectile());
            Utilities.putArrow(evt.getProjectile(), arrow, (Player) evt.getEntity());
            return true;
        }
    }

    public static class Rainbow extends CustomEnchantment {

        private static final short[] FLOWER_DATA_VALUES = new short[]{0, 1, 2, 3, 4, 5, 10};

        public Rainbow() {
            maxLevel = 1;
            loreName = "Rainbow";
            probability = 0;
            enchantable = new Tool[]{SHEAR};
            conflicting = new Class[]{};
            description = "Drops random flowers and wool colors when used";
            cooldown = 0;
            power = -1.0;
            handUse = 3;
        }

        public int getEnchantmentId() {
            return 47;
        }

        @Override
        public boolean onBlockBreak(BlockBreakEvent evt, int level, boolean usedHand) {
            short blockData;
            Material dropMaterial;
            if (evt.getBlock().getType() == RED_ROSE || evt.getBlock().getType() == YELLOW_FLOWER) {
                short sh = (short) Storage.rnd.nextInt(9);
                dropMaterial = (sh == 7) ? YELLOW_FLOWER : RED_ROSE;
                blockData = (sh == 7) ? 0 : (short) Storage.rnd.nextInt(9);
            } else if (evt.getBlock().getType() == DOUBLE_PLANT
                    && (ArrayUtils.contains(FLOWER_DATA_VALUES, evt.getBlock().getData()))) {
                dropMaterial = DOUBLE_PLANT;
                blockData = (short) Storage.rnd.nextInt(6);
                blockData = FLOWER_DATA_VALUES[blockData];
            } else {
                return false;
            }
            evt.setCancelled(true);
            if (evt.getBlock().getRelative(DOWN).getType() == DOUBLE_PLANT) {

                evt.getBlock().getRelative(DOWN).setType(AIR);
            }
            evt.getBlock().setType(AIR);
            Utilities.damageTool(evt.getPlayer(), 1, usedHand);
            evt.getPlayer().getWorld().dropItem(Utilities.getCenter(evt.getBlock()), new ItemStack(dropMaterial, 1, blockData));
            return true;
        }

        @Override
        public boolean onShear(PlayerShearEntityEvent evt, int level, boolean usedHand) {
            Sheep sheep = (Sheep) evt.getEntity();
            if (!sheep.isSheared()) {
                int color = Storage.rnd.nextInt(16);
                int number = Storage.rnd.nextInt(3) + 1;
                Utilities.damageTool(evt.getPlayer(), 1, usedHand);
                evt.setCancelled(true);
                sheep.setSheared(true);
                evt.getEntity().getWorld().dropItemNaturally(Utilities.getCenter(evt.getEntity().getLocation()), new ItemStack(WOOL, number, (short) color));
            }
            return true;
        }
    }

    public static class RainbowSlam extends CustomEnchantment {

        public RainbowSlam() {
            maxLevel = 4;
            loreName = "Rainbow Slam";
            probability = 0;
            enchantable = new Tool[]{SWORD};
            conflicting = new Class[]{Force.class, Gust.class};
            description = "Attacks enemy mobs with a powerful swirling slam";
            cooldown = 0;
            power = 1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 48;
        }

        @Override
        public boolean onEntityInteract(final PlayerInteractEntityEvent evt, final int level, boolean usedHand) {
            if (!(evt.getRightClicked() instanceof LivingEntity) || !ADAPTER.attackEntity((LivingEntity) evt.getRightClicked(), evt.getPlayer(), 0)) {
                return false;
            }
            Utilities.damageTool(evt.getPlayer(), 9, usedHand);
            final LivingEntity ent = (LivingEntity) evt.getRightClicked();
            final Location l = ent.getLocation().clone();
            ent.teleport(l);
            for (int i = 0; i < 30; i++) {
                final int fI = i;
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Storage.zenchantments, () -> {
                    for (int j = 0; j < 40; j++) {
                        if (ent.isDead()) {
                            return;
                        }
                        Location loc = l.clone();
                        float t = 30 * fI + j;
                        loc.setY(loc.getY() + (t / 100));
                        loc.setX(loc.getX() + Math.sin(Math.toRadians(t)) * t / 330);
                        loc.setZ(loc.getZ() + Math.cos(Math.toRadians(t)) * t / 330);
                        Utilities.display(loc, Particle.REDSTONE, 1, 10f, 0, 0, 0);
                        loc.setY(loc.getY() + 1.3);
                        ent.setVelocity(loc.toVector().subtract(ent.getLocation().toVector()));
                    }
                }, i);
            }
            AtomicBoolean applied = new AtomicBoolean(false);
            Storage.rainbowSlamNoFallEntities.add(ent);
            for (int i = 0; i < 3; i++) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Storage.zenchantments, () -> {
                    //ent.setNoDamageTicks(20); // Prevent fall damage
                    ent.setVelocity(l.toVector().subtract(ent.getLocation().toVector()).multiply(.3));
                    ent.setFallDistance(0);
                    if (ent.isOnGround() && !applied.get()) {
                        applied.set(true);
                        Storage.rainbowSlamNoFallEntities.remove(ent);
                        ADAPTER.attackEntity(ent, evt.getPlayer(), level * power);
                        for (int c = 0; c < 1000; c++) {
                            Vector v = new Vector(Math.sin(Math.toRadians(c)), Storage.rnd.nextFloat(), Math.cos(Math.toRadians(c))).multiply(.75);
                            Utilities.display(Utilities.getCenter(l), Particle.BLOCK_DUST, 1, (float) v.length(), 0, 0, 0);
                        }
                    }
                }, 35 + (i * 5));
            }
            return true;
        }
    }

    public static class Reaper extends CustomEnchantment {

        public Reaper() {
            maxLevel = 4;
            loreName = "Reaper";
            probability = 0;
            enchantable = new Tool[]{BOW_, SWORD};
            conflicting = new Class[]{};
            description = "Gives the target temporary wither effect and blindness";
            cooldown = 0;
            power = 1.0;
            handUse = 3;
        }

        public int getEnchantmentId() {
            return 49;
        }

        @Override
        public boolean onEntityShootBow(EntityShootBowEvent evt, int level, boolean usedHand) {
            EnchantArrow.ArrowEnchantReaper arrow = new EnchantArrow.ArrowEnchantReaper((Projectile) evt.getProjectile(), level, power);
            Utilities.putArrow(evt.getProjectile(), arrow, (Player) evt.getEntity());
            return true;
        }

        @Override
        public boolean onEntityHit(EntityDamageByEntityEvent evt, int level, boolean usedHand) {
            if (evt.getEntity() instanceof LivingEntity && ADAPTER.attackEntity((LivingEntity) evt.getEntity(), (Player) evt.getDamager(), 0)) {
                int pow = (int) Math.round(level * power);
                int dur = (int) Math.round(10 + level * 20 * power);
                Utilities.addPotion((LivingEntity) evt.getEntity(), PotionEffectType.WITHER, dur, pow);
                Utilities.addPotion((LivingEntity) evt.getEntity(), BLINDNESS, dur, pow);
            }
            return true;
        }
    }

    public static class Reveal extends CustomEnchantment {

        public Reveal() {
            maxLevel = 4;
            loreName = "Reveal";
            probability = 0;
            enchantable = new Tool[]{PICKAXE};
            conflicting = new Class[]{Switch.class, Pierce.class, Spectral.class};
            description = "Makes nearby ores glow white through the stone.";
            cooldown = 100;
            power = 1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 68;
        }

        @Override
        public boolean onBlockInteract(final PlayerInteractEvent evt, int level, boolean usedHand) {
            Player player = evt.getPlayer();
            if (evt.getAction() == Action.RIGHT_CLICK_AIR || evt.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (evt.getPlayer().isSneaking()) {
                    int radius = (int) Math.max(2, Math.round((2 + level) * power));
                    int found = 0;
                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            for (int z = -radius; z <= radius; z++) {
                                Block blk = evt.getPlayer().getLocation().getBlock().getRelative(x, y, z);
                                if (ArrayUtils.contains(Storage.ORES, blk.getType())) {
                                    boolean exposed = false;
                                    for (BlockFace face : Storage.CARDINAL_BLOCK_FACES) {
                                        if (blk.getRelative(face).getType() == Material.AIR) {
                                            exposed = true;
                                        }
                                    }
                                    if (exposed) {
                                        continue;
                                    }

                                    found++;
                                    int entityId = 2000000000 + (blk.hashCode()) % 10000000;
                                    if (Storage.glowingBlocks.containsKey(blk)) {
                                        Storage.glowingBlocks.put(blk,
                                                Storage.glowingBlocks.get(blk) + 1);
                                    } else {
                                        Storage.glowingBlocks.put(blk, 1);
                                    }

                                    if (!ADAPTER.showShulker(blk, entityId, player)) {
                                        return false;
                                    }
                                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Storage.zenchantments, () -> {
                                        ADAPTER.hideShulker(entityId, player);
                                        if (Storage.glowingBlocks.containsKey(blk)
                                                && Storage.glowingBlocks.get(blk) > 1) {
                                            Storage.glowingBlocks.put(blk,
                                                    Storage.glowingBlocks.get(blk) - 1);
                                        } else {
                                            Storage.glowingBlocks.remove(blk);
                                        }
                                    }, 100);
                                }
                            }
                        }
                    }
                    Utilities.damageTool(evt.getPlayer(), Math.max(16, (int) Math.round(found * 1.3)), usedHand);
                    return true;
                }
            }
            return false;
        }

    }

    public static class Saturation extends CustomEnchantment {

        public Saturation() {
            maxLevel = 3;
            loreName = "Saturation";
            probability = 0;
            enchantable = new Tool[]{LEGGINGS};
            conflicting = new Class[]{};
            description = "Uses less of the player's hunger";
            cooldown = 0;
            power = 1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 50;
        }

        @Override
        public boolean onHungerChange(FoodLevelChangeEvent evt, int level, boolean usedHand) {
            if (evt.getFoodLevel() < ((Player) evt.getEntity()).getFoodLevel() && Storage.rnd.nextInt(10) > 10 - 2 * level * power) {
                evt.setCancelled(true);
            }
            return true;
        }
    }

    public static class ShortCast extends CustomEnchantment {

        public ShortCast() {
            maxLevel = 2;
            loreName = "Short Cast";
            probability = 0;
            enchantable = new Tool[]{ROD};
            conflicting = new Class[]{LongCast.class};
            description = "Launches fishing hooks closer in when casting";
            cooldown = 0;
            power = 1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 51;
        }

        @Override
        public boolean onProjectileLaunch(ProjectileLaunchEvent evt, int level, boolean usedHand) {
            if (evt.getEntity().getType() == EntityType.FISHING_HOOK) {
                evt.getEntity().setVelocity(evt.getEntity().getVelocity().normalize().multiply((.8f / (level * power))));
            }
            return true;
        }
    }

    public static class Shred extends CustomEnchantment {

        private static final Material[] ALLOWED_MATERIALS = new Material[]{STONE, COAL_ORE, REDSTONE_ORE, DIAMOND_ORE, GOLD_ORE, IRON_ORE,
            NETHERRACK, LAPIS_ORE, GLOWSTONE, QUARTZ_ORE, EMERALD_ORE, GRASS, SOUL_SAND, GLOWING_REDSTONE_ORE,
            DIRT, MYCEL, SAND, GRAVEL, SOUL_SAND, CLAY, HARD_CLAY, STAINED_CLAY, SANDSTONE, RED_SANDSTONE, ICE, PACKED_ICE};

        private static final Material SHOVELABLE_MATERIALS[] = new Material[]{GLOWSTONE, GRASS, DIRT, MYCEL, SOUL_SAND, SAND, GRAVEL, SOUL_SAND, CLAY};

        public Shred() {
            maxLevel = 5;
            loreName = "Shred";
            probability = 0;
            enchantable = new Tool[]{SHOVEL, PICKAXE};
            conflicting = new Class[]{Pierce.class, Switch.class};
            description = "Breaks the blocks within a radius of the original block mined";
            cooldown = 0;
            power = -1.0;
            handUse = 1;
        }

        public int getEnchantmentId() {
            return 52;
        }

        @Override
        public boolean onBlockBreak(BlockBreakEvent evt, int level, boolean usedHand) {
            if (evt.getBlock().getType() != AIR && !ArrayUtils.contains(ALLOWED_MATERIALS, evt.getBlock().getType())) {
                return false;
            }
            ItemStack hand = Utilities.usedStack(evt.getPlayer(), usedHand);
            final Config config = Config.get(evt.getBlock().getWorld());
            Set<Block> broken = new HashSet<>();
            blocks(evt.getBlock(), evt.getBlock(), new int[]{level + 3, level + 3, level + 3},
                    0, 4.6 + (level * .22), broken, evt.getPlayer(), config, hand.getType(), usedHand);
            return true;
        }

        public void blocks(Block centerBlock, final Block relativeBlock, int[] coords, int time, double size, Set<Block> used,
                final Player player, final Config config, final Material itemType, boolean usedHand) {
            if (relativeBlock.getType() != AIR && !used.contains(relativeBlock)) {
                final Material originalType = relativeBlock.getType();
                if (!ArrayUtils.contains(ALLOWED_MATERIALS, relativeBlock.getType())
                        || (Tool.SHOVEL.contains(itemType)
                        && !ArrayUtils.contains(SHOVELABLE_MATERIALS, relativeBlock.getType()))) {
                    return;
                }
                if (config.getShredDrops() == 0) {
                    ADAPTER.breakBlockNMS(relativeBlock, player);
                } else {
                    BlockShredEvent relativeEvent = new BlockShredEvent(relativeBlock, player);
                    Bukkit.getServer().getPluginManager().callEvent(relativeEvent);
                    if (relativeEvent.isCancelled()) {
                        return;
                    }
                    if (config.getShredDrops() == 1) {
                        if (relativeBlock.getType().equals(QUARTZ_ORE)) {
                            relativeBlock.setType(NETHERRACK);
                        } else if (ArrayUtils.contains(Storage.ORES, relativeBlock.getType())) {
                            relativeBlock.setType(STONE);
                        }
                        WatcherEnchant.instance().onBlockShred(relativeEvent);
                        if (relativeEvent.isCancelled()) {
                            return;
                        }
                        relativeBlock.breakNaturally();
                    } else {
                        relativeBlock.setType(AIR);
                    }
                }
                Sound sound = null;
                switch (originalType) {
                    case GRASS:
                        sound = Sound.BLOCK_GRASS_BREAK;
                        break;
                    case DIRT:
                    case GRAVEL:
                    case CLAY:
                        sound = Sound.BLOCK_GRAVEL_BREAK;
                        break;
                    case SAND:
                        sound = Sound.BLOCK_SAND_BREAK;
                        break;
                    case AIR:
                        break;
                    default:
                        sound = Sound.BLOCK_STONE_BREAK;
                        break;
                }
                if (sound != null) {
                    relativeBlock.getLocation().getWorld().playSound(relativeBlock.getLocation(), sound, 10, 1);
                }

                Utilities.damageTool(player, 1, usedHand);

                used.add(relativeBlock);
                for (int i = 0; i < 3; i++) {
                    if (coords[i] > 0) {
                        coords[i] -= 1;
                        Block blk1 = relativeBlock.getRelative(i == 0 ? -1 : 0, i == 1 ? -1 : 0, i == 2 ? -1 : 0);
                        Block blk2 = relativeBlock.getRelative(i == 0 ? 1 : 0, i == 1 ? 1 : 0, i == 2 ? 1 : 0);
                        if (blk1.getLocation().distanceSquared(centerBlock.getLocation()) < size + (-1 + 2 * Math.random())) {
                            blocks(centerBlock, blk1, coords, time + 2, size, used, player, config, itemType, usedHand);
                        }
                        if (blk2.getLocation().distanceSquared(centerBlock.getLocation()) < size + (-1 + 2 * Math.random())) {
                            blocks(centerBlock, blk2, coords, time + 2, size, used, player, config, itemType, usedHand);
                        }
                        coords[i] += 1;
                    }
                }
            }
        }
    }

    public static class Siphon extends CustomEnchantment {

        public Siphon() {
            maxLevel = 4;
            loreName = "Siphon";
            probability = 0;
            enchantable = new Tool[]{BOW_, SWORD};
            conflicting = new Class[]{};
            description = "Drains the health of the mob that you attack, giving it to you";
            cooldown = 0;
            power = 1.0;
            handUse = 3;
        }

        public int getEnchantmentId() {
            return 53;
        }

        @Override
        public boolean onEntityHit(EntityDamageByEntityEvent evt, int level, boolean usedHand) {
            if (evt.getEntity() instanceof LivingEntity && ADAPTER.attackEntity((LivingEntity) evt.getEntity(), (Player) evt.getDamager(), 0)) {
                Player p = (Player) evt.getDamager();
                LivingEntity ent = (LivingEntity) evt.getEntity();
                int difference = (int) Math.round(.17 * level * power * evt.getDamage());
                while (difference > 0) {
                    if (p.getHealth() <= 19) {
                        p.setHealth(p.getHealth() + 1);
                    }
                    difference--;
                }

            }
            return true;
        }

        @Override
        public boolean onEntityShootBow(EntityShootBowEvent evt, int level, boolean usedHand) {
            EnchantArrow.ArrowEnchantSiphon arrow = new EnchantArrow.ArrowEnchantSiphon((Projectile) evt.getProjectile(), level, power);
            Utilities.putArrow(evt.getProjectile(), arrow, (Player) evt.getEntity());
            return true;
        }
    }

    public static class Spectral extends CustomEnchantment {

        private static int increase(int old, int add) {
            if (old < add) {
                return ++old;
            } else {
                return 0;
            }
        }

        public Spectral() {
            maxLevel = 1;
            loreName = "Spectral";
            probability = 0;
            enchantable = new Tool[]{SHOVEL};
            conflicting = new Class[]{};
            description = "Allows for cycling through a block's types";
            cooldown = 0;
            power = -1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 54;
        }

        @Override
        public boolean onBlockInteract(PlayerInteractEvent evt, int level, boolean usedHand) {
            if (evt.getClickedBlock() == null) {
                return false;
            }
            Material original = evt.getClickedBlock().getType();
            int originalInt = evt.getClickedBlock().getData();
            if (evt.getAction() != RIGHT_CLICK_BLOCK) {
                return false;
            }
            int data = evt.getClickedBlock().getData();
            switch (evt.getClickedBlock().getType()) {
                case WOOL:
                case STAINED_GLASS:
                case STAINED_GLASS_PANE:
                case CARPET:
                case STAINED_CLAY:
                    data = increase(data, 15);
                    break;
                case WOOD:
                case WOOD_STEP:
                case WOOD_DOUBLE_STEP:
                case SAPLING:
                    data = increase(data, 6);
                    break;
                case RED_SANDSTONE:
                    if (data < 2) {
                        data++;
                    } else {
                        data = 0;
                        evt.getClickedBlock().setType(SANDSTONE);
                    }
                    break;
                case SANDSTONE:
                    if (data < 2) {
                        data++;
                    } else {
                        data = 0;
                        evt.getClickedBlock().setType(RED_SANDSTONE);
                    }
                    break;
                case RED_SANDSTONE_STAIRS:
                    evt.getClickedBlock().setType(SANDSTONE_STAIRS);
                    break;
                case SANDSTONE_STAIRS:
                    evt.getClickedBlock().setType(RED_SANDSTONE_STAIRS);
                    break;
                case SAND:
                    data = increase(data, 2);
                    break;
                case LONG_GRASS:
                    data = increase(data, 3);
                    break;
                case QUARTZ_BLOCK:
                    data = increase(data, 4);
                    break;
                case COBBLE_WALL:
                    data = increase(data, 2);
                    break;
                case STONE:
                    data = increase(data, 7);
                    break;
                case SMOOTH_BRICK:
                    data = increase(data, 4);
                    break;
                case COBBLESTONE:
                    evt.getClickedBlock().setType(MOSSY_COBBLESTONE);
                    break;
                case MOSSY_COBBLESTONE:
                    evt.getClickedBlock().setType(COBBLESTONE);
                    break;
                case BROWN_MUSHROOM:
                    evt.getClickedBlock().setType(RED_MUSHROOM);
                    break;
                case RED_MUSHROOM:
                    evt.getClickedBlock().setType(BROWN_MUSHROOM);
                    break;
                case HUGE_MUSHROOM_1:
                    evt.getClickedBlock().setType(HUGE_MUSHROOM_2);
                    break;
                case HUGE_MUSHROOM_2:
                    evt.getClickedBlock().setType(HUGE_MUSHROOM_1);
                    break;
                case STEP:
                    if (evt.getClickedBlock().getData() == 1) {
                        evt.getClickedBlock().setType(STONE_SLAB2);
                        data = 0;
                    }
                    break;
                case STONE_SLAB2:
                    if (evt.getClickedBlock().getData() == 0) {
                        evt.getClickedBlock().setType(STEP);
                        data = 1;
                    }
                    break;
                case DOUBLE_STEP:
                    if (evt.getClickedBlock().getData() == 1) {
                        evt.getClickedBlock().setType(DOUBLE_STONE_SLAB2);
                        data = 0;
                    }
                    break;
                case DOUBLE_STONE_SLAB2:
                    if (evt.getClickedBlock().getData() == 0) {
                        evt.getClickedBlock().setType(DOUBLE_STEP);
                        data = 1;
                    }
                    break;
                case DOUBLE_PLANT:
                    if (evt.getClickedBlock().getRelative(DOWN).getType().equals(DOUBLE_PLANT)) {
                        evt.getClickedBlock().getRelative(DOWN).setData((byte) increase(evt.getClickedBlock().getRelative(DOWN).getData(), 6));
                    } else if (evt.getClickedBlock().getRelative(UP).getType().equals(DOUBLE_PLANT)) {
                        data = increase(data, 6);
                    }
                    break;
                case LEAVES:
                    if ((data + 1) % 4 != 0 || data == 0) {
                        data++;
                    } else {
                        data -= 3;
                        evt.getClickedBlock().setType(LEAVES_2);
                    }
                    break;
                case LEAVES_2:
                    if ((data + 1) % 2 != 0 || data == 0) {
                        data++;
                    } else {
                        evt.getClickedBlock().setType(LEAVES);
                        data -= 1;
                    }
                    break;
                case LOG:
                    if ((data + 1) % 4 != 0 || data == 0) {
                        data++;
                    } else {
                        data -= 3;
                        evt.getClickedBlock().setType(LOG_2);
                    }
                    break;
                case LOG_2:
                    if ((data + 1) % 2 != 0 || data == 0) {
                        data++;
                    } else {
                        evt.getClickedBlock().setType(LOG);
                        data -= 1;
                    }
                    break;
                case YELLOW_FLOWER:
                    evt.getClickedBlock().setType(RED_ROSE);
                    break;
                case RED_ROSE:
                    if (data < 8) {
                        data++;
                    } else {
                        data = 0;
                        evt.getClickedBlock().setType(YELLOW_FLOWER);
                    }
                    break;
                case GRASS_PATH:
                    evt.getClickedBlock().setType(GRASS);
                    break;
                case GRASS:
                    evt.getClickedBlock().setType(GRASS_PATH);
                    break;
                case DIRT:
                    if (data < 2) {
                        data++;
                    } else {
                        data = 0;
                    }
                    break;
                case FENCE:
                case SPRUCE_FENCE:
                case BIRCH_FENCE:
                case JUNGLE_FENCE:
                case DARK_OAK_FENCE:
                case ACACIA_FENCE: {
                    Material[] mats = new Material[]{FENCE, SPRUCE_FENCE, BIRCH_FENCE,
                        JUNGLE_FENCE, DARK_OAK_FENCE, ACACIA_FENCE};
                    int index = ArrayUtils.indexOf(mats, evt.getClickedBlock().getType());
                    if (index < mats.length - 1) {
                        evt.getClickedBlock().setType(mats[index + 1]);
                    } else {
                        evt.getClickedBlock().setType(mats[0]);
                    }
                    break;
                }
                case FENCE_GATE:
                case SPRUCE_FENCE_GATE:
                case BIRCH_FENCE_GATE:
                case JUNGLE_FENCE_GATE:
                case DARK_OAK_FENCE_GATE:
                case ACACIA_FENCE_GATE: {
                    Material[] mats = new Material[]{FENCE_GATE, SPRUCE_FENCE_GATE,
                        BIRCH_FENCE_GATE, JUNGLE_FENCE_GATE, DARK_OAK_FENCE_GATE, ACACIA_FENCE_GATE};
                    int index = ArrayUtils.indexOf(mats, evt.getClickedBlock().getType());
                    if (index < mats.length - 1) {
                        evt.getClickedBlock().setType(mats[index + 1]);
                    } else {
                        evt.getClickedBlock().setType(mats[0]);
                    }
                    break;
                }
                case WOOD_STAIRS:
                case SPRUCE_WOOD_STAIRS:
                case BIRCH_WOOD_STAIRS:
                case JUNGLE_WOOD_STAIRS:
                case DARK_OAK_STAIRS:
                case ACACIA_STAIRS: {
                    Material[] mats = new Material[]{WOOD_STAIRS, SPRUCE_WOOD_STAIRS,
                        BIRCH_WOOD_STAIRS, JUNGLE_WOOD_STAIRS, DARK_OAK_STAIRS, ACACIA_STAIRS};
                    int index = ArrayUtils.indexOf(mats, evt.getClickedBlock().getType());
                    if (index < mats.length - 1) {
                        evt.getClickedBlock().setType(mats[index + 1]);
                    } else {
                        evt.getClickedBlock().setType(mats[0]);
                    }
                    break;
                }
                case WOODEN_DOOR:
                case SPRUCE_DOOR:
                case BIRCH_DOOR:
                case JUNGLE_DOOR:
                case DARK_OAK_DOOR:
                case ACACIA_DOOR: {
                    Material type;
                    Material[] mats = new Material[]{WOODEN_DOOR, SPRUCE_DOOR,
                        BIRCH_DOOR, JUNGLE_DOOR, DARK_OAK_DOOR, ACACIA_DOOR};
                    int index = ArrayUtils.indexOf(mats, evt.getClickedBlock().getType());
                    if (index < mats.length - 1) {
                        type = mats[index + 1];
                    } else {
                        type = mats[0];
                    }
                    if (evt.getClickedBlock().getRelative(UP).getType().equals(evt.getClickedBlock().getType())) {
                        evt.getClickedBlock().setTypeIdAndData(type.getId(), (byte) data, false);
                        evt.getClickedBlock().getRelative(UP).setTypeIdAndData(type.getId(), (byte) 8, true);
                    } else if (evt.getClickedBlock().getRelative(DOWN).getType().equals(evt.getClickedBlock().getType())) {
                        evt.getClickedBlock().setTypeIdAndData(type.getId(), (byte) 8, false);
                        evt.getClickedBlock().getRelative(DOWN).setTypeIdAndData(type.getId(), evt.getClickedBlock().getRelative(DOWN).getData(), true);
                    }
                    break;
                }
            }
            if (!evt.getClickedBlock().getType().equals(original) || data != originalInt) {
                evt.getClickedBlock().setData((byte) data);
                Utilities.damageTool(evt.getPlayer(), 1, usedHand);
                return true;
            }
            return false;
        }

    }

    public static class Speed extends CustomEnchantment {

        public Speed() {
            maxLevel = 4;
            loreName = "Speed";
            probability = 0;
            enchantable = new Tool[]{BOOTS};
            conflicting = new Class[]{Meador.class, Weight.class};
            description = "Gives the player a speed boost";
            cooldown = 0;
            power = 1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 55;
        }

        @Override
        public boolean onScan(Player player, int level, boolean usedHand) {
            player.setWalkSpeed((float) Math.min((.05f * level * power) + .2f, 1));
            player.setFlySpeed((float) Math.min((.05f * level * power) + .2f, 1));
            player.setMetadata("ze.speed", new FixedMetadataValue(Storage.zenchantments, true));
            return true;
        }
    }

    public static class Spikes extends CustomEnchantment {

        public Spikes() {
            maxLevel = 3;
            loreName = "Spikes";
            probability = 0;
            enchantable = new Tool[]{BOOTS};
            conflicting = new Class[]{};
            description = "Damages entities the player jumps onto";
            cooldown = 0;
            power = 1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 56;
        }

        @Override
        public boolean onFastScan(Player player, int level, boolean usedHand) {
            if (player.getVelocity().getY() < -0.45) {
                for (Entity e : player.getNearbyEntities(0, 2, 0)) {
                    double fall = Math.min(player.getFallDistance(), 20.0);
                    if (e instanceof LivingEntity) {
                        ADAPTER.attackEntity((LivingEntity) e, player, power * level * fall * 0.25);
                    }
                }
            }
            return true;
        }
    }

    public static class Spread extends CustomEnchantment {

        public Spread() {
            maxLevel = 5;
            loreName = "Spread";
            probability = 0;
            enchantable = new Tool[]{BOW_};
            conflicting = new Class[]{Burst.class};
            description = "Fires an array of arrows simultaneously";
            cooldown = 0;
            power = 1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 57;
        }

        @Override
        public boolean onProjectileLaunch(ProjectileLaunchEvent evt, int level, boolean usedHand) {
            Arrow originalArrow = (Arrow) evt.getEntity();
            Player player = (Player) originalArrow.getShooter();
            ItemStack hand = Utilities.usedStack(player, usedHand);
            EnchantArrow.ArrowGenericMulitple ar = new EnchantArrow.ArrowGenericMulitple(originalArrow);
            Utilities.putArrow(originalArrow, ar, player);
            Bukkit.getPluginManager().callEvent(new EntityShootBowEvent(player, hand, originalArrow, (float) originalArrow.getVelocity().length()));
            Utilities.damageTool(player, (int) Math.round(level / 2.0 + 1), usedHand);
            for (int i = 0; i < (int) Math.round(power * level * 4); i++) {
                Vector v = originalArrow.getVelocity();
                v.setX(v.getX() + Math.max(Math.min(Storage.rnd.nextGaussian() / 8, 0.75), -0.75));
                v.setZ(v.getZ() + Math.max(Math.min(Storage.rnd.nextGaussian() / 8, 0.75), -0.75));
                Arrow arrow = player.getWorld().spawnArrow(player.getEyeLocation().add(player.getLocation().getDirection().multiply(1.0)), v, 1, 0);
                arrow.setShooter(player);
                arrow.setVelocity(v.normalize().multiply(originalArrow.getVelocity().length()));
                arrow.setFireTicks(originalArrow.getFireTicks());
                arrow.setKnockbackStrength(originalArrow.getKnockbackStrength());
                EntityShootBowEvent event = new EntityShootBowEvent(player, hand, arrow, (float) originalArrow.getVelocity().length());
                Bukkit.getPluginManager().callEvent(event);
                if (evt.isCancelled()) {
                    arrow.remove();
                    return false;
                }
                arrow.setMetadata("ze.arrow", new FixedMetadataValue(Storage.zenchantments, null));
                arrow.setCritical(originalArrow.isCritical());
                Utilities.putArrow(originalArrow, new EnchantArrow.ArrowGenericMulitple(originalArrow), player);
            }
            return true;
        }
    }

    public static class Stationary extends CustomEnchantment {

        public Stationary() {
            maxLevel = 1;
            loreName = "Stationary";
            probability = 0;
            enchantable = new Tool[]{BOW_, SWORD};
            conflicting = new Class[]{};
            description = "Negates any knockback when attacking mobs, leaving them clueless as to who is attacking";
            cooldown = 0;
            power = -1.0;
            handUse = 3;
        }

        public int getEnchantmentId() {
            return 58;
        }

        @Override
        public boolean onEntityHit(EntityDamageByEntityEvent evt, int level, boolean usedHand) {
            if (!(evt.getEntity() instanceof LivingEntity) || !ADAPTER.attackEntity((LivingEntity) evt.getEntity(), (Player) evt.getDamager(), 0)) {
                LivingEntity ent = (LivingEntity) evt.getEntity();
                if (evt.getDamage() < ent.getHealth()) {
                    evt.setCancelled(true);
                    Utilities.damageTool(((Player) evt.getDamager()), 1, usedHand);
                    ent.damage(evt.getDamage());
                }
            }
            return true;
        }

        @Override
        public boolean onEntityShootBow(EntityShootBowEvent evt, int level, boolean usedHand) {
            EnchantArrow.ArrowEnchantStationary arrow = new EnchantArrow.ArrowEnchantStationary((Projectile) evt.getProjectile());
            Utilities.putArrow(evt.getProjectile(), arrow, (Player) evt.getEntity());
            return true;
        }

    }

    public static class Stock extends CustomEnchantment {

        public Stock() {
            maxLevel = 1;
            loreName = "Stock";
            probability = 0;
            enchantable = new Tool[]{CHESTPLATE};
            conflicting = new Class[]{};
            description = "Refills the player's item in hand when they run out";
            cooldown = -1;
            power = -1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 59;
        }

        @Override
        public boolean onBlockInteract(final PlayerInteractEvent evt, int level, boolean usedHand) {
            final ItemStack stk = evt.getPlayer().getInventory().getItemInMainHand().clone();
            if (stk == null || stk.getType() == AIR) {
                return false;
            }
            final Player player = evt.getPlayer();
            Bukkit.getScheduler().scheduleSyncDelayedTask(Storage.zenchantments, () -> {
                int current = -1;
                ItemStack newHandItem = evt.getPlayer().getInventory().getItemInMainHand();
                if (newHandItem != null && newHandItem.getType() != AIR) {
                    return;
                }
                for (int i = 0; i < evt.getPlayer().getInventory().getContents().length; i++) {
                    ItemStack s = player.getInventory().getContents()[i];
                    if (s != null && s.getType().equals(stk.getType())) {
                        if (s.getData().getData() == stk.getData().getData()) {
                            current = i;
                            break;
                        }
                        current = i;
                    }
                }
                if (current != -1) {
                    evt.getPlayer().getInventory().setItemInMainHand(evt.getPlayer().getInventory().getContents()[current]);
                    evt.getPlayer().getInventory().setItem(current, new ItemStack(AIR));
                    evt.getPlayer().updateInventory();
                }
            }, 1);
            return false;
        }
    }

    public static class Stream extends CustomEnchantment {

        private static final Particle[] trailTypes = {
            Particle.CLOUD,
            Particle.CRIT,
            Particle.VILLAGER_HAPPY,
            Particle.REDSTONE,
            Particle.HEART,};

        public Stream() {
            maxLevel = 1;
            loreName = "Stream";
            probability = 0;
            enchantable = new Tool[]{WINGS};
            conflicting = new Class[]{};
            description = "Creates a trail of particles when in flight";
            cooldown = 0;
            power = 1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 420;
        }

        @Override
        public boolean onBlockInteract(PlayerInteractEvent evt, int level, boolean usedHand) {
            if (evt.getItem() == null || evt.getItem().getType() != Material.ELYTRA) {
                return false;
            }
            Player player = evt.getPlayer();

            if (!evt.getPlayer().hasMetadata("ze.stream.mode")) {
                player.setMetadata("ze.stream.mode", new FixedMetadataValue(Storage.zenchantments, 0));
            }
            if (player.isSneaking() && (evt.getAction() == RIGHT_CLICK_AIR || evt.getAction() == RIGHT_CLICK_BLOCK)) {
                int b = player.getMetadata("ze.stream.mode").get(0).asInt();
                b = b == 4 ? 0 : b + 1;
                player.setMetadata("ze.stream.mode", new FixedMetadataValue(Storage.zenchantments, b));
                switch (b) {
                    case 0:
                        player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Clouds");
                        break;
                    case 1:
                        player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Gold Sparks");
                        break;
                    case 2:
                        player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Green Sparks");
                        break;
                    case 3:
                        player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Rainbow Dust");
                        break;
                    case 4:
                        player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Hearts");
                        break;
                }
                evt.setCancelled(true);

                // Prevent auto-equipping
                if ((player.getInventory().getChestplate() == null || player.getInventory().getChestplate().getType() == Material.AIR)) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Storage.zenchantments, () -> {
                        if ((player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR)) {
                            ItemStack stack = player.getInventory().getChestplate();
                            player.getInventory().setItemInMainHand(stack);
                            player.getInventory().setChestplate(new ItemStack(Material.AIR));
                        }
                    }, 0);
                }
            }
            return false;
        }

        @Override
        public boolean onFastScan(Player player, int level, boolean usedHand) {
            if (player.isGliding() && player.getVelocity().length() >= 0.5) {
                if (!player.hasMetadata("ze.stream.mode")) {
                    player.setMetadata("ze.stream.mode", new FixedMetadataValue(Storage.zenchantments, 0));
                }
                int b = player.getMetadata("ze.stream.mode").get(0).asInt();
                Utilities.display(player.getLocation(), trailTypes[b], 3, 0.1, 0, 0, 0);
                return true;
            }
            return false;
        }

    }

    public static class Switch extends CustomEnchantment {

        public Switch() {
            maxLevel = 1;
            loreName = "Switch";
            probability = 0;
            enchantable = new Tool[]{PICKAXE};
            conflicting = new Class[]{Shred.class, Anthropomorphism.class, Fire.class, Extraction.class, Pierce.class, Reveal.class};
            description = "Replaces the clicked block with the leftmost block in your hotbar when sneaking";
            cooldown = 0;
            power = -1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 60;
        }

        @Override
        public boolean onBlockInteract(final PlayerInteractEvent evt, int level, boolean usedHand) {
            if (evt.getAction() == Action.RIGHT_CLICK_BLOCK && evt.getPlayer().isSneaking()) {
                // Make sure clicked block is okay to break
                if (!ADAPTER.isBlockSafeToBreak(evt.getClickedBlock())) {
                    return false;
                }

                Player player = evt.getPlayer();
                int c = -1;
                ItemStack switchItem = null;
                for (int i = 0; i < 9; i++) { // Find a suitable block in hotbar
                    switchItem = player.getInventory().getItem(i);
                    if (switchItem != null
                            && switchItem.getType() != AIR
                            && switchItem.getType().isSolid()
                            && !ArrayUtils.contains(Storage.UNBREAKABLE_BLOCKS, switchItem.getType())
                            && !ArrayUtils.contains(Storage.INTERACTABLE_BLOCKS, switchItem.getType())) {
                        c = i;
                        break;
                    }
                }
                if (c == -1 || switchItem == null) { // No suitable block in inventory
                    return false;
                }

                // Block has been selected, attempt breaking
                if (!ADAPTER.breakBlockNMS(evt.getClickedBlock(), evt.getPlayer())) {
                    return false;
                }

                // Breaking succeeded, begin invasive operations
                Block clickedBlock = evt.getClickedBlock();
                Storage.grabLocs.put(clickedBlock, evt.getPlayer().getLocation());
                evt.setCancelled(true);

                Material mat = switchItem.getType();
                byte blockData;
                if (mat == HUGE_MUSHROOM_1 || mat == HUGE_MUSHROOM_2) {
                    blockData = 14;
                } else {
                    blockData = switchItem.getData().getData();
                }

                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Storage.zenchantments, () -> {
                    Storage.grabLocs.remove(clickedBlock);
                }, 3);

                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Storage.zenchantments, () -> {
                    ADAPTER.placeBlock(clickedBlock, player, mat, blockData);
                }, 1);
                Utilities.removeItem(evt.getPlayer(), mat, (short) blockData, 1);
                evt.getPlayer().updateInventory();
                return true;
            }
            return false;
        }
    }

    public static class Terraformer extends CustomEnchantment {

        private static final BlockFace[] SEARCH_FACES = {
            BlockFace.NORTH,
            BlockFace.SOUTH,
            BlockFace.EAST,
            BlockFace.WEST,
            BlockFace.DOWN,};

        public Terraformer() {
            maxLevel = 1;
            loreName = "Terraformer";
            probability = 0;
            enchantable = new Tool[]{SHOVEL};
            conflicting = new Class[]{};
            description = "Places the leftmost blocks in the players inventory within a 7 block radius";
            cooldown = 0;
            power = -1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 61;
        }

        @Override
        public boolean onBlockInteract(PlayerInteractEvent evt, int level, boolean usedHand) {
            if (evt.getPlayer().isSneaking()) {
                if (evt.getAction().equals(RIGHT_CLICK_BLOCK)) {
                    Block start = evt.getClickedBlock().getRelative(evt.getBlockFace());
                    List<Block> blocks = bfs(start);
                    Material mat = AIR;
                    byte bt = 0;
                    int c = -1;

                    for (int i = 0; i < 9; i++) {
                        if (evt.getPlayer().getInventory().getItem(i) != null) {
                            if (evt.getPlayer().getInventory().getItem(i).getType().isBlock() && ArrayUtils.contains(Storage.COMPATIBILITY_ADAPTER.getTerraformerMaterials(), evt.getPlayer().getInventory().getItem(i).getType())) {
                                mat = evt.getPlayer().getInventory().getItem(i).getType();
                                c = i;
                                bt = evt.getPlayer().getInventory().getItem(i).getData().getData();
                                break;
                            }
                        }
                    }
                    if (mat == HUGE_MUSHROOM_1 || mat == HUGE_MUSHROOM_2) {
                        bt = 14;
                    }

                    for (Block b : blocks) {
                        if (b.getType().equals(AIR)) {
                            if (Utilities.removeItemCheck(evt.getPlayer(), mat, bt, 1)) {
                                Storage.COMPATIBILITY_ADAPTER.placeBlock(b, evt.getPlayer(), mat, bt);
                                if (Storage.rnd.nextInt(10) == 5) {
                                    Utilities.damageTool(evt.getPlayer(), 1, usedHand);
                                }
                            }
                        }
                    }
                    evt.getPlayer().updateInventory();
                    return true;
                }
            }
            return false;
        }

        private List<Block> bfs(Block start) {
            LinkedList<Block> core = new LinkedList<>();
            LinkedList<Block> perimeter = new LinkedList<>();
            perimeter.add(start);

            while (!perimeter.isEmpty() && core.size() < 64) {
                Block block = perimeter.remove(0);
                for (BlockFace bf : SEARCH_FACES) {
                    Block rBlock = block.getRelative(bf);
                    if (rBlock.getType() == Material.AIR
                            && rBlock.getLocation().distanceSquared(start.getLocation()) < 25
                            && !perimeter.contains(rBlock)
                            && !core.contains(rBlock)) {
                        perimeter.add(rBlock);
                    }
                }
                core.add(block);
            }
            return core;
        }

    }

    public static class Toxic extends CustomEnchantment {

        public Toxic() {
            maxLevel = 4;
            loreName = "Toxic";
            probability = 0;
            enchantable = new Tool[]{BOW_, SWORD};
            conflicting = new Class[]{};
            description = "Sickens the target, making them nauseous and unable to eat";
            cooldown = 0;
            power = 1.0;
            handUse = 3;
        }

        public int getEnchantmentId() {
            return 62;
        }

        @Override
        public boolean onEntityShootBow(EntityShootBowEvent evt, int level, boolean usedHand) {
            EnchantArrow.ArrowEnchantToxic arrow = new EnchantArrow.ArrowEnchantToxic((Projectile) evt.getProjectile(), level, power);
            Utilities.putArrow(evt.getProjectile(), arrow, (Player) evt.getEntity());
            return true;
        }

        @Override
        public boolean onEntityHit(final EntityDamageByEntityEvent evt, int level, boolean usedHand) {
            if (!(evt.getEntity() instanceof LivingEntity) || !ADAPTER.attackEntity((LivingEntity) evt.getEntity(), (Player) evt.getDamager(), 0)) {
                final int value = (int) Math.round(level * power);
                Utilities.addPotion((LivingEntity) evt.getEntity(), CONFUSION, 80 + 60 * value, 4);
                Utilities.addPotion((LivingEntity) evt.getEntity(), HUNGER, 40 + 60 * value, 4);
                if (evt.getEntity() instanceof Player) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Storage.zenchantments, () -> {
                        ((LivingEntity) evt.getEntity()).removePotionEffect(HUNGER);
                        Utilities.addPotion((LivingEntity) evt.getEntity(), HUNGER, 60 + 40 * value, 0);
                    }, 20 + 60 * value);
                    Storage.hungerPlayers.put((Player) evt.getEntity(), (1 + value) * 100);
                }
            }
            return true;
        }

    }

    public static class Tracer extends CustomEnchantment {

        public Tracer() {
            maxLevel = 4;
            loreName = "Tracer";
            probability = 0;
            enchantable = new Tool[]{BOW_};
            conflicting = new Class[]{};
            description = "Guides the arrow to targets and then attacks";
            cooldown = 0;
            power = 1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 63;
        }

        @Override
        public boolean onEntityShootBow(EntityShootBowEvent evt, int level, boolean usedHand) {
            EnchantArrow.ArrowEnchantTracer arrow = new EnchantArrow.ArrowEnchantTracer((Projectile) evt.getProjectile(), level, power);
            Utilities.putArrow(evt.getProjectile(), arrow, (Player) evt.getEntity());
            return true;
        }

    }

    public static class Transformation extends CustomEnchantment {

        public Transformation() {
            maxLevel = 3;
            loreName = "Transformation";
            probability = 0;
            enchantable = new Tool[]{SWORD};
            conflicting = new Class[]{};
            description = "Occasionally causes the attacked mob to be transformed into its similar cousin";
            cooldown = 0;
            power = 1.0;
            handUse = 1;
        }

        public int getEnchantmentId() {
            return 64;
        }

        @Override
        public boolean onEntityHit(EntityDamageByEntityEvent evt, int level, boolean usedHand) {
            if (!(evt.getEntity() instanceof LivingEntity) || !ADAPTER.attackEntity((LivingEntity) evt.getEntity(), (Player) evt.getDamager(), 0)) {
                if (Storage.rnd.nextInt(100) > (100 - (level * power * 5))) {
                    int position = ArrayUtils.indexOf(Storage.TRANSFORMATION_ENTITY_TYPES, evt.getEntity().getType());
                    if (position != -1) {
                        if (evt.getDamage() > ((LivingEntity) evt.getEntity()).getHealth()) {
                            evt.setCancelled(true);
                        }
                        int newPosition = position + 1 - 2 * (position % 2);
                        Utilities.display(Utilities.getCenter(evt.getEntity().getLocation()), Particle.HEART, 70, .1f, .5f, 2, .5f);
                        evt.getEntity().remove();
                        LivingEntity ent = (LivingEntity) ((Player) evt.getDamager()).getWorld().spawnEntity(evt.getEntity().getLocation(),
                                Storage.TRANSFORMATION_ENTITY_TYPES[newPosition]);
                        ent.setHealth(Math.max(1, ((LivingEntity) evt.getEntity()).getHealth()));
                    }
                }
            }
            return true;
        }
    }

    public static class Variety extends CustomEnchantment {

        ItemStack[] logs = new ItemStack[]{new ItemStack(LOG, 1, (short) 0), new ItemStack(LOG, 1, (short) 1),
            new ItemStack(LOG, 1, (short) 2), new ItemStack(LOG, 1, (short) 3), new ItemStack(LOG_2, 1, (short) 0),
            new ItemStack(LOG_2, 1, (short) 1)};
        ItemStack[] leaves = new ItemStack[]{new ItemStack(LEAVES, 1, (short) 0), new ItemStack(LEAVES, 1, (short) 1),
            new ItemStack(LEAVES, 1, (short) 2), new ItemStack(LEAVES, 1, (short) 3),
            new ItemStack(LEAVES_2, 1, (short) 0), new ItemStack(LEAVES_2, 1, (short) 1)};

        public Variety() {
            maxLevel = 1;
            loreName = "Variety";
            probability = 0;
            enchantable = new Tool[]{AXE};
            conflicting = new Class[]{Fire.class};
            description = "Drops random types of wood or leaves";
            cooldown = 0;
            power = -1.0;
            handUse = 1;
        }

        public int getEnchantmentId() {
            return 65;
        }

        @Override
        public boolean onBlockBreak(BlockBreakEvent evt, int level, boolean usedHand) {
            if (evt.getBlock().getType() == LOG || evt.getBlock().getType() == LOG_2) {
                evt.getBlock().setType(AIR);
                evt.getBlock().getWorld().dropItemNaturally(Utilities.getCenter(evt.getBlock()), logs[Storage.rnd.nextInt(6)]);
                Utilities.damageTool(evt.getPlayer(), 1, usedHand);
            } else if (evt.getBlock().getType() == LEAVES || evt.getBlock().getType() == LEAVES_2) {
                evt.getBlock().setType(AIR);
                evt.getBlock().getWorld().dropItemNaturally(Utilities.getCenter(evt.getBlock()), leaves[Storage.rnd.nextInt(6)]);
                Utilities.damageTool(evt.getPlayer(), 1, usedHand);
            }
            return true;
        }
    }

    public static class Vortex extends CustomEnchantment {

        public Vortex() {
            maxLevel = 1;
            loreName = "Vortex";
            probability = 0;
            enchantable = new Tool[]{BOW_, SWORD};
            conflicting = new Class[]{};
            description = "Teleports mob loot and XP directly to the player";
            cooldown = 0;
            power = -1.0;
            handUse = 3;
        }

        public int getEnchantmentId() {
            return 66;
        }

        @Override
        public boolean onEntityKill(final EntityDeathEvent evt, int level, boolean usedHand) {
            Storage.vortexLocs.put(evt.getEntity().getLocation().getBlock(), evt.getEntity().getKiller().getLocation());
            int i = evt.getDroppedExp();
            evt.setDroppedExp(0);
            evt.getEntity().getKiller().giveExp(i);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Storage.zenchantments, () -> {
                Storage.vortexLocs.remove(evt.getEntity().getLocation().getBlock());
            }, 3);
            return true;
        }

        @Override
        public boolean onEntityShootBow(EntityShootBowEvent evt, int level, boolean usedHand) {
            EnchantArrow.ArrowEnchantVortex arrow = new EnchantArrow.ArrowEnchantVortex((Projectile) evt.getProjectile());
            Utilities.putArrow(evt.getProjectile(), arrow, (Player) evt.getEntity());
            return true;
        }

    }

    public static class Weight extends CustomEnchantment {

        public Weight() {
            maxLevel = 4;
            loreName = "Weight";
            probability = 0;
            enchantable = new Tool[]{BOOTS};
            conflicting = new Class[]{Meador.class, Speed.class};
            description = "Slows the player down but makes them stronger and more resistant to knockback";
            cooldown = 0;
            power = 1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 67;
        }

        @Override
        public boolean onBeingHit(EntityDamageByEntityEvent evt, int level, boolean usedHand) {
            if (!(evt.getEntity() instanceof LivingEntity) || !ADAPTER.attackEntity((LivingEntity) evt.getEntity(), (Player) evt.getDamager(), 0)) {
                return true;
            }

            if (evt.getEntity() instanceof Player) {
                Player player = (Player) evt.getEntity();
                if (evt.getDamage() < player.getHealth()) {
                    evt.setCancelled(true);
                    player.damage(evt.getDamage());
                    player.setVelocity(player.getLocation().subtract(evt.getDamager().getLocation()).toVector().multiply((float) (1 / (level * power + 1.5))));
                    ItemStack[] s = player.getInventory().getArmorContents();
                    for (int i = 0; i < 4; i++) {
                        if (s[i] != null) {
                            Utilities.addUnbreaking(player, s[i], 1);
                            if (s[i].getDurability() > s[i].getType().getMaxDurability()) {
                                s[i] = null;
                            }
                        }
                    }
                    player.getInventory().setArmorContents(s);
                }
            }

            return true;
        }

        @Override
        public boolean onScan(Player player, int level, boolean usedHand
        ) {
            player.setWalkSpeed((float) (.164f - level * power * .014f));
            Utilities.addPotion(player, INCREASE_DAMAGE, 610, (int) Math.round(power * level));
            player.setMetadata("ze.speed", new FixedMetadataValue(Storage.zenchantments, true));
            return true;
        }
    }

    public static class Apocalypse extends CustomEnchantment {

        public Apocalypse() {
            maxLevel = 1;
            loreName = "Apocalypse";
            probability = 0;
            enchantable = new Tool[]{BOW_};
            conflicting = new Class[]{};
            description = "Unleashes hell";
            cooldown = 0;
            power = -1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 69;
        }

        @Override
        public boolean onEntityShootBow(EntityShootBowEvent evt, int level, boolean usedHand) {
            EnchantArrow.ArrowAdminApocalypse arrow = new EnchantArrow.ArrowAdminApocalypse((Projectile) evt.getProjectile());
            Utilities.putArrow(evt.getProjectile(), arrow, (Player) evt.getEntity());
            return true;
        }
    }

    public static class Ethereal extends CustomEnchantment {

        public Ethereal() {
            maxLevel = 1;
            loreName = "Ethereal";
            probability = 0;
            enchantable = new Tool[]{ALL};
            conflicting = new Class[]{};
            description = "Prevents tools from breaking";
            cooldown = 0;
            power = -1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 70;
        }

        @Override
        public boolean onScanHands(Player player, int level, boolean usedHand) {
            ItemStack stk = Utilities.usedStack(player, usedHand);
            int dura = stk.getDurability();
            stk.setDurability((short) 0);
            if (dura != 0) {
                if (usedHand) {
                    player.getInventory().setItemInMainHand(stk);
                } else {
                    player.getInventory().setItemInOffHand(stk);
                }
                player.updateInventory();
            }
            return dura != 0;
        }

        @Override
        public boolean onScan(Player player, int level, boolean usedHand) {
            for (ItemStack s : player.getInventory().getArmorContents()) {
                if (s != null) {
                    Map<CustomEnchantment, Integer> map = Config.get(player.getWorld()).getEnchants(s);
                    if (map.containsKey(CustomEnchantment.Ethereal.this)) {
                        s.setDurability((short) 0);
                    }
                }
            }
            return true;
        }
    }

    public static class Missile extends CustomEnchantment {

        public Missile() {
            maxLevel = 1;
            loreName = "Missile";
            probability = 0;
            enchantable = new Tool[]{BOW_};
            conflicting = new Class[]{};
            description = "Shoots a missile from the bow";
            cooldown = 0;
            power = -1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 71;
        }

        @Override
        public boolean onEntityShootBow(EntityShootBowEvent evt, int level, boolean usedHand) {
            EnchantArrow.ArrowAdminMissile arrow = new EnchantArrow.ArrowAdminMissile((Projectile) evt.getProjectile());
            Utilities.putArrow(evt.getProjectile(), arrow, (Player) evt.getEntity());
            evt.setCancelled(true);
            Utilities.damageTool((Player) evt.getEntity(), 1, usedHand);
            Utilities.removeItem(((Player) evt.getEntity()), Material.ARROW, 1);
            return true;
        }
    }

    public static class Singularity extends CustomEnchantment {

        public Singularity() {
            maxLevel = 1;
            loreName = "Singularity";
            probability = 0;
            enchantable = new Tool[]{BOW_};
            conflicting = new Class[]{};
            description = "Creates a black hole that attracts nearby entities and then discharges them";
            cooldown = 0;
            power = -1.0;
            handUse = 2;
        }

        public int getEnchantmentId() {
            return 72;
        }

        @Override
        public boolean onEntityShootBow(EntityShootBowEvent evt, int level, boolean usedHand) {
            EnchantArrow.ArrowAdminSingularity arrow = new EnchantArrow.ArrowAdminSingularity((Projectile) evt.getProjectile(), level);
            Utilities.putArrow(evt.getProjectile(), arrow, (Player) evt.getEntity());
            return true;
        }

    }

    public static class Unrepairable extends CustomEnchantment {

        public Unrepairable() {
            maxLevel = 1;
            loreName = "Unrepairable";
            probability = 0;
            enchantable = new Tool[]{ALL};
            conflicting = new Class[]{};
            description = "Prevents an item from being repaired";
            cooldown = 0;
            power = -1.0;
            handUse = 0;
        }

        public int getEnchantmentId() {
            return 73;
        }
    }
}
