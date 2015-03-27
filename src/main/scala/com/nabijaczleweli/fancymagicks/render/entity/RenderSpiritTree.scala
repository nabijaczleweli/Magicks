package com.nabijaczleweli.fancymagicks.render.entity

import com.nabijaczleweli.fancymagicks.util.ResourceLocationFancyMagicks
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.entity.{Entity, EntityLivingBase}

@SideOnly(Side.CLIENT)
object RenderSpiritTree extends RenderLiving(new ModelSpiritTree, 0) {
	setRenderPassModel(new ModelSpiritTree)

	override def getEntityTexture(entity: Entity) =
		new ResourceLocationFancyMagicks("textures/entity/spirit_tree.png")

	override def shouldRenderPass(entity: EntityLivingBase, pass: Int, renderPartialTicks: Float) =
		pass match {
			case 0 =>
				1
			case _ =>
				-1
		}
}
