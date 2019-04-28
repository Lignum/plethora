package org.squiddev.plethora.gameplay.modules.glasses.objects.object2d;

import com.google.common.base.Objects;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.squiddev.plethora.api.method.MethodResult;
import org.squiddev.plethora.api.method.wrapper.FromTarget;
import org.squiddev.plethora.api.method.wrapper.PlethoraMethod;
import org.squiddev.plethora.gameplay.modules.glasses.CanvasClient;
import org.squiddev.plethora.gameplay.modules.glasses.objects.ColourableObject;
import org.squiddev.plethora.gameplay.modules.glasses.objects.ObjectRegistry;
import org.squiddev.plethora.gameplay.modules.glasses.renderer.PrimitiveRenderer;
import org.squiddev.plethora.gameplay.modules.glasses.renderer.Renderers;
import org.squiddev.plethora.gameplay.modules.glasses.renderer.Vertex;
import org.squiddev.plethora.utils.ByteBufUtils;
import org.squiddev.plethora.utils.Vec2d;

import javax.annotation.Nonnull;

import static org.lwjgl.opengl.GL11.GL_QUADS;

public class Rectangle extends ColourableObject implements Positionable2D {
	private Vec2d position = Vec2d.ZERO;
	private float width;
	private float height;

	public Rectangle(int id, int parent) {
		super(id, parent, ObjectRegistry.RECTANGLE_2D);
	}

	@Nonnull
	@Override
	public Vec2d getPosition() {
		return position;
	}

	@Override
	public void setPosition(@Nonnull Vec2d position) {
		if (!Objects.equal(this.position, position)) {
			this.position = position;
			setDirty();
		}
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public void setSize(float width, float height) {
		if (this.width != width || this.height != height) {
			this.width = width;
			this.height = height;
			setDirty();
		}
	}

	@Override
	public void writeInitial(ByteBuf buf) {
		super.writeInitial(buf);
		ByteBufUtils.writeVec2d(buf, position);
		buf.writeFloat(width);
		buf.writeFloat(height);
	}

	@Override
	public void readInitial(ByteBuf buf) {
		super.readInitial(buf);
		position = ByteBufUtils.readVec2d(buf);
		width = buf.readFloat();
		height = buf.readFloat();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void draw(CanvasClient canvas) {
		Renderers renderers = canvas.getRenderers();
		PrimitiveRenderer renderer = renderers.getRendererFor(Renderers.Primitive.TRIANGLES);

		float red = getRed() / 255.0f, green = getGreen() / 255.0f, blue = getBlue() / 255.0f, alpha = getAlpha() / 255.0f;

		float x = (float)position.x;
		float y = (float)position.y;

		// Top left
		renderer.addVertex(new Vertex(x, y, 0, red, green, blue, alpha));
		renderer.addVertex(new Vertex(x, y + height, 0, red, green, blue, alpha));
		renderer.addVertex(new Vertex(x + width, y, 0, red, green, blue, alpha));

		// Bottom right
		renderer.addVertex(new Vertex(x + width, y, 0, red, green, blue, alpha));
		renderer.addVertex(new Vertex(x, y + height, 0, red, green, blue, alpha));
		renderer.addVertex(new Vertex(x + width, y + height, 0, red, green, blue, alpha));
	}

	@PlethoraMethod(doc = "function():number, number -- Get the size of this rectangle.", worldThread = false)
	public static MethodResult getSize(@FromTarget Rectangle rect) {
		return MethodResult.result(rect.getWidth(), rect.getHeight());
	}

	@PlethoraMethod(doc = "-- Set the size of this rectangle.", worldThread = false)
	public static void setSize(@FromTarget Rectangle rect, float width, float height) {
		rect.setSize(width, height);
	}
}
