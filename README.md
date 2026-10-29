# SomeoneIsWatchingYou

Fabric mod requested by AbdulHadi_0078 featuring Dahda, a watcher who appears during exploration.

## Layout

- `common`: loader-neutral mod constants and shared code.
- `fabric`: Fabric entrypoints, commands, rendering, HUD, and assets.

The project targets Minecraft `26.1.2`, uses mod version `1.0.0`, and requires Java 25.

`./gradlew build` currently depends on Mojang publishing official mappings for Minecraft `26.1.2`; Loom reports that those mappings are not available yet.