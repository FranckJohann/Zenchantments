/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zedly.zenchantments.compatibility;

import net.minecraft.server.v1_13_R1.*;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftMushroomCow;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftSheep;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.UUID;

/**
 *
 * @author Dennis
 */

public class NMS_1_13_R1 extends CompatibilityAdapter {

    public enum Material {
        ACACIA_BOAT(org.bukkit.Material.ACACIA_BOAT),
        ACACIA_BUTTON(org.bukkit.Material.ACACIA_BUTTON),
        ACACIA_DOOR(org.bukkit.Material.ACACIA_DOOR),
        ACACIA_FENCE(org.bukkit.Material.ACACIA_FENCE),
        ACACIA_FENCE_GATE(org.bukkit.Material.ACACIA_FENCE_GATE),
        ACACIA_LEAVES(org.bukkit.Material.ACACIA_LEAVES),
        ACACIA_LOG(org.bukkit.Material.ACACIA_LOG),
        ACACIA_PLANKS(org.bukkit.Material.ACACIA_PLANKS),
        ACACIA_PRESSURE_PLATE(org.bukkit.Material.ACACIA_PRESSURE_PLATE),
        ACACIA_SAPLING(org.bukkit.Material.ACACIA_SAPLING),
        ACACIA_SLAB(org.bukkit.Material.ACACIA_SLAB),
        ACACIA_STAIRS(org.bukkit.Material.ACACIA_STAIRS),
        ACACIA_TRAPDOOR(org.bukkit.Material.ACACIA_TRAPDOOR),
        ACACIA_WOOD(org.bukkit.Material.ACACIA_WOOD),
        ACTIVATOR_RAIL(org.bukkit.Material.ACTIVATOR_RAIL),
        AIR(org.bukkit.Material.AIR),
        ALLIUM(org.bukkit.Material.ALLIUM),
        ANDESITE(org.bukkit.Material.ANDESITE),
        ANVIL(org.bukkit.Material.ANVIL),
        APPLE(org.bukkit.Material.APPLE),
        ARMOR_STAND(org.bukkit.Material.ARMOR_STAND),
        ARROW(org.bukkit.Material.ARROW),
        AZURE_BLUET(org.bukkit.Material.AZURE_BLUET),
        BAKED_POTATO(org.bukkit.Material.BAKED_POTATO),
        BARRIER(org.bukkit.Material.BARRIER),
        BAT_SPAWN_EGG(org.bukkit.Material.BAT_SPAWN_EGG),
        BEACON(org.bukkit.Material.BEACON),
        BEDROCK(org.bukkit.Material.BEDROCK),
        BEEF(org.bukkit.Material.BEEF),
        BEETROOT(org.bukkit.Material.BEETROOT),
        BEETROOT_SEEDS(org.bukkit.Material.BEETROOT_SEEDS),
        BEETROOT_SOUP(org.bukkit.Material.BEETROOT_SOUP),
        BIRCH_BOAT(org.bukkit.Material.BIRCH_BOAT),
        BIRCH_BUTTON(org.bukkit.Material.BIRCH_BUTTON),
        BIRCH_DOOR(org.bukkit.Material.BIRCH_DOOR),
        BIRCH_FENCE(org.bukkit.Material.BIRCH_FENCE),
        BIRCH_FENCE_GATE(org.bukkit.Material.BIRCH_FENCE_GATE),
        BIRCH_LEAVES(org.bukkit.Material.BIRCH_LEAVES),
        BIRCH_LOG(org.bukkit.Material.BIRCH_LOG),
        BIRCH_PLANKS(org.bukkit.Material.BIRCH_PLANKS),
        BIRCH_PRESSURE_PLATE(org.bukkit.Material.BIRCH_PRESSURE_PLATE),
        BIRCH_SAPLING(org.bukkit.Material.BIRCH_SAPLING),
        BIRCH_SLAB(org.bukkit.Material.BIRCH_SLAB),
        BIRCH_STAIRS(org.bukkit.Material.BIRCH_STAIRS),
        BIRCH_TRAPDOOR(org.bukkit.Material.BIRCH_TRAPDOOR),
        BIRCH_WOOD(org.bukkit.Material.BIRCH_WOOD),
        BLACK_BANNER(org.bukkit.Material.BLACK_BANNER),
        BLACK_BED(org.bukkit.Material.BLACK_BED),
        BLACK_CARPET(org.bukkit.Material.BLACK_CARPET),
        BLACK_CONCRETE(org.bukkit.Material.BLACK_CONCRETE),
        BLACK_CONCRETE_POWDER(org.bukkit.Material.BLACK_CONCRETE_POWDER),
        BLACK_GLAZED_TERRACOTTA(org.bukkit.Material.BLACK_GLAZED_TERRACOTTA),
        BLACK_SHULKER_BOX(org.bukkit.Material.BLACK_SHULKER_BOX),
        BLACK_STAINED_GLASS(org.bukkit.Material.BLACK_STAINED_GLASS),
        BLACK_STAINED_GLASS_PANE(org.bukkit.Material.BLACK_STAINED_GLASS_PANE),
        BLACK_TERRACOTTA(org.bukkit.Material.BLACK_TERRACOTTA),
        BLACK_WOOL(org.bukkit.Material.BLACK_WOOL),
        BLAZE_POWDER(org.bukkit.Material.BLAZE_POWDER),
        BLAZE_ROD(org.bukkit.Material.BLAZE_ROD),
        BLAZE_SPAWN_EGG(org.bukkit.Material.BLAZE_SPAWN_EGG),
        BLUE_BANNER(org.bukkit.Material.BLUE_BANNER),
        BLUE_BED(org.bukkit.Material.BLUE_BED),
        BLUE_CARPET(org.bukkit.Material.BLUE_CARPET),
        BLUE_CONCRETE(org.bukkit.Material.BLUE_CONCRETE),
        BLUE_CONCRETE_POWDER(org.bukkit.Material.BLUE_CONCRETE_POWDER),
        BLUE_GLAZED_TERRACOTTA(org.bukkit.Material.BLUE_GLAZED_TERRACOTTA),
        BLUE_ICE(org.bukkit.Material.BLUE_ICE),
        BLUE_ORCHID(org.bukkit.Material.BLUE_ORCHID),
        BLUE_SHULKER_BOX(org.bukkit.Material.BLUE_SHULKER_BOX),
        BLUE_STAINED_GLASS(org.bukkit.Material.BLUE_STAINED_GLASS),
        BLUE_STAINED_GLASS_PANE(org.bukkit.Material.BLUE_STAINED_GLASS_PANE),
        BLUE_TERRACOTTA(org.bukkit.Material.BLUE_TERRACOTTA),
        BLUE_WOOL(org.bukkit.Material.BLUE_WOOL),
        BONE(org.bukkit.Material.BONE),
        BONE_BLOCK(org.bukkit.Material.BONE_BLOCK),
        BONE_MEAL(org.bukkit.Material.BONE_MEAL),
        BOOK(org.bukkit.Material.BOOK),
        BOOKSHELF(org.bukkit.Material.BOOKSHELF),
        BOW(org.bukkit.Material.BOW),
        BOWL(org.bukkit.Material.BOWL),
        BRAIN_CORAL(org.bukkit.Material.BRAIN_CORAL),
        BRAIN_CORAL_BLOCK(org.bukkit.Material.BRAIN_CORAL_BLOCK),
        BRAIN_CORAL_FAN(org.bukkit.Material.BRAIN_CORAL_FAN),
        BREAD(org.bukkit.Material.BREAD),
        BREWING_STAND(org.bukkit.Material.BREWING_STAND),
        BRICK(org.bukkit.Material.BRICK),
        BRICK_SLAB(org.bukkit.Material.BRICK_SLAB),
        BRICK_STAIRS(org.bukkit.Material.BRICK_STAIRS),
        BRICKS(org.bukkit.Material.BRICKS),
        BROWN_BANNER(org.bukkit.Material.BROWN_BANNER),
        BROWN_BED(org.bukkit.Material.BROWN_BED),
        BROWN_CARPET(org.bukkit.Material.BROWN_CARPET),
        BROWN_CONCRETE(org.bukkit.Material.BROWN_CONCRETE),
        BROWN_CONCRETE_POWDER(org.bukkit.Material.BROWN_CONCRETE_POWDER),
        BROWN_GLAZED_TERRACOTTA(org.bukkit.Material.BROWN_GLAZED_TERRACOTTA),
        BROWN_MUSHROOM(org.bukkit.Material.BROWN_MUSHROOM),
        BROWN_MUSHROOM_BLOCK(org.bukkit.Material.BROWN_MUSHROOM_BLOCK),
        BROWN_SHULKER_BOX(org.bukkit.Material.BROWN_SHULKER_BOX),
        BROWN_STAINED_GLASS(org.bukkit.Material.BROWN_STAINED_GLASS),
        BROWN_STAINED_GLASS_PANE(org.bukkit.Material.BROWN_STAINED_GLASS_PANE),
        BROWN_TERRACOTTA(org.bukkit.Material.BROWN_TERRACOTTA),
        BROWN_WOOL(org.bukkit.Material.BROWN_WOOL),
        BUBBLE_CORAL(org.bukkit.Material.BUBBLE_CORAL),
        BUBBLE_CORAL_BLOCK(org.bukkit.Material.BUBBLE_CORAL_BLOCK),
        BUBBLE_CORAL_FAN(org.bukkit.Material.BUBBLE_CORAL_FAN),
        BUCKET(org.bukkit.Material.BUCKET),
        CACTUS(org.bukkit.Material.CACTUS),
        CACTUS_GREEN(org.bukkit.Material.CACTUS_GREEN),
        CAKE(org.bukkit.Material.CAKE),
        CARROT(org.bukkit.Material.CARROT),
        CARROT_ON_A_STICK(org.bukkit.Material.CARROT_ON_A_STICK),
        CARVED_PUMPKIN(org.bukkit.Material.CARVED_PUMPKIN),
        CAULDRON(org.bukkit.Material.CAULDRON),
        CAVE_SPIDER_SPAWN_EGG(org.bukkit.Material.CAVE_SPIDER_SPAWN_EGG),
        CHAIN_COMMAND_BLOCK(org.bukkit.Material.CHAIN_COMMAND_BLOCK),
        CHAINMAIL_BOOTS(org.bukkit.Material.CHAINMAIL_BOOTS),
        CHAINMAIL_CHESTPLATE(org.bukkit.Material.CHAINMAIL_CHESTPLATE),
        CHAINMAIL_HELMET(org.bukkit.Material.CHAINMAIL_HELMET),
        CHAINMAIL_LEGGINGS(org.bukkit.Material.CHAINMAIL_LEGGINGS),
        CHARCOAL(org.bukkit.Material.CHARCOAL),
        CHEST(org.bukkit.Material.CHEST),
        CHEST_MINECART(org.bukkit.Material.CHEST_MINECART),
        CHICKEN(org.bukkit.Material.CHICKEN),
        CHICKEN_SPAWN_EGG(org.bukkit.Material.CHICKEN_SPAWN_EGG),
        CHIPPED_ANVIL(org.bukkit.Material.CHIPPED_ANVIL),
        CHISELED_QUARTZ_BLOCK(org.bukkit.Material.CHISELED_QUARTZ_BLOCK),
        CHISELED_RED_SANDSTONE(org.bukkit.Material.CHISELED_RED_SANDSTONE),
        CHISELED_SANDSTONE(org.bukkit.Material.CHISELED_SANDSTONE),
        CHISELED_STONE_BRICKS(org.bukkit.Material.CHISELED_STONE_BRICKS),
        CHORUS_FLOWER(org.bukkit.Material.CHORUS_FLOWER),
        CHORUS_FRUIT(org.bukkit.Material.CHORUS_FRUIT),
        POPPED_CHORUS_FRUIT(org.bukkit.Material.POPPED_CHORUS_FRUIT),
        CHORUS_PLANT(org.bukkit.Material.CHORUS_PLANT),
        CLAY(org.bukkit.Material.CLAY),
        CLAY_BALL(org.bukkit.Material.CLAY_BALL),
        CLOCK(org.bukkit.Material.CLOCK),
        TROPICAL_FISH_BUCKET(org.bukkit.Material.TROPICAL_FISH_BUCKET),
        COAL(org.bukkit.Material.COAL),
        COAL_BLOCK(org.bukkit.Material.COAL_BLOCK),
        COAL_ORE(org.bukkit.Material.COAL_ORE),
        COARSE_DIRT(org.bukkit.Material.COARSE_DIRT),
        COBBLESTONE(org.bukkit.Material.COBBLESTONE),
        COBBLESTONE_SLAB(org.bukkit.Material.COBBLESTONE_SLAB),
        COBBLESTONE_WALL(org.bukkit.Material.COBBLESTONE_WALL),
        COBWEB(org.bukkit.Material.COBWEB),
        COCOA_BEANS(org.bukkit.Material.COCOA_BEANS),
        COD(org.bukkit.Material.COD),
        COD_BUCKET(org.bukkit.Material.COD_BUCKET),
        COD_SPAWN_EGG(org.bukkit.Material.COD_SPAWN_EGG),
        COMMAND_BLOCK(org.bukkit.Material.COMMAND_BLOCK),
        COMMAND_BLOCK_MINECART(org.bukkit.Material.COMMAND_BLOCK_MINECART),
        COMPARATOR(org.bukkit.Material.COMPARATOR),
        COMPASS(org.bukkit.Material.COMPASS),
        CONDUIT(org.bukkit.Material.CONDUIT),
        COOKED_BEEF(org.bukkit.Material.COOKED_BEEF),
        COOKED_CHICKEN(org.bukkit.Material.COOKED_CHICKEN),
        COOKED_COD(org.bukkit.Material.COOKED_COD),
        COOKED_MUTTON(org.bukkit.Material.COOKED_MUTTON),
        COOKED_PORKCHOP(org.bukkit.Material.COOKED_PORKCHOP),
        COOKED_RABBIT(org.bukkit.Material.COOKED_RABBIT),
        COOKED_SALMON(org.bukkit.Material.COOKED_SALMON),
        COOKIE(org.bukkit.Material.COOKIE),
        COW_SPAWN_EGG(org.bukkit.Material.COW_SPAWN_EGG),
        CRACKED_STONE_BRICKS(org.bukkit.Material.CRACKED_STONE_BRICKS),
        CRAFTING_TABLE(org.bukkit.Material.CRAFTING_TABLE),
        CREEPER_HEAD(org.bukkit.Material.CREEPER_HEAD),
        CREEPER_SPAWN_EGG(org.bukkit.Material.CREEPER_SPAWN_EGG),
        CUT_RED_SANDSTONE(org.bukkit.Material.CUT_RED_SANDSTONE),
        CUT_SANDSTONE(org.bukkit.Material.CUT_SANDSTONE),
        CYAN_BANNER(org.bukkit.Material.CYAN_BANNER),
        CYAN_BED(org.bukkit.Material.CYAN_BED),
        CYAN_CARPET(org.bukkit.Material.CYAN_CARPET),
        CYAN_CONCRETE(org.bukkit.Material.CYAN_CONCRETE),
        CYAN_CONCRETE_POWDER(org.bukkit.Material.CYAN_CONCRETE_POWDER),
        CYAN_DYE(org.bukkit.Material.CYAN_DYE),
        CYAN_GLAZED_TERRACOTTA(org.bukkit.Material.CYAN_GLAZED_TERRACOTTA),
        CYAN_SHULKER_BOX(org.bukkit.Material.CYAN_SHULKER_BOX),
        CYAN_STAINED_GLASS(org.bukkit.Material.CYAN_STAINED_GLASS),
        CYAN_STAINED_GLASS_PANE(org.bukkit.Material.CYAN_STAINED_GLASS_PANE),
        CYAN_TERRACOTTA(org.bukkit.Material.CYAN_TERRACOTTA),
        CYAN_WOOL(org.bukkit.Material.CYAN_WOOL),
        DAMAGED_ANVIL(org.bukkit.Material.DAMAGED_ANVIL),
        DANDELION(org.bukkit.Material.DANDELION),
        DANDELION_YELLOW(org.bukkit.Material.DANDELION_YELLOW),
        DARK_OAK_BOAT(org.bukkit.Material.DARK_OAK_BOAT),
        DARK_OAK_BUTTON(org.bukkit.Material.DARK_OAK_BUTTON),
        DARK_OAK_DOOR(org.bukkit.Material.DARK_OAK_DOOR),
        DARK_OAK_FENCE(org.bukkit.Material.DARK_OAK_FENCE),
        DARK_OAK_FENCE_GATE(org.bukkit.Material.DARK_OAK_FENCE_GATE),
        DARK_OAK_LEAVES(org.bukkit.Material.DARK_OAK_LEAVES),
        DARK_OAK_LOG(org.bukkit.Material.DARK_OAK_LOG),
        DARK_OAK_PLANKS(org.bukkit.Material.DARK_OAK_PLANKS),
        DARK_OAK_PRESSURE_PLATE(org.bukkit.Material.DARK_OAK_PRESSURE_PLATE),
        DARK_OAK_SAPLING(org.bukkit.Material.DARK_OAK_SAPLING),
        DARK_OAK_SLAB(org.bukkit.Material.DARK_OAK_SLAB),
        DARK_OAK_STAIRS(org.bukkit.Material.DARK_OAK_STAIRS),
        DARK_OAK_TRAPDOOR(org.bukkit.Material.DARK_OAK_TRAPDOOR),
        DARK_OAK_WOOD(org.bukkit.Material.DARK_OAK_WOOD),
        DARK_PRISMARINE(org.bukkit.Material.DARK_PRISMARINE),
        DARK_PRISMARINE_SLAB(org.bukkit.Material.DARK_PRISMARINE_SLAB),
        DARK_PRISMARINE_STAIRS(org.bukkit.Material.DARK_PRISMARINE_STAIRS),
        DAYLIGHT_DETECTOR(org.bukkit.Material.DAYLIGHT_DETECTOR),
        DEAD_BRAIN_CORAL_BLOCK(org.bukkit.Material.DEAD_BRAIN_CORAL_BLOCK),
        DEAD_BUBBLE_CORAL_BLOCK(org.bukkit.Material.DEAD_BUBBLE_CORAL_BLOCK),
        DEAD_BUSH(org.bukkit.Material.DEAD_BUSH),
        DEAD_FIRE_CORAL_BLOCK(org.bukkit.Material.DEAD_FIRE_CORAL_BLOCK),
        DEAD_HORN_CORAL_BLOCK(org.bukkit.Material.DEAD_HORN_CORAL_BLOCK),
        DEAD_TUBE_CORAL_BLOCK(org.bukkit.Material.DEAD_TUBE_CORAL_BLOCK),
        DETECTOR_RAIL(org.bukkit.Material.DETECTOR_RAIL),
        DIAMOND(org.bukkit.Material.DIAMOND),
        DIAMOND_AXE(org.bukkit.Material.DIAMOND_AXE),
        DIAMOND_BLOCK(org.bukkit.Material.DIAMOND_BLOCK),
        DIAMOND_BOOTS(org.bukkit.Material.DIAMOND_BOOTS),
        DIAMOND_CHESTPLATE(org.bukkit.Material.DIAMOND_CHESTPLATE),
        DIAMOND_HELMET(org.bukkit.Material.DIAMOND_HELMET),
        DIAMOND_HOE(org.bukkit.Material.DIAMOND_HOE),
        DIAMOND_HORSE_ARMOR(org.bukkit.Material.DIAMOND_HORSE_ARMOR),
        DIAMOND_LEGGINGS(org.bukkit.Material.DIAMOND_LEGGINGS),
        DIAMOND_ORE(org.bukkit.Material.DIAMOND_ORE),
        DIAMOND_PICKAXE(org.bukkit.Material.DIAMOND_PICKAXE),
        DIAMOND_SHOVEL(org.bukkit.Material.DIAMOND_SHOVEL),
        DIAMOND_SWORD(org.bukkit.Material.DIAMOND_SWORD),
        DIORITE(org.bukkit.Material.DIORITE),
        DIRT(org.bukkit.Material.DIRT),
        DISPENSER(org.bukkit.Material.DISPENSER),
        DOLPHIN_SPAWN_EGG(org.bukkit.Material.DOLPHIN_SPAWN_EGG),
        DONKEY_SPAWN_EGG(org.bukkit.Material.DONKEY_SPAWN_EGG),
        DRAGON_BREATH(org.bukkit.Material.DRAGON_BREATH),
        DRAGON_EGG(org.bukkit.Material.DRAGON_EGG),
        DRAGON_HEAD(org.bukkit.Material.DRAGON_HEAD),
        DRIED_KELP(org.bukkit.Material.DRIED_KELP),
        DRIED_KELP_BLOCK(org.bukkit.Material.DRIED_KELP_BLOCK),
        DROPPER(org.bukkit.Material.DROPPER),
        DROWNED_SPAWN_EGG(org.bukkit.Material.DROWNED_SPAWN_EGG),
        EGG(org.bukkit.Material.EGG),
        ELDER_GUARDIAN_SPAWN_EGG(org.bukkit.Material.ELDER_GUARDIAN_SPAWN_EGG),
        ELYTRA(org.bukkit.Material.ELYTRA),
        EMERALD(org.bukkit.Material.EMERALD),
        EMERALD_BLOCK(org.bukkit.Material.EMERALD_BLOCK),
        EMERALD_ORE(org.bukkit.Material.EMERALD_ORE),
        ENCHANTED_BOOK(org.bukkit.Material.ENCHANTED_BOOK),
        ENCHANTED_GOLDEN_APPLE(org.bukkit.Material.ENCHANTED_GOLDEN_APPLE),
        ENCHANTING_TABLE(org.bukkit.Material.ENCHANTING_TABLE),
        END_CRYSTAL(org.bukkit.Material.END_CRYSTAL),
        END_PORTAL_FRAME(org.bukkit.Material.END_PORTAL_FRAME),
        END_ROD(org.bukkit.Material.END_ROD),
        END_STONE(org.bukkit.Material.END_STONE),
        END_STONE_BRICKS(org.bukkit.Material.END_STONE_BRICKS),
        ENDER_CHEST(org.bukkit.Material.ENDER_CHEST),
        ENDER_EYE(org.bukkit.Material.ENDER_EYE),
        ENDER_PEARL(org.bukkit.Material.ENDER_PEARL),
        ENDERMAN_SPAWN_EGG(org.bukkit.Material.ENDERMAN_SPAWN_EGG),
        ENDERMITE_SPAWN_EGG(org.bukkit.Material.ENDERMITE_SPAWN_EGG),
        EVOKER_SPAWN_EGG(org.bukkit.Material.EVOKER_SPAWN_EGG),
        EXPERIENCE_BOTTLE(org.bukkit.Material.EXPERIENCE_BOTTLE),
        FARMLAND(org.bukkit.Material.FARMLAND),
        FEATHER(org.bukkit.Material.FEATHER),
        FERMENTED_SPIDER_EYE(org.bukkit.Material.FERMENTED_SPIDER_EYE),
        FERN(org.bukkit.Material.FERN),
        FILLED_MAP(org.bukkit.Material.FILLED_MAP),
        FIRE_CHARGE(org.bukkit.Material.FIRE_CHARGE),
        FIRE_CORAL(org.bukkit.Material.FIRE_CORAL),
        FIRE_CORAL_BLOCK(org.bukkit.Material.FIRE_CORAL_BLOCK),
        FIRE_CORAL_FAN(org.bukkit.Material.FIRE_CORAL_FAN),
        FIREWORK_ROCKET(org.bukkit.Material.FIREWORK_ROCKET),
        FIREWORK_STAR(org.bukkit.Material.FIREWORK_STAR),
        FISHING_ROD(org.bukkit.Material.FISHING_ROD),
        FLINT(org.bukkit.Material.FLINT),
        FLINT_AND_STEEL(org.bukkit.Material.FLINT_AND_STEEL),
        FLOWER_POT(org.bukkit.Material.FLOWER_POT),
        FURNACE(org.bukkit.Material.FURNACE),
        FURNACE_MINECART(org.bukkit.Material.FURNACE_MINECART),
        GHAST_SPAWN_EGG(org.bukkit.Material.GHAST_SPAWN_EGG),
        GHAST_TEAR(org.bukkit.Material.GHAST_TEAR),
        GLASS(org.bukkit.Material.GLASS),
        GLASS_BOTTLE(org.bukkit.Material.GLASS_BOTTLE),
        GLASS_PANE(org.bukkit.Material.GLASS_PANE),
        GLISTERING_MELON_SLICE(org.bukkit.Material.GLISTERING_MELON_SLICE),
        GLOWSTONE(org.bukkit.Material.GLOWSTONE),
        GLOWSTONE_DUST(org.bukkit.Material.GLOWSTONE_DUST),
        GOLD_BLOCK(org.bukkit.Material.GOLD_BLOCK),
        GOLD_INGOT(org.bukkit.Material.GOLD_INGOT),
        GOLD_NUGGET(org.bukkit.Material.GOLD_NUGGET),
        GOLD_ORE(org.bukkit.Material.GOLD_ORE),
        GOLDEN_APPLE(org.bukkit.Material.GOLDEN_APPLE),
        GOLDEN_AXE(org.bukkit.Material.GOLDEN_AXE),
        GOLDEN_BOOTS(org.bukkit.Material.GOLDEN_BOOTS),
        GOLDEN_CARROT(org.bukkit.Material.GOLDEN_CARROT),
        GOLDEN_CHESTPLATE(org.bukkit.Material.GOLDEN_CHESTPLATE),
        GOLDEN_HELMET(org.bukkit.Material.GOLDEN_HELMET),
        GOLDEN_HOE(org.bukkit.Material.GOLDEN_HOE),
        GOLDEN_HORSE_ARMOR(org.bukkit.Material.GOLDEN_HORSE_ARMOR),
        GOLDEN_LEGGINGS(org.bukkit.Material.GOLDEN_LEGGINGS),
        GOLDEN_PICKAXE(org.bukkit.Material.GOLDEN_PICKAXE),
        GOLDEN_SHOVEL(org.bukkit.Material.GOLDEN_SHOVEL),
        GOLDEN_SWORD(org.bukkit.Material.GOLDEN_SWORD),
        GRANITE(org.bukkit.Material.GRANITE),
        GRASS(org.bukkit.Material.GRASS),
        GRASS_BLOCK(org.bukkit.Material.GRASS_BLOCK),
        GRASS_PATH(org.bukkit.Material.GRASS_PATH),
        GRAVEL(org.bukkit.Material.GRAVEL),
        GRAY_BANNER(org.bukkit.Material.GRAY_BANNER),
        GRAY_BED(org.bukkit.Material.GRAY_BED),
        GRAY_CARPET(org.bukkit.Material.GRAY_CARPET),
        GRAY_CONCRETE(org.bukkit.Material.GRAY_CONCRETE),
        GRAY_CONCRETE_POWDER(org.bukkit.Material.GRAY_CONCRETE_POWDER),
        GRAY_DYE(org.bukkit.Material.GRAY_DYE),
        GRAY_GLAZED_TERRACOTTA(org.bukkit.Material.GRAY_GLAZED_TERRACOTTA),
        GRAY_SHULKER_BOX(org.bukkit.Material.GRAY_SHULKER_BOX),
        GRAY_STAINED_GLASS(org.bukkit.Material.GRAY_STAINED_GLASS),
        GRAY_STAINED_GLASS_PANE(org.bukkit.Material.GRAY_STAINED_GLASS_PANE),
        GRAY_TERRACOTTA(org.bukkit.Material.GRAY_TERRACOTTA),
        GRAY_WOOL(org.bukkit.Material.GRAY_WOOL),
        GREEN_BANNER(org.bukkit.Material.GREEN_BANNER),
        GREEN_BED(org.bukkit.Material.GREEN_BED),
        GREEN_CARPET(org.bukkit.Material.GREEN_CARPET),
        GREEN_CONCRETE(org.bukkit.Material.GREEN_CONCRETE),
        GREEN_CONCRETE_POWDER(org.bukkit.Material.GREEN_CONCRETE_POWDER),
        GREEN_GLAZED_TERRACOTTA(org.bukkit.Material.GREEN_GLAZED_TERRACOTTA),
        GREEN_SHULKER_BOX(org.bukkit.Material.GREEN_SHULKER_BOX),
        GREEN_STAINED_GLASS(org.bukkit.Material.GREEN_STAINED_GLASS),
        GREEN_STAINED_GLASS_PANE(org.bukkit.Material.GREEN_STAINED_GLASS_PANE),
        GREEN_TERRACOTTA(org.bukkit.Material.GREEN_TERRACOTTA),
        GREEN_WOOL(org.bukkit.Material.GREEN_WOOL),
        GUARDIAN_SPAWN_EGG(org.bukkit.Material.GUARDIAN_SPAWN_EGG),
        GUNPOWDER(org.bukkit.Material.GUNPOWDER),
        HAY_BLOCK(org.bukkit.Material.HAY_BLOCK),
        HEART_OF_THE_SEA(org.bukkit.Material.HEART_OF_THE_SEA),
        HEAVY_WEIGHTED_PRESSURE_PLATE(org.bukkit.Material.HEAVY_WEIGHTED_PRESSURE_PLATE),
        HOPPER(org.bukkit.Material.HOPPER),
        HOPPER_MINECART(org.bukkit.Material.HOPPER_MINECART),
        HORN_CORAL(org.bukkit.Material.HORN_CORAL),
        HORN_CORAL_BLOCK(org.bukkit.Material.HORN_CORAL_BLOCK),
        HORN_CORAL_FAN(org.bukkit.Material.HORN_CORAL_FAN),
        HORSE_SPAWN_EGG(org.bukkit.Material.HORSE_SPAWN_EGG),
        HUSK_SPAWN_EGG(org.bukkit.Material.HUSK_SPAWN_EGG),
        ICE(org.bukkit.Material.ICE),
        INFESTED_CHISELED_STONE_BRICKS(org.bukkit.Material.INFESTED_CHISELED_STONE_BRICKS),
        INFESTED_COBBLESTONE(org.bukkit.Material.INFESTED_COBBLESTONE),
        INFESTED_CRACKED_STONE_BRICKS(org.bukkit.Material.INFESTED_CRACKED_STONE_BRICKS),
        INFESTED_MOSSY_STONE_BRICKS(org.bukkit.Material.INFESTED_MOSSY_STONE_BRICKS),
        INFESTED_STONE(org.bukkit.Material.INFESTED_STONE),
        INFESTED_STONE_BRICKS(org.bukkit.Material.INFESTED_STONE_BRICKS),
        INK_SAC(org.bukkit.Material.INK_SAC),
        IRON_AXE(org.bukkit.Material.IRON_AXE),
        IRON_BARS(org.bukkit.Material.IRON_BARS),
        IRON_BLOCK(org.bukkit.Material.IRON_BLOCK),
        IRON_BOOTS(org.bukkit.Material.IRON_BOOTS),
        IRON_CHESTPLATE(org.bukkit.Material.IRON_CHESTPLATE),
        IRON_DOOR(org.bukkit.Material.IRON_DOOR),
        IRON_HELMET(org.bukkit.Material.IRON_HELMET),
        IRON_HOE(org.bukkit.Material.IRON_HOE),
        IRON_HORSE_ARMOR(org.bukkit.Material.IRON_HORSE_ARMOR),
        IRON_INGOT(org.bukkit.Material.IRON_INGOT),
        IRON_LEGGINGS(org.bukkit.Material.IRON_LEGGINGS),
        IRON_NUGGET(org.bukkit.Material.IRON_NUGGET),
        IRON_ORE(org.bukkit.Material.IRON_ORE),
        IRON_PICKAXE(org.bukkit.Material.IRON_PICKAXE),
        IRON_SHOVEL(org.bukkit.Material.IRON_SHOVEL),
        IRON_SWORD(org.bukkit.Material.IRON_SWORD),
        IRON_TRAPDOOR(org.bukkit.Material.IRON_TRAPDOOR),
        ITEM_FRAME(org.bukkit.Material.ITEM_FRAME),
        JACK_O_LANTERN(org.bukkit.Material.JACK_O_LANTERN),
        JUKEBOX(org.bukkit.Material.JUKEBOX),
        JUNGLE_BOAT(org.bukkit.Material.JUNGLE_BOAT),
        JUNGLE_BUTTON(org.bukkit.Material.JUNGLE_BUTTON),
        JUNGLE_DOOR(org.bukkit.Material.JUNGLE_DOOR),
        JUNGLE_FENCE(org.bukkit.Material.JUNGLE_FENCE),
        JUNGLE_FENCE_GATE(org.bukkit.Material.JUNGLE_FENCE_GATE),
        JUNGLE_LEAVES(org.bukkit.Material.JUNGLE_LEAVES),
        JUNGLE_LOG(org.bukkit.Material.JUNGLE_LOG),
        JUNGLE_PLANKS(org.bukkit.Material.JUNGLE_PLANKS),
        JUNGLE_PRESSURE_PLATE(org.bukkit.Material.JUNGLE_PRESSURE_PLATE),
        JUNGLE_SAPLING(org.bukkit.Material.JUNGLE_SAPLING),
        JUNGLE_SLAB(org.bukkit.Material.JUNGLE_SLAB),
        JUNGLE_STAIRS(org.bukkit.Material.JUNGLE_STAIRS),
        JUNGLE_TRAPDOOR(org.bukkit.Material.JUNGLE_TRAPDOOR),
        JUNGLE_WOOD(org.bukkit.Material.JUNGLE_WOOD),
        KELP(org.bukkit.Material.KELP),
        LADDER(org.bukkit.Material.LADDER),
        LAPIS_BLOCK(org.bukkit.Material.LAPIS_BLOCK),
        LAPIS_LAZULI(org.bukkit.Material.LAPIS_LAZULI),
        LAPIS_ORE(org.bukkit.Material.LAPIS_ORE),
        LARGE_FERN(org.bukkit.Material.LARGE_FERN),
        LAVA_BUCKET(org.bukkit.Material.LAVA_BUCKET),
        LEAD(org.bukkit.Material.LEAD),
        LEATHER(org.bukkit.Material.LEATHER),
        LEATHER_BOOTS(org.bukkit.Material.LEATHER_BOOTS),
        LEATHER_CHESTPLATE(org.bukkit.Material.LEATHER_CHESTPLATE),
        LEATHER_HELMET(org.bukkit.Material.LEATHER_HELMET),
        LEATHER_LEGGINGS(org.bukkit.Material.LEATHER_LEGGINGS),
        LEVER(org.bukkit.Material.LEVER),
        LIGHT_BLUE_BANNER(org.bukkit.Material.LIGHT_BLUE_BANNER),
        LIGHT_BLUE_BED(org.bukkit.Material.LIGHT_BLUE_BED),
        LIGHT_BLUE_CARPET(org.bukkit.Material.LIGHT_BLUE_CARPET),
        LIGHT_BLUE_CONCRETE(org.bukkit.Material.LIGHT_BLUE_CONCRETE),
        LIGHT_BLUE_CONCRETE_POWDER(org.bukkit.Material.LIGHT_BLUE_CONCRETE_POWDER),
        LIGHT_BLUE_DYE(org.bukkit.Material.LIGHT_BLUE_DYE),
        LIGHT_BLUE_GLAZED_TERRACOTTA(org.bukkit.Material.LIGHT_BLUE_GLAZED_TERRACOTTA),
        LIGHT_BLUE_SHULKER_BOX(org.bukkit.Material.LIGHT_BLUE_SHULKER_BOX),
        LIGHT_BLUE_STAINED_GLASS(org.bukkit.Material.LIGHT_BLUE_STAINED_GLASS),
        LIGHT_BLUE_STAINED_GLASS_PANE(org.bukkit.Material.LIGHT_BLUE_STAINED_GLASS_PANE),
        LIGHT_BLUE_TERRACOTTA(org.bukkit.Material.LIGHT_BLUE_TERRACOTTA),
        LIGHT_BLUE_WOOL(org.bukkit.Material.LIGHT_BLUE_WOOL),
        LIGHT_GRAY_BANNER(org.bukkit.Material.LIGHT_GRAY_BANNER),
        LIGHT_GRAY_BED(org.bukkit.Material.LIGHT_GRAY_BED),
        LIGHT_GRAY_CARPET(org.bukkit.Material.LIGHT_GRAY_CARPET),
        LIGHT_GRAY_CONCRETE(org.bukkit.Material.LIGHT_GRAY_CONCRETE),
        LIGHT_GRAY_CONCRETE_POWDER(org.bukkit.Material.LIGHT_GRAY_CONCRETE_POWDER),
        LIGHT_GRAY_DYE(org.bukkit.Material.LIGHT_GRAY_DYE),
        LIGHT_GRAY_STAINED_GLASS(org.bukkit.Material.LIGHT_GRAY_STAINED_GLASS),
        LIGHT_GRAY_STAINED_GLASS_PANE(org.bukkit.Material.LIGHT_GRAY_STAINED_GLASS_PANE),
        LIGHT_GRAY_TERRACOTTA(org.bukkit.Material.LIGHT_GRAY_TERRACOTTA),
        LIGHT_GRAY_WOOL(org.bukkit.Material.LIGHT_GRAY_WOOL),
        LIGHT_WEIGHTED_PRESSURE_PLATE(org.bukkit.Material.LIGHT_WEIGHTED_PRESSURE_PLATE),
        LILAC(org.bukkit.Material.LILAC),
        LILY_PAD(org.bukkit.Material.LILY_PAD),
        LIME_BANNER(org.bukkit.Material.LIME_BANNER),
        LIME_BED(org.bukkit.Material.LIME_BED),
        LIME_CARPET(org.bukkit.Material.LIME_CARPET),
        LIME_CONCRETE(org.bukkit.Material.LIME_CONCRETE),
        LIME_CONCRETE_POWDER(org.bukkit.Material.LIME_CONCRETE_POWDER),
        LIME_DYE(org.bukkit.Material.LIME_DYE),
        LIME_GLAZED_TERRACOTTA(org.bukkit.Material.LIME_GLAZED_TERRACOTTA),
        LIME_SHULKER_BOX(org.bukkit.Material.LIME_SHULKER_BOX),
        LIME_STAINED_GLASS(org.bukkit.Material.LIME_STAINED_GLASS),
        LIME_STAINED_GLASS_PANE(org.bukkit.Material.LIME_STAINED_GLASS_PANE),
        LIME_TERRACOTTA(org.bukkit.Material.LIME_TERRACOTTA),
        LIME_WOOL(org.bukkit.Material.LIME_WOOL),
        LINGERING_POTION(org.bukkit.Material.LINGERING_POTION),
        LLAMA_SPAWN_EGG(org.bukkit.Material.LLAMA_SPAWN_EGG),
        MAGENTA_BANNER(org.bukkit.Material.MAGENTA_BANNER),
        MAGENTA_BED(org.bukkit.Material.MAGENTA_BED),
        MAGENTA_CARPET(org.bukkit.Material.MAGENTA_CARPET),
        MAGENTA_CONCRETE(org.bukkit.Material.MAGENTA_CONCRETE),
        MAGENTA_CONCRETE_POWDER(org.bukkit.Material.MAGENTA_CONCRETE_POWDER),
        MAGENTA_DYE(org.bukkit.Material.MAGENTA_DYE),
        MAGENTA_GLAZED_TERRACOTTA(org.bukkit.Material.MAGENTA_GLAZED_TERRACOTTA),
        MAGENTA_SHULKER_BOX(org.bukkit.Material.MAGENTA_SHULKER_BOX),
        MAGENTA_STAINED_GLASS(org.bukkit.Material.MAGENTA_STAINED_GLASS),
        MAGENTA_STAINED_GLASS_PANE(org.bukkit.Material.MAGENTA_STAINED_GLASS_PANE),
        MAGENTA_TERRACOTTA(org.bukkit.Material.MAGENTA_TERRACOTTA),
        MAGENTA_WOOL(org.bukkit.Material.MAGENTA_WOOL),
        MAGMA_BLOCK(org.bukkit.Material.MAGMA_BLOCK),
        MAGMA_CREAM(org.bukkit.Material.MAGMA_CREAM),
        MAGMA_CUBE_SPAWN_EGG(org.bukkit.Material.MAGMA_CUBE_SPAWN_EGG),
        MAP(org.bukkit.Material.MAP),
        MELON(org.bukkit.Material.MELON),
        MELON_SEEDS(org.bukkit.Material.MELON_SEEDS),
        MELON_SLICE(org.bukkit.Material.MELON_SLICE),
        MILK_BUCKET(org.bukkit.Material.MILK_BUCKET),
        MINECART(org.bukkit.Material.MINECART),
        SPAWNER(org.bukkit.Material.SPAWNER),
        MOOSHROOM_SPAWN_EGG(org.bukkit.Material.MOOSHROOM_SPAWN_EGG),
        MOSSY_COBBLESTONE(org.bukkit.Material.MOSSY_COBBLESTONE),
        MOSSY_COBBLESTONE_WALL(org.bukkit.Material.MOSSY_COBBLESTONE_WALL),
        MOSSY_STONE_BRICKS(org.bukkit.Material.MOSSY_STONE_BRICKS),
        MULE_SPAWN_EGG(org.bukkit.Material.MULE_SPAWN_EGG),
        MUSHROOM_STEM(org.bukkit.Material.MUSHROOM_STEM),
        MUSHROOM_STEW(org.bukkit.Material.MUSHROOM_STEW),
        MUSIC_DISC_11(org.bukkit.Material.MUSIC_DISC_11),
        MUSIC_DISC_13(org.bukkit.Material.MUSIC_DISC_13),
        MUSIC_DISC_BLOCKS(org.bukkit.Material.MUSIC_DISC_BLOCKS),
        MUSIC_DISC_CAT(org.bukkit.Material.MUSIC_DISC_CAT),
        MUSIC_DISC_CHIRP(org.bukkit.Material.MUSIC_DISC_CHIRP),
        MUSIC_DISC_FAR(org.bukkit.Material.MUSIC_DISC_FAR),
        MUSIC_DISC_MALL(org.bukkit.Material.MUSIC_DISC_MALL),
        MUSIC_DISC_MELLOHI(org.bukkit.Material.MUSIC_DISC_MELLOHI),
        MUSIC_DISC_STAL(org.bukkit.Material.MUSIC_DISC_STAL),
        MUSIC_DISC_STRAD(org.bukkit.Material.MUSIC_DISC_STRAD),
        MUSIC_DISC_WAIT(org.bukkit.Material.MUSIC_DISC_WAIT),
        MUSIC_DISC_WARD(org.bukkit.Material.MUSIC_DISC_WARD),
        MUTTON(org.bukkit.Material.MUTTON),
        MYCELIUM(org.bukkit.Material.MYCELIUM),
        NAME_TAG(org.bukkit.Material.NAME_TAG),
        NAUTILUS_SHELL(org.bukkit.Material.NAUTILUS_SHELL),
        NETHER_BRICK(org.bukkit.Material.NETHER_BRICK),
        NETHER_BRICK_FENCE(org.bukkit.Material.NETHER_BRICK_FENCE),
        NETHER_BRICK_SLAB(org.bukkit.Material.NETHER_BRICK_SLAB),
        NETHER_BRICK_STAIRS(org.bukkit.Material.NETHER_BRICK_STAIRS),
        NETHER_BRICKS(org.bukkit.Material.NETHER_BRICKS),
        NETHER_QUARTZ_ORE(org.bukkit.Material.NETHER_QUARTZ_ORE),
        NETHER_STAR(org.bukkit.Material.NETHER_STAR),
        NETHER_WART(org.bukkit.Material.NETHER_WART),
        NETHER_WART_BLOCK(org.bukkit.Material.NETHER_WART_BLOCK),
        NETHERRACK(org.bukkit.Material.NETHERRACK),
        NOTE_BLOCK(org.bukkit.Material.NOTE_BLOCK),
        OAK_BOAT(org.bukkit.Material.OAK_BOAT),
        OAK_BUTTON(org.bukkit.Material.OAK_BUTTON),
        OAK_DOOR(org.bukkit.Material.OAK_DOOR),
        OAK_FENCE(org.bukkit.Material.OAK_FENCE),
        OAK_FENCE_GATE(org.bukkit.Material.OAK_FENCE_GATE),
        OAK_LEAVES(org.bukkit.Material.OAK_LEAVES),
        OAK_LOG(org.bukkit.Material.OAK_LOG),
        OAK_PLANKS(org.bukkit.Material.OAK_PLANKS),
        OAK_PRESSURE_PLATE(org.bukkit.Material.OAK_PRESSURE_PLATE),
        OAK_SAPLING(org.bukkit.Material.OAK_SAPLING),
        OAK_SLAB(org.bukkit.Material.OAK_SLAB),
        OAK_STAIRS(org.bukkit.Material.OAK_STAIRS),
        OAK_TRAPDOOR(org.bukkit.Material.OAK_TRAPDOOR),
        OAK_WOOD(org.bukkit.Material.OAK_WOOD),
        OBSERVER(org.bukkit.Material.OBSERVER),
        OBSIDIAN(org.bukkit.Material.OBSIDIAN),
        OCELOT_SPAWN_EGG(org.bukkit.Material.OCELOT_SPAWN_EGG),
        ORANGE_BANNER(org.bukkit.Material.ORANGE_BANNER),
        ORANGE_BED(org.bukkit.Material.ORANGE_BED),
        ORANGE_CARPET(org.bukkit.Material.ORANGE_CARPET),
        ORANGE_CONCRETE(org.bukkit.Material.ORANGE_CONCRETE),
        ORANGE_CONCRETE_POWDER(org.bukkit.Material.ORANGE_CONCRETE_POWDER),
        ORANGE_DYE(org.bukkit.Material.ORANGE_DYE),
        ORANGE_GLAZED_TERRACOTTA(org.bukkit.Material.ORANGE_GLAZED_TERRACOTTA),
        ORANGE_SHULKER_BOX(org.bukkit.Material.ORANGE_SHULKER_BOX),
        ORANGE_STAINED_GLASS(org.bukkit.Material.ORANGE_STAINED_GLASS),
        ORANGE_STAINED_GLASS_PANE(org.bukkit.Material.ORANGE_STAINED_GLASS_PANE),
        ORANGE_TERRACOTTA(org.bukkit.Material.ORANGE_TERRACOTTA),
        ORANGE_TULIP(org.bukkit.Material.ORANGE_TULIP),
        ORANGE_WOOL(org.bukkit.Material.ORANGE_WOOL),
        OXEYE_DAISY(org.bukkit.Material.OXEYE_DAISY),
        PACKED_ICE(org.bukkit.Material.PACKED_ICE),
        PAINTING(org.bukkit.Material.PAINTING),
        PAPER(org.bukkit.Material.PAPER),
        PARROT_SPAWN_EGG(org.bukkit.Material.PARROT_SPAWN_EGG),
        PEONY(org.bukkit.Material.PEONY),
        PETRIFIED_OAK_SLAB(org.bukkit.Material.PETRIFIED_OAK_SLAB),
        PHANTOM_MEMBRANE(org.bukkit.Material.PHANTOM_MEMBRANE),
        PHANTOM_SPAWN_EGG(org.bukkit.Material.PHANTOM_SPAWN_EGG),
        PIG_SPAWN_EGG(org.bukkit.Material.PIG_SPAWN_EGG),
        PINK_BANNER(org.bukkit.Material.PINK_BANNER),
        PINK_BED(org.bukkit.Material.PINK_BED),
        PINK_CARPET(org.bukkit.Material.PINK_CARPET),
        PINK_CONCRETE(org.bukkit.Material.PINK_CONCRETE),
        PINK_CONCRETE_POWDER(org.bukkit.Material.PINK_CONCRETE_POWDER),
        PINK_DYE(org.bukkit.Material.PINK_DYE),
        PINK_GLAZED_TERRACOTTA(org.bukkit.Material.PINK_GLAZED_TERRACOTTA),
        PINK_SHULKER_BOX(org.bukkit.Material.PINK_SHULKER_BOX),
        PINK_STAINED_GLASS(org.bukkit.Material.PINK_STAINED_GLASS),
        PINK_STAINED_GLASS_PANE(org.bukkit.Material.PINK_STAINED_GLASS_PANE),
        PINK_TERRACOTTA(org.bukkit.Material.PINK_TERRACOTTA),
        PINK_TULIP(org.bukkit.Material.PINK_TULIP),
        PINK_WOOL(org.bukkit.Material.PINK_WOOL),
        PISTON(org.bukkit.Material.PISTON),
        PLAYER_HEAD(org.bukkit.Material.PLAYER_HEAD),
        PODZOL(org.bukkit.Material.PODZOL),
        POISONOUS_POTATO(org.bukkit.Material.POISONOUS_POTATO),
        POLAR_BEAR_SPAWN_EGG(org.bukkit.Material.POLAR_BEAR_SPAWN_EGG),
        POLISHED_ANDESITE(org.bukkit.Material.POLISHED_ANDESITE),
        POLISHED_DIORITE(org.bukkit.Material.POLISHED_DIORITE),
        POLISHED_GRANITE(org.bukkit.Material.POLISHED_GRANITE),
        POPPY(org.bukkit.Material.POPPY),
        PORKCHOP(org.bukkit.Material.PORKCHOP),
        POTATO(org.bukkit.Material.POTATO),
        POTION(org.bukkit.Material.POTION),
        POWERED_RAIL(org.bukkit.Material.POWERED_RAIL),
        PRISMARINE(org.bukkit.Material.PRISMARINE),
        PRISMARINE_BRICK_SLAB(org.bukkit.Material.PRISMARINE_BRICK_SLAB),
        PRISMARINE_BRICK_STAIRS(org.bukkit.Material.PRISMARINE_BRICK_STAIRS),
        PRISMARINE_BRICKS(org.bukkit.Material.PRISMARINE_BRICKS),
        PRISMARINE_CRYSTALS(org.bukkit.Material.PRISMARINE_CRYSTALS),
        PRISMARINE_SHARD(org.bukkit.Material.PRISMARINE_SHARD),
        PRISMARINE_SLAB(org.bukkit.Material.PRISMARINE_SLAB),
        PRISMARINE_STAIRS(org.bukkit.Material.PRISMARINE_STAIRS),
        PUFFERFISH(org.bukkit.Material.PUFFERFISH),
        PUFFERFISH_BUCKET(org.bukkit.Material.PUFFERFISH_BUCKET),
        PUFFERFISH_SPAWN_EGG(org.bukkit.Material.PUFFERFISH_SPAWN_EGG),
        PUMPKIN(org.bukkit.Material.PUMPKIN),
        PUMPKIN_PIE(org.bukkit.Material.PUMPKIN_PIE),
        PUMPKIN_SEEDS(org.bukkit.Material.PUMPKIN_SEEDS),
        PURPLE_BANNER(org.bukkit.Material.PURPLE_BANNER),
        PURPLE_BED(org.bukkit.Material.PURPLE_BED),
        PURPLE_CARPET(org.bukkit.Material.PURPLE_CARPET),
        PURPLE_CONCRETE(org.bukkit.Material.PURPLE_CONCRETE),
        PURPLE_CONCRETE_POWDER(org.bukkit.Material.PURPLE_CONCRETE_POWDER),
        PURPLE_DYE(org.bukkit.Material.PURPLE_DYE),
        PURPLE_GLAZED_TERRACOTTA(org.bukkit.Material.PURPLE_GLAZED_TERRACOTTA),
        PURPLE_SHULKER_BOX(org.bukkit.Material.PURPLE_SHULKER_BOX),
        PURPLE_STAINED_GLASS(org.bukkit.Material.PURPLE_STAINED_GLASS),
        PURPLE_STAINED_GLASS_PANE(org.bukkit.Material.PURPLE_STAINED_GLASS_PANE),
        PURPLE_TERRACOTTA(org.bukkit.Material.PURPLE_TERRACOTTA),
        PURPLE_WOOL(org.bukkit.Material.PURPLE_WOOL),
        PURPUR_BLOCK(org.bukkit.Material.PURPUR_BLOCK),
        PURPUR_PILLAR(org.bukkit.Material.PURPUR_PILLAR),
        PURPUR_SLAB(org.bukkit.Material.PURPUR_SLAB),
        PURPUR_STAIRS(org.bukkit.Material.PURPUR_STAIRS),
        QUARTZ(org.bukkit.Material.QUARTZ),
        QUARTZ_BLOCK(org.bukkit.Material.QUARTZ_BLOCK),
        QUARTZ_PILLAR(org.bukkit.Material.QUARTZ_PILLAR),
        QUARTZ_SLAB(org.bukkit.Material.QUARTZ_SLAB),
        QUARTZ_STAIRS(org.bukkit.Material.QUARTZ_STAIRS),
        RABBIT(org.bukkit.Material.RABBIT),
        RABBIT_FOOT(org.bukkit.Material.RABBIT_FOOT),
        RABBIT_HIDE(org.bukkit.Material.RABBIT_HIDE),
        RABBIT_SPAWN_EGG(org.bukkit.Material.RABBIT_SPAWN_EGG),
        RABBIT_STEW(org.bukkit.Material.RABBIT_STEW),
        RAIL(org.bukkit.Material.RAIL),
        RED_BANNER(org.bukkit.Material.RED_BANNER),
        RED_BED(org.bukkit.Material.RED_BED),
        RED_CARPET(org.bukkit.Material.RED_CARPET),
        RED_CONCRETE(org.bukkit.Material.RED_CONCRETE),
        RED_CONCRETE_POWDER(org.bukkit.Material.RED_CONCRETE_POWDER),
        RED_GLAZED_TERRACOTTA(org.bukkit.Material.RED_GLAZED_TERRACOTTA),
        RED_MUSHROOM(org.bukkit.Material.RED_MUSHROOM),
        RED_MUSHROOM_BLOCK(org.bukkit.Material.RED_MUSHROOM_BLOCK),
        RED_NETHER_BRICKS(org.bukkit.Material.RED_NETHER_BRICKS),
        RED_SAND(org.bukkit.Material.RED_SAND),
        RED_SANDSTONE(org.bukkit.Material.RED_SANDSTONE),
        RED_SANDSTONE_SLAB(org.bukkit.Material.RED_SANDSTONE_SLAB),
        RED_SANDSTONE_STAIRS(org.bukkit.Material.RED_SANDSTONE_STAIRS),
        RED_SHULKER_BOX(org.bukkit.Material.RED_SHULKER_BOX),
        RED_STAINED_GLASS(org.bukkit.Material.RED_STAINED_GLASS),
        RED_STAINED_GLASS_PANE(org.bukkit.Material.RED_STAINED_GLASS_PANE),
        RED_TERRACOTTA(org.bukkit.Material.RED_TERRACOTTA),
        RED_TULIP(org.bukkit.Material.RED_TULIP),
        RED_WOOL(org.bukkit.Material.RED_WOOL),
        REDSTONE(org.bukkit.Material.REDSTONE),
        REDSTONE_BLOCK(org.bukkit.Material.REDSTONE_BLOCK),
        REDSTONE_LAMP(org.bukkit.Material.REDSTONE_LAMP),
        REDSTONE_ORE(org.bukkit.Material.REDSTONE_ORE),
        REDSTONE_TORCH(org.bukkit.Material.REDSTONE_TORCH),
        REPEATER(org.bukkit.Material.REPEATER),
        REPEATING_COMMAND_BLOCK(org.bukkit.Material.REPEATING_COMMAND_BLOCK),
        ROSE_BUSH(org.bukkit.Material.ROSE_BUSH),
        ROSE_RED(org.bukkit.Material.ROSE_RED),
        ROTTEN_FLESH(org.bukkit.Material.ROTTEN_FLESH),
        SADDLE(org.bukkit.Material.SADDLE),
        SALMON(org.bukkit.Material.SALMON),
        SALMON_BUCKET(org.bukkit.Material.SALMON_BUCKET),
        SALMON_SPAWN_EGG(org.bukkit.Material.SALMON_SPAWN_EGG),
        SAND(org.bukkit.Material.SAND),
        SANDSTONE(org.bukkit.Material.SANDSTONE),
        SANDSTONE_SLAB(org.bukkit.Material.SANDSTONE_SLAB),
        SANDSTONE_STAIRS(org.bukkit.Material.SANDSTONE_STAIRS),
        SCUTE(org.bukkit.Material.SCUTE),
        SEA_LANTERN(org.bukkit.Material.SEA_LANTERN),
        SEA_PICKLE(org.bukkit.Material.SEA_PICKLE),
        SEAGRASS(org.bukkit.Material.SEAGRASS),
        SHEARS(org.bukkit.Material.SHEARS),
        SHEEP_SPAWN_EGG(org.bukkit.Material.SHEEP_SPAWN_EGG),
        SHIELD(org.bukkit.Material.SHIELD),
        SHULKER_BOX(org.bukkit.Material.SHULKER_BOX),
        SHULKER_SHELL(org.bukkit.Material.SHULKER_SHELL),
        SHULKER_SPAWN_EGG(org.bukkit.Material.SHULKER_SPAWN_EGG),
        SIGN(org.bukkit.Material.SIGN),
        LIGHT_GRAY_GLAZED_TERRACOTTA(org.bukkit.Material.LIGHT_GRAY_GLAZED_TERRACOTTA),
        LIGHT_GRAY_SHULKER_BOX(org.bukkit.Material.LIGHT_GRAY_SHULKER_BOX),
        SILVERFISH_SPAWN_EGG(org.bukkit.Material.SILVERFISH_SPAWN_EGG),
        SKELETON_HORSE_SPAWN_EGG(org.bukkit.Material.SKELETON_HORSE_SPAWN_EGG),
        SKELETON_SKULL(org.bukkit.Material.SKELETON_SKULL),
        SKELETON_SPAWN_EGG(org.bukkit.Material.SKELETON_SPAWN_EGG),
        SLIME_BALL(org.bukkit.Material.SLIME_BALL),
        SLIME_BLOCK(org.bukkit.Material.SLIME_BLOCK),
        SLIME_SPAWN_EGG(org.bukkit.Material.SLIME_SPAWN_EGG),
        SMOOTH_QUARTZ(org.bukkit.Material.SMOOTH_QUARTZ),
        SMOOTH_RED_SANDSTONE(org.bukkit.Material.SMOOTH_RED_SANDSTONE),
        SMOOTH_SANDSTONE(org.bukkit.Material.SMOOTH_SANDSTONE),
        SMOOTH_STONE(org.bukkit.Material.SMOOTH_STONE),
        SNOW(org.bukkit.Material.SNOW),
        SNOW_BLOCK(org.bukkit.Material.SNOW_BLOCK),
        SNOWBALL(org.bukkit.Material.SNOWBALL),
        SOUL_SAND(org.bukkit.Material.SOUL_SAND),
        SPECTRAL_ARROW(org.bukkit.Material.SPECTRAL_ARROW),
        SPIDER_EYE(org.bukkit.Material.SPIDER_EYE),
        SPIDER_SPAWN_EGG(org.bukkit.Material.SPIDER_SPAWN_EGG),
        SPLASH_POTION(org.bukkit.Material.SPLASH_POTION),
        SPONGE(org.bukkit.Material.SPONGE),
        SPRUCE_BOAT(org.bukkit.Material.SPRUCE_BOAT),
        SPRUCE_BUTTON(org.bukkit.Material.SPRUCE_BUTTON),
        SPRUCE_DOOR(org.bukkit.Material.SPRUCE_DOOR),
        SPRUCE_FENCE(org.bukkit.Material.SPRUCE_FENCE),
        SPRUCE_FENCE_GATE(org.bukkit.Material.SPRUCE_FENCE_GATE),
        SPRUCE_LEAVES(org.bukkit.Material.SPRUCE_LEAVES),
        SPRUCE_LOG(org.bukkit.Material.SPRUCE_LOG),
        SPRUCE_PLANKS(org.bukkit.Material.SPRUCE_PLANKS),
        SPRUCE_PRESSURE_PLATE(org.bukkit.Material.SPRUCE_PRESSURE_PLATE),
        SPRUCE_SAPLING(org.bukkit.Material.SPRUCE_SAPLING),
        SPRUCE_SLAB(org.bukkit.Material.SPRUCE_SLAB),
        SPRUCE_STAIRS(org.bukkit.Material.SPRUCE_STAIRS),
        SPRUCE_TRAPDOOR(org.bukkit.Material.SPRUCE_TRAPDOOR),
        SPRUCE_WOOD(org.bukkit.Material.SPRUCE_WOOD),
        SQUID_SPAWN_EGG(org.bukkit.Material.SQUID_SPAWN_EGG),
        STICK(org.bukkit.Material.STICK),
        STICKY_PISTON(org.bukkit.Material.STICKY_PISTON),
        STONE(org.bukkit.Material.STONE),
        STONE_AXE(org.bukkit.Material.STONE_AXE),
        STONE_BRICK_SLAB(org.bukkit.Material.STONE_BRICK_SLAB),
        STONE_BRICK_STAIRS(org.bukkit.Material.STONE_BRICK_STAIRS),
        STONE_BRICKS(org.bukkit.Material.STONE_BRICKS),
        STONE_BUTTON(org.bukkit.Material.STONE_BUTTON),
        STONE_HOE(org.bukkit.Material.STONE_HOE),
        STONE_PICKAXE(org.bukkit.Material.STONE_PICKAXE),
        STONE_PRESSURE_PLATE(org.bukkit.Material.STONE_PRESSURE_PLATE),
        STONE_SHOVEL(org.bukkit.Material.STONE_SHOVEL),
        STONE_SLAB(org.bukkit.Material.STONE_SLAB),
        COBBLESTONE_STAIRS(org.bukkit.Material.COBBLESTONE_STAIRS),
        STONE_SWORD(org.bukkit.Material.STONE_SWORD),
        STRAY_SPAWN_EGG(org.bukkit.Material.STRAY_SPAWN_EGG),
        STRING(org.bukkit.Material.STRING),
        STRIPPED_ACACIA_LOG(org.bukkit.Material.STRIPPED_ACACIA_LOG),
        STRIPPED_BIRCH_LOG(org.bukkit.Material.STRIPPED_BIRCH_LOG),
        STRIPPED_DARK_OAK_LOG(org.bukkit.Material.STRIPPED_DARK_OAK_LOG),
        STRIPPED_JUNGLE_LOG(org.bukkit.Material.STRIPPED_JUNGLE_LOG),
        STRIPPED_OAK_LOG(org.bukkit.Material.STRIPPED_OAK_LOG),
        STRIPPED_SPRUCE_LOG(org.bukkit.Material.STRIPPED_SPRUCE_LOG),
        STRUCTURE_BLOCK(org.bukkit.Material.STRUCTURE_BLOCK),
        STRUCTURE_VOID(org.bukkit.Material.STRUCTURE_VOID),
        SUGAR(org.bukkit.Material.SUGAR),
        SUGAR_CANE(org.bukkit.Material.SUGAR_CANE),
        SUNFLOWER(org.bukkit.Material.SUNFLOWER),
        TALL_GRASS(org.bukkit.Material.TALL_GRASS),
        TERRACOTTA(org.bukkit.Material.TERRACOTTA),
        TIPPED_ARROW(org.bukkit.Material.TIPPED_ARROW),
        TNT(org.bukkit.Material.TNT),
        TNT_MINECART(org.bukkit.Material.TNT_MINECART),
        TORCH(org.bukkit.Material.TORCH),
        TOTEM_OF_UNDYING(org.bukkit.Material.TOTEM_OF_UNDYING),
        TRAPPED_CHEST(org.bukkit.Material.TRAPPED_CHEST),
        TRIDENT(org.bukkit.Material.TRIDENT),
        TRIPWIRE_HOOK(org.bukkit.Material.TRIPWIRE_HOOK),
        TROPICAL_FISH(org.bukkit.Material.TROPICAL_FISH),
        TROPICAL_FISH_SPAWN_EGG(org.bukkit.Material.TROPICAL_FISH_SPAWN_EGG),
        TUBE_CORAL(org.bukkit.Material.TUBE_CORAL),
        TUBE_CORAL_BLOCK(org.bukkit.Material.TUBE_CORAL_BLOCK),
        TUBE_CORAL_FAN(org.bukkit.Material.TUBE_CORAL_FAN),
        TURTLE_EGG(org.bukkit.Material.TURTLE_EGG),
        TURTLE_HELMET(org.bukkit.Material.TURTLE_HELMET),
        TURTLE_SPAWN_EGG(org.bukkit.Material.TURTLE_SPAWN_EGG),
        VEX_SPAWN_EGG(org.bukkit.Material.VEX_SPAWN_EGG),
        VILLAGER_SPAWN_EGG(org.bukkit.Material.VILLAGER_SPAWN_EGG),
        VINDICATOR_SPAWN_EGG(org.bukkit.Material.VINDICATOR_SPAWN_EGG),
        VINE(org.bukkit.Material.VINE),
        WATER_BUCKET(org.bukkit.Material.WATER_BUCKET),
        WET_SPONGE(org.bukkit.Material.WET_SPONGE),
        WHEAT(org.bukkit.Material.WHEAT),
        WHEAT_SEEDS(org.bukkit.Material.WHEAT_SEEDS),
        WHITE_BANNER(org.bukkit.Material.WHITE_BANNER),
        WHITE_BED(org.bukkit.Material.WHITE_BED),
        WHITE_CARPET(org.bukkit.Material.WHITE_CARPET),
        WHITE_CONCRETE(org.bukkit.Material.WHITE_CONCRETE),
        WHITE_CONCRETE_POWDER(org.bukkit.Material.WHITE_CONCRETE_POWDER),
        WHITE_GLAZED_TERRACOTTA(org.bukkit.Material.WHITE_GLAZED_TERRACOTTA),
        WHITE_SHULKER_BOX(org.bukkit.Material.WHITE_SHULKER_BOX),
        WHITE_STAINED_GLASS(org.bukkit.Material.WHITE_STAINED_GLASS),
        WHITE_STAINED_GLASS_PANE(org.bukkit.Material.WHITE_STAINED_GLASS_PANE),
        WHITE_TERRACOTTA(org.bukkit.Material.WHITE_TERRACOTTA),
        WHITE_TULIP(org.bukkit.Material.WHITE_TULIP),
        WHITE_WOOL(org.bukkit.Material.WHITE_WOOL),
        WITCH_SPAWN_EGG(org.bukkit.Material.WITCH_SPAWN_EGG),
        WITHER_SKELETON_SKULL(org.bukkit.Material.WITHER_SKELETON_SKULL),
        WITHER_SKELETON_SPAWN_EGG(org.bukkit.Material.WITHER_SKELETON_SPAWN_EGG),
        WOLF_SPAWN_EGG(org.bukkit.Material.WOLF_SPAWN_EGG),
        WOODEN_AXE(org.bukkit.Material.WOODEN_AXE),
        WOODEN_HOE(org.bukkit.Material.WOODEN_HOE),
        WOODEN_PICKAXE(org.bukkit.Material.WOODEN_PICKAXE),
        WOODEN_SHOVEL(org.bukkit.Material.WOODEN_SHOVEL),
        WOODEN_SWORD(org.bukkit.Material.WOODEN_SWORD),
        WRITABLE_BOOK(org.bukkit.Material.WRITABLE_BOOK),
        WRITTEN_BOOK(org.bukkit.Material.WRITTEN_BOOK),
        YELLOW_BANNER(org.bukkit.Material.YELLOW_BANNER),
        YELLOW_BED(org.bukkit.Material.YELLOW_BED),
        YELLOW_CARPET(org.bukkit.Material.YELLOW_CARPET),
        YELLOW_CONCRETE(org.bukkit.Material.YELLOW_CONCRETE),
        YELLOW_CONCRETE_POWDER(org.bukkit.Material.YELLOW_CONCRETE_POWDER),
        YELLOW_GLAZED_TERRACOTTA(org.bukkit.Material.YELLOW_GLAZED_TERRACOTTA),
        YELLOW_SHULKER_BOX(org.bukkit.Material.YELLOW_SHULKER_BOX),
        YELLOW_STAINED_GLASS(org.bukkit.Material.YELLOW_STAINED_GLASS),
        YELLOW_STAINED_GLASS_PANE(org.bukkit.Material.YELLOW_STAINED_GLASS_PANE),
        YELLOW_TERRACOTTA(org.bukkit.Material.YELLOW_TERRACOTTA),
        YELLOW_WOOL(org.bukkit.Material.YELLOW_WOOL),
        ZOMBIE_HEAD(org.bukkit.Material.ZOMBIE_HEAD),
        ZOMBIE_HORSE_SPAWN_EGG(org.bukkit.Material.ZOMBIE_HORSE_SPAWN_EGG),
        ZOMBIE_PIGMAN_SPAWN_EGG(org.bukkit.Material.ZOMBIE_PIGMAN_SPAWN_EGG),
        ZOMBIE_SPAWN_EGG(org.bukkit.Material.ZOMBIE_SPAWN_EGG),
        ZOMBIE_VILLAGER_SPAWN_EGG(org.bukkit.Material.ZOMBIE_VILLAGER_SPAWN_EGG);

