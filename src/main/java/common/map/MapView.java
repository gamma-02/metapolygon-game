package common.map;

import client.render.gl.Texture;
import client.render.utils.Shaders;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL43C;
import org.lwjgl.stb.STBImageWrite;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

public class MapView {
	protected Texture texture;
	
	protected int width, height;
	
	public Texture getTexture() {
		return texture;
	}
	
	public MapView() {
	}
	
	public void setSize(int width, int height) {
		long start = System.nanoTime();
		if (texture != null) texture.close();
		texture = new Texture(this.width = width, this.height = height, GL11.GL_RED);
		System.out.println((System.nanoTime() - start) + " ns");
	}
	
	public void compute() {
		Shaders.getMap().bind();
		GL20.glActiveTexture(GL20.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.id());
		
		GL43C.glDispatchCompute(width, height, 1);
		GL43C.glMemoryBarrier(GL43C.GL_ALL_BARRIER_BITS);
		Shaders.getMap().unbind();
	}
	
	public void write() {
		MemoryStack stack = MemoryStack.stackPush();
		
		long start = System.nanoTime();
		ByteBuffer dataBuffer = texture.getValuesBuffer(stack, GL11.GL_RGBA);
		System.out.println((System.nanoTime() - start) + " ns");
		start = System.nanoTime();
		STBImageWrite.stbi_write_png("test.png", width, height, 4, dataBuffer, 0);
		System.out.println((System.nanoTime() - start) + " ns");
		stack.pop();
	}
}
