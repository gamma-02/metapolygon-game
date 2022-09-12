package common.map;

import client.render.utils.Shaders;
import client.render.utils.Texture;
import common.Options;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL43C;
import org.lwjgl.stb.STBImageWrite;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.util.Random;

public class Tile {
	public final int x, y;
	public final Texture texture;
	
	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
		
		texture = new Texture(Options.gridSize, Options.gridSize, GL11.GL_RED);
		
		Shaders.getMap().bind();
		GL20.glActiveTexture(GL20.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.id());
		GL43C.glDispatchCompute(Options.gridSize, Options.gridSize, 1);
		long start = System.nanoTime();
		GL43C.glMemoryBarrier(GL43C.GL_ALL_BARRIER_BITS);
		System.out.println((System.nanoTime() - start) + " ns");
		Shaders.getMap().unbind();
		
		MemoryStack stack = MemoryStack.stackPush();
		
//		int[] data = new int[Options.gridSize * Options.gridSize * 4];
//		GL11.glReadPixels(0, 0, Options.gridSize, Options.gridSize, GL11.GL_RGBA, GL11.GL_INT, data);
		byte[] data = texture.getValues(GL11.GL_RGBA);
		ByteBuffer dataBuffer = stack.malloc(Options.gridSize * Options.gridSize * 4);
//		for (int i = 0; i < data.length; i++) dataBuffer.put(i, (byte) (Math.random() * 255));
		for (int i = 0; i < data.length; i++) dataBuffer.put(i, (byte) data[i]);
		STBImageWrite.stbi_write_png("test.png", Options.gridSize, Options.gridSize, 4, dataBuffer, 0);
		stack.pop();
	}
}
