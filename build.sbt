scalaJSSettings

name := "Scala.js ProSET"

scalaVersion := "2.11.4"

libraryDependencies += "org.scala-lang.modules.scalajs" %%% "scalajs-jquery" % "0.6"

libraryDependencies += "com.scalatags" %%% "scalatags" % "0.4.2"

libraryDependencies += "com.lihaoyi" %%% "utest" % "0.2.4"

testFrameworks += new TestFramework("utest.runner.JvmFramework")

utest.jsrunner.Plugin.utestJsSettings

skip in ScalaJSKeys.packageJSDependencies := false

ScalaJSKeys.jsDependencies += scala.scalajs.sbtplugin.RuntimeDOM
