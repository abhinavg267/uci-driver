name := "uci-driver"

version := "0.1"

scalaVersion := "2.13.3"

// https://mvnrepository.com/artifact/com.google.inject/guice
libraryDependencies += "com.google.inject" % "guice" % "4.0"

// Fail compilation on warnings
scalacOptions += "-Xfatal-warnings"