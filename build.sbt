organization := "com.xebia"

name := "codejam"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "1.6.3-SNAPSHOT" % "test",
  "junit" % "junit" % "4.8.1"
)

resolvers ++= Seq("snapshots" at "http://scala-tools.org/repo-snapshots",
                    "releases"  at "http://scala-tools.org/repo-releases", 
                    "typesafe snapshots" at "http://repo.typesafe.com/typesafe/snapshots")
