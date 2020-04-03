package com.harry1453.klaunchpad.api

import kotlinx.cinterop.*
import platform.windows.*

@Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
object WindowsMidiApi {
    val midiConnect: CPointer<CFunction<(hmi: HMIDI, hmo: HMIDIOUT, pReserved: LPVOID) -> MMRESULT>>?
    val midiDisconnect: CPointer<CFunction<(hmi: HMIDI, hmo: HMIDIOUT, pReserved: LPVOID) -> MMRESULT>>?
    val midiInAddBuffer: CPointer<CFunction<(hmi: HMIDIIN, pmg: LPMIDIHDR, cbmh: UINT) -> MMRESULT>>?
    val midiInClose: CPointer<CFunction<(hmi: HMIDIIN) -> MMRESULT>>?
    val midiInGetDevCaps: CPointer<CFunction<(uDeviceID: UINT, pmic: LPMIDIINCAPS, cbmic: UINT) -> MMRESULT>>?
    val midiInGetErrorText: CPointer<CFunction<(mmrError: MMRESULT, pszText: LPWSTR, cchText: UINT) -> MMRESULT>>?
    val midiInGetID: CPointer<CFunction<(hmi: HMIDIIN, puDeviceID: LPUINT) -> MMRESULT>>?
    val midiInGetNumDevs: CPointer<CFunction<() -> UINT>>?
    val midiInMessage: CPointer<CFunction<(hmi: HMIDIIN, uMsg: UINT, dw1: DWORD_PTR, dw2: DWORD_PTR) -> MMRESULT>>?
    val midiInOpen: CPointer<CFunction<(phmi: LPHMIDIIN, uDeviceID: UINT, dwCallback: DWORD_PTR, dwInstance: DWORD_PTR, fdwOpen: DWORD) -> MMRESULT>>?
    val midiInPrepareHeader: CPointer<CFunction<(hmi: HMIDIIN, pmg: LPMIDIHDR, cbmh: UINT) -> MMRESULT>>?
    val midiInReset: CPointer<CFunction<(hmi: HMIDIIN) -> MMRESULT>>?
    val midiInStart: CPointer<CFunction<(hmi: HMIDIIN) -> MMRESULT>>?
    val midiInStop: CPointer<CFunction<(hmi: HMIDIIN) -> MMRESULT>>?
    val midiInUnprepareHeader: CPointer<CFunction<(hmi: HMIDIIN, pmg: LPMIDIHDR, cbmh: UINT) -> MMRESULT>>?
    val midiOutCacheDrumPatches: CPointer<CFunction<(hmo: HMIDIOUT, uPatch: UINT, pwkya: LPWORD, fuCache: UINT) -> MMRESULT>>?
    val midiOutCachePatches: CPointer<CFunction<(hmo: HMIDIOUT, uBank: UINT, pwpa: LPWORD, fuCache: UINT) -> MMRESULT>>?
    val midiOutClose: CPointer<CFunction<(hmo: HMIDIOUT) -> MMRESULT>>?
    val midiOutGetDevCaps: CPointer<CFunction<(uDeviceID: UINT, pmoc: LPMIDIOUTCAPS, cbmoc: UINT) -> MMRESULT>>?
    val midiOutGetErrorText: CPointer<CFunction<(mmrError: MMRESULT, pszText: LPWSTR, cchText: UINT) -> MMRESULT>>?
    val midiOutGetID: CPointer<CFunction<(hmo: HMIDIOUT, puDeviceID: LPUINT) -> MMRESULT>>?
    val midiOutGetNumDevs: CPointer<CFunction<() -> UINT>>?
    val midiOutGetVolume: CPointer<CFunction<(hmo: HMIDIOUT, pdwVolume: LPDWORD) -> MMRESULT>>?
    val midiOutLongMsg: CPointer<CFunction<(hmo: HMIDIOUT, pmg: LPMIDIHDR, cbmh: UINT) -> MMRESULT>>?
    val midiOutMessage: CPointer<CFunction<(hmo: HMIDIOUT, uMsg: UINT, dw1: DWORD_PTR, dw2: DWORD_PTR) -> MMRESULT>>?
    val midiOutOpen: CPointer<CFunction<(phmo: LPHMIDIOUT, uDeviceID: UINT, dwCallback: DWORD_PTR, dwInstance: DWORD_PTR, fdwOpen: DWORD) -> MMRESULT>>?
    val midiOutPrepareHeader: CPointer<CFunction<(hmo: HMIDIOUT, pmg: LPMIDIHDR, cbmh: UINT) -> MMRESULT>>?
    val midiOutReset: CPointer<CFunction<(hmo: HMIDIOUT) -> MMRESULT>>?
    val midiOutSetVolume: CPointer<CFunction<(hmo: HMIDIOUT, dwVolume: DWORD) -> MMRESULT>>?
    val midiOutShortMsg: CPointer<CFunction<(hmo: HMIDIOUT, dwMsg: DWORD) -> MMRESULT>>?
    val midiOutUnprepareHeader: CPointer<CFunction<(hmo: HMIDIOUT, pmh: LPMIDIHDR, cbmh: UINT) -> MMRESULT>>?
    val midiStreamClose: CPointer<CFunction<(hms: HMIDISTRM) -> MMRESULT>>?
    val midiStreamOpen: CPointer<CFunction<(phms: LPHMIDISTRM, puDeviceID: LPUINT, cMidi: DWORD, dwCallback: DWORD_PTR, dwInstance: DWORD_PTR, fdwOpen: DWORD) -> MMRESULT>>?
    val midiStreamOut: CPointer<CFunction<(hms: HMIDISTRM, pmg: LPMIDIHDR, cbmh: UINT) -> MMRESULT>>?
    val midiStreamPause: CPointer<CFunction<(hms: HMIDISTRM) -> MMRESULT>>?
    val midiStreamPosition: CPointer<CFunction<(hms: HMIDISTRM, lpmmt: LPMMTIME, cbmmt: UINT) -> MMRESULT>>?
    val midiStreamProperty: CPointer<CFunction<(hms: HMIDISTRM, lppropdata: LPBYTE, dwProperty: DWORD) -> MMRESULT>>?
    val midiStreamRestart: CPointer<CFunction<(hms: HMIDISTRM) -> MMRESULT>>?
    val midiStreamStop: CPointer<CFunction<(hms: HMIDISTRM) -> MMRESULT>>?

