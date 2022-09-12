package client.render.utils;

import client.render.gl.Shader;
import client.render.gl.ShaderProgram;
import common.utils.StreamUtils;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GL43C;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Shaders {
	private static ShaderProgram STANDARD = null;
	private static ShaderProgram MAP = null;
	public static HashMap<String, ShaderProgram> programHashMap = new HashMap<>();
	
	public static ShaderProgram getStandard() {
		return STANDARD;
	}
	
	public static ShaderProgram getMap() {
		return MAP;
	}
	
	public static void reload() {
		STANDARD = loadShader("standard");
		MAP = loadShader("map");
	}
	
	public static ShaderProgram loadShader(String resource) {
		ArrayList<Shader> shaders = new ArrayList<>();
		
		if (programHashMap.containsKey(resource)) programHashMap.remove(resource).delete();
		
		if (fileExists("shaders/" + resource + ".vsh"))
			shaders.add(new Shader(ShaderType.VERTEX, new String(StreamUtils.readStream(getStream("shaders/" + resource + ".vsh")))));
		if (fileExists("shaders/" + resource + ".fsh"))
			shaders.add(new Shader(ShaderType.FRAGMENT, new String(StreamUtils.readStream(getStream("shaders/" + resource + ".fsh")))));
		if (fileExists("shaders/" + resource + ".geom"))
			shaders.add(new Shader(ShaderType.GEOMETRY, new String(StreamUtils.readStream(getStream("shaders/" + resource + ".geom")))));
		
		if (fileExists("shaders/" + resource + ".tesc"))
			shaders.add(new Shader(ShaderType.TESSELATION_CONTROL, new String(StreamUtils.readStream(getStream("shaders/" + resource + ".tesc")))));
		if (fileExists("shaders/" + resource + ".tese"))
			shaders.add(new Shader(ShaderType.TESSELATION_EVALUATION, new String(StreamUtils.readStream(getStream("shaders/" + resource + ".tese")))));
		
		if (fileExists("shaders/" + resource + ".comp"))
			shaders.add(new Shader(ShaderType.COMPUTE, new String(StreamUtils.readStream(getStream("shaders/" + resource + ".comp")))));
		
		ShaderProgram program = new ShaderProgram(shaders.toArray(new Shader[0]));
		programHashMap.put(resource, program);
		return program;
	}
	
	private static boolean fileExists(String s) {
		// TODO: resource packs?
		return Shaders.class.getClassLoader().getResource(s) != null;
	}
	
	private static InputStream getStream(String s) {
		// TODO: resource packs?
		return Shaders.class.getClassLoader().getResourceAsStream(s);
	}
}
