package zedly.zenchantments.enchantments;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import zedly.zenchantments.*;
import zedly.zenchantments.enums.Tool;

import static zedly.zenchantments.enums.Tool.BOW_;
import static zedly.zenchantments.enums.Tool.SWORD;

public class Vortex extends CustomEnchantment {

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
        EnchantArrow.ArrowEnchantVortex arrow =
                new EnchantArrow.ArrowEnchantVortex((Projectile) evt.getProjectile());
        Utilities.putArrow(evt.getProjectile(), arrow, (Player) evt.getEntity());
        return true;
    }

}
