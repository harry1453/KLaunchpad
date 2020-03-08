package com.harry1453.launchpad

interface Pad {
    /**
      * Bottom left pad is 0, top right pad is 7
      */
    val gridX: Int

    /**
     * Bottom left pad is 0, top right pad is 7
     */
    val gridY: Int

    val midiCode: Int

    val isControlChange: Boolean
}
