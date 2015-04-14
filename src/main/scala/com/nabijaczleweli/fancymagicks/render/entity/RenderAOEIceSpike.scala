package com.nabijaczleweli.fancymagicks.render.entity

import com.nabijaczleweli.fancymagicks.entity.EntityAOEIceSpike
import com.nabijaczleweli.fancymagicks.render.model.TechneModelLoader
import com.nabijaczleweli.fancymagicks.util.ResourceLocationFancyMagicks
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.entity.Render
import net.minecraft.entity.Entity
import net.minecraftforge.client.model.AdvancedModelLoader
import org.lwjgl.opengl.GL11._

import scala.collection.mutable.{LongMap => mLongMap}

@SideOnly(Side.CLIENT)
object RenderAOEIceSpike extends Render {
	TechneModelLoader //TODO: Move elsewhere

	private val entityTexture = ResourceLocationFancyMagicks("textures/entity/aoe_spike_ice.png")
	private val model = AdvancedModelLoader loadModel ResourceLocationFancyMagicks("models/entity/aoe_spike_ice.tcn")
	private val progresses = mLongMap.empty withDefaultValue 0f -> false

	override def doRender(_entity: Entity, x: Double, y: Double, z: Double, f0: Float, partialTickTime: Float) {
		val entity = _entity.asInstanceOf[EntityAOEIceSpike]

		glPushMatrix()
			glTranslated(x, y, z)
		  bindEntityTexture(entity)

			glScalef(1, progresses(entity.getEntityId)._1, 1)
			model.renderAll()
		glPopMatrix()

		var (progress, wasUp) = progresses(entity.getEntityId)
		(
			if(wasUp)
				math.max(progress - .01f, 0)
			else
				math.min(progress + .01f, 1)
		) match {
			case 0 if wasUp =>
			case 1 =>
				println(progress, wasUp)
				progress = 1
				wasUp = true
				println(progress, wasUp)
			case f =>
				progress = f
		}
		progresses(entity.getEntityId) = progress -> wasUp
	}

	override def getEntityTexture(entity: Entity) =
		entityTexture
}
