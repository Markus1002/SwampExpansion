package com.farcr.swampexpansion.core;

import com.farcr.swampexpansion.common.block.fluid.MudFluid;
import com.farcr.swampexpansion.common.item.SwampExSpawnEggItem;
import com.farcr.swampexpansion.core.registries.SwampExBiomes;
import com.farcr.swampexpansion.core.registries.SwampExBlocks;
import com.farcr.swampexpansion.core.registries.SwampExData;
import com.farcr.swampexpansion.core.registries.SwampExEntities;
import com.farcr.swampexpansion.core.registries.SwampExFeatures;
import com.farcr.swampexpansion.core.registries.SwampExItems;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("swampexpansion")
@Mod.EventBusSubscriber(modid = "swampexpansion")
public class SwampExpansion {
	public static final String MODID = "swampexpansion";

    public SwampExpansion() {
    	IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	
        SwampExBlocks.BLOCKS.register(modEventBus);
        SwampExItems.ITEMS.register(modEventBus);
        SwampExEntities.ENTITY_TYPES.register(modEventBus);
        SwampExBlocks.PAINTINGS.register(modEventBus);
        SwampExBiomes.BIOMES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        
        modEventBus.addListener(this::setupCommon);
    	DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
        	modEventBus.addListener(this::setupClient);
        	modEventBus.addListener(this::registerItemColors);
        });
    }

    private void setupCommon(final FMLCommonSetupEvent event) {
        SwampExData.registerBlockData();
        SwampExBiomes.registerBiomesToDictionary();
        SwampExFeatures.generateFeatures();
    	SwampExEntities.addEntitySpawns();
    }
    
    private void setupClient(final FMLClientSetupEvent event) {
    	SwampExData.setRenderLayers();
    	SwampExEntities.registerRendering();
        SwampExData.registerBlockColors();
        
    }
    
    @OnlyIn(Dist.CLIENT)
	private void registerItemColors(ColorHandlerEvent.Item event) {
		for(RegistryObject<Item> items : SwampExItems.SPAWN_EGGS) {
			//RegistryObject#isPresent causes a null pointer when it's false :crying: thanks forge
			if(ObfuscationReflectionHelper.getPrivateValue(RegistryObject.class, items, "value") != null) {
				Item item = items.get();
				if(item instanceof SwampExSpawnEggItem) {
					event.getItemColors().register((itemColor, itemsIn) -> {
						return ((SwampExSpawnEggItem) item).getColor(itemsIn);
					}, item);
				}
			}
		}
	}
    
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onFogColor(FogColors event) {
        ActiveRenderInfo info = event.getInfo();
        IFluidState state = info.getFluidState();
        if (state.getFluid() instanceof MudFluid) {
            event.setRed(0.140625F);
            event.setGreen(0.0625F);
            event.setBlue(0.015625F);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onFogDensity(FogDensity event) {
        ActiveRenderInfo info = event.getInfo();
        IFluidState state = info.getFluidState();
        if (state.getFluid() instanceof MudFluid) {
            //GlStateManager.fogMode(FogMode.EXP);
            event.setDensity(1.0F);
            event.setCanceled(true);
        }

    }
}