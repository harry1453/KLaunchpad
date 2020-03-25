let launchpad = null;

function randomColor() {
    return KLaunchpad.color(Math.floor(Math.random() * 255), Math.floor(Math.random() * 255), Math.floor(Math.random() * 255));
}

function resetLaunchpad() {
    launchpad.exitFaderView();
    launchpad.stopScrollingText();
    launchpad.clearAllPadLights();
    launchpad.setPadButtonListener(null);
    launchpad.setFaderUpdateListener(null);
    launchpad.setTextScrollFinishedListener(null);
    launchpad.autoClockEnabled = false;
    launchpad.autoClockTempo = 120;
}

function setCurrentExample(currentExample) {
    document.getElementById("currentlyRunningExample").textContent = currentExample;
}

function connect(promise) {
    promise.then(connectedLaunchpad => {
        launchpad = connectedLaunchpad;
        setCurrentExample("None, Connected to Launchpad");
        document.getElementById("connectMK2Button").style.display = 'none';
        document.getElementById("connectProButton").style.display = 'none';
        document.getElementById("disconnectButton").style.display = 'inline';
    })
        .catch(e => {
            alert("Could not connect to Launchpad.");
            console.log(e);
        });
}

function connectPro() {
    connect(KLaunchpad.connectToLaunchpadPro());
}

function connectMK2() {
    connect(KLaunchpad.connectToLaunchpadMK2());
}

function disconnect() {
    launchpad.close();
    setCurrentExample("Disconnected");
    document.getElementById("connectMK2Button").style.display = 'inline';
    document.getElementById("connectProButton").style.display = 'inline';
    document.getElementById("disconnectButton").style.display = 'none';
}

function setupAutoClock(bpm) {
    launchpad.autoClockEnabled = true;
    launchpad.autoClockTempo = bpm;
}

function example_differentLightingModes() {
    if (launchpad != null) {
        resetLaunchpad();
        setCurrentExample("Different Lighting Modes");

        const red = KLaunchpad.color(255, 0, 0);
        const green = KLaunchpad.color(0, 255, 0);
        const blue = KLaunchpad.color(0, 0, 255);
        setupAutoClock(60);
        launchpad.flashPadLightOnAndOff(launchpad.getPad(0, 0), red);
        launchpad.setPadLight(launchpad.getPad(1, 0), green);
        launchpad.pulsePadLight(launchpad.getPad(2, 0), blue);
    }
}

function example_enterBootloader() {
    if (launchpad != null) {
        resetLaunchpad();
        launchpad.enterBootloader();
        launchpad.close();
        launchpad = null;
        setCurrentExample("Disconnected");
    }
}

function example_faders(bipolar) {
    if (launchpad != null) {
        resetLaunchpad();
        setCurrentExample((bipolar ? "Bipolar" : "Unipolar") + " Faders");

        // TODO
    }
}

function example_fadersUnipolar() {
    example_faders(false);
}

function example_fadersBipolar() {
    example_faders(true);
}

function example_flashPressedPad() {
    if (launchpad != null) {
        resetLaunchpad();
        setCurrentExample("Flash Pressed Pad");

        const color1 = KLaunchpad.color(0, 0, 255);
        const color2 = KLaunchpad.color(255, 0, 0);
        setupAutoClock(60);
        launchpad.setPadButtonListener((pad, pressed, velocity) => {
            if (pressed) {
                launchpad.flashPadLight(pad, color1, color2);
            } else {
                launchpad.clearPadLight(pad);
            }
        });
    }
}

function example_helloWorld() {
    if (launchpad != null) {
        resetLaunchpad();
        setCurrentExample("Hello World");

        launchpad.scrollText("{s7}Hello {s3}World!", KLaunchpad.color(0, 255, 0), true)
    }
}

function example_lightAllWhenPressed() {
    if (launchpad != null) {
        resetLaunchpad();
        setCurrentExample("Light All Pads When Pressed");

        launchpad.setPadButtonListener((pad, pressed, velocity) => {
            const color = randomColor();
            if (pressed) {
                launchpad.setAllPadLights(color);
            } else {
                launchpad.clearAllPadLights();
            }
        });
    }
}

function example_lightAroundPressedPad() {
    if (launchpad != null) {
        resetLaunchpad();
        setCurrentExample("Light Around Pressed Pad");

        launchpad.setPadButtonListener((pad, pressed, velocity) => {
            const color = pressed ? randomColor() : KLaunchpad.BLACK;
            const padAbove = launchpad.getPad(pad.gridX, pad.gridY + 1);
            const padBelow = launchpad.getPad(pad.gridX, pad.gridY - 1);
            const padLeft = launchpad.getPad(pad.gridX - 1, pad.gridY);
            const padRight = launchpad.getPad(pad.gridX + 1, pad.gridY);
            const map = new Map();
            map.set(pad, color);
            map.set(padAbove, color);
            map.set(padBelow, color);
            map.set(padLeft, color);
            map.set(padRight, color);
            launchpad.batchSetPadLights(map);
        });
    }
}

function example_lightPressedCoordinate() {
    if (launchpad != null) {
        resetLaunchpad();
        setCurrentExample("Light Pressed Coordinate");

        launchpad.setPadButtonListener((pad, pressed, velocity) => {
            const color = pressed ? randomColor() : KLaunchpad.BLACK;
            const mapX = new Map();
            const mapY = new Map();
            mapX.set(pad.gridX, color);
            mapY.set(pad.gridY, color);
            launchpad.batchSetColumnLights(mapX);
            launchpad.batchSetRowLights(mapY);
        });
    }
}

function example_lightPressedPad() {
    if (launchpad != null) {
        resetLaunchpad();
        setCurrentExample("Light Pressed Pad");

        launchpad.setPadButtonListener((pad, pressed, velocity) => {
            const color = randomColor();
            if (pressed) {
                launchpad.setPadLight(pad, color);
            } else {
                launchpad.clearPadLight(pad);
            }
        });
    }
}
