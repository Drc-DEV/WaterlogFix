package pro.dracarys.WaterlogFix.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import pro.dracarys.WaterlogFix.WaterlogFix;

public class ExplosionListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onExplosion(EntityExplodeEvent e) {
        if (WaterlogFix.getInstance().getConfig().getStringList("Settings.enabled-worlds").stream().noneMatch(e.getLocation().getWorld().getName()::equalsIgnoreCase))
            return;
        int radius = WaterlogFix.getInstance().getConfig().getInt("Settings.radius-check");
        double chance = WaterlogFix.getInstance().getConfig().getDouble("Settings.drain-chance");
        if (radius <= 0 || chance <= 0) return;
        if (chance > 1) chance = 1;
        drainInRadius(e.getLocation(), e.getYield(), radius, chance);
    }

    private void drainInRadius(Location source, float yield, double dmgRadius, double chance) {
        if (yield > 1) dmgRadius += yield / 10;
        int radius = (int) Math.ceil(dmgRadius);
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Location loc = new Location(source.getWorld(), x + source.getX(), y + source.getY(), z + source.getZ());
                    if (source.distance(loc) <= dmgRadius && loc.getY() > 0 && (chance == 1 || Math.random() <= chance)) {
                        setWaterlogged(loc.getBlock(), false);
                    }
                }
            }
        }
    }

    private static boolean isWaterlogged(Block block) {
        if (block == null || block.getType().equals(Material.AIR)) {
            return false;
        }
        BlockData blockData = block.getBlockData();
        return blockData instanceof Waterlogged && ((Waterlogged) blockData).isWaterlogged();
    }

    private static void setWaterlogged(Block block, boolean waterlogged) {
        if (!isWaterlogged(block)) return;
        Waterlogged blockData = (Waterlogged) block.getBlockData();
        BlockState bs = block.getState();
        blockData.setWaterlogged(waterlogged);
        bs.setBlockData(blockData);
        bs.update(true, false);
    }

}
