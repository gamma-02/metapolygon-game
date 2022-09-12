package client.render.gl;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;
import client.render.Uniform;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class GLUniform implements Uniform {
	int id;
	int program;
	
	int type;
	FloatBuffer floats;
	IntBuffer ints;
	
	Runnable uploader;
	
	private final MemoryStack stack = MemoryStack.stackPush();
	
	public GLUniform(int id, int program, int type, int count) {
		this.id = id;
		this.program = program;
		this.type = type;
		uploader = switch (count) {
			case 1 -> switch (type) {
				case GL11.GL_FLOAT -> () -> GL20.glUniform1fv(id, floats);
				case GL11.GL_INT -> uploader = () -> GL20.glUniform1iv(id, ints);
				default -> throw new IllegalStateException("Unexpected value: " + type);
			};
			case 2 -> switch (type) {
				case GL11.GL_FLOAT -> () -> GL20.glUniform2fv(id, floats);
				case GL11.GL_INT -> uploader = () -> GL20.glUniform2iv(id, ints);
				default -> throw new IllegalStateException("Unexpected value: " + type);
			};
			case 3 -> switch (type) {
				case GL11.GL_FLOAT -> () -> GL20.glUniform3fv(id, floats);
				case GL11.GL_INT -> uploader = () -> GL20.glUniform3iv(id, ints);
				default -> throw new IllegalStateException("Unexpected value: " + type);
			};
			case 4 -> switch (type) {
				case GL11.GL_FLOAT -> () -> GL20.glUniform4fv(id, floats);
				case GL11.GL_INT -> uploader = () -> GL20.glUniform4iv(id, ints);
				default -> throw new IllegalStateException("Unexpected value: " + type);
			};
			case 9 -> switch (type) {
				case GL11.GL_FLOAT -> () -> GL20.glUniformMatrix3fv(id, false, floats);
				default -> throw new IllegalStateException("Unexpected value: " + type);
			};
			case (4 * 4) -> switch (type) {
				case GL11.GL_FLOAT -> () -> GL20.glUniformMatrix4fv(id, false, floats);
				default -> throw new IllegalStateException("Unexpected value: " + type);
			};
			default -> throw new IllegalStateException("Unexpected value: " + type);
		};
		switch (type) {
			case GL11.GL_FLOAT:
				floats = stack.callocFloat(count);
			case GL11.GL_INT:
				ints = stack.callocInt(count);
		}
	}
	
	@Override
	public void upload() {
		uploader.run();
	}
	
	@Override
	public void set(Matrix4f matrix) {
		float[] floats = new float[4 * 4];
		floats = matrix.get(floats);
		for (int i = 0; i < floats.length; i++)
			this.floats.put(i, floats[i]);
	}
	
	public void delete() {
		stack.pop();
	}
	
	@Override
	public void set(int val) {
		ints.put(0, val);
	}
}
