package client.render.utils;

import client.render.Uniform;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class FlagUniform implements Uniform {
	int id;
	int program;
	
	int flag = 0;
	
	IntBuffer ints;
	
	private final MemoryStack stack = MemoryStack.stackPush();
	
	public FlagUniform(int id, int program) {
		this.id = id;
		this.program = program;
		ints = stack.mallocInt(1);
		set(flag);
		upload();
	}
	
	@Override
	public void upload() {
		GL20.glUniform1iv(id, ints);
	}
	
	@Override
	public void set(Matrix4f matrix) {
		throw new RuntimeException();
	}
	
	public void enableDirectionalLighting() {
		set(flag | 1);
	}
	
	public void disableDirectionalLighting() {
		if ((flag & 1) == 1) set(flag ^ 1);
	}
	
	public void enableTexture() {
		set(flag | 2);
	}
	
	public void disableTexture() {
		if ((flag & 2) == 2) set(flag ^ 2);
	}
	
	@Override
	public void set(int val) {
		ints.put(0, flag = val);
	}
	
	@Override
	public void set(int... val) {
		throw new RuntimeException();
	}
	
	@Override
	public void set(float val) {
		throw new RuntimeException();
	}
	
	@Override
	public void set(float... val) {
		throw new RuntimeException();
	}
}
