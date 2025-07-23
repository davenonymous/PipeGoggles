package com.davenonymous.pipegoggles.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

import java.util.OptionalDouble;

public class OverlayLineRenderType extends RenderType {
	public OverlayLineRenderType(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
		super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
	}

	private static final LineStateShard LIGHT_LINES = new LineStateShard(OptionalDouble.of(1.0D));
	private static final LineStateShard NORMAL_LINES = new LineStateShard(OptionalDouble.of(2.0D));
	private static final LineStateShard THICK_LINES = new LineStateShard(OptionalDouble.of(3.0D));

	private static RenderType.CompositeState createComposite(LineStateShard lineState) {
		return CompositeState.builder()
			.setLineState(lineState)
			.setLayeringState(NO_LAYERING)
			.setShaderState(RENDERTYPE_LINES_SHADER)
			.setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
			.setTextureState(NO_TEXTURE)
			.setDepthTestState(NO_DEPTH_TEST)
			.setOverlayState(NO_OVERLAY)
			.setCullState(NO_CULL)
			.setLightmapState(NO_LIGHTMAP)
			.setWriteMaskState(RenderStateShard.COLOR_WRITE)
			.createCompositeState(false);
	}

	private static RenderType createRenderType(int thickness, LineStateShard lineStateShard) {
		return create("overlay_lines_"+thickness+"_pipegoggles",
			DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINES, 65536, false, false,
			createComposite(lineStateShard));
	}

	public static final RenderType OVERLAY_LINES_LIGHT = createRenderType(1, LIGHT_LINES);
	public static final RenderType OVERLAY_LINES_NORMAL = createRenderType(2, NORMAL_LINES);
	public static final RenderType OVERLAY_LINES_THICK = createRenderType(3, THICK_LINES);

	public static RenderType forThickness(int thickness) {
		return switch (thickness) {
			case 1 -> OVERLAY_LINES_LIGHT;
			case 2 -> OVERLAY_LINES_NORMAL;
			case 3 -> OVERLAY_LINES_THICK;
			default -> OVERLAY_LINES_NORMAL;
		};
	}
}
