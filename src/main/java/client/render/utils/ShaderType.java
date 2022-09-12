package client.render.utils;

import org.lwjgl.opengl.GL46;

public enum ShaderType {
	VERTEX(GL46.GL_VERTEX_SHADER),
	FRAGMENT(GL46.GL_FRAGMENT_SHADER),
	GEOMETRY(GL46.GL_GEOMETRY_SHADER),
	COMPUTE(GL46.GL_COMPUTE_SHADER),
	TESSELATION_CONTROL(GL46.GL_TESS_CONTROL_SHADER),
	TESSELATION_EVALUATION(GL46.GL_TESS_EVALUATION_SHADER),
	;
	
	public final int type;
	
	ShaderType(int type) {
		this.type = type;
	}
}
