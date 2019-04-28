package org.squiddev.plethora.gameplay.modules.glasses.renderer;

import net.minecraft.util.math.Vec3d;
import org.squiddev.plethora.utils.Vec2d;

import java.nio.ByteBuffer;
import java.util.Objects;

public final class Vertex {
	public final float x, y, z;
	public final float r, g, b, a;

	public static final int VERTEX_SIZE = 7 * Float.BYTES;

	public static final int POSITION_OFFSET = 0;
	public static final int COLOUR_OFFSET = 3 * Float.BYTES;

	public Vertex(float x, float y, float z, float r, float g, float b, float a) {
		this.x = x;
		this.y = y;
		this.z = z;

		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public Vertex(Vec3d pos, float r, float g, float b, float a) {
		this((float)pos.x, (float)pos.y, (float)pos.z, r, g, b, a);
	}

	public void writeToByteBuffer(ByteBuffer buffer) {
		buffer.putFloat(x).putFloat(y).putFloat(z);
		buffer.putFloat(r).putFloat(g).putFloat(b).putFloat(a);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Vertex vertex = (Vertex) o;
		return Float.compare(vertex.x, x) == 0 &&
			Float.compare(vertex.y, y) == 0 &&
			Float.compare(vertex.z, z) == 0 &&
			Float.compare(vertex.r, r) == 0 &&
			Float.compare(vertex.g, g) == 0 &&
			Float.compare(vertex.b, b) == 0 &&
			Float.compare(vertex.a, a) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z, r, g, b, a);
	}
}
