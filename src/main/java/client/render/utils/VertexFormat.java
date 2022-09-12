package client.render.utils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class VertexFormat {
	private final VertexElement[] elements;
	private final int stride;
	private final int length;
	
	public static final VertexFormat POSITION = new VertexFormat(VertexElement.POSITION);
	public static final VertexFormat POSITION_COLOR = new VertexFormat(VertexElement.POSITION, VertexElement.COLOR);
	public static final VertexFormat POSITION_COLOR_NORMAL = new VertexFormat(VertexElement.POSITION, VertexElement.COLOR, VertexElement.NORMAL);
	public static final VertexFormat POSITION_COLOR_NORMAL_TEX = new VertexFormat(VertexElement.POSITION, VertexElement.COLOR, VertexElement.NORMAL, VertexElement.UV);
	
	public VertexFormat(VertexElement... elements) {
		this.elements = elements;
		int stride = 0;
		int length = 0;
		for (VertexElement element : elements) {
			stride += element.count * length(element.type);
			length += element.count;
		}
		this.stride = stride;
		this.length = length;
	}
	
	protected int length(int type) {
		return switch (type) {
			case GL11.GL_INT, GL11.GL_FLOAT -> 4;
			case GL11.GL_SHORT -> 2;
			case GL11.GL_BYTE -> 1;
			case GL11.GL_DOUBLE -> 8;
			default -> -1;
		};
	}
	
	public void setup() {
		int offset = 0;
		for (int i = 0; i < elements.length; i++) {
			GL20.glVertexAttribPointer(
					i,
					elements[i].count,
					elements[i].type,
					elements[i].normalized,
					stride, // TODO: check?
					offset
			);
			offset += elements[i].count * length(elements[i].type);
			GL20.glEnableVertexAttribArray(i);
		}
	}
	
	public void teardown() {
		for (int i = 0; i < elements.length; i++) {
			GL20.glDisableVertexAttribArray(i);
		}
	}
	
	public int vertexSize() {
		return length;
	}
}
