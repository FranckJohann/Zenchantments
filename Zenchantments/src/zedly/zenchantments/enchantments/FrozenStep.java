package zedly.zenchantments.enchantments;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import zedly.zenchantments.CustomEnchantment;
import zedly.zenchantments.enums.Hand;
import zedly.zenchantments.enums.Tool;

import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Material.*;
import static zedly.zenchantments.enums.Tool.BOOTS;

public class FrozenStep extends CustomEnchantment {

	// Blocks spawned from the Water Walker enchantment
	public static final Map<Location, Long> frozenLocs = new HashMap<>();
    public static final int ID = 17;

    @Override
    public Builder<FrozenStep> defaults() {
        return new Builder<>(FrozenStep::new, ID)
            .maxLevel(3)
            .loreName("Frozen Step")
            .probability(0)
            .enchantable(new Tool[]{BOOTS})
            .conflicting(new Class[]{NetherStep.class})
            .description("Allows the player to walk on water and safely emerge from it when sneaking")
            .cooldown(0)
            .power(1.0)
            .handUse(Hand.NONE);
    }

    public boolean onScan(Player player, int level, boolean usedHand) {
        if(player.isSneaking() && player.getLocation().getBlock().getType() == STATIONARY_WATER &&
           !player.isFlying()) {
            player.setVelocity(player.getVelocity().setY(.4));
        }
        Block block = (Block) player.getLocation().getBlock();
        int radius = (int) Math.round(power * level + 2);
        for(int x = -(radius); x <= radius; x++) {
            for(int z = -(radius); z <= radius; z++) {
                Block possiblePlatformBlock = block.getRelative(x, -1, z);
                Location possiblePlatformLoc = possiblePlatformBlock.getLocation();
                if(possiblePlatformLoc.distanceSquared(block.getLocation()) < radius * radius - 2) {
                    if(frozenLocs.containsKey(possiblePlatformLoc)) {
                        frozenLocs.put(possiblePlatformLoc, System.nanoTime());
                    } else if(possiblePlatformBlock.getType() == STATIONARY_WATER
                              && possiblePlatformBlock.getData() == 0
                              && possiblePlatformBlock.getRelative(0, 1, 0).getType() == AIR) {
                        if(ADAPTER.formBlock(possiblePlatformBlock, PACKED_ICE, (byte) 0, player)) {
                            frozenLocs.put(possiblePlatformLoc, System.nanoTime());
                        }
                    }
                }
            }
        }
        return true;
    }
}
