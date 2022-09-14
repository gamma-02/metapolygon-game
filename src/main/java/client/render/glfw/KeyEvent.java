package client.render.glfw;

import org.lwjgl.glfw.GLFW;

public class KeyEvent {
	private final int keyCode, scancode, mods;
	private final char character;
	private final int action;
	
	public static final int KEY_PRESS = GLFW.GLFW_PRESS;
	public static final int KEY_TYPED = 2;
	public static final int KEY_RELEASED = GLFW.GLFW_RELEASE;
	
	public KeyEvent(int action, int key, int scancode, int mods, char character) {
		this.action = action;
		this.keyCode = key;
		this.scancode = scancode;
		this.mods = mods;
		this.character = character;
	}
	
	public int getAction() {
		return action;
	}
	
	public int getKeyCode() {
		return keyCode;
	}
	
	public int getScancode() {
		return scancode;
	}
	
	public int getMods() {
		return mods;
	}
	
	public char getCharacter() {
		return character;
	}
}
