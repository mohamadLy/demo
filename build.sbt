name := "sapdemo"

version := "0.1"

scalaVersion := "2.12.8"

val elastic4sVersion = "6.5.1"

libraryDependencies ++=Seq(
  "com.typesafe.akka" %% "akka-cluster" % "2.5.26",
  "com.typesafe.akka" %% "akka-http"   % "10.1.10",
  "com.typesafe.akka" %% "akka-stream" % "2.5.23", // or whatever the latest version i
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.10",
  "org.apache.kafka" %% "kafka" % "2.1.0",
  "net.debasishg" %% "redisclient" % "3.10",
  "com.datastax.cassandra"    % "cassandra-driver-core"   % "3.1.1",
  "com.sksamuel.elastic4s" %% "elastic4s-core" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-embedded" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-http" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-spray-json" % elastic4sVersion

  //  "com.typesafe.akka" %% "akka-stream-kafka" % "1.0.3" // or whatever the latest version i
)

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)
enablePlugins(AshScriptPlugin)

mainClass in compile := Some("server.Server")
dockerBaseImage := "java:8-jre-alpine"
version in Docker := "latest"
dockerExposedPorts := Seq(8000)
packageName in Docker := "sap-demo"
dockerRepository := Some("mohamadou")
