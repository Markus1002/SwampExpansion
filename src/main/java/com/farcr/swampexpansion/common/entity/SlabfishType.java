package com.farcr.swampexpansion.common.entity;

import java.util.Arrays;
import java.util.Comparator;

import net.minecraft.util.IStringSerializable;

public enum SlabfishType implements IStringSerializable {
	SWAMP(0, "swamp"),
	OCEAN(1, "ocean"),
	MARSH(2, "marsh"),
	MIRE(3, "mire"),
	CAVE(4, "cave"),
	JUNGLE(5, "jungle"),
	DESERT(6, "desert"),
	SAVANNA(7, "savanna"),
	MESA(8, "mesa"),
	SNOWY(9, "snowy"),
	TOTEM(10, "totem"),
	TAIGA(11, "taiga"),
	FOREST(12, "forest"),
	PLAINS(13, "plains"),
	SKELETON(14, "skeleton"),
	WITHER(15, "wither"),
	RIVER(16, "river"),
	MAPLE(17, "maple"),
	ROSEWOOD(18, "rosewood"),
	DUNES(19, "dunes"),
	NIGHTMARE(20, "nightmare"),
	ICY(21, "icy"),
	STRAY(22, "stray"),
	NETHER(23, "nether"),
	END(24, "end"),
	POISE(25, "poise");

	private static final SlabfishType[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(SlabfishType::getId)).toArray((array) -> {
		return new SlabfishType[array];
	});
	private final int id;
	private final String name;

	private SlabfishType(int idIn, String name) {
		this.id = idIn;
		this.name = name;
	}

	public int getId() {
		return this.id;
	}

	public static SlabfishType byId(int id) {
		if (id < 0 || id >= VALUES.length) {
			id = 0;
		}
		return VALUES[id];
	}

	public static SlabfishType byName(String key, SlabfishType type) {
		for(SlabfishType slabfishtype : values()) {
			if (slabfishtype.name.equals(key)) {
				return slabfishtype;
			}
		}
		return type;
	}

	public String getName() {
		return this.name;
	}
}