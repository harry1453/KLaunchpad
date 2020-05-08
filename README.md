# KLaunchpad (Novation Launchpad for Kotlin and many other languages)

Kotlin Launchpad is a high-level library for manipulating and interacting with [Novation Launchpads](https://novationmusic.com/en/launch).

It provides two main unified interfaces: `Launchpad` and `Pad`, which can represent any Launchpad.

Theoretically support for other grid-based control surfaces such as MIDI fighters could be built.

It is also a personal challenge to build one library and support as many languages as possible using one main library, and a testament to Kotlin's ability to do this. You can use KLaunchpad from a multitude of languages, not just Kotlin.

## Using this Library (Installation)

### Kotlin Multiplatform

TODO

### On the JVM (Java, Kotlin, Groovy, Scala etc)

Maven:

```xml
TODO
```

Gradle:

```groovy
TODO
```

### In JavaScript (JavaScript, TypeScript

KLaunchpad supports usage **in the browser** via JavaScript. It does not support Node.js because Node does not implement the Web MIDI API.

NPM: [`klauchpad`](https://www.npmjs.com/package/klaunchpad)

Pure JS: `TODO`

### Via a native shared library (C, Go, Rust, Python, etc)

This is only supported on Windows at the moment.

TODO

## Examples

Standard examples, available in every language that has examples:

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

* [Java Examples](TODO)
* [Kotlin Examples](src/jvmMain/kotlin/com/harry1453/klaunchpad/examples)
* [Kotlin Applications](src/jvmMain/kotlin/com/harry1453/klaunchpad/examples/applications) - These are some more complex applications that make use of the launchpad. Not part of the standard examples.

### JS (JavaScript, TypeScript)

* [React Examples](examples/js/react-examples)
* [Pure JS Examples](examples/js/purejs-examples)
* [Kotlin Examples](TODO)

### Via a native shared library (C, Go, Rust, Python, etc)

* [C Examples](TODO)
* [Go Examples](TODO)
* [Rust Examples](TODO)
* [Python Examples](TODO)

## Launchpad Support

Gen 1:
- Launchpad (Original): Not Supported
- Launchpad S: Not Supported
- Launchpad Mini: Not Supported

Gen 2:
- Launchpad MK2: **Fully Supported**
- Launchpad Pro: **Fully Supported but untested, likely buggy**

Gen 3:
- Launchpad Mini MK3: Not Supported
- Launchpad X: Not Supported, but support planned
- Launchpad Pro MK3: Not Supported

## Platform Support

- JVM: **Supported, using Java MIDI API**
- JS: **Supported for Web both in NPM (eg. React) and Pure JS, using Web MIDI API**. Not supported in Node.JS / non web.
- Native:
  - Windows: **Supported, using `winmm.dll`**
  - Other platforms: Not supported.

## Building KLaunchpad

TODO
