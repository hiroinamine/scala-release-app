# scala-release-app

### Plugins
- sbt-buildinfo - prints build information
- sbt-release - release application
- sbt-native-packager - publish docker container

### Usage
if it is a private repository, you need to login first:

`$ docker login `

`$ sbt "release with-defaults" `