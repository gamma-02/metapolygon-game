package client.render;

import org.joml.Matrix4f;

public interface Uniform {
	void upload();
	void set(Matrix4f matrix);
	void set(int val);
	void set(int... val);
	void set(float val);
	void set(float... val);
}
