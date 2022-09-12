package client.render.gl;

import client.render.exceptions.ShaderCompilationException;
import client.render.utils.ShaderType;
import common.utils.TheUnsafe;
import org.lwjgl.opengl.GL20;

public class Shader {
	protected final ShaderType type;
	public final int id;
	
	public Shader(ShaderType type, String source) {
		this.type = type;
		
		id = GL20.glCreateShader(type.type);
		GL20.glShaderSource(id, source);
		GL20.glCompileShader(id);
		
		int[] data = new int[1];
		GL20.glGetShaderiv(id, GL20.GL_COMPILE_STATUS, data);
//		System.out.println(data[0]); // TODO: figure this out?
		int[] logLen = new int[1];
		GL20.glGetShaderiv(id, GL20.GL_COMPILE_STATUS, data);
		if (logLen[0] > 0) {
			String log = GL20.glGetShaderInfoLog(id, logLen[0]);
			System.err.println(log);
			TheUnsafe.throwUnchecked(new ShaderCompilationException("Failed to compile shader"));
		}
//		GL20.glGetUniformLocation();
	}
	
	public void delete() {
		GL20.glDeleteShader(id);
	}
}
