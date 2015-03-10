package com.nabijaczleweli.fancymagicks.staves

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.{StatCollector, IIcon}

class StaffType(texture: String, val name: String, val activeAbility: StaffAbility, val passiveAbility: StaffAbility) {
	@SideOnly(Side.CLIENT)
	val icon = new Array[IIcon](1)

	@SideOnly(Side.CLIENT)
	def registerIcons(registry: IIconRegister) =
		icon(0) = registry registerIcon texture

	def localizedName =
		StatCollector translateToLocal name
}
