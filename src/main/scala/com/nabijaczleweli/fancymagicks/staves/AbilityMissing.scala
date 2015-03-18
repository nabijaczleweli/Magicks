package com.nabijaczleweli.fancymagicks.staves

import com.nabijaczleweli.fancymagicks.reference.Reference
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.StatCollector

class AbilityMissing(which: String) extends StaffAbility {
	override def apply(player: EntityPlayer) {}

	override def description =
		s"tooltip.${Reference.NAMESPACED_PREFIX}staffAbilityErrorMissing"

	override def displayDescription =
		StatCollector.translateToLocalFormatted(description, which)
}
