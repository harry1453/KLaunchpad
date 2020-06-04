//+build windows

package main

import (
    "fmt"
    "log"
    "syscall"
    "unsafe"
)

import "C"

func main() {
	dll := syscall.NewLazyDLL("KLaunchpad.dll")
    proc := dll.NewProc("KLaunchpad_version")
    version, _, err := proc.Call()
    HandleError(err)
    fmt.Println("KLaunchpad version " + GoString(version))
}

func GoString(cString uintptr) string {
    var i uintptr = 0
    var buffer []byte
    for {
        newVal := *(*byte)(unsafe.Pointer(cString+i))
        if newVal == 0x00 {
            break
        }
        buffer = append(buffer, newVal)
        i++
    }
    return string(buffer)
}

func HandleError(err error) {
    if err != nil && err.Error() != "The operation completed successfully." {
        log.Fatalln(err)
    }
}
