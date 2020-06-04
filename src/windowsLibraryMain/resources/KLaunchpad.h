#ifndef KLAUNCHPAD_H
#define KLAUNCHPAD_H

#endif
#ifdef __cplusplus
typedef bool            KotlinBool;
#else
typedef _Bool           KotlinBool;
#endif

#ifdef __cplusplus
extern "C" {

extern void KLaunchpad_batchSetColumnLights(void* launchpadPtr, void* padsAndColors);
extern void KLaunchpad_batchSetPadLights(void* launchpadPtr, void* padsAndColors);
extern void KLaunchpad_batchSetRowLights(void* launchpadPtr, void* padsAndColors);
extern void KLaunchpad_clearAllPadLights(void* launchpadPtr);
extern void KLaunchpad_clearPadLight(void* launchpadPtr, void* padPtr);
extern void KLaunchpad_clock(void* launchpadPtr);
extern void KLaunchpad_closeLaunchpad(void* launchpadPtr);
extern void* KLaunchpad_connectToLaunchpadMK2(void* inputDevicePtr, void* outputDevicePtr);
extern void* KLaunchpad_connectToLaunchpadPro(void* inputDevicePtr, void* outputDevicePtr);
extern void KLaunchpad_enterBootloader(void* launchpadPtr);
extern void KLaunchpad_exitFaderView(void* launchpadPtr);
extern void KLaunchpad_flashPadLight(void* launchpadPtr, void* padPtr, void* colorPtr);
extern void KLaunchpad_flashPadLightOnAndOff(void* launchpadPtr, void* padPtr, void* color1Ptr, void* color2Ptr);
extern unsigned int KLaunchpad_getAutoClockTempo(void* launchpadPtr);
extern unsigned int KLaunchpad_getMaximumNumberOfFaders(void* launchpadPtr);
extern void* KLaunchpad_getPad(void* launchpadPtr, int x, int y);
extern int KLaunchpad_getX(void* padPtr);
extern int KLaunchpad_getY(void* padPtr);
extern unsigned int KLaunchpad_gridColumnCount(void* launchpadPtr);
extern unsigned int KLaunchpad_gridColumnStart(void* launchpadPtr);
extern unsigned int KLaunchpad_gridRowCount(void* launchpadPtr);
extern unsigned int KLaunchpad_gridRowStart(void* launchpadPtr);
extern KotlinBool KLaunchpad_isAutoClockEnabled(void* launchpadPtr);
extern KotlinBool KLaunchpad_isClosed(void* launchpadPtr);
extern void KLaunchpad_pulsePadLight(void* launchpadPtr, void* padPtr, void* colorPtr);
extern void KLaunchpad_scrollText(void* launchpadPtr, char* text, void* colorPtr, KotlinBool loop);
extern void KLaunchpad_setAllPadLights(void* launchpadPtr, void* colorPtr);
extern void KLaunchpad_setAutoClockEnabled(void* launchpadPtr, KotlinBool autoClockEnabled);
extern void KLaunchpad_setAutoClockTempo(void* launchpadPtr, unsigned int autoClockTempo);
extern void KLaunchpad_setFaderUpdateListener(void* launchpadPtr, void* listener);
extern void KLaunchpad_setPadButtonListener(void* launchpadPtr, void* listener);
extern void KLaunchpad_setPadLight(void* launchpadPtr, void* padPtr, void* colorPtr);
extern void KLaunchpad_setTextScrollFinishedListener(void* launchpadPtr, void* listener);
extern void KLaunchpad_setupFaderView(void* launchpadPtr, void* faders, KotlinBool bipolar);
extern void KLaunchpad_stopScrollingText(void* launchpadPtr);
extern void KLaunchpad_updateFader(void* launchpadPtr, unsigned int faderIndex, signed char value);
extern char* KLaunchpad_version();

#ifdef __cplusplus
}  /* extern "C" */
#endif

#endif // KLAUNCHPAD_H
