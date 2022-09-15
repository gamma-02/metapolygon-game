package client.render.packs;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ClassLoaderPack extends ResourcePack{
	@Override
	public String getName() {
		return "builtin";
	}
	
	@Override
	public byte[] read(String path) throws IOException {
		URL url = ClassLoaderPack.class.getClassLoader().getResource(path);
		if (url != null) {
			InputStream stream = ClassLoaderPack.class.getClassLoader().getResourceAsStream(path);
			byte[] bytes = stream.readAllBytes();
			stream.close();
			return bytes;
		}
		return null;
	}
	
	@Override
	public boolean hasFile(String path) {
		URL url = ClassLoaderPack.class.getClassLoader().getResource(path);
		return url != null;
	}
}
