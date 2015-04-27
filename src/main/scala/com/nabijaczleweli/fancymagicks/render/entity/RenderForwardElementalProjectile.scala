package com.nabijaczleweli.fancymagicks.render.entity

import com.nabijaczleweli.fancymagicks.entity.EntityForwardElementalProjectile
import com.nabijaczleweli.fancymagicks.util.ResourceLocationFancyMagicks
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.entity.Render
import net.minecraft.entity.Entity
import net.minecraftforge.client.model.AdvancedModelLoader
import org.lwjgl.opengl.GL11._

@SideOnly(Side.CLIENT)
class RenderForwardElementalProjectile(name: String) extends Render {
	private lazy val entityTexture = ResourceLocationFancyMagicks(s"textures/entity/$name.png")
	private lazy val model = AdvancedModelLoader loadModel ResourceLocationFancyMagicks(s"models/entity/$name.tcn")

	override def doRender(_entity: Entity, x: Double, y: Double, z: Double, f0: Float, partialTickTime: Float) {
		val entity = _entity.asInstanceOf[EntityForwardElementalProjectile]

		glPushMatrix()
			glTranslated(x, y, z)
			bindEntityTexture(entity)

			glScalef(entity.force, entity.force, entity.force)
			glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTickTime, 0, 1, 0) // Slightly modified version of EntityArrow's rotation algo
			glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTickTime, 0, 0, 1)
			model.renderAll()
		glPopMatrix()
	}

	override def getEntityTexture(entity: Entity) =
		entityTexture
}
