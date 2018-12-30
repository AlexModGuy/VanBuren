package com.github.alexthe666.oldworldblues;

import net.ilexiconn.llibrary.server.config.ConfigEntry;

public class OWBConfig {

    @SuppressWarnings("deprecation")
    @ConfigEntry(category = "vats", comment = "distance from the player that mobs will be slowed down in VATS.")
    public double vatsEffectiveDistance = 32D;

    @SuppressWarnings("deprecation")
    @ConfigEntry(category = "vats", comment = "should vats slow down other players. Not reccomended for big servers or PVP.")
    public boolean vatsSlowPlayers = false;

    @SuppressWarnings("deprecation")
   // @ConfigEntry(category = "vats", comment = "How much vats should slow down entities. 1.0 = 100% of normal speed, 0 = no moving entities in VATS")
    public double vatsSlowRatio = 0.1;

    public int wastelandDimensionID = 76;

}
