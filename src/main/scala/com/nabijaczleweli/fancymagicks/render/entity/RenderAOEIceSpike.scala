package com.nabijaczleweli.fancymagicks.render.entity

import com.nabijaczleweli.fancymagicks.util.ResourceLocationFancyMagicks
import net.minecraft.client.renderer.entity.Render
import net.minecraft.entity.Entity

object RenderAOEIceSpike extends Render {
	private val entityTexture = new ResourceLocationFancyMagicks("textures/entity/aoe_spike_ice.png")

	override def doRender(entity: Entity, posX: Double, posY: Double, posZ: Double, rotationYaw: Float, partialTickTime: Float) {
		bindEntityTexture(entity)
		ModelAOEIceSpike.render(entity, posX.toFloat, posY.toFloat, posZ.toFloat, rotationYaw, partialTickTime, 0)
	}

	override def getEntityTexture(entity: Entity) =
		entityTexture
}
