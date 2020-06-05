#include <iostream>
#include "KLaunchpad.h"

Launchpad connectToLaunchpad();
void listenForPads(Pad pad, bool pressed, char velocity);

Launchpad launchpad;
Color white = {255, 255, 255};

int main() {
    std::cout << "KLaunchpad Version: " << KLaunchpad_version() << std::endl << std::endl;

    launchpad = connectToLaunchpad();
    if (launchpad == nullptr) return 1;

    std::cout << "Successfully connected to Launchpad MK2!" << std::endl;

    Color blue = {0, 0, 255};

    KLaunchpad_scrollText(launchpad, "{s7}Hello From C!", blue, false);

    Pad bottomLeftPad = KLaunchpad_getPad(launchpad, 0, 0);
    Color green = {0, 255, 0};

    KLaunchpad_flashPadLightOnAndOff(launchpad, bottomLeftPad, green);

    KLaunchpad_setPadButtonListener(launchpad, listenForPads);

    for (;;) {

    }
}

void listenForPads(Pad pad, bool pressed, char velocity) {
    if (pressed) {
        KLaunchpad_setPadLight(launchpad, pad, white);
    } else {
        KLaunchpad_clearPadLight(launchpad, pad);
    }
}

Launchpad connectToLaunchpad() {
    unsigned int inputDeviceCount;
    MidiInputDeviceInfo chosenInputDevice = nullptr;
    MidiInputDeviceInfo* inputDevices = KLaunchpad_listMidiInputDevices(&inputDeviceCount);
    std::cout << inputDeviceCount << " input devices available." << std::endl;
    for (int i = 0; i < inputDeviceCount; i++) {
        char* name = KLaunchpad_midiInputDeviceInfo_getName(inputDevices[i]);
        char* version = KLaunchpad_midiInputDeviceInfo_getVersion(inputDevices[i]);
        std::cout << "Device " << i << ": " << name << ", version " << version;
        if (strcmp(name, "Launchpad MK2") == 0) {
            chosenInputDevice = inputDevices[i];
            std::cout << " << Choosing this one!";
        }
        std::cout << std::endl;
    }

    std::cout << std::endl;

    unsigned int outputDeviceCount;
    MidiOutputDeviceInfo chosenOutputDevice = nullptr;
    MidiOutputDeviceInfo* outputDevices = KLaunchpad_listMidiOutputDevices(&outputDeviceCount);
    std::cout << outputDeviceCount << " output devices available." << std::endl;
    for (int i = 0; i < outputDeviceCount; i++) {
        char* name = KLaunchpad_midiOutputDeviceInfo_getName(outputDevices[i]);
        char* version = KLaunchpad_midiOutputDeviceInfo_getVersion(outputDevices[i]);
        std::cout << "Device " << i << ": " << name << ", version " << version;
        if (strcmp(name, "Launchpad MK2") == 0) {
            chosenOutputDevice = outputDevices[i];
            std::cout << " << Choosing this one!";
        }
        std::cout << std::endl;
    }

    std::cout << std::endl;

    if (chosenInputDevice == nullptr) {
        std::cout << "Error: Could not choose an input device!" << std::endl;
        return nullptr;
    }

    if (chosenOutputDevice == nullptr) {
        std::cout << "Error: Could not choose an output device!" << std::endl;
        return nullptr;
    }

    MidiInputDevice inputDevice = KLaunchpad_openMidiInputDevice(chosenInputDevice);
    MidiOutputDevice outputDevice = KLaunchpad_openMidiOutputDevice(chosenOutputDevice);

    return KLaunchpad_connectToLaunchpadMK2(inputDevice, outputDevice);
}
