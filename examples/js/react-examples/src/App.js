import React from 'react';
import logo from './logo.svg';
import './App.css';
import KLaunchpad from 'klaunchpad';

class App extends React.Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            launchpad: null,
            currentExample: "Disconnected"
        };

        this.connect = this.connect.bind(this);
        this.example_lightPressedButton = this.example_lightPressedButton.bind(this);
    }

    connect() {
        if (this.state.launchpad == null) {
            KLaunchpad.connectToLaunchpadMK2()
                .then(launchpad => {
                    this.setState({launchpad: launchpad, currentExample: "None"});
                })
                .catch(e => {
                    console.log("Could not connect to launchpad");
                    console.log(e);
                });
        } else {
            // this.state.launchpad.close(); // TODO
            this.setState({launchpad: null, currentExample: "Disconnected"});
            console.log("Disconnected.");
        }
    }

    example_lightPressedButton() {
        if (this.state.launchpad != null) {
            const color = KLaunchpad.color(0, 50, 255);
            this.setState({currentExample: "Light Pressed Button"});
            this.state.launchpad.setPadButtonListener((pad, pressed, velocity) => {
                if (pressed) {
                    this.state.launchpad.setPadLight(pad, color);
                } else {
                    this.state.launchpad.clearPadLight(pad);
                }
            });
        }
    }

    render() {
        return (
            <div className="App">
                <header className="App-header">
                    <p>Currently Running Example: {this.state.currentExample}</p>
                    <button onClick={this.connect}>{this.state.launchpad == null ? 'Connect' : 'Disconnect'}</button>
                    <button onClick={this.example_lightPressedButton}>{"Example: Light Pressed Button"}</button>
                </header>
            </div>
        );
    }
}

export default App;
