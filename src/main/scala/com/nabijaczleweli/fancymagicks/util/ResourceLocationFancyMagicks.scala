package com.nabijaczleweli.fancymagicks.util

import com.nabijaczleweli.fancymagicks.reference.Reference
import net.minecraft.util.ResourceLocation

case class ResourceLocationFancyMagicks(path: String) extends ResourceLocation(Reference.MOD_ID, path)
