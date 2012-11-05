import AssemblyKeys._ // put this at the top of the file

assemblySettings

name := "replicator"

organization := "org.tort.trade"

version := "0.2-SNAPSHOT"

libraryDependencies += "com.h2database" % "h2" % "1.3.168"

libraryDependencies += "com.chuusai" %% "shapeless" % "1.2.2"