        Material(org.bukkit.Material mat) {
            this.m = mat;
            this.d = 0;
        }

        final public org.bukkit.Material m;
        final public int d;

    }

    private static final NMS_1_13_R1 INSTANCE = new NMS_1_13_R1();

    private static final Material[] UNBREAKABLE_BLOCKS = {Material.AIR, Material.BEDROCK, Material.WATER,
        Material.LAVA, PISTON_HEAD, MOVING_PISTON, END_PORTAL, NETHER_PORTAL,
        END_PORTAL_FRAME, DRAGON_EGG, BARRIER, END_GATEWAY, STRUCTURE_BLOCK, END_GATEWAY};

    private static final Material[] STORAGE_BLOCKS = {DISPENSER, SPAWNER, CHEST, FURNACE,
        JUKEBOX, ENDER_CHEST, COMMAND_BLOCK, BEACON, TRAPPED_CHEST, HOPPER, DROPPER,
        OBSERVER,
        PURPLE_SHULKER_BOX, RED_SHULKER_BOX, ORANGE_SHULKER_BOX, YELLOW_SHULKER_BOX,
        LIME_SHULKER_BOX, GREEN_SHULKER_BOX, LIGHT_BLUE_SHULKER_BOX, BLUE_SHULKER_BOX,
        BLACK_SHULKER_BOX, GRAY_SHULKER_BOX, WHITE_SHULKER_BOX, BROWN_SHULKER_BOX,
        CYAN_SHULKER_BOX, MAGENTA_SHULKER_BOX, PINK_SHULKER_BOX, LIGHT_GRAY_SHULKER_BOX
    };

