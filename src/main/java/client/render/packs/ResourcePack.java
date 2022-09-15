package client.render.packs;

import java.io.IOException;

public abstract class ResourcePack {
	public abstract String getName();
	public abstract byte[] read(String path) throws IOException;
	public abstract boolean hasFile(String path);
}