    init {
        val dll = LoadLibrary?.invoke(memScoped { "winmm.dll".wcstr.getPointer(this) })
        if (dll == null) { // LoadLibrary or dll is null
            midiConnect = null
            midiDisconnect = null
            midiInAddBuffer = null
            midiInClose = null
            midiInGetDevCaps = null
            midiInGetErrorText = null
            midiInGetID = null
            midiInGetNumDevs = null
            midiInMessage = null
            midiInOpen = null
            midiInPrepareHeader = null
            midiInReset = null
            midiInStart = null
            midiInStop = null
            midiInUnprepareHeader = null
            midiOutCacheDrumPatches = null
            midiOutCachePatches = null
            midiOutClose = null
            midiOutGetDevCaps = null
            midiOutGetErrorText = null
            midiOutGetID = null
            midiOutGetNumDevs = null
            midiOutGetVolume = null
            midiOutLongMsg = null
            midiOutMessage = null
            midiOutOpen = null
            midiOutPrepareHeader = null
            midiOutReset = null
            midiOutSetVolume = null
            midiOutShortMsg = null
            midiOutUnprepareHeader = null
            midiStreamClose = null
            midiStreamOpen = null
            midiStreamOut = null
            midiStreamPause = null
            midiStreamPosition = null
            midiStreamProperty = null
            midiStreamRestart = null
            midiStreamStop = null
        } else {
            midiConnect = GetProcAddress(dll, "midiConnect")?.reinterpret()
            midiDisconnect = GetProcAddress(dll, "midiDisconnect")?.reinterpret()
            midiInAddBuffer = GetProcAddress(dll, "midiInAddBuffer")?.reinterpret()
            midiInClose = GetProcAddress(dll, "midiInClose")?.reinterpret()
            midiInGetDevCaps = GetProcAddress(dll, "midiInGetDevCapsW")?.reinterpret()
            midiInGetErrorText = GetProcAddress(dll, "midiInGetErrorTextW")?.reinterpret()
            midiInGetID = GetProcAddress(dll, "midiInGetID")?.reinterpret()
            midiInGetNumDevs = GetProcAddress(dll, "midiInGetNumDevs")?.reinterpret()
            midiInMessage = GetProcAddress(dll, "midiInMessage")?.reinterpret()
            midiInOpen = GetProcAddress(dll, "midiInOpen")?.reinterpret()
            midiInPrepareHeader = GetProcAddress(dll, "midiInPrepareHeader")?.reinterpret()
            midiInReset = GetProcAddress(dll, "midiInReset")?.reinterpret()
            midiInStart = GetProcAddress(dll, "midiInStart")?.reinterpret()
            midiInStop = GetProcAddress(dll, "midiInStop")?.reinterpret()
            midiInUnprepareHeader = GetProcAddress(dll, "midiInUnprepareHeader")?.reinterpret()
            midiOutCacheDrumPatches = GetProcAddress(dll, "midiOutCacheDrumPatches")?.reinterpret()
            midiOutCachePatches = GetProcAddress(dll, "midiOutCachePatches")?.reinterpret()
            midiOutClose = GetProcAddress(dll, "midiOutClose")?.reinterpret()
            midiOutGetDevCaps = GetProcAddress(dll, "midiOutGetDevCapsW")?.reinterpret()
            midiOutGetErrorText = GetProcAddress(dll, "midiOutGetErrorTextW")?.reinterpret()
            midiOutGetID = GetProcAddress(dll, "midiOutGetID")?.reinterpret()
            midiOutGetNumDevs = GetProcAddress(dll, "midiOutGetNumDevs")?.reinterpret()
            midiOutGetVolume = GetProcAddress(dll, "midiOutGetVolume")?.reinterpret()
            midiOutLongMsg = GetProcAddress(dll, "midiOutLongMsg")?.reinterpret()
            midiOutMessage = GetProcAddress(dll, "midiOutMessage")?.reinterpret()
            midiOutOpen = GetProcAddress(dll, "midiOutOpen")?.reinterpret()
            midiOutPrepareHeader = GetProcAddress(dll, "midiOutPrepareHeader")?.reinterpret()
            midiOutReset = GetProcAddress(dll, "midiOutReset")?.reinterpret()
            midiOutSetVolume = GetProcAddress(dll, "midiOutSetVolume")?.reinterpret()
            midiOutShortMsg = GetProcAddress(dll, "midiOutShortMsg")?.reinterpret()
            midiOutUnprepareHeader = GetProcAddress(dll, "midiOutUnprepareHeader")?.reinterpret()
            midiStreamClose = GetProcAddress(dll, "midiStreamClose")?.reinterpret()
            midiStreamOpen = GetProcAddress(dll, "midiStreamOpen")?.reinterpret()
            midiStreamOut = GetProcAddress(dll, "midiStreamOut")?.reinterpret()
            midiStreamPause = GetProcAddress(dll, "midiStreamPause")?.reinterpret()
            midiStreamPosition = GetProcAddress(dll, "midiStreamPosition")?.reinterpret()
            midiStreamProperty = GetProcAddress(dll, "midiStreamProperty")?.reinterpret()
            midiStreamRestart = GetProcAddress(dll, "midiStreamRestart")?.reinterpret()
            midiStreamStop = GetProcAddress(dll, "midiStreamStop")?.reinterpret()
        }
    }

    fun checkError(mm: MMRESULT): String? {
        if (mm == MMSYSERR_NOERROR.toUInt()) return null
        memScoped {
            val stringBuffer = allocArray<UShortVar>(256)
            if (WindowsMidiApi.midiOutGetErrorText == null) return "DLL Not Loaded" // Compiler bug: The smart cast does not work if you remove explicit WindowsMidiApi TODO report!
            val retVal = WindowsMidiApi.midiOutGetErrorText(mm, stringBuffer, 256u)
            if (retVal != MMSYSERR_NOERROR.toUInt()) return "Error fetching error message"
            return stringBuffer.toKString()
        }
    }

    fun throwIfError(mm: MMRESULT) {
        val error = checkError(mm)
        if (error != null) {
            throw Exception(error)
        }
    }
}
