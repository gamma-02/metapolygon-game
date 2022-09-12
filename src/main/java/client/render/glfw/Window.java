package client.render.glfw;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

public class Window {
	long handle;
	
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
	}
	
	public void dispose() {
		hide();
		GLFW.glfwDestroyWindow(handle);
	}
	
	public void grabContext() {
		GLFW.glfwMakeContextCurrent(handle);
	}
}
