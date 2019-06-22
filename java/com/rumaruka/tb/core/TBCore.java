package com.rumaruka.tb.core;

import static com.rumaruka.tb.core.TBCore.*;


import DummyCore.Core.Core;



import com.rumaruka.tb.common.handlers.EnchatmentHandler;
import com.rumaruka.tb.common.handlers.RegisterHandlers;


import com.rumaruka.tb.init.*;

import com.rumaruka.tb.network.proxy.TBServer;
import com.rumaruka.tb.utils.KnowledgeTB;
import com.rumaruka.tb.utils.TBConfig;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import thaumcraft.api.aspects.AspectRegistryEvent;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.FocusEngine;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Mod(modid = modid, name = name, version = version,dependencies = dependencies)
public class TBCore {

    public static final String modid = "thaumicbases";
    public static final String name = "Thaumic Bases";
    public static final String version = "3.3.310.5r";
    public static final String dependencies = "required-after:thaumcraft@[6.1.BETA26,);required-after:dummycore@[2.4.112.3,)";

    //Networking
    public static final String serverProxy = "com.rumaruka.tb.network.proxy.TBServer";
    public static final String clientProxy = "com.rumaruka.tb.network.proxy.TBClient";



    @SidedProxy(serverSide = serverProxy, clientSide = clientProxy)
    public static TBServer proxy;

    public static SimpleNetworkWrapper network;
    static {
        FluidRegistry.enableUniversalBucket(); // Must be called before preInit
    }

    public static TBCore instance;
    public static final TBConfig cfg = new TBConfig();
    public static ResearchCategory RES_CAT;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {

        instance = this;
        Core.registerModAbsolute(getClass(), name, e.getModConfigurationDirectory().getAbsolutePath(), cfg);
        setupModInfo(e.getModMetadata());
        TBFluids.init.call();
        TBBlocks.init();
        TBBlocks.InGameRegister();
        TBItems.init();
        TBItems.InGameRegistr();
    //    MinecraftForge.EVENT_BUS.register(new KnowledgeTB());

        KnowledgeTB.clInit.call();
        TBTiles.setup();
        TBEnchant.setupEnchatments();
        proxy.preInit(e);
        FMLInterModComms.sendMessage("Wailla","register","tb");




    }


    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new TBGuiHandler());
        RES_CAT = ResearchCategories.registerCategory(catName,null,null,icon,back,back2);
        proxy.registerRenderInformation();
        MinecraftForge.EVENT_BUS.register(new EnchatmentHandler());
        TBThaumonomicon.setup();

        KnowledgeTB.clInit.call();
        network = NetworkRegistry.INSTANCE.newSimpleChannel("thaumbases");
        RegisterHandlers.init();

         proxy.init(e);

    }


    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {

        proxy.Renders();
        TBRecipe.setup();
        TBOreDirection.setup();
        KnowledgeTB.init.call();
        KnowledgeTB.insertAspects.call();
        network = NetworkRegistry.INSTANCE.newSimpleChannel("thaumicbases");




    }




    public void setupModInfo(ModMetadata meta) {

        meta.autogenerated = false;
        meta.modId = modid;
        meta.name = name;
        meta.version = version;
        meta.description = "A huge addon to an amazing mod named Thaumcraft by Azanor and former developer Modbder";
        ArrayList<String> authors = new ArrayList<String>();
        authors.add("Modbder, Rumaruka");
        meta.authorList = authors;

    }
    public static final String catName ="THAUMICBASES";
    public static final ResourceLocation icon = new ResourceLocation("thaumicbases","textures/thaumonomicon/bases.png");
    public static final ResourceLocation back = new ResourceLocation("thaumicbases","textures/thaumonomicon/background.png");
    public static final ResourceLocation back2 = new ResourceLocation("thaumcraft","textures/gui/gui_research_back_over.png");
}