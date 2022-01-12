type Nullable<T> = T | null | undefined
export interface Launchpad {
    gridColumnCount: number;
    gridColumnStart: number;
    gridRowCount: number;
    gridRowStart: number;
    setPadButtonListener(listener: Nullable<(p0: Pad, p1: boolean, p2: number) => void>): void;
    setPadLight(pad: Nullable<Pad>, color: Color): void;
    clearPadLight(pad: Nullable<Pad>): void;
    flashPadLight(pad: Nullable<Pad>, color1: Color, color2: Color): void;
    flashPadLightOnAndOff(pad: Nullable<Pad>, color: Color): void;
    pulsePadLight(pad: Nullable<Pad>, color: Color): void;
    batchSetPadLights(padsAndColors: Map<Nullable<Pad>, Color>): void;
    batchSetRowLights(rowsAndColors: Map<number, Color>): void;
    batchSetColumnLights(columnsAndColors: Map<number, Color>): void;
    setAllPadLights(color: Color): void;
    clearAllPadLights(): void;
    scrollText(message: string, color: Color, loop: Nullable<boolean>): void;
    stopScrollingText(): void;
    setTextScrollFinishedListener(listener: Nullable<() => void>): void;
    autoClockEnabled: Nullable<boolean>;
    autoClockTempo: number;
    autoClockTempoRange: any;
    clock(): void;
    enterBootloader(): void;
    getPad(x: number, y: number): Nullable<Pad>;
    maxNumberOfFaders: number;
    setupFaderView(faders: Map<number, FaderSettings>, bipolar: Nullable<boolean>): void;
    updateFader(faderIndex: number, value: number): void;
    setFaderUpdateListener(listener: Nullable<(p0: number, p1: number) => void>): void;
    exitFaderView(): void;
    close(): void;
}

export interface MidiInputDevice {
}

export interface MidiOutputDevice {
}

export interface MidiInputDeviceInfo {
    name: string;
    version: string;
}

export interface MidiOutputDeviceInfo {
    name: string;
    version: string;
}

export interface Pad {
    gridX: number;
    gridY: number;
}

export interface Color {
    r: number;
    g: number;
    b: number;
}

export interface FaderSettings {
    color: Color;
    initialValue: number;
}

export const BLACK: Color;
export function connectToLaunchpadMK2(inputDevice: MidiInputDevice, outputDevice: MidiOutputDevice): Launchpad;
export function connectToLaunchpadPro(inputDevice: MidiInputDevice, outputDevice: MidiOutputDevice): Launchpad;
export function listMidiInputDevices(): Promise<Array<MidiInputDeviceInfo>>;
export function openMidiInputDevice(deviceInfo: MidiInputDeviceInfo): Promise<MidiInputDevice>;
export function listMidiOutputDevices(): Promise<Array<MidiOutputDeviceInfo>>;
export function openMidiOutputDevice(deviceInfo: MidiOutputDeviceInfo): Promise<MidiOutputDevice>;
export as namespace KLaunchpad;
