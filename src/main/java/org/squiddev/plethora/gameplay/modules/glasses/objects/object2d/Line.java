package org.squiddev.plethora.gameplay.modules.glasses.objects.object2d;

import com.google.common.base.Objects;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.squiddev.plethora.gameplay.modules.glasses.CanvasClient;
import org.squiddev.plethora.gameplay.modules.glasses.objects.ColourableObject;
import org.squiddev.plethora.gameplay.modules.glasses.objects.ObjectRegistry;
import org.squiddev.plethora.gameplay.modules.glasses.objects.Scalable;
import org.squiddev.plethora.gameplay.modules.glasses.renderer.PrimitiveRenderer;
import org.squiddev.plethora.gameplay.modules.glasses.renderer.Renderers;
import org.squiddev.plethora.gameplay.modules.glasses.renderer.Vertex;
import org.squiddev.plethora.utils.ByteBufUtils;
import org.squiddev.plethora.utils.Vec2d;

import javax.annotation.Nonnull;

import static org.lwjgl.opengl.GL11.*;

public class Line extends ColourableObject implements Scalable, MultiPoint2D {
	private Vec2d start = Vec2d.ZERO;
	private Vec2d end = Vec2d.ZERO;
	private float thickness = 1;

	public Line(int id, int parent) {
		super(id, parent, ObjectRegistry.LINE_2D);
	}

	@Override
	public float getScale() {
		return thickness;
	}

	@Override
	public void setScale(float scale) {
		if (thickness != scale) {
			thickness = scale;
			setDirty();
		}
	}

	@Nonnull
	@Override
	public Vec2d getPoint(int idx) {
		return idx == 0 ? start : end;
	}

	@Override
	public void setVertex(int idx, @Nonnull Vec2d point) {
		if (idx == 0) {
			if (!Objects.equal(start, point)) {
				start = point;
				setDirty();
			}
		} else {
			if (!Objects.equal(end, point)) {
				end = point;
				setDirty();
			}
		}
	}

	@Override
	public int getVertices() {
		return 2;
	}

	@Override
	public void writeInitial(ByteBuf buf) {
		super.writeInitial(buf);
		ByteBufUtils.writeVec2d(buf, start);
		ByteBufUtils.writeVec2d(buf, end);
		buf.writeFloat(thickness);
	}

	@Override
	public void readInitial(ByteBuf buf) {
		super.readInitial(buf);
		start = ByteBufUtils.readVec2d(buf);
		end = ByteBufUtils.readVec2d(buf);
		thickness = buf.readFloat();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void draw(CanvasClient canvas) {
		Renderers renderers = canvas.getRenderers();
		PrimitiveRenderer renderer = renderers.getRendererFor(Renderers.Primitive.LINES);

		float red = getRed() / 255.0f, green = getGreen() / 255.0f, blue = getBlue() / 255.0f, alpha = getAlpha() / 255.0f;

		float startX = (float)start.x;
		float startY = (float)start.y;
		float endX = (float)end.x;
		float endY = (float)end.y;

		renderer.addVertex(new Vertex(startX, startY, 0, red, green, blue, alpha));
		renderer.addVertex(new Vertex(endX, endY, 0, red, green, blue, alpha));

		/*float slope = (startY - endY) / (endX - startX);
		float tangent = 1.0f - slope;

		float topLeftX = startX + tangent * thickness;
		float topLeftY = startY + tangent * thickness;
		float bottomLeftX = startX + tangent * -thickness;
		float bottomLeftY = startY + tangent * -thickness;
		float topRightX = endX + tangent * -thickness;
		float topRightY = endY + tangent * -thickness;
		float bottomRightX = endX + tangent * thickness;
		float bottomRightY = endY + tangent * thickness;

		// Top left
		renderer.addVertex(new Vertex(topLeftX, topLeftY, 0, red, green, blue, alpha));
		renderer.addVertex(new Vertex(bottomRightX, bottomRightY, 0, red, green, blue, alpha));
		renderer.addVertex(new Vertex(topRightX, topRightY, 0, red, green, blue, alpha));

		// Bottom right
		renderer.addVertex(new Vertex(topLeftX, topLeftY, 0, red, green, blue, alpha));
		renderer.addVertex(new Vertex(bottomLeftX, bottomLeftY, 0, red, green, blue, alpha));
		renderer.addVertex(new Vertex(bottomRightX, bottomRightY, 0, red, green, blue, alpha));*/
	}
}
