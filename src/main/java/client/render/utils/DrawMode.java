package client.render.utils;

import org.lwjgl.opengl.GL11;

public enum DrawMode {
	QUADS(GL11.GL_QUADS),
	QUAD_STRIP(GL11.GL_QUAD_STRIP),
	TRIANGLES(GL11.GL_TRIANGLES),
	TRIANGLE_STRIP(GL11.GL_TRIANGLE_STRIP),
	LINES(GL11.GL_LINES),
	LINE_STRIP(GL11.GL_LINE_STRIP),
	;
	
	public final int mode;
	
	DrawMode(int mode) {
		this.mode = mode;
	}
}
