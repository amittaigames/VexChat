package com.amittaigames.vexchat;

public class SystemData {

	private String os;
	private String version;
	private String arch;

	public SystemData() {
		this.os = System.getProperty("os.name");
		this.version = System.getProperty("os.version");
		this.arch = System.getProperty("os.arch");
	}

	private SystemData(String os, String version, String arch) {
		this.os = os;
		this.version = version;
		this.arch = arch;
	}

	public static String toPacketData(SystemData data) {
		return data.os + "%" + data.version + "%" + data.arch;
	}

	public static SystemData fromPacketData(String data) {
		String[] args = data.split("%");
		return new SystemData(args[0], args[1], args[2]);
	}

	public String getOS() {
		return os;
	}

	public String getVersion() {
		return version;
	}

	public String getArch() {
		return arch;
	}
}
