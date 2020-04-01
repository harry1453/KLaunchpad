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
        setCurrentExample((bipolar ? "Bipolar" : "Unipolar") + " Faders (Look in console for fader values)");

        const color1 = KLaunchpad.color(0, 0, 255);
        const color2 = KLaunchpad.color(255, 100, 50);
        const color3 = KLaunchpad.color(0, 50, 255);
        const color4 = KLaunchpad.color(255, 50, 255);
        const color5 = KLaunchpad.color(0, 255, 0);
        const color6 = KLaunchpad.color(255, 50, 0);
        const color7 = KLaunchpad.color(200, 200, 255);
        const color8 = KLaunchpad.color(255, 0, 0);
        const color9 = KLaunchpad.color(0, 255, 255);

        const faders = new Map();
        const offset = bipolar ? 63 : 0;
        faders.set(0, KLaunchpad.faderSettings(color1, 15 - offset));
        faders.set(1, KLaunchpad.faderSettings(color2, 31 - offset));
        faders.set(2, KLaunchpad.faderSettings(color3, 47 - offset));
        faders.set(3, KLaunchpad.faderSettings(color4, 63 - offset));
        faders.set(4, KLaunchpad.faderSettings(color5, 79 - offset));
        faders.set(5, KLaunchpad.faderSettings(color6, 95 - offset));
        faders.set(6, KLaunchpad.faderSettings(color7, 111 - offset));
        faders.set(7, KLaunchpad.faderSettings(color8, 127 - offset));
        launchpad.setupFaderView(faders, bipolar);

        launchpad.setFaderUpdateListener((faderIndex, faderValue) => {
            console.log("Fader " + faderIndex + " updated to " + faderValue);
        });

        launchpad.setPadButtonListener((pad, pressed, velocity) => {
            if (pressed && pad.gridX === 8) { // Right column of edge buttons
                // Update every fader
                let faderValue = pad.gridY * 127 / 7;
                if (bipolar) faderValue = faderValue - 64;
                for (let i = 0; i < 8; i++) {
                    launchpad.updateFader(i, faderValue);
                }
            }
        });

        // Update the right edge lights. We can't do a bulk update because it bugs the launchpad (fader lights don't update)
        for (let i = 0; i < 8; i++) {
            const pad = launchpad.getPad(8, i);
            launchpad.setPadLight(pad, color9);
        }
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
