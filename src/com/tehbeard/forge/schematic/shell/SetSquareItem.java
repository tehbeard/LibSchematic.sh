package com.tehbeard.forge.schematic.shell;

import java.util.List;

import com.tehbeard.forge.schematic.SchVector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class SetSquareItem extends Item {

    public SetSquareItem(int id) {
        super(id);
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.tabRedstone);
        setUnlocalizedName("protractor");
        func_111206_d("tehbeard.schematic.sh:setsquare");
    }
    
    @Override
    public void onCreated(ItemStack itemStack, World world,
            EntityPlayer player) {
        initTag(itemStack);
        super.onCreated(itemStack, world, player);
    }
    
    
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player,
            World world, int x, int y, int z, int side, float hitX, float hitY,
            float hitZ) {
        initTag(stack);
        
        if(player.isSneaking()){
            setPos1(stack, new SchVector(x,y,z));
        }
        else
        {
            setPos2(stack, new SchVector(x,y,z));   
        }
        // TODO Auto-generated method stub
        System.out.println(player.isSneaking() ? "Sneak" : "no sneak");
        System.out.println("coords? " + x + ", "+ y + ", "+ z);
        System.out.println("Button? " + side);
        System.out.println("click precise? " + hitX + ", "+ hitY + ", "+ hitZ);
        return true;// super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }
    

    
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack,
            EntityPlayer player, List infoList, boolean unknown) {
        super.addInformation(itemStack, player, infoList, unknown);
        SchVector pos1 = getPos1(itemStack);
        SchVector pos2 = getPos2(itemStack);
        SchVector size = new SchVector(SchVector.max(pos1, pos2)).sub(SchVector.min(pos1, pos2).sub(new SchVector(1,1,1)));
        infoList.add("§cPos 1 : " + pos1);
        infoList.add("§9Pos 2 : " + pos2);
        infoList.add("§5Size   : " + size);
        infoList.add("§5Area   : " + size.area());
        
        
    }
    
    private void initTag(ItemStack stack){
        if(stack.stackTagCompound==null){
        stack.stackTagCompound = new NBTTagCompound();
        stack.stackTagCompound.setCompoundTag("pos1", new SchVector().asTag());
        stack.stackTagCompound.setCompoundTag("pos2", new SchVector().asTag());
        }
    }
    
    public SchVector getPos1(ItemStack stack){
        initTag(stack);
        return new SchVector(stack.stackTagCompound.getCompoundTag("pos1"));
    }
    
    public SchVector getPos2(ItemStack stack){
        initTag(stack);
        return new SchVector(stack.stackTagCompound.getCompoundTag("pos2"));
    }
    
    private void setPos1(ItemStack stack,SchVector sch){
        initTag(stack);
        stack.stackTagCompound.setCompoundTag("pos1", sch.asTag());
    }
    
    private void setPos2(ItemStack stack,SchVector sch){
        initTag(stack);
        stack.stackTagCompound.setCompoundTag("pos2", sch.asTag());
    }
    
    
    
    @Override
    public boolean canItemEditBlocks() {
        // TODO Auto-generated method stub
        return false;//super.canItemEditBlocks();
    }

    
}
