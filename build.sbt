
name := "parboiled2"
organization := "com.threatstack.blog"
version := "1.0.0"
scalaVersion := "2.11.8"

scalacOptions ++= compilerOptions
libraryDependencies ++= Seq(
  "org.parboiled" %% "parboiled" % "2.1.3",
  "joda-time" % "joda-time" % "2.9.6",

  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

initialCommands in console :=
  """
    |import com.threatstack.blog._
  """.stripMargin


lazy val compilerOptions = Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused-import",
  "-Xfuture",
  "-Xlint"
)
