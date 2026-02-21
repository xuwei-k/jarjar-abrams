package com.eed3si9n.jarjarabrams.sbtjarjarabrams

import sbt._
import sbt.Keys._
import com.eed3si9n.jarjarabrams.sbtjarjarabrams.JarjarAbramsInternalKeys._

private[sbtjarjarabrams] object JarjarAbramsPluginCompat {
  val jarjarPackageBinImpl: Def.Initialize[Task[File]] = Def.task {
    val config = (jarjarPackageBin / packageConfiguration).value
    val s = streams.value
    Package(
      config,
      s.cacheStoreFactory,
      s.log,
    )
    config.jar
  }

  val jarjarPackageBinMappingsImpl: Def.Initialize[Task[Seq[(File, String)]]] = Def.task {
    import sbt.util.CacheImplicits._
    val s = streams.value
    val input = jarjarInputJar.value
    val prev = jarjarPackageBinMappings.previous

    def doMapping() = JarjarAbramsPlugin.doMapping(
      input = jarjarInputJar.value,
      dir = (jarjarPackageBin / target).value,
      rules = JarjarAbramsKeys.jarjarShadeRules.value,
      verbose = (jarjarPackageBin / logLevel).value == sbt.Level.Debug,
    )

    val cachedMappings =
      Tracked
        .inputChanged[HashFileInfo, Seq[(File, String)]](s.cacheStoreFactory.make("input")) {
          (changed: Boolean, in: HashFileInfo) =>
            prev match {
              case None =>
                doMapping()
              case Some(last) =>
                if (changed) doMapping()
                else last
            }
        }
    cachedMappings(FileInfo.hash(input))
  }
}
