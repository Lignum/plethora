package org.squiddev.plethora.gameplay.modules.glasses.renderer;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.BufferUtils;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class PrimitiveRenderer implements Closeable {
	private final int primitive;
	private final int buffer;
	private int vertexCount;

	public PrimitiveRenderer(int primitive) {
		this.primitive = primitive;
		buffer = glGenBuffers();
	}

	public void prepareRender(ByteBuffer buffer, int vertexCount) {
		this.vertexCount = vertexCount;
		glBindBuffer(GL_ARRAY_BUFFER, this.buffer);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_DYNAMIC_DRAW);
	}

	public void render() {
		GlStateManager.glEnableClientState(GL_VERTEX_ARRAY);
		GlStateManager.glEnableClientState(GL_COLOR_ARRAY);

		glBindBuffer(GL_ARRAY_BUFFER, buffer);
		GlStateManager.glVertexPointer(3, GL_FLOAT, Vertex.VERTEX_SIZE, Vertex.POSITION_OFFSET);
		GlStateManager.glColorPointer(3, GL_FLOAT, Vertex.VERTEX_SIZE, Vertex.COLOUR_OFFSET);

		GlStateManager.glDrawArrays(primitive, 0, vertexCount);

		GlStateManager.glDisableClientState(GL_COLOR_ARRAY);
		GlStateManager.glDisableClientState(GL_VERTEX_ARRAY);
	}

	@Override
	public void close() {
		glDeleteBuffers(buffer);
	}
}
