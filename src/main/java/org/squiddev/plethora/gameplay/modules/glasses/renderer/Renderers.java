package org.squiddev.plethora.gameplay.modules.glasses.renderer;

import org.lwjgl.opengl.GL11;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;

public class Renderers implements Closeable {
	public enum Primitive {
		TRIANGLES(GL11.GL_TRIANGLES),
		LINES(GL11.GL_LINES);

		private final int primitive;

		Primitive(int primitive) {
			this.primitive = primitive;
		}

		protected int getGLPrimitive() {
			return primitive;
		}
	}

	private final Map<Primitive, PrimitiveRenderer> renderers = new HashMap<>();

	public Renderers() {
		for (Primitive p : Primitive.values()) {
			renderers.put(p, new PrimitiveRenderer(p.getGLPrimitive()));
		}
	}

	public PrimitiveRenderer getRendererFor(Primitive primitive) {
		return renderers.get(primitive);
	}

	public void reset() {
		for (PrimitiveRenderer renderer : renderers.values()) {
			renderer.reset();
		}
	}

	public void render() {
		for (PrimitiveRenderer renderer : renderers.values()) {
			renderer.render();
		}
	}

	@Override
	public void close() {
		for (PrimitiveRenderer renderer : renderers.values()) {
			renderer.close();
		}
	}
}
