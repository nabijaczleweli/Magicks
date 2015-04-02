package com.nabijaczleweli.fancymagicks.potion

import com.nabijaczleweli.fancymagicks.util.{EntityUtil, IConfigurable}
import net.minecraft.entity.EntityLivingBase
import net.minecraftforge.common.config.Configuration

object PotionDeflectAura extends Potion(false, 0xFDCB00) with IConfigurable {
	var blocksPerLevel = 2.5F

	override def performEffect(entity: EntityLivingBase, amplifier: Int) =
		EntityUtil.entitiesInRadius[EntityLivingBase](entity, blocksPerLevel * amplifier) foreach {Potion applyEffect this}

	override def configure(config: Configuration) {
		blocksPerLevel = config.getFloat("blocksPerDeflectAuraLevel", "potion", blocksPerLevel, .5F, Float.MaxValue, "Radius at which the deflecting aura will affect other entities")
	}
}