    private static final Material[] INTERACTABLE_BLOCKS = {
        DISPENSER, NOTE_BLOCK, CHEST, CRAFTING_TABLE, FURNACE,
         LEVER, STONE_BUTTON, JUKEBOX,
         BREWING_STAND, ENDER_CHEST, COMMAND_BLOCK, BEACON,
        ANVIL, TRAPPED_CHEST, COMPARATOR, DAYLIGHT_DETECTOR,
        HOPPER, DROPPER, SPRUCE_FENCE_GATE, BIRCH_FENCE_GATE, JUNGLE_FENCE_GATE, DARK_OAK_FENCE_GATE,
        ACACIA_FENCE_GATE, SPRUCE_DOOR, BIRCH_DOOR, JUNGLE_DOOR, ACACIA_DOOR, DARK_OAK_DOOR, OBSERVER,
        PURPLE_SHULKER_BOX, STRUCTURE_BLOCK, PURPLE_BED, RED_BED, ORANGE_BED, YELLOW_BED,
        LIME_BED, GREEN_BED, LIGHT_BLUE_BED, BLUE_BED,
        BLACK_BED, GRAY_BED, WHITE_BED, BROWN_BED,
        CYAN_BED, MAGENTA_BED, PINK_BED, LIGHT_GRAY_BED
    };

