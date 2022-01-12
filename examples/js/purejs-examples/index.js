let launchpad = null;

function randomColor() {
    return { r: Math.floor(Math.random() * 255), g: Math.floor(Math.random() * 255), b: Math.floor(Math.random() * 255) };
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
        if (connectedLaunchpad == null) {
            alert("Could not find Launchpad!");
        } else {
            launchpad = connectedLaunchpad;
            setCurrentExample("None, Connected to Launchpad");
            document.getElementById("connectMK2Button").style.display = 'none';
            document.getElementById("connectProButton").style.display = 'none';
            document.getElementById("disconnectButton").style.display = 'inline';
        }
    })
        .catch(e => {
            alert("Could not connect to Launchpad.");
            console.log(e);
        });
}

// noinspection JSUnusedGlobalSymbols
function connectPro() {
    connect(new Promise(async resolve => {
            let inputDevice = (await KLaunchpad.listMidiInputDevices()).find(inputDevice => inputDevice.name === "Launchpad Pro");
            if (inputDevice == null) {
                resolve(null);
                return;
            }
            let outputDevice = (await KLaunchpad.listMidiOutputDevices()).find(inputDevice => inputDevice.name === "Launchpad Pro");
            if (inputDevice == null) {
                resolve(null);
                return;
            }
            let launchpad = await KLaunchpad.connectToLaunchpadPro(inputDevice, outputDevice);
            resolve(launchpad);
        }
    ));
}

// noinspection JSUnusedGlobalSymbols
function connectMK2() {
    connect(new Promise(async resolve => {
            let inputDevice = await KLaunchpad.listMidiInputDevices()
                .then(deviceList => deviceList.find(device => device.name === "Launchpad MK2"))
                .then(device => device == null ? null : KLaunchpad.openMidiInputDevice(device));
            if (inputDevice == null) {
                resolve(null);
                return;
            }

            let outputDevice = await KLaunchpad.listMidiOutputDevices()
                .then(deviceList => deviceList.find(device => device.name === "Launchpad MK2"))
                .then(device => device == null ? null : KLaunchpad.openMidiOutputDevice(device));
            if (outputDevice == null) {
                resolve(null);
                return;
            }

            let launchpad = await KLaunchpad.connectToLaunchpadMK2(inputDevice, outputDevice);
            resolve(launchpad);
        }
    ));
}

function disconnect() {
    setCurrentExample("Disconnected");
    document.getElementById("connectMK2Button").style.display = 'inline';
    document.getElementById("connectProButton").style.display = 'inline';
    document.getElementById("disconnectButton").style.display = 'none';
    launchpad.close();
    launchpad = null;
}

function setupAutoClock(bpm) {
    launchpad.autoClockEnabled = true;
    launchpad.autoClockTempo = bpm;
}

// noinspection JSUnusedGlobalSymbols
function example_differentLightingModes() {
    if (launchpad != null) {
        resetLaunchpad();
        setCurrentExample("Different Lighting Modes");

        const red = { r: 255, g: 0, b: 0 };
        const green = { r: 0, g: 255, b: 0 };
        const blue = { r: 0, g: 0, b: 255 };
        setupAutoClock(60);
        launchpad.flashPadLightOnAndOff(launchpad.getPad(0, 0), red);
        launchpad.setPadLight(launchpad.getPad(1, 0), green);
        launchpad.pulsePadLight(launchpad.getPad(2, 0), blue);
    }
}

// noinspection JSUnusedGlobalSymbols
function example_enterBootloader() {
    if (launchpad != null) {
        resetLaunchpad();
        launchpad.enterBootloader();
        disconnect();
    }
}

function example_faders(bipolar) {
    if (launchpad != null) {
        resetLaunchpad();
        setCurrentExample((bipolar ? "Bipolar" : "Unipolar") + " Faders (Look in console for fader values)");

        const color1 = { r: 0, g: 0, b: 255 };
        const color2 = { r: 255, g: 100, b: 50 };
        const color3 = { r: 0, g: 50, b: 255 };
        const color4 = { r: 255, g: 50, b: 255 };
        const color5 = { r: 0, g: 255, b: 0 };
        const color6 = { r: 255, g: 50, b: 0 };
        const color7 = { r: 200, g: 200, b: 255 };
        const color8 = { r: 255, g: 0, b: 0 };
        const color9 = { r: 0, g: 255, b: 255 };

        const faders = new Map();
        const offset = bipolar ? 63 : 0;
        faders.set(0, { color: color1, initialValue: 15 - offset });
        faders.set(1, { color: color2, initialValue: 31 - offset });
        faders.set(2, { color: color3, initialValue: 47 - offset });
        faders.set(3, { color: color4, initialValue: 63 - offset });
        faders.set(4, { color: color5, initialValue: 79 - offset });
        faders.set(5, { color: color6, initialValue: 95 - offset });
        faders.set(6, { color: color7, initialValue: 111 - offset });
        faders.set(7, { color: color8, initialValue: 127 - offset });
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

// noinspection JSUnusedGlobalSymbols
function example_fadersUnipolar() {
    example_faders(false);
}

// noinspection JSUnusedGlobalSymbols
function example_fadersBipolar() {
    example_faders(true);
}

// noinspection JSUnusedGlobalSymbols
function example_flashPressedPad() {
    if (launchpad != null) {
        resetLaunchpad();
        setCurrentExample("Flash Pressed Pad");

        const color1 = { r: 0, g: 0, b: 255 };
        const color2 = { r: 255, g: 0, b: 0 };
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

// noinspection JSUnusedGlobalSymbols
function example_helloWorld() {
    if (launchpad != null) {
        resetLaunchpad();
        setCurrentExample("Hello World");

        launchpad.scrollText("{s7}Hello {s3}World!", { r: 0, g: 255, b: 0 }, true)
    }
}

// noinspection JSUnusedGlobalSymbols
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

// noinspection JSUnusedGlobalSymbols
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

// noinspection JSUnusedGlobalSymbols
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

// noinspection JSUnusedGlobalSymbols
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
