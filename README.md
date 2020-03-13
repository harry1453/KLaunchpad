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

### In JavaScript

KLaunchpad supports being used **in the browser** via JavaScript. It does not support Node.js because Node does not implement the Web MIDI API.

NPM: [`klauchpad`](https://www.npmjs.com/package/klaunchpad)

Pure JS: `TODO`

## Examples

Available Examples:

* Different Lighting Modes - Shows off solid, flash and pulse lighting modes. Demonstrates using different lighting modes.
* Enter Bootloader - Forces the launchpad to open its bootloader menu.
* Faders - The launchpad acts as a bank of faders. Demonstrates using the native fader mode of the Launchpad. Two modes: Unipolar and Bipolar faders.
* Flash Pressed Pad - The pressed pad will flash between red and blue with a period of 1 second. Demonstrates 2-colour flashing.
* Hello World - The text "Hello World" will scroll across the launchpad. Demonstrates the Launchpad's native text scrolling.
* Light All When Pressed - Pressing a pad will light all pads in a random color. Demonstrates updating every pad at once.
* Light Around Pressed Pag - Pressing a pad will light it and the pads next to it in a random color. Demonstrates using the grid coordinate system to find pads.
* Light Pressed Coordinate - Pressing a pad will light its row and its column in a random color. Demonstrates updating a whole row or column of pads at once.
* Light Pressed Pag - Pressing a pad will light it in a random color. Demonstrates updating pads and listening for pad updates.

### JVM (Java, Kotlin, Groovy, Scala etc)

* [Kotlin Examples](src/jvmMain/kotlin/com/harry1453/klaunchpad/examples) (The API is the same as Java)

### JS (JavaScript, TypeScript)

* [React Examples](examples/js/react-examples)
* [Pure JS Examples](examples/js/purejs-examples)

## Launchpad Support

Gen 1:
- Launchpad (Original): Not Supported
- Launchpad S: Not Supported
- Launchpad Mini: Not Supported

Gen 2:
- Launchpad MK2: **Fully Supported**
- Launchpad Pro: **Fully Supported but untested, likely buggy**

Gen 3:
- Launchpad Mini MK3: Not Supported, but support planned
- Launchpad X: Not Supported, but support planned
- Launchpad Pro MK3: Not Supported, but support planned

## Platform Support

- JVM: **Supported, using Java MIDI API**
- JS: **Supported for web both in NPM and Pure JS, using Web MIDI API**
- Native:
  - Windows: Not supported, but support planned using `winmm.dll` API
  - Other platforms: Not supported.