    private static final Material ORES[] = new Material[]{COAL_ORE, REDSTONE_ORE, DIAMOND_ORE, GOLD_ORE,
        IRON_ORE, LAPIS_ORE, GLOWSTONE, QUARTZ, EMERALD_ORE};

    private static final Material[] TERRAFORMER_MATERIALS = {STONE, GRASS, DIRT, COBBLESTONE, SAND, GRAVEL,
         PURPLE_GLAZED_TERRACOTTA,
    RED_GLAZED_TERRACOTTA, ORANGE_GLAZED_TERRACOTTA, YELLOW_GLAZED_TERRACOTTA, LIME_GLAZED_TERRACOTTA,
    GREEN_GLAZED_TERRACOTTA, LIGHT_BLUE_SHULKER_BOX, BLUE_GLAZED_TERRACOTTA, BLACK_GLAZED_TERRACOTTA,
    GRAY_GLAZED_TERRACOTTA, WHITE_GLAZED_TERRACOTTA, BROWN_GLAZED_TERRACOTTA, CYAN_GLAZED_TERRACOTTA,
    MAGENTA_GLAZED_TERRACOTTA, PINK_GLAZED_TERRACOTTA, LIGHT_GRAY_GLAZED_TERRACOTTA};

    private static final EntityType[] TRANSFORMATION_ENTITY_TYPES = new EntityType[]{BAT, VEX, STRAY, SKELETON, HUSK, ZOMBIE, SILVERFISH, ENDERMITE, ZOMBIE, PIG_ZOMBIE, VILLAGER, WITCH, COW, MUSHROOM_COW, SLIME, MAGMA_CUBE, WITHER_SKULL, SKELETON, OCELOT, WOLF};

