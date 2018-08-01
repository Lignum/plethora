package org.squiddev.plethora.gameplay.modules.glasses.objects;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import org.squiddev.plethora.gameplay.modules.glasses.BaseObject;
import org.squiddev.plethora.gameplay.modules.glasses.objects.object2d.*;

public final class ObjectRegistry {
	public static final byte RECTANGLE_2D = 0;
	public static final byte LINE_2D = 1;
	public static final byte DOT_2D = 2;
	public static final byte TEXT_2D = 3;
	public static final byte TRIANGLE_2D = 4;
	public static final byte POLYGON_2D = 5;
	public static final byte LINE_LOOP_2D = 6;
	public static final byte ITEM_2D = 7;
	public static final byte GROUP_2D = 8;

	private static final BaseObject.Factory[] FACTORIES = {
		Rectangle::new,
		Line::new,
		Dot::new,
		Text::new,
		Triangle::new,
		Polygon::new,
		LineLoop::new,
		Item2D::new,
		ObjectGroup2D::new
	};

	private ObjectRegistry() {
	}

	public static BaseObject create(int id, int parent, byte type) {
		if (type < 0 || type >= FACTORIES.length) throw new IllegalStateException("Unknown type " + type);
		return FACTORIES[type].create(id, parent);
	}

	public static BaseObject read(ByteBuf buf) {
		int id = ByteBufUtils.readVarInt(buf, 5);
		int parent = ByteBufUtils.readVarInt(buf, 5);
		byte type = buf.readByte();

		BaseObject object = ObjectRegistry.create(id, parent, type);
		object.readInitial(buf);
		return object;
	}

	public static void write(ByteBuf buf, BaseObject object) {
		ByteBufUtils.writeVarInt(buf, object.id(), 5);
		ByteBufUtils.writeVarInt(buf, object.parent(), 5);
		buf.writeByte(object.type());
		object.writeInitial(buf);
	}
}