package com.hbm.items.machine;

import java.util.List;

import com.hbm.inventory.material.Mats;
import com.hbm.lib.RefStrings;
import com.hbm.util.i18n.I18nUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

public class ItemMagneticDisc extends Item {

	public static enum EnumMagneticDisc {
		IRON(1, 1_000),
		NEODYMIUM(4, 10_000),
		VANADIUM(8, 100_000),
		NICKEL(16, 1_000_000);

		public final int parallels;
		public final int durability;

		private EnumMagneticDisc(int parallels, int durability) {
			this.parallels = parallels;
			this.durability = durability;
		}

		public int getColor() {
			switch(this) {
			case IRON: return Mats.MAT_IRON.solidColorLight;
			case NEODYMIUM: return Mats.MAT_NEODYMIUM.solidColorLight;
			case VANADIUM: return Mats.MAT_VANADIUM.solidColorLight;
			case NICKEL: return Mats.MAT_NICKEL.solidColorLight;
			default: return 0xFFFFFF;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	protected IIcon iconBase;
	@SideOnly(Side.CLIENT)
	protected IIcon iconOverlay;

	public ItemMagneticDisc() {
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}

	public static EnumMagneticDisc getType(ItemStack stack) {
		if(stack == null) return EnumMagneticDisc.IRON;
		int meta = stack.getItemDamage();
		EnumMagneticDisc[] values = EnumMagneticDisc.values();
		return values[meta < 0 || meta >= values.length ? 0 : meta];
	}

	public static int getDurability(ItemStack stack) {
		EnumMagneticDisc type = getType(stack);
		if(!stack.hasTagCompound()) return type.durability;
		return stack.stackTagCompound.getInteger("dur");
	}

	public static void setDurability(ItemStack stack, int dur) {
		if(!stack.hasTagCompound()) stack.stackTagCompound = new NBTTagCompound();
		stack.stackTagCompound.setInteger("dur", Math.max(0, dur));
	}

	public static int getMaxDurability(ItemStack stack) {
		return getType(stack).durability;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		this.iconBase = reg.registerIcon(RefStrings.MODID + ":magnetic_disc");
		this.iconOverlay = reg.registerIcon(RefStrings.MODID + ":magnetic_disc_overlay");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(int meta, int pass) {
		return pass == 0 ? this.iconBase : this.iconOverlay;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass) {
		return pass == 0 ? 0xFFFFFF : getType(stack).getColor();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return this.iconBase;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < EnumMagneticDisc.values().length; i++) list.add(new ItemStack(item, 1, i));
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + getType(stack).name().toLowerCase();
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return getDurability(stack) < getMaxDurability(stack);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1D - (double) getDurability(stack) / (double) getMaxDurability(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean ext) {
		EnumMagneticDisc type = getType(stack);
		list.add(EnumChatFormatting.YELLOW + ">>> " + I18nUtil.resolveKey(this.getUnlocalizedName(stack) + ".name") + " <<<");
		list.add(EnumChatFormatting.GREEN + "Parallels: " + type.parallels);
		list.add(EnumChatFormatting.YELLOW + "Durability: " + getDurability(stack) + " / " + getMaxDurability(stack));
	}
}
