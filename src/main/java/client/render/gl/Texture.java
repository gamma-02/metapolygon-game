package client.render.gl;

import common.Options;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

public class Texture {
	int[] id = new int[1];
	
	public int id() {
		return id[0];
	}
	
	int width, height;
	int type;
	
	// https://medium.com/@daniel.coady/compute-shaders-in-opengl-4-3-d1c741998c03
	public Texture(int width, int height, int type) { // TODO: options, I guess?
		this.width = width;
		this.height = height;
		this.type = type;
		
		GL11.glGenTextures(id);
		GL20.glActiveTexture(GL20.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id[0]);
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);// create empty texture
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_R32F, width, height, 0, type, GL11.GL_FLOAT, 0);
		GL42.glBindImageTexture(0, id[0], 0, false, 0, GL20.GL_READ_WRITE, GL30.GL_R32F);
	}
	
	public void setValues(byte[] data) {
		MemoryStack stack = MemoryStack.stackPush();
		ByteBuffer dataBuffer = stack.malloc(Options.gridSize * Options.gridSize * 4);
		for (int i = 0; i < data.length; i++) dataBuffer.put(i, data[i]);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL40.GL_R32F, width, height, 0, type, GL11.GL_UNSIGNED_BYTE, dataBuffer);
		stack.pop();
	}
	
	public ByteBuffer getValuesBuffer(MemoryStack stack) {
		return getValuesBuffer(stack, type);
	}
	
	public ByteBuffer getValuesBuffer(MemoryStack stack, int type) {
		int mul = switch (type) {
			case GL11.GL_RED, GL11.GL_GREEN, GL11.GL_BLUE, GL11.GL_ALPHA -> 1;
			case GL11.GL_RGB -> 3;
			default -> 4;
		};
		ByteBuffer dataBuffer = stack.malloc(Options.gridSize * Options.gridSize * mul);
		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, type, GL11.GL_UNSIGNED_BYTE, dataBuffer);
		return dataBuffer;
	}
	
	public byte[] getValues() {
		return getValues(type);
	}
	
	public byte[] getValues(int type) {
		MemoryStack stack = MemoryStack.stackPush();
		ByteBuffer buffer = getValuesBuffer(stack, type);
		byte[] data = new byte[buffer.limit()];
		for (int i = 0; i < data.length; i++) data[i] = buffer.get(i);
		stack.pop();
		return data;
	}
}
