package com.harry1453.launchpad.api

interface Pad {
    /**
      * X position of this pad in the grid. 0 is the bottom left pad of the main grid area. Can be negative.
      */
    val gridX: Int

    /**
     * Y position of this pad in the grid. 0 is the bottom left pad of the main grid area. Can be negative.
     */
    val gridY: Int
}
