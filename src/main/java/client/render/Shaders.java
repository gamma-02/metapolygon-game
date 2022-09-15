package client.render;

import client.render.gl.Shader;
import client.render.gl.ShaderProgram;
import client.render.packs.ResourcePacks;
import client.render.utils.ShaderType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Shaders {
	private static ShaderProgram STANDARD = null;
	private static ShaderProgram LEVEL = null;
	private static ShaderProgram MAP = null;
	public static HashMap<String, ShaderProgram> programHashMap = new HashMap<>();
	
	public static ShaderProgram getStandard() {
		return STANDARD;
	}
	
	public static ShaderProgram getMap() {
		return MAP;
	}
	
	public static ShaderProgram getLevel() {
		return LEVEL;
	}
	
	public static void reload() {
		for (int i = 0; i < 2; i++) {
			try {
				STANDARD = loadShader("standard");
				LEVEL = loadShader("game/level");
				MAP = loadShader("computation/map");
				return;
			} catch (Throwable err) {
				ResourcePacks.disable();
				// TODO: proper exception
			}
		}
	}
	
	public static ShaderProgram loadShader(String resource) throws IOException {
		ArrayList<Shader> shaders = new ArrayList<>();
		
		if (programHashMap.containsKey(resource)) programHashMap.remove(resource).delete();
		
		if (fileExists("modid/shaders/" + resource + ".vsh"))
			shaders.add(new Shader(ShaderType.VERTEX, new String(ResourcePacks.read("modid/shaders/" + resource + ".vsh"))));
		if (fileExists("modid/shaders/" + resource + ".fsh"))
			shaders.add(new Shader(ShaderType.FRAGMENT, new String(ResourcePacks.read("modid/shaders/" + resource + ".fsh"))));
		if (fileExists("modid/shaders/" + resource + ".geom"))
			shaders.add(new Shader(ShaderType.GEOMETRY, new String(ResourcePacks.read("modid/shaders/" + resource + ".geom"))));
		
		if (fileExists("modid/shaders/" + resource + ".tesc"))
			shaders.add(new Shader(ShaderType.TESSELATION_CONTROL, new String(ResourcePacks.read("modid/shaders/" + resource + ".tesc"))));
		if (fileExists("modid/shaders/" + resource + ".tese"))
			shaders.add(new Shader(ShaderType.TESSELATION_EVALUATION, new String(ResourcePacks.read("modid/shaders/" + resource + ".tese"))));
		
		if (fileExists("modid/shaders/" + resource + ".comp"))
			shaders.add(new Shader(ShaderType.COMPUTE, new String(ResourcePacks.read("modid/shaders/" + resource + ".comp"))));
		
		ShaderProgram program = new ShaderProgram(shaders.toArray(new Shader[0]));
		programHashMap.put(resource, program);
		return program;
	}
	
	private static boolean fileExists(String s) {
		// TODO: resource packs?
		return ResourcePacks.hasFile(s);
	}
}
