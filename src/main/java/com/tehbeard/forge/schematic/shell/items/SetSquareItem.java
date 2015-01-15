package com.tehbeard.forge.schematic.shell.items;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.shell.Reference;
import com.tehbeard.pluginChannel.Message;

import com.tehbeard.pluginChannel.netty.PacketHandler;
import com.tehbeard.pluginChannel.netty.SchemPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

/**
 * Class Name - SetSquareItem
 * Package - com.tehbeard.forge.schematic.shell.items
 * Desc of Class - This is our set square item which is used in
 *                 conjunction with a lot of stuff in this addition
 * Author(s) - J. Holt (@James), M. D. Ball (@M4Numbers)
 */
public class SetSquareItem extends Item {

    /**
     * On initialisation... we just need to set the stack size
     */
    public SetSquareItem() {
        super();
        setMaxStackSize(1);
    }

    /**
     * On creation, we need to create a new tag to hold /this/ set
     * square's co-ordinate details
     *
     * @param itemStack The item itself
     * @param world The world in which it was created in
     * @param player The player that created it
     */
    @Override
    public void onCreated(ItemStack itemStack, World world,
            EntityPlayer player) {
        //Start up a new tag
        initTag(itemStack);
        //And call on upwards
        super.onCreated(itemStack, world, player);
    }

    /**
     * When a player uses this item, it means they've right-clicked it and
     * we need to go about checking which right-click it was and adding it
     * to the current box of stuff to sort
     *
     * @param stack This is the item that was used
     * @param player This is the player that used the item
     * @param world This is the world in which they used that item
     * @param x This is the x-co-ordinate of where they used that item
     * @param y This is the y-co-ordinate of where they used that item
     * @param z This is the z-co-ordinate of where they used that item
     * @param side This is which side of the server/client we're getting info on
     * @param hitX This is the x-co-ordinate of where they used that item
     * @param hitY This is the y-co-ordinate of where they used that item
     * @param hitZ This is the z-co-ordinate of where they used that item
     * @return Whether they used the item successfully???
     */
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player,
            World world, int x, int y, int z, int side, float hitX, float hitY,
            float hitZ) {
        
        initTag(stack);
        
        setCoords(player.isSneaking(),new SchVector(x,y,z));
        List<String> l = new ArrayList<String>();
        genList(stack,l);
        
        player.addChatMessage(new ChatComponentText(l.get(2)));
        player.addChatMessage(new ChatComponentText(l.get(3)));

        return true;// super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    /**
     * Send the details of the use to the server so that it can
     * deal with how this item was used
     *
     * @param pos1 The boolean value of whether this is pos 1 or 2
     * @param vector The vector of the point
     */
    private void setCoords(boolean pos1,SchVector vector){
        try {

            //Let's start up a few streams
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);

            //And write the data to the streams
            dos.writeBoolean(pos1);
            dos.writeInt(vector.getX());
            dos.writeInt(vector.getY());
            dos.writeInt(vector.getZ());

            //Flush that
            dos.flush();

            //And create a new message from what we've got
            Message m = new Message(bos.toByteArray(), (char)0, Reference.SUB_CHANNEL, 0);

            //Now send that message to the server
            PacketHandler.network.sendToServer(new SchemPacket.SchemMessage(m.getParts()[0]));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add information to this item
     *
     * @param itemStack The item we're adding the information to
     * @param player The player currently holding the item
     * @param infoList The list of information we're filling
     * @param unknown Unknown.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack,
            EntityPlayer player, List infoList, boolean unknown) {
        super.addInformation(itemStack, player, infoList, unknown);

        //Generate the information
        genList(itemStack,infoList);
    }

    /**
     * Generate the co-ordinate information that goes along with the
     * set square
     *
     * @param itemStack The item which is going to be blessed with
     *                  knowlege
     * @param infoList The list of information which holds the knowledge
     */
    private void genList(ItemStack itemStack, List<String> infoList) {

        //Get the details about the box
        SchVector pos1 = getPos1(itemStack);
        SchVector pos2 = getPos2(itemStack);

        //And do some calculations on it
        SchVector size = new SchVector(SchVector.max(pos1, pos2)).sub(SchVector.min(pos1, pos2).sub(new SchVector(1,1,1)));

        //Now add the data that we've calculated to the list of information
        infoList.add(EnumChatFormatting.RED          + "Pos 1 : " + pos1);
        infoList.add(EnumChatFormatting.BLUE         + "Pos 2 : " + pos2);
        infoList.add(EnumChatFormatting.LIGHT_PURPLE + "Size : " + size);
        infoList.add(EnumChatFormatting.LIGHT_PURPLE + "Area : " + size.area());
        
    }

    /**
     * Initialise a new tag for this item
     *
     * @param stack This is the new item that we're generating a tag
     *              for
     */
    private void initTag(ItemStack stack){

        //If nothing has been set yet...
        if(stack.stackTagCompound==null){
            //Set everything to zero
            stack.stackTagCompound = new NBTTagCompound();
            stack.stackTagCompound.setTag("pos1", new SchVector().asTag());
            stack.stackTagCompound.setTag("pos2", new SchVector().asTag());
        }

    }

    /**
     * Get the first position of a given set square
     *
     * @param stack Said given set square
     * @return The first position of that given set square
     */
    public SchVector getPos1(ItemStack stack){
        initTag(stack);
        return new SchVector(stack.stackTagCompound.getCompoundTag("pos1"));
    }

    /**
     * Get the second position of a given set square
     *
     * @param stack Said given set square
     * @return The second position of that given set square
     */
    public SchVector getPos2(ItemStack stack){
        initTag(stack);
        return new SchVector(stack.stackTagCompound.getCompoundTag("pos2"));
    }

    /**
     * Set the first position of a given set square
     *
     * @param stack Said given set square
     * @param sch The first position to be set
     */
    public void setPos1(ItemStack stack,SchVector sch){
        initTag(stack);
        stack.stackTagCompound.setTag("pos1", sch.asTag());
    }

    /**
     * Set the second position of a given set square
     *
     * @param stack Said given set square
     * @param sch The second position to be set
     */
    public void setPos2(ItemStack stack,SchVector sch){
        initTag(stack);
        stack.stackTagCompound.setTag("pos2", sch.asTag());
    }

    /**
     * @return False. This item does nothing to blocks.
     */
    @Override
    public boolean canItemEditBlocks() {
        return false;
    }

}
