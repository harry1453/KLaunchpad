import React from 'react';
import KLaunchpad from 'klaunchpad';

class App extends React.Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            launchpad: null,
            currentExample: "Disconnected"
        };

        this.resetLaunchpad = this.resetLaunchpad.bind(this);
        this.connect = this.connect.bind(this);
        this.connectPro = this.connectPro.bind(this);
        this.connectMK2 = this.connectMK2.bind(this);
        this.disconnect = this.disconnect.bind(this);
        this.setupAutoClock = this.setupAutoClock.bind(this);

        this.example_differentLightingModes = this.example_differentLightingModes.bind(this);
        this.example_enterBootloader = this.example_enterBootloader.bind(this);
        this.example_faders = this.example_faders.bind(this);
        this.example_fadersUnipolar = this.example_fadersUnipolar.bind(this);
        this.example_fadersBipolar = this.example_fadersBipolar.bind(this);
        this.example_flashPressedPad = this.example_flashPressedPad.bind(this);
        this.example_helloWorld = this.example_helloWorld.bind(this);
        this.example_lightAllWhenPressed = this.example_lightAllWhenPressed.bind(this);
        this.example_lightAroundPressedPad = this.example_lightAroundPressedPad.bind(this);
        this.example_lightPressedCoordinate = this.example_lightPressedCoordinate.bind(this);
        this.example_lightPressedPad = this.example_lightPressedPad.bind(this);
    }

    static randomColor() {
        return { r: Math.floor(Math.random() * 255), g: Math.floor(Math.random() * 255), b: Math.floor(Math.random() * 255) };
    }

    resetLaunchpad() {
        this.state.launchpad.exitFaderView();
        this.state.launchpad.stopScrollingText();
        this.state.launchpad.clearAllPadLights();
        this.state.launchpad.setPadButtonListener(null);
        this.state.launchpad.setFaderUpdateListener(null);
        this.state.launchpad.setTextScrollFinishedListener(null);
        // eslint-disable-next-line react/no-direct-mutation-state
        this.state.launchpad.autoClockEnabled = false;
        // eslint-disable-next-line react/no-direct-mutation-state
        this.state.launchpad.autoClockTempo = 120;
    }

    connect(promise) {
        promise.then(launchpad => {
            if (launchpad == null) {
                alert("Could not find Launchpad!");
            } else {
                this.setState({launchpad: launchpad, currentExample: "None, Connected to Launchpad"});
            }})
            .catch(e => {
                alert("Could not connect to Launchpad.");
                console.log(e);
            });
    }

    connectPro() {
        this.connect(new Promise(async resolve => {
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

    connectMK2() {
        this.connect(new Promise(async resolve => {
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

    disconnect() {
        this.state.launchpad.close();
        this.setState({launchpad: null, currentExample: "Disconnected"});
    }

    setupAutoClock(bpm) {
        // eslint-disable-next-line react/no-direct-mutation-state
        this.state.launchpad.autoClockEnabled = true;
        // eslint-disable-next-line react/no-direct-mutation-state
        this.state.launchpad.autoClockTempo = bpm;
    }

    example_differentLightingModes() {
        if (this.state.launchpad != null) {
            this.resetLaunchpad();
            this.setState({currentExample: "Different Lighting Modes"});

            const red = { r: 255, g: 0, b: 0 };
            const green = { r: 0, g: 255, b: 0 };
            const blue = { r: 0, g: 0, b: 255 };
            this.setupAutoClock(60);
            this.state.launchpad.flashPadLightOnAndOff(this.state.launchpad.getPad(0, 0), red);
            this.state.launchpad.setPadLight(this.state.launchpad.getPad(1, 0), green);
            this.state.launchpad.pulsePadLight(this.state.launchpad.getPad(2, 0), blue);
        }
    }

    example_enterBootloader() {
        if (this.state.launchpad != null) {
            this.resetLaunchpad();
            this.state.launchpad.enterBootloader();
            this.disconnect();
        }
    }

    example_faders(bipolar) {
        if (this.state.launchpad != null) {
            this.resetLaunchpad();
            this.setState({currentExample: (bipolar ? "Bipolar" : "Unipolar") + " Faders (Look in console for fader values)"});

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
            this.state.launchpad.setupFaderView(faders, bipolar);

            this.state.launchpad.setFaderUpdateListener((faderIndex, faderValue) => {
                console.log("Fader " + faderIndex + " updated to " + faderValue);
            });

            this.state.launchpad.setPadButtonListener((pad, pressed, velocity) => {
                if (pressed && pad.gridX === 8) { // Right column of edge buttons
                    // Update every fader
                    let faderValue = pad.gridY * 127 / 7;
                    if (bipolar) faderValue = faderValue - 64;
                    for (let i = 0; i < 8; i++) {
                        this.state.launchpad.updateFader(i, faderValue);
                    }
                }
            });

            // Update the right edge lights. We can't do a bulk update because it bugs the launchpad (fader lights don't update)
            for (let i = 0; i < 8; i++) {
                const pad = this.state.launchpad.getPad(8, i);
                this.state.launchpad.setPadLight(pad, color9);
            }
        }
    }

    example_fadersUnipolar() {
        this.example_faders(false);
    }

    example_fadersBipolar() {
        this.example_faders(true);
    }

    example_flashPressedPad() {
        if (this.state.launchpad != null) {
            this.resetLaunchpad();
            this.setState({currentExample: "Flash Pressed Pad"});

            const color1 = { r: 0, g: 0, b: 255 };
            const color2 = { r: 255, g: 0, b: 0 };
            this.setupAutoClock(60);
            this.state.launchpad.setPadButtonListener((pad, pressed, velocity) => {
                if (pressed) {
                    this.state.launchpad.flashPadLight(pad, color1, color2);
                } else {
                    this.state.launchpad.clearPadLight(pad);
                }
            });
        }
    }

    example_helloWorld() {
        if (this.state.launchpad != null) {
            this.resetLaunchpad();
            this.setState({currentExample: "Hello World"});

            this.state.launchpad.scrollText("{s7}Hello {s3}World!", { r: 0, g: 255, b: 0 }, true)
        }
    }

    example_lightAllWhenPressed() {
        if (this.state.launchpad != null) {
            this.resetLaunchpad();
            this.setState({currentExample: "Light All Pads When Pressed"});

            this.state.launchpad.setPadButtonListener((pad, pressed, velocity) => {
                const color = App.randomColor();
                if (pressed) {
                    this.state.launchpad.setAllPadLights(color);
                } else {
                    this.state.launchpad.clearAllPadLights();
                }
            });
        }
    }

    example_lightAroundPressedPad() {
        if (this.state.launchpad != null) {
            this.resetLaunchpad();
            this.setState({currentExample: "Light Around Pressed Pad"});

            this.state.launchpad.setPadButtonListener((pad, pressed, velocity) => {
                const color = pressed ? App.randomColor() : KLaunchpad.BLACK;
                const padAbove = this.state.launchpad.getPad(pad.gridX, pad.gridY + 1);
                const padBelow = this.state.launchpad.getPad(pad.gridX, pad.gridY - 1);
                const padLeft = this.state.launchpad.getPad(pad.gridX - 1, pad.gridY);
                const padRight = this.state.launchpad.getPad(pad.gridX + 1, pad.gridY);
                const map = new Map();
                map.set(pad, color);
                map.set(padAbove, color);
                map.set(padBelow, color);
                map.set(padLeft, color);
                map.set(padRight, color);
                this.state.launchpad.batchSetPadLights(map);
            });
        }
    }

    example_lightPressedCoordinate() {
        if (this.state.launchpad != null) {
            this.resetLaunchpad();
            this.setState({currentExample: "Light Pressed Coordinate"});

            this.state.launchpad.setPadButtonListener((pad, pressed, velocity) => {
                const color = pressed ? App.randomColor() : KLaunchpad.BLACK;
                const mapX = new Map();
                const mapY = new Map();
                mapX.set(pad.gridX, color);
                mapY.set(pad.gridY, color);
                this.state.launchpad.batchSetColumnLights(mapX);
                this.state.launchpad.batchSetRowLights(mapY);
            });
        }
    }

    example_lightPressedPad() {
        if (this.state.launchpad != null) {
            this.resetLaunchpad();
            this.setState({currentExample: "Light Pressed Pad"});

            this.state.launchpad.setPadButtonListener((pad, pressed, velocity) => {
                const color = App.randomColor();
                if (pressed) {
                    this.state.launchpad.setPadLight(pad, color);
                } else {
                    this.state.launchpad.clearPadLight(pad);
                }
            });
        }
    }

    render() {
        const connectButton = this.state.launchpad == null ?
            (<div><button onClick={this.connectMK2}>Connect to Launchpad MK2</button>
                <button onClick={this.connectPro}>Connect to Launchpad Pro</button></div>)
            : (<button onClick={this.disconnect}>Disconnect from Launchpad</button>);
        return (
            <div>
                <p>Currently Running Example: {this.state.currentExample}</p>
                <div>
                    {connectButton}
                </div>
                <button onClick={this.example_differentLightingModes}>{"Example: Different Lighting Modes"}</button>
                <button onClick={this.example_enterBootloader}>{"Example: Enter Bootloader (Will disconnect Launchpad!)"}</button>
                <button onClick={this.example_fadersUnipolar}>{"Example: Unipolar Faders"}</button>
                <button onClick={this.example_fadersBipolar}>{"Example: Bipolar Faders"}</button>
                <button onClick={this.example_flashPressedPad}>{"Example: Flash Pressed Pad"}</button>
                <button onClick={this.example_helloWorld}>{"Example: Hello World"}</button>
                <button onClick={this.example_lightAllWhenPressed}>{"Example: Light All Pads When Pressed"}</button>
                <button onClick={this.example_lightAroundPressedPad}>{"Example: Light Around Pressed Pad"}</button>
                <button onClick={this.example_lightPressedCoordinate}>{"Example: Light Pressed Coordinate"}</button>
                <button onClick={this.example_lightPressedPad}>{"Example: Light Pressed Pad"}</button>
            </div>
        );
    }
}

export default App;
