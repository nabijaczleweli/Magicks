package com.nabijaczleweli.fancymagicks.render.entity

import com.nabijaczleweli.fancymagicks.entity.EntityForwardElementalProjectile
import com.nabijaczleweli.fancymagicks.util.ResourceLocationFancyMagicks
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.entity.Render
import net.minecraft.entity.Entity
import net.minecraftforge.client.model.AdvancedModelLoader
import org.lwjgl.opengl.GL11._

@SideOnly(Side.CLIENT)
object RenderEarthBall extends Render {
	private lazy val entityTexture = ResourceLocationFancyMagicks("textures/entity/earth_ball.png")
	private lazy val model = AdvancedModelLoader loadModel ResourceLocationFancyMagicks("models/entity/earth_ball.tcn")

	override def doRender(_entity: Entity, x: Double, y: Double, z: Double, f0: Float, partialTickTime: Float) {
		val entity = _entity.asInstanceOf[EntityForwardElementalProjectile]

		glPushMatrix()
			glTranslated(x, y, z)
			bindEntityTexture(entity)

			glScalef(entity.force, entity.force, entity.force)
			model.renderAll()
		glPopMatrix()
	}

	override def getEntityTexture(entity: Entity) =
		entityTexture
}
