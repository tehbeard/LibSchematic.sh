package com.tehbeard.forge.schematic.shell;

import com.tehbeard.pluginChannel.netty.PacketHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.command.CommandHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.tehbeard.forge.schematic.shell.items.SetSquareItem;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;

@Mod(modid = Reference.MODID,name="LibSchematic.sh",version=Reference.VERSION,useMetadata=true)
public class LibSchematicShell {
    
    @SidedProxy(serverSide="com.tehbeard.forge.schematic.shell.CommonProxy",clientSide="com.tehbeard.forge.schematic.shell.ClientProxy")
    public static CommonProxy proxy;

    public static SetSquareItem setSquareItem;
    
    public static CreativeTabs tabLibSch = new CreativeTabs("tabLibSchematic"){
    	 public ItemStack getIconItemStack() {
    		 return new ItemStack(setSquareItem,1,0);
    	 }

        @Override
        public Item getTabIconItem() {
            return null;
        }

    };

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        setSquareItem = (SetSquareItem) new SetSquareItem().setUnlocalizedName("protractor")
                .setTextureName(String.format("%s:setsquare", Reference.MODID))
                .setCreativeTab(tabLibSch);
        GameRegistry.registerItem(setSquareItem, "protractor");
    }
    
    //public static final 
    @EventHandler
    public void init(FMLInitializationEvent event){

        PacketHandler.init();

        proxy.registerItems();
        proxy.registerGUI();

    }
    
    @EventHandler
    public void preServerStart(FMLServerAboutToStartEvent event){
        proxy.registerCommands(((CommandHandler)event.getServer().getCommandManager()));
    }

}
