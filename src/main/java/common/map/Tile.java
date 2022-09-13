package common.map;

import client.render.utils.Shaders;
import client.render.gl.Texture;
import common.Options;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GL43C;
import org.lwjgl.stb.STBImageWrite;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

public class Tile {
	public final int x, y;
	public final Texture texture;
	
	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
		
		long start = System.nanoTime();
		texture = new Texture(Options.gridSize, Options.gridSize, GL11.GL_RED);
		System.out.println((System.nanoTime() - start) + " ns");
		
		Shaders.getMap().bind();
		GL20.glActiveTexture(GL20.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.id());
		
		start = System.nanoTime();
		GL43C.glDispatchCompute(Options.gridSize, Options.gridSize, 1);
		GL43C.glMemoryBarrier(GL43C.GL_ALL_BARRIER_BITS);
		System.out.println((System.nanoTime() - start) + " ns");
		Shaders.getMap().unbind();
		
		MemoryStack stack = MemoryStack.stackPush();
		
		start = System.nanoTime();
		ByteBuffer dataBuffer = texture.getValuesBuffer(stack, GL11.GL_RGBA);
		System.out.println((System.nanoTime() - start) + " ns");
		start = System.nanoTime();
		STBImageWrite.stbi_write_png("test.png", Options.gridSize, Options.gridSize, 4, dataBuffer, 0);
		System.out.println((System.nanoTime() - start) + " ns");
		stack.pop();
	}
}
