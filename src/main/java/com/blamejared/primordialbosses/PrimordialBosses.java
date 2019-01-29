package com.blamejared.primordialbosses;

import com.blamejared.primordialbosses.client.render.*;
import com.blamejared.primordialbosses.entities.*;
import com.blamejared.primordialbosses.references.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.*;


@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class PrimordialBosses {
    
    @Mod.EventHandler
    public void onFMLPreInitialization(FMLPreInitializationEvent event) {
        EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID, "bigarrow"), EntityBigArrow.class, "bigarrow", 0, this, 80, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID, "calciumskeleton"), EntityCalciumSkeleton.class, "calciumskeleton", 1, this, 80, 3, true, 0xFFFFFFFF, 0xFFAAAAAA);
        EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID, "bonechunk"), EntityBoneChunk.class, "bonechunk", 3, this, 80, 3, true);
    }
    
    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void onClientPreInit(FMLPreInitializationEvent event) {
        
        RenderingRegistry.registerEntityRenderingHandler(EntityCalciumSkeleton.class, RenderCalciumSkeleton::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBigArrow.class, RenderBigArrow::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBoneChunk.class, RenderBoneChunk::new);
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    
    @SubscribeEvent
    public void onTickClientTick(TickEvent.ClientTickEvent event) {
    }
    
}
