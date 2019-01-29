package com.blamejared.primordialbosses.entities;

import com.google.common.base.*;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class EntityBoneChunk extends EntityArrow {
    
    public static final BlockPos[] BLOCK_POSITIONS = new BlockPos[]{new BlockPos(0, 0, 0), new BlockPos(1, 0, 0), new BlockPos(1, 1, 0), new BlockPos(0, 1, 0), new BlockPos(0, 0, 1), new BlockPos(1, 0, 1), new BlockPos(1, 1, 1), new BlockPos(0, 1, 1)};
    
    private final Predicate<Entity> ARROW_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, new Predicate<Entity>() {
        public boolean apply(@Nullable Entity p_apply_1_) {
            return p_apply_1_.canBeCollidedWith();
        }
    }, input -> input.getRidingEntity() == null || !input.getRidingEntity().isEntityEqual(this), input -> !input.getIsInvulnerable());
    
    public EntityBoneChunk(World worldIn) {
        super(worldIn);
        setSize(2, 2);
        this.setDamage(6);
    }
    
    public EntityBoneChunk(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        setSize(2, 2);
        this.setDamage(6);
    }
    
    public EntityBoneChunk(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
        setSize(2, 2);
        this.setDamage(6);
    }
    
    private int ticksInGround = 0;
    private int ticksInGroundMax = 100;
    
    @Nullable
    protected Entity findEntityOnPath(Vec3d start, Vec3d end) {
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D), ARROW_TARGETS);
        if(!list.isEmpty()) {
            return super.findEntityOnPath(start, end);
        }
        return null;
    }
    
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("ticksInGroundCustom", ticksInGround);
        return super.writeToNBT(compound);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.ticksInGround = compound.getInteger("ticksInGroundCustom");
    }
    
    @Override
    public void onUpdate() {
        if(this.inGround) {
            this.setPosition(getPosition().getX(), getPosition().getY(), getPosition().getZ());
            for(BlockPos pos : BLOCK_POSITIONS) {
                world.setBlockState(pos.add(getPosition()), Blocks.BONE_BLOCK.getDefaultState());
            }
            this.setDead();
            return;
        }
        super.onUpdate();
        if(this.inGround && !this.getPassengers().isEmpty()) {
            for(Entity entity : getPassengers()) {
                entity.setEntityInvulnerable(false);
            }
            this.removePassengers();
        }
    }
    
    @Override
    public void setDead() {
        super.setDead();
        if(!this.getPassengers().isEmpty()) {
            for(Entity entity : getPassengers()) {
                entity.setEntityInvulnerable(false);
            }
        }
    }
    
    public double getMountedYOffset() {
        return (double) this.height * 0.31D;
    }
    
    @Override
    protected ItemStack getArrowStack() {
        return ItemStack.EMPTY;
    }
}
