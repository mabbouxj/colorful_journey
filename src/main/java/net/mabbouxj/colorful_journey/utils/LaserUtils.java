package net.mabbouxj.colorful_journey.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class LaserUtils {

    public static Optional<Entity> getFirstEntityOnLine(World world, Vector3d from, Vector3d to, Predicate<Entity> filter) {
        Class<Entity> entityType = Entity.class;
        return getEntitiesOnLine(world, from, to)
                .filter(entityType::isInstance)
                .map(entityType::cast)
                .filter(filter)
                .map(e -> IPair.of(e, e.distanceToSqr(from.x, from.y, from.z)))
                .min(Comparator.comparing(IPair::getB))
                .map(IPair::getA);
    }

    public static Stream<Entity> getEntitiesOnLine(World world, Vector3d from, Vector3d to) {
        return world.getEntities((Entity) null, new AxisAlignedBB(from.x, from.y, from.z, to.x, to.y, to.z), null)
                .stream().filter(e -> intersectsLine(e.getBoundingBox().inflate(0.5D), from, to));
    }

    public static boolean intersectsLine(AxisAlignedBB prism, Vector3d lineMin, Vector3d lineMax) {
        double x1 = lineMin.x, x2 = lineMax.x, dx = x2 - x1;
        double y1 = lineMin.y, y2 = lineMax.y, dy = y2 - y1;
        double z1 = lineMin.z, z2 = lineMax.z, dz = z2 - z1;
        double dydx = dy / dx, dzdx = dz / dx, dzdy = dz / dy;
        double inter1, inter2;
        // project to xy plane
        inter1 = (prism.minX - x1) * dydx + y1;
        inter2 = (prism.maxX - x1) * dydx + y1;
        if (Math.min(inter1, inter2) > prism.maxY || Math.max(inter1, inter2) < prism.minY) return false;
        // project to xz plane
        inter1 = (prism.minX - x1) * dzdx + z1;
        inter2 = (prism.maxX - x1) * dzdx + z1;
        if (Math.min(inter1, inter2) > prism.maxZ || Math.max(inter1, inter2) < prism.minZ) return false;
        // project to yz plane
        inter1 = (prism.minY - y1) * dzdy + z1;
        inter2 = (prism.maxY - y1) * dzdy + z1;
        return !(Math.min(inter1, inter2) > prism.maxZ || Math.max(inter1, inter2) < prism.minZ);
    }

}
