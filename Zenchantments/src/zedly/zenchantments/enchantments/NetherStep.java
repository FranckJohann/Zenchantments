package zedly.zenchantments.enchantments;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import zedly.zenchantments.CustomEnchantment;
import zedly.zenchantments.annotations.EffectTask;
import zedly.zenchantments.enums.Frequency;
import zedly.zenchantments.enums.Hand;
import zedly.zenchantments.enums.Tool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.bukkit.Material.*;
import static zedly.zenchantments.enums.Tool.BOOTS;

public class NetherStep extends CustomEnchantment {

	// Blocks spawned from the NatherStep enchantment
	public static final Map<Location, Long> netherstepLocs = new HashMap<>();
	public static final int ID = 39;

	@Override
	public Builder<NetherStep> defaults() {
		return new Builder<>(NetherStep::new, ID)
			.maxLevel(3)
			.loreName("Nether Step")
			.probability(0)
			.enchantable(new Tool[]{BOOTS})
			.conflicting(new Class[]{FrozenStep.class})
			.description("Allows the player to slowly but safely walk on lava")
			.cooldown(0)
			.power(1.0)
			.handUse(Hand.NONE);
    }

    @Override
    public boolean onScan(Player player, int level, boolean usedHand) {
        if(player.isSneaking() && player.getLocation().getBlock().getType() == STATIONARY_LAVA &&
           !player.isFlying()) {
            player.setVelocity(player.getVelocity().setY(.4));
        }
        Block block = (Block) player.getLocation().add(0, 0.2, 0).getBlock();
        int radius = (int) Math.round(power * level + 2);
        for(int x = -(radius); x <= radius; x++) {
            for(int z = -(radius); z <= radius; z++) {
                Block possiblePlatformBlock = block.getRelative(x, -1, z);
                Location possiblePlatformLoc = possiblePlatformBlock.getLocation();
                if(possiblePlatformLoc.distanceSquared(block.getLocation()) < radius * radius - 2) {
                    if(netherstepLocs.containsKey(possiblePlatformLoc)) {
                        netherstepLocs.put(possiblePlatformLoc, System.nanoTime());
                    } else if(possiblePlatformBlock.getType() == STATIONARY_LAVA
                              && possiblePlatformBlock.getData() == 0
                              && possiblePlatformBlock.getRelative(0, 1, 0).getType() == AIR) {
                        if(ADAPTER.formBlock(possiblePlatformBlock, SOUL_SAND, (byte) 0, player)) {
                            netherstepLocs.put(possiblePlatformLoc, System.nanoTime());
                        }
                    }
                }
            }
        }
        return true;
    }

	@EffectTask(Frequency.MEDIUM_HIGH)
	// Removes the blocks from NetherStep and FrozenStep after a peroid of time
	public static void updateBlocks() {
		Iterator it = FrozenStep.frozenLocs.keySet().iterator();
		while (it.hasNext()) {
			Location location = (Location) it.next();
			if (Math.abs(System.nanoTime() - FrozenStep.frozenLocs.get(location)) > 9E8) {
				location.getBlock().setType(STATIONARY_WATER);
				it.remove();
			}
		}
		it = netherstepLocs.keySet().iterator();
		while (it.hasNext()) {
			Location location = (Location) it.next();
			if (Math.abs(System.nanoTime() - netherstepLocs.get(location)) > 9E8) {
				location.getBlock().setType(STATIONARY_LAVA);
				it.remove();
			}
		}
	}
}
