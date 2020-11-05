package io.github.cottonmc.cotton.cauldron.driver;

import io.github.cottonmc.libdp.api.DriverInitializer;
import io.github.cottonmc.libdp.api.driver.DriverManager;

public class LibDPCompat implements DriverInitializer {

	@Override
	public void init(DriverManager tweakerManager) {
		tweakerManager.addDriver("cotton.cauldron.CauldronDriver", CauldronDriver.INSTANCE);
	}

}
