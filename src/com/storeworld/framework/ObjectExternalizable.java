package com.storeworld.framework;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author sunbingbing

 * 
 * 通过Externalizable 来实现序列化的优化函数

 */

public abstract class ObjectExternalizable implements IStoreworld,CommonDTO{

	public String readUTF(ObjectInput in) throws IOException {
		byte exist = in.readByte();
		if (exist == 0) {
			return null;
		}
		return in.readUTF();
	}

	public void writeUTF(ObjectOutput out, String utf) throws IOException {
		if (utf == null) {
			out.writeByte(0);
		} else {
			out.writeByte(1);
			out.writeUTF(utf);
		}
	}



	public Long readLong(ObjectInput in) throws IOException {
		byte exist = in.readByte();
		if (exist == 0) {
			return null;
		}
		long l = in.readLong();
		return Long.valueOf(l);
	}

	public void writeLong(ObjectOutput out, Long l) throws IOException {
		if (l == null) {
			out.writeByte(0);
		} else {
			out.writeByte(1);
			out.writeLong(l);
		}
	}

	public Double readDouble(ObjectInput in) throws IOException {
		byte exist = in.readByte();
		if (exist == 0) {
			return null;
		}
		double l = in.readDouble();
		return Double.valueOf(l);
	}

	public void writeDouble(ObjectOutput out, Double l) throws IOException {
		if (l == null) {
			out.writeByte(0);
		} else {
			out.writeByte(1);
			out.writeDouble(l);
		}
	}

	public Integer readInteger(ObjectInput in) throws IOException {
		byte exist = in.readByte();
		if (exist == 0) {
			return null;
		}
		int l = in.readInt();
		return Integer.valueOf(l);
	}

	public void writeInteger(ObjectOutput out, Integer l) throws IOException {
		if (l == null) {
			out.writeByte(0);
		} else {
			out.writeByte(1);
			out.writeInt(l);
		}
	}

	public Short readShort(ObjectInput in) throws IOException {
		byte exist = in.readByte();
		if (exist == 0) {
			return null;
		}
		short l = in.readShort();
		return Short.valueOf(l);
	}

	public void writeShort(ObjectOutput out, Short l) throws IOException {
		if (l == null) {
			out.writeByte(0);
		} else {
			out.writeByte(1);
			out.writeShort(l);
		}
	}

	public Float readFloat(ObjectInput in) throws IOException {
		byte exist = in.readByte();
		if (exist == 0) {
			return null;
		}
		float l = in.readFloat();
		return Float.valueOf(l);
	}

	public void writeFloat(ObjectOutput out, Float l) throws IOException {
		if (l == null) {
			out.writeByte(0);
		} else {
			out.writeByte(1);
			out.writeFloat(l);
		}
	}

	public Boolean readBoolean(ObjectInput in) throws IOException {
		byte exist = in.readByte();
		if (exist == 0) {
			return null;
		}
		boolean l = in.readBoolean();
		return Boolean.valueOf(l);
	}

	public void writeBoolean(ObjectOutput out, Boolean l) throws IOException {
		if (l == null) {
			out.writeByte(0);
		} else {
			out.writeByte(1);
			out.writeBoolean(l);
		}
	}

	public Byte readByte(ObjectInput in) throws IOException {
		byte exist = in.readByte();
		if (exist == 0) {
			return null;
		}
		byte l = in.readByte();
		return Byte.valueOf(l);
	}

	public void writeByte(ObjectOutput out, Byte l) throws IOException {
		if (l == null) {
			out.writeByte(0);
		} else {
			out.writeByte(1);
			out.writeByte(l);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList readArrayList(ObjectInput in) throws IOException,
			ClassNotFoundException {
		int count = in.readInt();
		if (count < 0) {
			return null;
		}
		ArrayList list = new ArrayList();
		for (int i = 0; i < count; i++) {
			list.add(in.readObject());
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public void writeArrayList(ObjectOutput out, List list)
			throws IOException {
		if (list == null) {
			out.writeInt(-1);
			return;
		}
		int count = list.size();
		out.writeInt(count);
		Iterator it = list.iterator();
		while (it.hasNext()) {
			out.writeObject(it.next());
		}
	}

}