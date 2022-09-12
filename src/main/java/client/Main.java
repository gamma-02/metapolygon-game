package client;

import client.render.glfw.Window;
import client.render.utils.Shaders;
import common.map.Tile;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.Configuration;

public class Main {
	protected static Window window;
	
	public static Window getWindow() {
		return window;
	}
	
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
		Tile tile = new Tile(0, 0);
		while (!window.shouldClose()) tick();
		
		window.hide();
		window.dispose();
	}
	
	public static void tick() {
		window.finishFrame();
	}
}
