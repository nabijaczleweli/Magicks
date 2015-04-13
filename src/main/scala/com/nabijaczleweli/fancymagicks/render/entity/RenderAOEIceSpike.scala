package com.nabijaczleweli.fancymagicks.render.entity

import com.nabijaczleweli.fancymagicks.util.ResourceLocationFancyMagicks
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.entity.Render
import net.minecraft.entity.Entity
import org.lwjgl.opengl.GL11._

@SideOnly(Side.CLIENT)
object RenderAOEIceSpike extends Render {
	private val entityTexture = ResourceLocationFancyMagicks("textures/entity/aoe_spike_ice.png")

	override def doRender(entity: Entity, x: Double, y: Double, z: Double, f0: Float, partialTickTime: Float) {
		glPushMatrix()
			glTranslated(x, y, z)
		  bindEntityTexture(entity)

      glPushMatrix()
				glRotatef(180, 0, 0, 1)
		    ModelAOEIceSpike.render(entity, 0, 0, -.1F, 0, 0, .0625F)
			glPopMatrix()
		glPopMatrix()
	}

	override def getEntityTexture(entity: Entity) =
		entityTexture
}
