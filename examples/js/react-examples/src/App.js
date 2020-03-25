import React from 'react';
import './App.css';
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
        return KLaunchpad.color(Math.floor(Math.random() * 255), Math.floor(Math.random() * 255), Math.floor(Math.random() * 255));
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
                this.setState({launchpad: launchpad, currentExample: "None, Connected to Launchpad"});
            })
            .catch(e => {
                alert("Could not connect to Launchpad.");
                console.log(e);
            });
    }

    connectPro() {
        this.connect(KLaunchpad.connectToLaunchpadPro());
    }

    connectMK2() {
        this.connect(KLaunchpad.connectToLaunchpadMK2());
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

            const red = KLaunchpad.color(255, 0, 0);
            const green = KLaunchpad.color(0, 255, 0);
            const blue = KLaunchpad.color(0, 0, 255);
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
            this.state.launchpad.close();
            this.setState({launchpad: null, currentExample: "Disconnected"});
        }
    }

    example_faders(bipolar) {
        if (this.state.launchpad != null) {
            this.resetLaunchpad();
            this.setState({currentExample: (bipolar ? "Bipolar" : "Unipolar") + " Faders"});

            // FIXME this doesn't work :(

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
            faders.set(0, KLaunchpad.faderSettings(color1, 15 - bipolar ? 63 : 0));
            faders.set(1, KLaunchpad.faderSettings(color2, 31 - bipolar ? 63 : 0));
            faders.set(2, KLaunchpad.faderSettings(color3, 47 - bipolar ? 63 : 0));
            faders.set(3, KLaunchpad.faderSettings(color4, 63 - bipolar ? 63 : 0));
            faders.set(4, KLaunchpad.faderSettings(color5, 79 - bipolar ? 63 : 0));
            faders.set(5, KLaunchpad.faderSettings(color6, 95 - bipolar ? 63 : 0));
            faders.set(6, KLaunchpad.faderSettings(color7, 111 - bipolar ? 63 : 0));
            faders.set(7, KLaunchpad.faderSettings(color8, 127) - bipolar ? 63 : 0);
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

            const color1 = KLaunchpad.color(0, 0, 255);
            const color2 = KLaunchpad.color(255, 0, 0);
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

            this.state.launchpad.scrollText("{s7}Hello {s3}World!", KLaunchpad.color(0, 255, 0), true)
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
            (<div><button onClick={this.connectPro}>Connect to Launchpad Pro</button>
                <button onClick={this.connectMK2}>Connect to Launchpad MK2</button></div>)
            : (<button onClick={this.disconnect}>Disconnect from Launchpad</button>);
        return (
            <div>
                <p>Currently Running Example: {this.state.currentExample}</p>
                {connectButton}
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
