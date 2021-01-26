name := "todo_scala"
 
version := "1.0" 
      
lazy val `todo_scala` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.34.0"

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

