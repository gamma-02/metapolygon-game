package client;

import client.render.Shaders;
import client.render.Uniform;
import client.render.gl.ShaderProgram;
import client.render.gl.VertexBuffer;
import client.render.glfw.KeyEvent;
import client.render.glfw.Window;
import client.render.utils.*;
import common.map.MapView;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.Configuration;

public class Main {
	protected static Window window;
	
	public static Window getWindow() {
		return window;
	}
	
	public static VertexBuffer vbo;
	
	public static void main(String[] args) {
		Configuration.STACK_SIZE.set(2048 * 64);
		if (!GLFW.glfwInit())
			throw new RuntimeException("Could not initialize GLFW");
		
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		window = new Window(300, 300, "game");
		window.center();
		window.initOpenGL();
		window.setSwapInterval(2); // vsync
		window.show();
		window.grabContext();
		GL.createCapabilities();
		
		Shaders.reload();
		
		FloatBufferBuilder bufferBuilder = new FloatBufferBuilder();
		bufferBuilder.appendFloats(
				-1, -1, 0, 1, 1, 1, 1, 1, 0, 1,
				1, -1, 0, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 0, 1, 1, 1, 1, 1, 1, 0,
				-1, 1, 0, 1, 1, 1, 1, 1, 0, 0
		);
//		bufferBuilder.appendFloats(
//				-1, -1, 0, 1, 0, 1, 1, 1, 0, 0,
//				1, -1, 0, 1, 1, 1, 0, 1, 0, 1,
//				1, 1, 0, 1, 0, 1, 1, 1, 1, 1,
//				-1, 1, 0, 1, 1, 0, 1, 1, 1, 0
//		);
		vbo = new VertexBuffer(VertexFormat.POSITION_COLOR_TEX, bufferBuilder.getData());
		
		ShaderProgram program = Shaders.getLevel();
		Uniform matrix = program.getUniform("modelViewMatrix", 4 * 4, true);
		Uniform projMat = program.getUniform("projectionMatrix", 4 * 4, true);
		Uniform scale = program.getUniform("scale", 2, true);
		Uniform offset = program.getUniform("offset", 2, true);
		Uniform texture = program.getUniform("tex0", 1, false);
		FlagUniform flags = program.getFlagUniform("flags");
		flags.enableTexture();
		
		program.bind();
		
		scale.set(1, 1f);
		scale.upload();
		offset.set(0, 0f);
		offset.upload();
		
		flags.upload();
		program.unbind();
		
		window.finishFrame();
		
		MapView tile = new MapView();
		
		int lw = 0;
		int lh = 0;
		final float[] dx = {0};
		final float[] dy = {0};
		
		boolean[] keysPressed = new boolean[4];
		window.addKeyListener((event) -> {
			boolean v;
			if ((v = (event.getAction() == KeyEvent.KEY_PRESS)) || event.getAction() == KeyEvent.KEY_RELEASED) {
				switch (Character.toLowerCase(event.getCharacter())) {
					case 'w':
						keysPressed[0] = v;
						break;
					case 's':
						keysPressed[1] = v;
						break;
					case 'a':
						keysPressed[2] = v;
						break;
					case 'd':
						keysPressed[3] = v;
						break;
				}
			}
		});
		
		while (!window.shouldClose()) {
			if (lh != window.getHeight() || lw != window.getWidth()) {
				lw = window.getWidth();
				lh = window.getHeight();
				
				tile.setSize(window.getWidth(), window.getHeight());
				texture.set(tile.getTexture().id());
				init2D(matrix, projMat);
				
				Shaders.getMap().bind();
				Uniform srcSize = Shaders.getMap().getUniform("srcSize", 2, true);
				srcSize.set((float) window.getWidth(), window.getHeight());
				srcSize.upload();
				Shaders.getMap().unbind();
			}
			
			if (keysPressed[0]) dy[0] += 0.01f;
			if (keysPressed[1]) dy[0] -= 0.01f;
			if (keysPressed[2]) dx[0] -= 0.01f;
			if (keysPressed[3]) dx[0] += 0.01f;
			
			Shaders.getMap().bind();
			Uniform mapOffset = Shaders.getMap().getUniform("offset", 2, true);
			mapOffset.set(dx[0], dy[0]);
			mapOffset.upload();
			Shaders.getMap().unbind();
			
			tile.compute();
			tick();
		}
		
		window.hide();
		window.dispose();
	}
	
	public static void init2D(Uniform matrix, Uniform projMat) {
		ShaderProgram program = Shaders.getLevel();
		
		program.bind();
		
		Matrix4f identityMatrix = new Matrix4f();
		identityMatrix.identity();
		
		matrix.set(identityMatrix);
		matrix.upload();
		projMat.set(identityMatrix);
		projMat.upload();
		
		program.unbind();
	}
	
	public static void tick() {
		ShaderProgram program = Shaders.getLevel();
		
		VertexFormat.POSITION_COLOR_TEX.setup();
		GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
		
		GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
		
		program.bind();
		vbo.bind();
		vbo.draw(DrawMode.QUADS);
		vbo.unbind();
		program.unbind();
		VertexFormat.POSITION_COLOR_TEX.teardown();
		
		window.finishFrame();
	}
}