    public static NMS_1_13_R1 getInstance() {
        return INSTANCE;
    }

    public static Material getMaterialFromBlock(Block blk) {
        if (blk.isLiquid()) {

        }



        return null;
    }

    /**
     * @return the UNBREAKABLE_BLOCKS
     */
    /*@Override
    public Material[] getUnbreakableBlocks() {
        return UNBREAKABLE_BLOCKS;
    }*.

    /**
     * @return the STORAGE_BLOCKS
     */
    /*@Override
    public Material[] getStorageBlocks() {
        return STORAGE_BLOCKS;
    }*/

    /**
     * @return the INTERACTABLE_BLOCKS
     */
    /*@Override
    public Material[] getInteractableBlocks() {
        return INTERACTABLE_BLOCKS;
    }*/

    /**
     * @return the ores
     */
    /*@Override
    public Material[] getOres() {
        return ORES;
    }*/

    /*public static Material[] getTerraformerMaterials() {
        return TERRAFORMER_MATERIALS;
    }*/

    /*@Override
    public EntityType[] getTransformationEntityTypes() {
        return TRANSFORMATION_ENTITY_TYPES;
    }*/

    private NMS_1_13_R1() {
    }

    @Override
    public boolean breakBlockNMS(Block block, Player player) {
        EntityPlayer ep = ((CraftPlayer) player).getHandle();
        boolean success = ep.playerInteractManager.breakBlock(new BlockPosition(block.getX(), block.getY(), block.getZ()));
        return success;
    }

