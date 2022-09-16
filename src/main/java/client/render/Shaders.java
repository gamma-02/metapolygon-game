package client.render;

import client.render.gl.Shader;
import client.render.gl.ShaderProgram;
import client.render.packs.ResourcePacks;
import client.render.utils.ShaderType;
import common.utils.FileIdentifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Shaders {
	private static ShaderProgram STANDARD = null;
	private static ShaderProgram LEVEL = null;
	private static ShaderProgram MAP = null;
	public static HashMap<FileIdentifier, ShaderProgram> programHashMap = new HashMap<>();
	
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
				STANDARD = loadShader(new FileIdentifier("standard"));
				LEVEL = loadShader(new FileIdentifier("game/level"));
				MAP = loadShader(new FileIdentifier("computation/map"));
				return;
			} catch (Throwable err) {
				ResourcePacks.disable();
				// TODO: proper exception
			}
		}
	}
	
	public static ShaderProgram loadShader(FileIdentifier resource) throws IOException {
		ArrayList<Shader> shaders = new ArrayList<>();
		
		if (programHashMap.containsKey(resource)) programHashMap.remove(resource).delete();
		
		if (fileExists(resource.getModId() + "/shaders/" + resource.getName() + ".vsh"))
			shaders.add(new Shader(ShaderType.VERTEX, new String(ResourcePacks.read(resource.getModId() + "/shaders/" + resource.getName() + ".vsh"))));
		if (fileExists(resource.getModId() + "/shaders/" + resource.getName() + ".fsh"))
			shaders.add(new Shader(ShaderType.FRAGMENT, new String(ResourcePacks.read(resource.getModId() + "/shaders/" + resource.getName() + ".fsh"))));
		if (fileExists(resource.getModId() + "/shaders/" + resource.getName() + ".geom"))
			shaders.add(new Shader(ShaderType.GEOMETRY, new String(ResourcePacks.read(resource.getModId() + "/shaders/" + resource.getName() + ".geom"))));
		
		if (fileExists(resource.getModId() + "/shaders/" + resource.getName() + ".tesc"))
			shaders.add(new Shader(ShaderType.TESSELATION_CONTROL, new String(ResourcePacks.read(resource.getModId() + "/shaders/" + resource.getName() + ".tesc"))));
		if (fileExists(resource.getModId() + "/shaders/" + resource.getName() + ".tese"))
			shaders.add(new Shader(ShaderType.TESSELATION_EVALUATION, new String(ResourcePacks.read(resource.getModId() + "/shaders/" + resource.getName() + ".tese"))));
		
		if (fileExists(resource.getModId() + "/shaders/" + resource.getName() + ".comp"))
			shaders.add(new Shader(ShaderType.COMPUTE, new String(ResourcePacks.read(resource.getModId() + "/shaders/" + resource.getName() + ".comp"))));
		
		ShaderProgram program = new ShaderProgram(shaders.toArray(new Shader[0]));
		programHashMap.put(resource, program);
		return program;
	}
	
	private static boolean fileExists(String s) {
		// TODO: resource packs?
		return ResourcePacks.hasFile(s);
	}
}
