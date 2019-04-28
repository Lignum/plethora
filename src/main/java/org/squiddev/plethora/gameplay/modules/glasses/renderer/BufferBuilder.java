package org.squiddev.plethora.gameplay.modules.glasses.renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.util.LinkedList;

public final class BufferBuilder {
	private final LinkedList<Vertex> vertices = new LinkedList<>();

	public void addVertex(Vertex vertex) {
		vertices.add(vertex);
	}

	public ByteBuffer getBuffer() {
		ByteBuffer buffer = BufferUtils.createByteBuffer(Vertex.VERTEX_SIZE * vertices.size());

		for (Vertex v : vertices) {
			v.writeToByteBuffer(buffer);
		}

		return buffer;
	}

	public int getVertexCount() {
		return vertices.size();
	}
}
