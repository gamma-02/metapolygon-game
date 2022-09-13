package client.render.utils;

public class FloatBufferBuilder {
	float[] data = new float[0];
	
	public void appendFloats(float... data) {
		float[] copy = new float[this.data.length + data.length];
		System.arraycopy(this.data, 0, copy, 0, this.data.length);
		System.arraycopy(data, 0, copy, 0 + this.data.length, data.length);
		this.data = copy;
	}
	
	public float[] getData() {
		return data;
	}
}
