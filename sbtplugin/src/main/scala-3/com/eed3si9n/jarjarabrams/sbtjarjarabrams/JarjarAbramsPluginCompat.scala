package com.eed3si9n.jarjarabrams.sbtjarjarabrams

import sbt._
import sbt.Keys._
import com.eed3si9n.jarjarabrams.sbtjarjarabrams.JarjarAbramsInternalKeys._

private[sbtjarjarabrams] object JarjarAbramsPluginCompat {
  val jarjarPackageBinImpl: Def.Initialize[Task[HashedVirtualFileRef]] = Def.task {
    val config = (jarjarPackageBin / packageConfiguration).value
    val s = streams.value
    val converter = fileConverter.value
    Package(
      config,
      converter,
      s.log,
    )
    converter.toVirtualFile(config.jar)
  }

  val jarjarPackageBinMappingsImpl = Def.task {
    val converter = fileConverter.value
    JarjarAbramsPlugin
      .doMapping(
        input = jarjarInputJar.value,
        dir = (jarjarPackageBin / target).value,
        rules = JarjarAbramsKeys.jarjarShadeRules.value,
        verbose = (jarjarPackageBin / logLevel).value == sbt.Level.Debug,
      )
      .map((x, y) => converter.toVirtualFile(x.toPath) -> y)
  }
}
