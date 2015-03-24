package com.nabijaczleweli.fancymagicks.render.item

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.{ItemRenderer, Tessellator}
import net.minecraft.item.ItemStack
import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.IItemRenderer.{ItemRenderType, ItemRendererHelper}

@SideOnly(Side.CLIENT)
object ItemStaffRenderer extends IItemRenderer {
	override def handleRenderType(item: ItemStack, `type`: ItemRenderType) =
		`type` == ItemRenderType.EQUIPPED_FIRST_PERSON

	override def shouldUseRenderHelper(`type`: ItemRenderType, item: ItemStack, helper: ItemRendererHelper) =
		false

	override def renderItem(`type`: ItemRenderType, item: ItemStack, data: AnyRef*) {
		val currentIcon = item.getItem getIconIndex item
		ItemRenderer.renderItemIn2D(Tessellator.instance, currentIcon.getMaxU, currentIcon.getMinV, currentIcon.getMinU, currentIcon.getMaxV, currentIcon.getIconWidth, currentIcon.getIconHeight, .0625F)
		// .0625F is Minecraft's magical default
	}
}
