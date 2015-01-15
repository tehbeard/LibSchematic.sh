package com.tehbeard.forge.schematic.shell;

import net.minecraft.command.CommandHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.tehbeard.pluginChannel.netty.PacketHandler;
import com.tehbeard.forge.schematic.shell.items.SetSquareItem;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * Class Name - LibSchematicShell
 * Package - com.tehbeard.forge.schematic.shell
 * Desc of Class - This is the central class for this add-on modification to
 *                 the original LibSchematic mod to Minecraft Forge
 * Author(s) - J. Holt (@James), M. D. Ball (@M4Numbers)
 */
@Mod(modid = Reference.MODID,name="LibSchematic.sh",version=Reference.VERSION,useMetadata=true)
public class LibSchematicShell {

    /**
     * Mark ourselves some proxies
     */
    @SidedProxy(serverSide="com.tehbeard.forge.schematic.shell.CommonProxy",clientSide="com.tehbeard.forge.schematic.shell.ClientProxy")
    public static CommonProxy proxy;

    /**
     * The one item in this mod
     */
    public static SetSquareItem setSquareItem;

    /**
     * Our tab for the mod
     */
    public static CreativeTabs tabLibSch = new CreativeTabs("tabLibSchematic"){

        /**
         * @return The set square item as our icon
         */
        @Override
        public ItemStack getIconItemStack() {
            return new ItemStack(setSquareItem,1,0);
        }

        @Override
        public Item getTabIconItem() {
            return null;
        }

    };

    /**
     * Okay... As per usual business, we need to initialise our item
     *
     * @param event This is the event we're handed, signifying when this
     *              event will be ran
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        //We've moved all the details for this item into this block
        // to keep everything more... abstract-ish
        setSquareItem = (SetSquareItem) new SetSquareItem().setUnlocalizedName("protractor")
                .setTextureName(String.format("%s:setsquare", Reference.MODID))
                .setCreativeTab(tabLibSch);

    }

    /**
     * Let's initialise some of the other things that are going to keep
     * this modification rolling - such as the proxies and the packets
     * (da-da-da, da-da da)
     *
     * @param event This means that this will be run during initialisation
     */
    @EventHandler
    public void init(FMLInitializationEvent event){

        //Initialise all the packet stuff
        PacketHandler.init();

        //Register all those items and other bits
        proxy.registerItems();
        proxy.registerGUI();

    }

    /**
     * We also need to make sure that our commands are ready to be used,
     * hence why we have this method
     *
     * @param event Run this method when the server is about to start
     */
    @EventHandler
    public void preServerStart(FMLServerAboutToStartEvent event){
        proxy.registerCommands(((CommandHandler)event.getServer().getCommandManager()));
    }

}
