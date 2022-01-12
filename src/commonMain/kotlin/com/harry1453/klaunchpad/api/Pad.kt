package com.harry1453.klaunchpad.api

public interface Pad {
    /**
      * X position of this pad in the grid. 0 is the bottom left pad of the main grid area. Can be negative.
      */
    public val gridX: Int

    /**
     * Y position of this pad in the grid. 0 is the bottom left pad of the main grid area. Can be negative.
     */
    public val gridY: Int
}
