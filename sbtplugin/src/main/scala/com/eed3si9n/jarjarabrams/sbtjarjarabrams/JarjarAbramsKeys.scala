package com.eed3si9n.jarjarabrams
package sbtjarjarabrams

import sbt._
import sbtcompat.PluginCompat

trait JarjarAbramsKeys {
  lazy val jarjarLibraryDependency = settingKey[ModuleID]("")
  lazy val jarjarShadeRules = settingKey[Seq[ShadeRule]]("")
}
object JarjarAbramsKeys extends JarjarAbramsKeys

trait JarjarAbramsInternalKeys {
  @transient
  lazy val jarjarPackageBin = taskKey[PluginCompat.FileRef]("")
  @transient
  lazy val jarjarPackageBinMappings = taskKey[Seq[(PluginCompat.FileRef, String)]]("")
  @transient
  lazy val jarjarInputJar = taskKey[File]("")
}
object JarjarAbramsInternalKeys extends JarjarAbramsInternalKeys
