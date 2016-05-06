import SbtMisc._

lazy val burraco = project in file(".")

organization := "com.dwijnand"
     version := "1.0.0-SNAPSHOT"
    licenses := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0"))
   startYear := Some(2016)
 description := "The Italian card game"
     scmInfo := Some(ScmInfo(url("https://github.com/dwijnand/burraco"), "scm:git:git@github.com:dwijnand/burraco.git"))
    homepage := scmInfo.value map (_.browseUrl)

scalaVersion := "2.11.8"

       maxErrors := 15
triggeredMessage := Watched.clearWhenTriggered

scalacOptions ++= "-encoding utf8"
scalacOptions ++= "-deprecation -feature -unchecked -Xlint"
scalacOptions  += "-language:experimental.macros"
scalacOptions  += "-language:higherKinds"
scalacOptions  += "-language:implicitConversions"
scalacOptions  += "-language:postfixOps"
scalacOptions  += "-Xfuture"
scalacOptions  += "-Yinline-warnings"
scalacOptions  += "-Yno-adapted-args"
scalacOptions  += "-Ywarn-dead-code"
scalacOptions  += "-Ywarn-numeric-widen"
scalacOptions  += "-Ywarn-unused"
scalacOptions  += "-Ywarn-unused-import"
scalacOptions  += "-Ywarn-value-discard"

scalacOptions in (Compile, console) -= "-Ywarn-unused-import"
scalacOptions in (Test,    console) -= "-Ywarn-unused-import"

wartremoverWarnings ++= Warts.unsafe
wartremoverWarnings  += Wart.Enumeration
wartremoverWarnings  += Wart.ExplicitImplicitTypes
wartremoverWarnings  += Wart.FinalCaseClass
wartremoverWarnings  += Wart.JavaConversions
wartremoverWarnings  += Wart.MutableDataStructures
wartremoverWarnings  += Wart.Nothing
wartremoverWarnings  += Wart.Option2Iterable
wartremoverWarnings  += Wart.ToString
wartremoverWarnings  -= Wart.Any                    // bans f-interpolator #158
wartremoverWarnings  -= Wart.DefaultArguments
wartremoverWarnings  -= Wart.NonUnitStatements      // bans this.type #118
wartremoverWarnings  -= Wart.Null                   // breaks for macros?
wartremoverWarnings  -= Wart.Product
wartremoverWarnings  -= Wart.Return
wartremoverWarnings  -= Wart.Serializable
wartremoverWarnings  -= Wart.Throw

libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value

initialCommands in console += "\nimport burraco._"

             fork in Test := false
      logBuffered in Test := false
parallelExecution in Test := true

         fork in run := true
cancelable in Global := true

noArtifacts // for now

pomExtra := pomExtra.value ++ {
  <developers>
    <developer>
      <id>dwijnand</id>
      <name>Dale Wijnand</name>
      <url>https://dwijnand.com</url>
    </developer>
  </developers>
}

watchSources ++= (baseDirectory.value * "*.sbt").get
watchSources ++= (baseDirectory.value / "project" * "*.scala").get
