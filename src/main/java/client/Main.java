package client;

import client.render.Uniform;
import client.render.gl.ShaderProgram;
import client.render.gl.VertexBuffer;
import client.render.glfw.Window;
import client.render.utils.DrawMode;
import client.render.utils.FloatBufferBuilder;
import client.render.utils.Shaders;
import client.render.utils.VertexFormat;
import common.map.Tile;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Quaternionf;
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
		window.setSwapInterval(1); // vsync
		window.show();
		window.grabContext();
		GL.createCapabilities();
		
		Shaders.reload();
		
		FloatBufferBuilder bufferBuilder = new FloatBufferBuilder();
		bufferBuilder.appendFloats(
				-1, -1, 0, 1, 1, 1, 1, 1, 0, 0, 1,
				1, -1, 0, 1, 1, 1, 1, 1, 0, 0, 1,
				1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1,
				-1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1
		);
		vbo = new VertexBuffer(VertexFormat.POSITION_COLOR_NORMAL, bufferBuilder.getData());
		
		ShaderProgram program = Shaders.getStandard();
		Uniform matrix = program.getUniform("modelViewMatrix", 4 * 4, true);
		Uniform projMat = program.getUniform("projectionMatrix", 4 * 4, true);
		Uniform flags = program.getUniform("flags", 1, false);
		
		program.bind();
		flags.set(1);
		flags.upload();
		program.unbind();
		
		Tile tile = new Tile(0, 0);
		init2D(matrix, projMat);
		while (!window.shouldClose()) tick();
		
		window.hide();
		window.dispose();
	}
	
	public static void init2D(Uniform matrix, Uniform projMat) {
		ShaderProgram program = Shaders.getStandard();

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
		ShaderProgram program = Shaders.getStandard();
		
		VertexFormat.POSITION_COLOR_NORMAL.setup();
		GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
		
		GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
		
		program.bind();
		vbo.bind();
		vbo.draw(DrawMode.QUADS);
		vbo.unbind();
		program.unbind();
		VertexFormat.POSITION_COLOR_NORMAL.teardown();
		
		window.finishFrame();
	}
}
