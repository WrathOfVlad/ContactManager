package com.contactmanager.utils.multiplatform;

public class MultiPlatformSupportFactory {
	private static final String OPERATING_SYSTEM = System.getProperty("os.name");
	
	public MultiPlatformSupportHandler getSupportHandler() {
		MultiPlatformSupportHandler multiPlatformSupport = null;
		if(OPERATING_SYSTEM.contains("Linux")) {
			multiPlatformSupport = new LinuxSupport();
		}
		if(OPERATING_SYSTEM.contains("Mac")) {
			multiPlatformSupport = new MacSupport();
		}
		if(OPERATING_SYSTEM.contains("Windows")) {
			multiPlatformSupport = new WindowsSupport();
		}
		return multiPlatformSupport;
		
	}
	
}
