package common.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.function.Function;

public class StreamUtils {
	private static final Function<InputStream, byte[]> reader;
	
	// idk if we're gonna use J8- or J9+
	// so I'm gonna support both
	static {
		String version = System.getProperty("java.version");
		version = version.split("\\.")[0];
		if (version.equals("1")) reader = StreamUtils::readLegacy;
		else reader = StreamUtils::readModern;
	}
	
	public static byte[] readStream(InputStream stream) {
		return reader.apply(stream); // small amount of overhead, for sake of supporting both J8 and J16 with hopefully fast code
	}
	
	protected static byte[] readModern(InputStream stream) {
		try {
			byte[] data = stream.readAllBytes();
			stream.close();
			return data;
		} catch (Throwable err) {
			// maybe the stream is from a J8 library
			return readLegacy(stream);
		}
	}
	
	protected static byte[] readLegacy(InputStream stream) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			int b;
			// so, fun thing about J8 streams
			// reading it byte by byte is slow
			// reading it all at once is unreliable
			// this does both: it reads as many bytes as the stream claims it has, and then reads a single byte to see if the stream is done
			while (true) {
				int availible = stream.available();
				if (availible != 0) {
					byte[] bytes = new byte[availible];
					stream.read(bytes);
					outputStream.write(bytes);
				}
				b = stream.read();
				if (b == -1) break;
				outputStream.write(b);
			}
			stream.close();
			byte[] data = outputStream.toByteArray();
			outputStream.flush();
			outputStream.close();
			return data;
		} catch (Throwable err) {
			TheUnsafe.throwUnchecked(err);
			return null;
		}
	}
}
