package client.render.packs;

import client.render.exceptions.ResourceNotFoundException;

import java.io.IOException;
import java.util.ArrayList;

public class ResourcePacks {
	private static final ArrayList<ResourcePack> PACKS = new ArrayList<>();
	private static String packList = new String();
	
	static {
		PACKS.add(new ClassLoaderPack());
	}
	
	public static void addPack(ResourcePack pack) {
		PACKS.add(pack);
		if (!packList.equals(""))
			packList += ", ";
		packList += pack.getName();
	}
	
	public static void removePack(ResourcePack pack) {
		PACKS.remove(pack);
	}
	
	public static byte[] read(String path) throws IOException {
		for (ResourcePack pack : PACKS) {
			byte[] bytes = pack.read(path);
			if (bytes != null) return bytes;
		}
		throw new ResourceNotFoundException("The requested resource " + path + " was not found in the following resource packs: " + packList);
	}
	
	public static boolean hasFile(String path) {
		for (ResourcePack pack : PACKS)
			if (pack.hasFile(path))
				return true;
		return false;
	}
	
	public static void disable() {
		for (ResourcePack pack : PACKS)
			removePack(pack);
		PACKS.add(new ClassLoaderPack());
	}
}
