package client.render.gl;

import client.render.Uniform;
import client.render.exceptions.ShaderCompilationException;
import client.render.utils.FlagUniform;
import common.utils.TheUnsafe;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.util.HashMap;

public class ShaderProgram {
	private final Shader[] shaders;
	private final int id;
	private final HashMap<String, Integer> attribs = new HashMap<>();
	private final HashMap<String, Uniform> uniforms = new HashMap<>();
	
	public ShaderProgram(Shader... shaders) {
		this.shaders = shaders;
		id = GL20.glCreateProgram();
		for (Shader shader : shaders)
			GL20.glAttachShader(id, shader.id);
		GL20.glLinkProgram(id);
		int[] status = new int[1];
		GL20.glGetProgramiv(id, GL20.GL_LINK_STATUS, status);
		int[] logLen = new int[1];
		GL20.glGetProgramiv(id, GL20.GL_INFO_LOG_LENGTH, logLen);
		if (logLen[0] > 0) {
			String log = GL20.glGetProgramInfoLog(id, logLen[0]);
			System.err.println(log);
			// TODO: exception class
			TheUnsafe.throwUnchecked(new ShaderCompilationException("Failed to compile shader"));
		}
	}
	
	public void delete() {
		for (Uniform value : uniforms.values())
			((GLUniform) value).delete();
		for (Shader shader : shaders)
			GL20.glDetachShader(id, shader.id);
		GL20.glDeleteProgram(id);
	}
	
	public void bind() {
		GL20.glUseProgram(id);
	}
	
	// TODO: use this
	public int getAttribLocation(String name) {
		return GL20.glGetAttribLocation(id, name);
	}
	
	public void unbind() {
		GL20.glUseProgram(0);
	}
	
	public Uniform getUniform(String name, int count, boolean floats) {
		if (!uniforms.containsKey(name)) {
			int location = GL20.glGetUniformLocation(id, name);
			GLUniform uniform = new GLUniform(location, id, floats ? GL11.GL_FLOAT : GL11.GL_INT, count);
			uniforms.put(name, uniform);
		}
		return uniforms.get(name);
	}
	
	public FlagUniform getFlagUniform(String name) {
		if (!uniforms.containsKey(name)) {
			int location = GL20.glGetUniformLocation(id, name);
			FlagUniform uniform = new FlagUniform(location, id);
			uniforms.put(name, uniform);
		}
		return (FlagUniform) uniforms.get(name);
	}
}
