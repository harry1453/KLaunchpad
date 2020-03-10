# KLaunchpad (Novation Launchpad for Kotlin and many other languages)

Kotlin Launchpads is a high-level library for manipulating and interacting with [Novation Launchpads](https://novationmusic.com/en/launch).

It provides two main unified interfaces: `Launchpad` and `Pad`, which can represent any Launchpad.

Theoretically support for other grid-based control surfaces such as MIDI fighters could be built.

## Using this Library

### On the JVM (Java, Kotlin, other JVM Languages)

Maven:

```xml
TODO
```

Gradle:

```groovy
TODO
```

## Examples

Please take a look at the [Examples](src/jvmMain/kotlin/com/harry1453/launchpad/examples). These should work with any supported launchpad.

## Launchpad Support

- Launchpad (Original): Not Supported
- Launchpad S: Not Supported
- Launchpad Mini: Not Supported
- Launchpad MK2: **Fully Supported**
- Launchpad Pro: **Fully Supported, but untested, likely buggy**
- Launchpad Mini MK3: Not Supported, but support planned
- Launchpad X: Not Supported, but support planned
- Launchpad Pro MK3: Not Supported, but support planned

## Platform Support

- JVM: **Supported, using Java MIDI API**
- JS: Not supported, but support planned using Web MIDI API
- Native
  - Windows: Not supported, but support planned using `winmm.dll` API
  - Other platforms: Not supported.
