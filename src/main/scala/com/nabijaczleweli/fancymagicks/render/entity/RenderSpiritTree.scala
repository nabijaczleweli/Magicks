package com.nabijaczleweli.fancymagicks.render.entity

import com.nabijaczleweli.fancymagicks.util.ResourceLocationFancyMagicks
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.entity.RenderGiantZombie
import net.minecraft.entity.{Entity, EntityLivingBase}

@SideOnly(Side.CLIENT)
object RenderSpiritTree extends RenderGiantZombie(new ModelBiped(0, 0, 64, 64), .5f, 4) {
	private val entityTexture = new ResourceLocationFancyMagicks("textures/entity/spirit_tree.png")

	setRenderPassModel(mainModel)

	override def getEntityTexture(entity: Entity) =
		entityTexture

	/** Override here to avoid cast exceptions from superclass */
	override def preRenderCallback(e: EntityLivingBase, f: Float) =
		super.preRenderCallback(null, f)

	override def shouldRenderPass(entity: EntityLivingBase, pass: Int, renderPartialTicks: Float) =
		pass match {
			case 0 =>
				1
			case _ =>
				-1
		}
}
