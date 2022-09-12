package client.render.utils;

import org.lwjgl.opengl.GL11;

public class VertexElement {
	public static final VertexElement POSITION = new VertexElement(4, GL11.GL_FLOAT);
	public static final VertexElement COLOR = new VertexElement(4, GL11.GL_FLOAT);
	public static final VertexElement NORMAL = new VertexElement(3, GL11.GL_FLOAT, true);
	public static final VertexElement UV = new VertexElement(2, GL11.GL_FLOAT);
	
	public final int count;
	public final int type;
	public final boolean normalized;
	
	public VertexElement(int count, int type) {
		this(count, type, false);
	}
	
	public VertexElement(int count, int type, boolean normalized) {
		this.count = count;
		this.type = type;
		this.normalized = normalized;
	}
}
