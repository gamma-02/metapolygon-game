package client.render.gl;

import client.render.utils.DrawMode;
import client.render.utils.VertexFormat;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class VertexBuffer {
	private final int id;
	private int length;
	
	public VertexBuffer(VertexFormat format, float[] floats) {
		int[] ids = new int[1];
		GL20.glGenBuffers(ids);
		id = ids[0];
		
		uploadData(format, floats);
	}
	
	public void uploadData(VertexFormat format, float[] data) {
		bind();
		GL20.glBufferData(GL30.GL_ARRAY_BUFFER, data, GL30.GL_STATIC_DRAW);
		length = data.length / format.vertexSize();
	}
	
	public void bind() {
		GL20.glBindBuffer(GL30.GL_ARRAY_BUFFER, id);
	}
	
	public void draw(DrawMode mode) {
		GL20.glDrawArrays(mode.mode, 0, length);
	}
	
	public void unbind() {
		GL20.glBindBuffer(GL30.GL_ARRAY_BUFFER, id);
	}
	
	public void delete() {
		GL20.glDeleteBuffers(id);
	}
}
