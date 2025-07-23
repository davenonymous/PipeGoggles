package com.davenonymous.pipegoggles.data;

import com.davenonymous.pipegoggles.config.Rules;
import com.davenonymous.pipegoggles.datacomponents.PipeGoggleDataComponent;
import com.davenonymous.pipegoggles.setup.ModDataComponents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.energy.EnergyStorage;

public class GoggleEnergyStorage extends EnergyStorage {
	ItemStack stack;

	public GoggleEnergyStorage(ItemStack stack) {
		super(Rules.maxEnergy);
		this.stack = stack;

		this.maxReceive = Rules.maxEnergyReceive;
		this.maxExtract = 0;

		var goggleData = stack.getOrDefault(ModDataComponents.PIPEGOGGLES_COMPONENT, new PipeGoggleDataComponent());
		if(goggleData.storedEnergy() > 0) {
			this.energy = (int)goggleData.storedEnergy();
		}
	}

	@Override
	public int receiveEnergy(int toReceive, boolean simulate) {
		if(!canReceive()) {
			return 0;
		}

		int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, toReceive));
		if(!simulate) {
			this.energy += energyReceived;
			var goggleData = stack.getOrDefault(ModDataComponents.PIPEGOGGLES_COMPONENT, new PipeGoggleDataComponent());
			var newData = goggleData.withStoredEnergy(this.energy);
			stack.set(ModDataComponents.PIPEGOGGLES_COMPONENT, newData);
		}

		return energyReceived;
	}

	@Override
	public int extractEnergy(int toExtract, boolean simulate) {
		if(!canExtract()) {
			return 0;
		}

		int energyExtracted = Math.min(energy, Math.min(this.maxExtract, toExtract));
		if(!simulate) {
			this.energy -= energyExtracted;
			var goggleData = stack.getOrDefault(ModDataComponents.PIPEGOGGLES_COMPONENT, new PipeGoggleDataComponent());
			var newData = goggleData.withStoredEnergy(this.energy);
			stack.set(ModDataComponents.PIPEGOGGLES_COMPONENT, newData);
		}

		return energyExtracted;
	}

	@Override
	public int getEnergyStored() {
		if(!Rules.requireEnergy) {
			return capacity;
		}

		var goggleData = stack.getOrDefault(ModDataComponents.PIPEGOGGLES_COMPONENT, new PipeGoggleDataComponent());
		return (int) goggleData.storedEnergy();
	}
}
