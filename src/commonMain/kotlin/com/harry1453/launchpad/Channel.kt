package com.harry1453.launchpad

// TODO this is not abstracted to allow for different launchpads
enum class Channel {
    /**
     * Session Channel
      */
    Channel1,

    /**
     * Flashing channel. When a colour is set on this channel, the launchpad will flash the LED between the previous colour and the new colour.
     * This can be stopped by setting a colour using channel 1.
     */
    Channel2,

    /**
     * Pulsing channel. When a colour is set on this channel, the launchpad will pulse the LED between 100% and 25% brighness.
     * This can be stopped by setting a colour using channel 1.
     */
    Channel3,
    Channel4,
    Channel5,

    /**
     * Default User 1 channel
     */
    Channel6,

    /**
     * Alternative User 1 channel
     */
    Channel7,

    /**
     * Alternative User 1 channel
     */
    Channel8,
    Channel9,
    Channel10,
    Channel11,
    Channel12,
    Channel13,

    /**
     * Default User 2 channel
     */
    Channel14,

    /**
     * Alternative User 2 channel
     */
    Channel15,

    /**
     * Alternative User 2 channel
     */
    Channel16,
    ;

    val channelId = ordinal
}
