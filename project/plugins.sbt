addSbtPlugin("com.typesafe.play" % "sbt-plugin"             % "2.8.1")
addSbtPlugin("com.timushev.sbt"  % "sbt-updates"            % "0.5.0")
addSbtPlugin("io.get-coursier"   % "sbt-coursier"           % "1.0.2")
addSbtPlugin("org.scalastyle"    %% "scalastyle-sbt-plugin" % "1.0.0")
addSbtPlugin("com.eed3si9n"      % "sbt-assembly"           % "0.14.5")
addSbtPlugin("io.gatling"        % "gatling-sbt"            % "3.1.0")

addSbtPlugin("com.lightbend.akka.grpc" %% "sbt-akka-grpc" % "0.7.3")
libraryDependencies += "com.lightbend.play" %% "play-grpc-generators" % "0.8.1"
