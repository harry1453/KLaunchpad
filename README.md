# KLaunchpad (Novation Launchpad for Kotlin and many other languages)

Kotlin Launchpad is a high-level library for manipulating and interacting with [Novation Launchpads](https://novationmusic.com/en/launch).

It provides two main unified interfaces: `Launchpad` and `Pad`, which can represent any Launchpad.

Theoretically support for other grid-based control surfaces such as MIDI fighters could be built.

It is also a personal challenge to build one library and support as many languages as possible using one main library, and a testament to Kotlin's ability to do this. You can use KLaunchpad from a multitude of languages, not just Kotlin.

## Installation

### Kotlin Multiplatform (Kotlin/JVM, Kotlin/JS, Kotlin/Native on Windows x64 only)

```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}
```

```kotlin
commonMain {
    dependencies {
        implementation("com.github.harry1453.klaunchpad:klaunchpad:master-SNAPSHOT")
    }
}
```

### On the JVM (Java, Kotlin, Groovy, Scala etc)

Maven:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependency>
    <groupId>com.github.harry1453.klaunchpad</groupId>
    <artifactId>klaunchpad</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```

Gradle:

```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}
```

```kotlin
dependencies {
    implementation("com.github.harry1453.klaunchpad:klaunchpad:master-SNAPSHOT")
}
```

### In JavaScript (JavaScript / TypeScript)

KLaunchpad supports usage **in the browser** via JavaScript. It does not support Node.js because Node does not implement the Web MIDI API. (TODO this is possible with JZZ)

NPM: [`klaunchpad`](https://www.npmjs.com/package/klaunchpad)

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
* Hello World - The text "Hello World" will scroll across the launchpad. Demonstrates the Launchpad's native text scrolling, as well as how to update the scrolling speed mid-text.
* Light All When Pressed - Pressing a pad will light all pads in a random color. Demonstrates updating every pad at once.
* Light Around Pressed Pad - Pressing a pad will light it and the pads next to it in a random color. Demonstrates using the grid coordinate system to find pads, as well as batch updating pads.
* Light Pressed Coordinate - Pressing a pad will light its row and its column in a random color. Demonstrates updating a whole row or column of pads at once.
* Light Pressed Pad - Pressing a pad will light it in a random color. Demonstrates updating pads and listening for pad updates.

### JVM (Java, Kotlin, Groovy, Scala etc)

* [Java Examples](TODO) TODO
* [Kotlin Examples](examples/Kotlin/src/main/kotlin)
* [Kotlin Applications](examples/Kotlin/src/main/kotlin/applications) - These are some more complex applications that make use of the launchpad. Not part of the standard examples.

### JS (JavaScript / TypeScript)

* [React Examples](examples/JS/react-examples)
* [Pure JS Examples](examples/JS/purejs-examples)

### Via a native shared library (C/C++, Go, Rust, Python, etc)

* [C++ Examples](examples/C++) INCOMPLETE
* [Go Examples](examples/Go) INCOMPLETE
* [Rust Examples](TODO) TODO
* [Python Examples](TODO) TODO

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
  - Windows x64: **Supported, using `winmm.dll`**
  - Other platforms: Not supported.

## Building KLaunchpad

Compile and run all tests: `gradlew build`

Build a Pure JS minified bundle: `gradlew packageForPureJS` (The output will tell you where the bundle is)

Package ready for deployment to NPM: `gradlew preparePublishNpm` (The output will tell you how to publish)
