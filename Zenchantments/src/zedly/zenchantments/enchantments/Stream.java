package zedly.zenchantments.enchantments;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import zedly.zenchantments.CustomEnchantment;
import zedly.zenchantments.Storage;
import zedly.zenchantments.Utilities;
import zedly.zenchantments.enums.Hand;
import zedly.zenchantments.enums.Tool;

import static org.bukkit.event.block.Action.RIGHT_CLICK_AIR;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;
import static zedly.zenchantments.enums.Tool.WINGS;

public class Stream extends CustomEnchantment {

    private static final Particle[] trailTypes = {
            Particle.CLOUD,
            Particle.CRIT,
            Particle.VILLAGER_HAPPY,
            Particle.REDSTONE,
            Particle.HEART,};
    public static final  int        ID         = 74;

    @Override
    public Builder<Stream> defaults() {
        return new Builder<>(Stream::new, ID)
            .maxLevel(1)
            .loreName("Stream")
            .probability(0)
            .enchantable(new Tool[]{WINGS})
            .conflicting(new Class[]{})
            .description("Creates a trail of particles when in flight")
            .cooldown(0)
            .power(1.0)
            .handUse(Hand.NONE);
    }

    @Override
    public boolean onBlockInteract(PlayerInteractEvent evt, int level, boolean usedHand) {
        if(evt.getItem() == null || evt.getItem().getType() != Material.ELYTRA) {
            return false;
        }
        Player player = evt.getPlayer();

        if(!evt.getPlayer().hasMetadata("ze.stream.mode")) {
            player.setMetadata("ze.stream.mode", new FixedMetadataValue(Storage.zenchantments, 0));
        }
        if(player.isSneaking() && (evt.getAction() == RIGHT_CLICK_AIR || evt.getAction() == RIGHT_CLICK_BLOCK)) {
            int b = player.getMetadata("ze.stream.mode").get(0).asInt();
            b = b == 4 ? 0 : b + 1;
            player.setMetadata("ze.stream.mode", new FixedMetadataValue(Storage.zenchantments, b));
            switch(b) {
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
            if((player.getInventory().getChestplate() == null ||
                player.getInventory().getChestplate().getType() == Material.AIR)) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(Storage.zenchantments, () -> {
                    if((player.getInventory().getItemInMainHand() == null ||
                        player.getInventory().getItemInMainHand().getType() == Material.AIR)) {
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
        if(player.isGliding() && player.getVelocity().length() >= 0.5) {
            if(!player.hasMetadata("ze.stream.mode")) {
                player.setMetadata("ze.stream.mode", new FixedMetadataValue(Storage.zenchantments, 0));
            }
            int b = player.getMetadata("ze.stream.mode").get(0).asInt();
            Utilities.display(player.getLocation(), trailTypes[b], 3, 0.1, 0, 0, 0);
            return true;
        }
        return false;
    }

}
