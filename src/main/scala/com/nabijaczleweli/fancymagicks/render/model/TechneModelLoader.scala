package com.nabijaczleweli.fancymagicks.render.model

import com.nabijaczleweli.fancymagicks.reference.Reference._
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.{AdvancedModelLoader, IModelCustomLoader}

/** A techne model loader for loading working techne models.<br />
  * Shamelessly stolen from Dullkus.
  *
  * @see [[https://github.com/drullkus/MateriaMuto/blob/master/src/main/java/com/agilemods/materiamuto/client/model/importer/TechneModelLoader.java]]
  */
object TechneModelLoader extends IModelCustomLoader {
	override val getType = s"Techne models ($MOD_NAME, working)"

	override val getSuffixes = Array("tcn")

	override def loadInstance(resource: ResourceLocation) = {
		val t = new TechneModel(resource)
		println(t)
		t
	}

	AdvancedModelLoader registerModelHandler this // Register here, because uninitialized otherwise TODO: Move elsewhere
}
