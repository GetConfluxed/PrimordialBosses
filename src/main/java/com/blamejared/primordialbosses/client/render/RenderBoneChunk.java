package com.blamejared.primordialbosses.client.render;

import com.blamejared.primordialbosses.entities.EntityBoneChunk;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

import static com.blamejared.primordialbosses.entities.EntityBoneChunk.BLOCK_POSITIONS;

public class RenderBoneChunk extends Render<EntityBoneChunk> {
    
   
    public RenderBoneChunk(RenderManager renderManagerIn) {
        super(renderManagerIn);
        
    }
    
    public void doRender(EntityBoneChunk entity, double x, double y, double z, float entityYaw, float partialTicks) {
//        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        this.bindEntityTexture(entity);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.translate(-1,0,-1);
        for(BlockPos pos : BLOCK_POSITIONS) {
            GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ());
            renderBlockModel(entity.world, entity.getPosition().add(0, Math.floor(entity.height/2), -entity.width / 2).add(pos), Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Z), true);
            GlStateManager.translate(-pos.getX(), -pos.getY(), -pos.getZ());
        }
        
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
    
    public static void renderBlockModel(World world, BlockPos pos, IBlockState state, boolean translateToOrigin) {
        buff().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        if(translateToOrigin) {
            buff().setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());
        }
        
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        BlockModelShapes modelShapes = blockrendererdispatcher.getBlockModelShapes();
        IBakedModel ibakedmodel = modelShapes.getModelForState(state);
        bind(TextureMap.LOCATION_BLOCKS_TEXTURE);
        for(BlockRenderLayer layer : BlockRenderLayer.values()) {
            if(state.getBlock().canRenderInLayer(state, layer)) {
                ForgeHooksClient.setRenderLayer(layer);
                blockrendererdispatcher.getBlockModelRenderer().renderModel(world, ibakedmodel, state, pos, buff(), false);
            }
        }
        ForgeHooksClient.setRenderLayer(null);
        if(translateToOrigin) {
            buff().setTranslation(0, 0, 0);
        }
        Tessellator.getInstance().draw();
    }
    
    public static void bind(ResourceLocation texture) {
        mc().renderEngine.bindTexture(texture);
    }
    
    public static BufferBuilder buff() {
        return tess().getBuffer();
    }
    
    public static Tessellator tess() {
        return Tessellator.getInstance();
    }
    
    public static Minecraft mc() {
        return Minecraft.getMinecraft();
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityBoneChunk entity) {
        return null;
    }
}
