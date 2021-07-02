package example

import buildinfo.BuildInfo

object Hello extends Greeting with App {
  println(s"======== greetings: '$greeting', version: ${BuildInfo.toJson} ========")
}

trait Greeting {
  lazy val greeting: String = "hello"
}
