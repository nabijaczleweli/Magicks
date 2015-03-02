package com.nabijaczleweli.fancymagicks.staves

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.StatCollector

trait StaffAbility {
	def apply(player: EntityPlayer): Unit

	def description: String

	def displayDescription =
		StatCollector translateToLocal description
}
