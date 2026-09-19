package com.hbm.inventory.container;

import com.hbm.inventory.SlotNonRetarded;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemMachineUpgrade;
import com.hbm.tileentity.machine.TileEntityMachineMagneticSeparator;

import api.hbm.energymk2.IBatteryItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMachineMagneticSeparator extends ContainerBase {

	public ContainerMachineMagneticSeparator(InventoryPlayer invPlayer, IInventory tile) {
		super(invPlayer, tile);

		// Battery
		this.addSlotToContainer(new SlotNonRetarded(tile, 0, 152, 81));
		// Item input
		this.addSlotToContainer(new SlotNonRetarded(tile, 1, 8, 90));
		// Magnetic disc
		this.addSlotToContainer(new SlotNonRetarded(tile, 2, 46, 51));
		// 6 outputs
		this.addOutputSlots(invPlayer.player, tile, 3, 80, 36, 3, 2);

		this.playerInv(invPlayer, 8, 174);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack slotOriginal = null;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if(slot != null && slot.getHasStack()) {
			ItemStack slotStack = slot.getStack();
			slotOriginal = slotStack.copy();

			if(index <= tile.getSizeInventory() - 1) {
				if(!this.mergeItemStack(slotStack, tile.getSizeInventory(), this.inventorySlots.size(), true)) {
					return null;
				}
			} else {

				if(slotOriginal.getItem() instanceof IBatteryItem || slotOriginal.getItem() == ModItems.battery_creative) {
					if(!this.mergeItemStack(slotStack, 0, 1, false)) return null;
				} else if(slotOriginal.getItem() instanceof com.hbm.items.machine.ItemMagneticDisc) {
					if(!this.mergeItemStack(slotStack, 2, 3, false)) return null;
				} else if(slotOriginal.getItem() instanceof ItemMachineUpgrade) {
					return null;
				} else {
					if(!this.mergeItemStack(slotStack, 1, 2, false)) return null;
				}
			}

			if(slotStack.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}

			slot.onPickupFromSlot(player, slotStack);
		}

		return slotOriginal;
	}
}
