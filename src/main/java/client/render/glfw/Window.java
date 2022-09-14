package client.render.glfw;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Window {
	long handle;
	private final int[] width = new int[1];
	private final int[] height = new int[1];
	
	public Window(int width, int height, String name) {
		handle = GLFW.glfwCreateWindow(width, height, name, 0, 0);
		GLFW.glfwSwapInterval(1);
	}
	
	public void center() {
		int[] width = new int[1];
		int[] height = new int[1];
		
		GLFW.glfwGetWindowSize(handle, width, height);
		
		GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		
		GLFW.glfwSetWindowPos(
				handle,
				(vidmode.width() - width[0]) / 2,
				(vidmode.height() - height[0]) / 2
		);
		
		GLFW.glfwSetKeyCallback(handle, (long window, int key, int scancode, int action, int mods) -> {
			char c = (char) key;
			if ((mods & GLFW.GLFW_MOD_SHIFT) == GLFW.GLFW_MOD_SHIFT) c = Character.toUpperCase(c);
			else c = Character.toLowerCase(c);
			KeyEvent event = new KeyEvent(action, key, scancode, mods, c);
			for (Consumer<KeyEvent> keyListener : keyListeners) {
				keyListener.accept(event);
			}
		});
	}
	
	public void setSwapInterval(int interval) {
		GLFW.glfwSwapInterval(interval);
	}
	
	public void initOpenGL() {
		GLFW.glfwMakeContextCurrent(handle);
	}
	
	public void show() {
		GLFW.glfwShowWindow(handle);
	}
	
	public void hide() {
		GLFW.glfwHideWindow(handle);
	}
	
	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(handle);
	}
	
	public void finishFrame() {
		GLFW.glfwSwapBuffers(handle);
		GLFW.glfwPollEvents();
		GLFW.glfwGetWindowSize(handle, width, height);
	}
	
	public void dispose() {
		hide();
		GLFW.glfwDestroyWindow(handle);
	}
	
	ArrayList<Consumer<KeyEvent>> keyListeners = new ArrayList<>();
	
	public void addKeyListener(Consumer<KeyEvent> eventConsumer) {
		keyListeners.add(eventConsumer);
	}
	
	public void removeKeyListener(Consumer<KeyEvent> eventConsumer) {
		keyListeners.remove(eventConsumer);
	}
	
	public void grabContext() {
		GLFW.glfwMakeContextCurrent(handle);
	}
	
	public int getWidth() {
		return width[0];
	}
	
	public int getHeight() {
		return height[0];
	}
}
