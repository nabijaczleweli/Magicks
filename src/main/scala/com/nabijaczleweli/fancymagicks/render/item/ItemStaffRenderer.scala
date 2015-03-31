package com.nabijaczleweli.fancymagicks.render.item

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.client.renderer.{ItemRenderer, Tessellator}
import net.minecraft.item.ItemStack
import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.IItemRenderer.{ItemRenderType, ItemRendererHelper}

@SideOnly(Side.CLIENT)
object ItemStaffRenderer extends IItemRenderer {
	override def handleRenderType(item: ItemStack, `type`: ItemRenderType) =
		ItemRenderType.INVENTORY :: ItemRenderType.EQUIPPED_FIRST_PERSON :: Nil contains `type`

	override def shouldUseRenderHelper(`type`: ItemRenderType, item: ItemStack, helper: ItemRendererHelper) =
		false

	override def renderItem(`type`: ItemRenderType, item: ItemStack, data: AnyRef*) {
		val currentIcon = item.getItem getIconIndex item
		`type` match {
			case ItemRenderType.INVENTORY =>
				RenderItem.getInstance.renderIcon(0, 0, currentIcon, currentIcon.getIconWidth, currentIcon.getIconHeight)
			case ItemRenderType.EQUIPPED_FIRST_PERSON =>
				ItemRenderer.renderItemIn2D(Tessellator.instance, currentIcon.getMaxU, currentIcon.getMinV, currentIcon.getMinU, currentIcon.getMaxV, currentIcon.getIconWidth, currentIcon.getIconHeight, .0625F)
				// .0625F is Minecraft's magical default
		}
	}
}
