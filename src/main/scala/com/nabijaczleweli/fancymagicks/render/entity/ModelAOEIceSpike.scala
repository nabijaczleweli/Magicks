package com.nabijaczleweli.fancymagicks.render.entity

import com.nabijaczleweli.fancymagicks.util.IConfigurable
import net.minecraft.client.model.{ModelBox, ModelRenderer, ModelBase}
import net.minecraft.entity.Entity
import net.minecraftforge.common.config.Configuration

import scala.collection.JavaConversions._

/** The model is built kinda like a 3D version of this:
  * {{{
  *                    ____
  *                   |    |
  *               ____|    |____
  *              |              |
  *              |              |
  *              |              |
  *  ____________|              |____________
  * |                                        |
  * |                                        |
  * |                                        |
  * |                                        |
  * |                                        |
  * |                                        |
  * |                                        |
  * |                                        |
  * |                                        |
  * |                                        |
  * |                                        |
  * |________________________________________|
  * }}}
  * As in: each layer is 2 times larger than the previous one.
  */
object ModelAOEIceSpike extends ModelBase with IConfigurable {
	textureWidth = 64
	textureHeight = 64

	var layerAmount = 3
	var layerSideLengthBase = 2

	lazy val spikeRenderer = {
		val t = new ModelRenderer(this, "Layers")

		for(fromBottom <- (0 until layerAmount).reverse) {
			val fromTop = layerAmount - fromBottom
			val layerName = fromBottom.toString
			val sideLength = layerSideLengthBase * math.pow(2, fromTop).toInt
			val invertedSideLength = layerSideLengthBase * math.pow(2, fromBottom).toInt

			setTextureOffset(s"${t.boxName}.$layerName", fromBottom, fromBottom)
			t.addBox(layerName, invertedSideLength, fromBottom, invertedSideLength, sideLength, sideLength, sideLength)
		}
		println(t.cubeList map {_.asInstanceOf[ModelBox]} map {m => (m.posX1, m.posX2, m.posY1, m.posY2, m.posZ1, m.posZ2)})

		t
	}


	override def render(entity: Entity, f0: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		spikeRenderer render 1
	}

	override def configure(config: Configuration) {
		layerAmount = config.getInt("layersPerIceSpike", "render", layerAmount, 1, Int.MaxValue, "Amount of \"layers\" each ice spike will have")
		layerSideLengthBase = config.getInt("baseSpikeSideLength", "render", layerSideLengthBase, 1, Int.MaxValue, "Base layer\'s side length, doubled on each layer")
	}
}