    @Override
    public boolean shearEntityNMS(Entity target, Player player, boolean mainHand) {
        if (target instanceof CraftSheep) {
            EntitySheep entitySheep = ((CraftSheep) target).getHandle();
            return entitySheep.a(((CraftPlayer) player).getHandle(), mainHand ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
        } else if (target instanceof CraftMushroomCow) {
            EntityMushroomCow entityMushroomCow = ((CraftMushroomCow) target).getHandle();
            return entityMushroomCow.a(((CraftPlayer) player).getHandle(), mainHand ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
        }
        return false;
    }

    @Override
    public boolean showShulker(Block blockToHighlight, int entityId, Player player) {
        PacketPlayOutSpawnEntityLiving pposel = generateShulkerSpawnPacket(blockToHighlight, entityId);
        if (pposel == null) {
            return false;
        }
        EntityPlayer ep = ((CraftPlayer) player).getHandle();
        ep.playerConnection.networkManager.sendPacket(pposel);
        return true;
    }

    @Override
    public boolean hideShulker(int entityId, Player player) {
        PacketPlayOutEntityDestroy ppoed = new PacketPlayOutEntityDestroy(entityId);
        EntityPlayer ep = ((CraftPlayer) player).getHandle();
        ep.playerConnection.networkManager.sendPacket(ppoed);
        return true;
    }

    @Override
    public Entity spawnGuardian(Location loc, boolean elderGuardian) {
        return loc.getWorld().spawnEntity(loc, elderGuardian ? EntityType.ELDER_GUARDIAN : EntityType.GUARDIAN);
    }

    @Override
    public boolean isZombie(Entity e) {
        return e.getType() == EntityType.ZOMBIE || e.getType() == EntityType.ZOMBIE_VILLAGER;
    }

    /*@Override
    public boolean isBlockSafeToBreak(Block b) {
        Material mat = b.getType();
        return mat.isSolid()
            && !b.isLiquid()
            && !ArrayUtils.contains(INTERACTABLE_BLOCKS, mat)
            && !ArrayUtils.contains(UNBREAKABLE_BLOCKS, mat)
            && !ArrayUtils.contains(STORAGE_BLOCKS, mat);
    }*/

    private static PacketPlayOutSpawnEntityLiving generateShulkerSpawnPacket(Block blockToHighlight, int entityId) {
        PacketPlayOutSpawnEntityLiving pposel = new PacketPlayOutSpawnEntityLiving();
        Class clazz = pposel.getClass();

        try {
            Field f = clazz.getDeclaredField("a");
            f.setAccessible(true);
            f.setInt(pposel, entityId);
            f = clazz.getDeclaredField("b");
            f.setAccessible(true);
            f.set(pposel, new UUID(0xFF00FF00FF00FF00L, 0xFF00FF00FF00FF00L));
            f = clazz.getDeclaredField("c");
            f.setAccessible(true);
            f.setInt(pposel, 69);
            f = clazz.getDeclaredField("d");
            f.setAccessible(true);
            f.setDouble(pposel, blockToHighlight.getX() + 0.5);
            f = clazz.getDeclaredField("e");
            f.setAccessible(true);
            f.setDouble(pposel, blockToHighlight.getY());
            f = clazz.getDeclaredField("f");
            f.setAccessible(true);
            f.setDouble(pposel, blockToHighlight.getZ() + 0.5);
            f = clazz.getDeclaredField("g");
            f.setAccessible(true);
            f.setInt(pposel, 0);
            f = clazz.getDeclaredField("h");
            f.setAccessible(true);
            f.setInt(pposel, 0);
            f = clazz.getDeclaredField("i");
            f.setAccessible(true);
            f.setInt(pposel, 0);
            f = clazz.getDeclaredField("j");
            f.setAccessible(true);
            f.setByte(pposel, (byte) 0);
            f = clazz.getDeclaredField("k");
            f.setAccessible(true);
            f.setByte(pposel, (byte) 0);
            f = clazz.getDeclaredField("l");
            f.setAccessible(true);
            f.setByte(pposel, (byte) 0);

            DataWatcher m = new FakeDataWatcher();
            f = clazz.getDeclaredField("m");
            f.setAccessible(true);
            f.set(pposel, m);

        } catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
            return null;
        }

        return pposel;
    }

    private static class FakeDataWatcher extends DataWatcher {

        public FakeDataWatcher() {
            super(null); // We don't actually need DataWatcher methods, just the inheritance
        }

        // Inject metadata into network stream
        @Override
        public void a(PacketDataSerializer pds) throws IOException {
            pds.writeByte(0); // Set Metadata at index 0
            pds.writeByte(0); // Value is type Byte
            pds.writeByte(0x60); // Set Glowing and Invisible bits
            pds.writeByte(0xFF); // Index -1 indicates end of Metadata
        }
    }
}

