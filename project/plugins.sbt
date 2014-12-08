resolvers += "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.5.1")

resolvers += Resolver.url("artifactory", url("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

resolvers += Resolver.url("scalasbt releases", new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

resolvers += Resolver.url("xerial", new URL("http://repo1.maven.org/maven2/org/xerial/sbt/"))(Resolver.ivyStylePatterns)

addSbtPlugin("org.xerial.sbt" % "sbt-pack" % "0.5.1")

addSbtPlugin("com.hanhuy.sbt" % "android-sdk-plugin" % "1.2.20")

addSbtPlugin("com.earldouglas" % "xsbt-web-plugin" % "1.0.0")
