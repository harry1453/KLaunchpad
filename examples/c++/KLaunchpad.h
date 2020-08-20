#ifndef KLAUNCHPAD_H
#define KLAUNCHPAD_H

#ifdef __cplusplus
#include <cstdint>
#else
#include <stdint.h>
#endif

typedef struct {
    uint8_t r;
    uint8_t g;
    uint8_t b;
} Color;

typedef void* Launchpad;
typedef void* Pad;
typedef void* MidiInputDeviceInfo;
typedef void* MidiInputDevice;
typedef void* MidiOutputDeviceInfo;
typedef void* MidiOutputDevice;

#ifdef __cplusplus
typedef bool            KotlinBool;
#else
typedef _Bool           KotlinBool;
#endif

#ifdef __cplusplus
extern "C" {
#endif

extern char* KLaunchpad_version();
extern void KLaunchpad_batchSetColumnLights(Launchpad launchpad, void* padsAndColors);
extern void KLaunchpad_batchSetPadLights(Launchpad launchpad, void* padsAndColors);
extern void KLaunchpad_batchSetRowLights(Launchpad launchpad, void* padsAndColors);
extern void KLaunchpad_clearAllPadLights(Launchpad launchpad);
extern void KLaunchpad_clearPadLight(Launchpad launchpad, Pad padPtr);
extern void KLaunchpad_clock(Launchpad launchpad);
extern void KLaunchpad_closeLaunchpad(Launchpad launchpad);
extern Launchpad KLaunchpad_connectToLaunchpadMK2(MidiInputDevice inputDevice, MidiOutputDevice outputDevice);
extern Launchpad KLaunchpad_connectToLaunchpadPro(MidiInputDevice inputDevice, MidiOutputDevice outputDevice);
extern void KLaunchpad_enterBootloader(Launchpad launchpad);
extern void KLaunchpad_exitFaderView(Launchpad launchpad);
extern void KLaunchpad_flashPadLight(Launchpad launchpad, Pad pad, Color color1, Color color2);
extern void KLaunchpad_flashPadLightOnAndOff(Launchpad launchpad, Pad pad, Color color);
extern unsigned int KLaunchpad_getAutoClockTempo(Launchpad launchpad);
extern unsigned int KLaunchpad_getMaximumNumberOfFaders(Launchpad launchpad);
extern Pad KLaunchpad_getPad(Launchpad launchpad, int x, int y);
extern int KLaunchpad_getX(Pad pad);
extern int KLaunchpad_getY(Pad pad);
extern unsigned int KLaunchpad_gridColumnCount(Launchpad launchpad);
extern unsigned int KLaunchpad_gridColumnStart(Launchpad launchpad);
extern unsigned int KLaunchpad_gridRowCount(Launchpad launchpad);
extern unsigned int KLaunchpad_gridRowStart(Launchpad launchpad);
extern KotlinBool KLaunchpad_isAutoClockEnabled(Launchpad launchpad);
extern KotlinBool KLaunchpad_isClosed(Launchpad launchpad);
extern void KLaunchpad_pulsePadLight(Launchpad launchpad, Pad pad, Color color);
extern void KLaunchpad_scrollText(Launchpad launchpad, char* text, Color color, KotlinBool loop);
extern void KLaunchpad_setAllPadLights(Launchpad launchpad, Color color);
extern void KLaunchpad_setAutoClockEnabled(Launchpad launchpad, KotlinBool autoClockEnabled);
extern void KLaunchpad_setAutoClockTempo(Launchpad launchpad, unsigned int autoClockTempo);
extern void KLaunchpad_setFaderUpdateListener(Launchpad launchpad, void listener(int faderIndex, char newFaderValue));
extern void KLaunchpad_setPadButtonListener(Launchpad launchpad, void listener(Pad pad, bool pressed, char velocity));
extern void KLaunchpad_setPadLight(Launchpad launchpad, Pad pad, Color color);
extern void KLaunchpad_setTextScrollFinishedListener(Launchpad launchpad, void listener());
extern void KLaunchpad_setupFaderView(Launchpad launchpad, void* faders, KotlinBool bipolar);
extern void KLaunchpad_stopScrollingText(Launchpad launchpad);
extern void KLaunchpad_updateFader(Launchpad launchpad, unsigned int faderIndex, signed char value);

extern MidiInputDeviceInfo* KLaunchpad_listMidiInputDevices(unsigned int* lengthPtr);
extern char* KLaunchpad_midiInputDeviceInfo_getName(MidiInputDeviceInfo midiInputDeviceInfo);
extern char* KLaunchpad_midiInputDeviceInfo_getVersion(MidiInputDeviceInfo midiInputDeviceInfo);
extern MidiInputDevice KLaunchpad_openMidiInputDevice(MidiInputDeviceInfo midiInputDeviceInfo);

extern MidiOutputDeviceInfo* KLaunchpad_listMidiOutputDevices(unsigned int* lengthPtr);
extern char* KLaunchpad_midiOutputDeviceInfo_getName(MidiOutputDeviceInfo midiOutputDeviceInfo);
extern char* KLaunchpad_midiOutputDeviceInfo_getVersion(MidiOutputDeviceInfo midiOutputDeviceInfo);
extern MidiOutputDevice KLaunchpad_openMidiOutputDevice(MidiOutputDeviceInfo midiOutputDeviceInfo);

#ifdef __cplusplus
}  /* extern "C" */
#endif

#endif /* KLAUNCHPAD_H */
