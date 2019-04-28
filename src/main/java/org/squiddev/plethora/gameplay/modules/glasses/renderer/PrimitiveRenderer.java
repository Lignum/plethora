package org.squiddev.plethora.gameplay.modules.glasses.renderer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;

import java.io.Closeable;
import java.nio.ByteBuffer;
import java.util.LinkedList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public final class PrimitiveRenderer implements Closeable {
	private final int primitive;
	private int buffer = -1;
	private LinkedList<Vertex> vertices = new LinkedList<>();

	private boolean dirty = true;

	public PrimitiveRenderer(int primitive) {
		this.primitive = primitive;
	}

	public void reset() {
		vertices = new LinkedList<>();
		dirty = true;
	}

	public void addVertex(Vertex vertex) {
		vertices.addLast(vertex);
		dirty = true;
	}

	private static void setupFlat() {
		GlStateManager.color(1, 1, 1);
		GlStateManager.disableCull();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
	}

	public void render() {
		if (vertices.isEmpty()) {
			return;
		}

		if (dirty) {
			if (this.buffer == -1) {
				this.buffer = glGenBuffers();
			}

			ByteBuffer buffer = BufferUtils.createByteBuffer(vertices.size() * Vertex.VERTEX_SIZE);

			for (Vertex v : vertices) {
				v.writeToByteBuffer(buffer);
			}

			buffer.flip();
			glBindBuffer(GL_ARRAY_BUFFER, this.buffer);
			glBufferData(GL_ARRAY_BUFFER, buffer, GL_DYNAMIC_DRAW);

			dirty = false;
		}

		setupFlat();

		if (buffer != -1) {
			glBindBuffer(GL_ARRAY_BUFFER, buffer);
			GlStateManager.glEnableClientState(GL_VERTEX_ARRAY);
			GlStateManager.glEnableClientState(GL_COLOR_ARRAY);

			GlStateManager.glVertexPointer(3, GL_FLOAT, Vertex.VERTEX_SIZE, Vertex.POSITION_OFFSET);
			GlStateManager.glColorPointer(4, GL_FLOAT, Vertex.VERTEX_SIZE, Vertex.COLOUR_OFFSET);

			GlStateManager.glDrawArrays(primitive, 0, vertices.size());

			GlStateManager.glDisableClientState(GL_COLOR_ARRAY);
			GlStateManager.glDisableClientState(GL_VERTEX_ARRAY);
		}

		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	@Override
	public void close() {
		glDeleteBuffers(buffer);
	}
}
