package com.davenonymous.pipegoggles.lib.gui;


import com.davenonymous.pipegoggles.PipeGoggles;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

public class GUIHelper {
	public static ResourceLocation tabIcons = PipeGoggles.resource("textures/gui/tabicons.png");
	private static Map<Integer, DynamicImageResources.DynTexture> circleCache = new HashMap<>();

	private static DynamicImageResources.DynTexture getCircleTexture(int radius) {
		if(circleCache.containsKey(radius)) {
			return circleCache.get(radius);
		}

		TextureManager tm = Minecraft.getInstance().getTextureManager();

		int diameter = radius * 2 + 1;
		var image = new NativeImage(diameter, diameter, true);
		int rmin = radius * radius - radius;
		int rmax = radius * radius + radius;
		for(int y = -radius; y <= radius; y++) {
			int sqy = y * y;
			for(int x = -radius; x <= radius; x++) {
				int sqd = x * x + sqy;
				if(sqd < rmin) {
					image.setPixelRGBA(x + radius, y + radius, 0xFFFFFFFF); // Fully filled pixel
				} else if(sqd < rmax) {
					int c = rmax - sqd;
					c *= 256;
					c /= 2 * radius;
					if(c > 255) c = 255;
					image.setPixelRGBA(x + radius, y + radius, (c << 24) + 0xFFFFFF); // Antialiased pixel
				}
			}
		}

		ResourceLocation resource = tm.register(
			"circle_" + radius, new DynamicTexture(image) {
				public void upload() {
					this.bind();
					NativeImage td = this.getPixels();
					this.getPixels().upload(0, 0, 0, 0, 0, td.getWidth(), td.getHeight(), false, false, false, false);
				}
			}
		);

		var result = new DynamicImageResources.DynTexture(resource, image);
		circleCache.put(radius, result);
		return result;
	}

	public static void drawFilledCircle(GuiGraphics guiGraphics, float x, float y, int radius, int color) {
		// This draws a filled pixel-shaded circle, i.e. a fixed color with slightly brighter pixels on the top and left edges
		// and slightly darker pixels on the bottom and right edges.

		DynamicImageResources.DynTexture circleTexture = getCircleTexture(radius);
		int diameter = radius * 2;
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
		setShaderColor(color);
		guiGraphics.pose().pushPose();
		guiGraphics.pose().translate(x, y, 1);
		guiGraphics.blitInscribed(circleTexture.resource(), 0, 0, diameter, diameter, diameter, diameter, true, true);
		guiGraphics.pose().popPose();
		RenderSystem.disableBlend();
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f); // Reset color to white
	}


	public static void drawLine(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int color) {
		drawLine(guiGraphics, x1, y1, x2, y2, color, color);
	}

	public static void drawLine(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int color1, int color2) {
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		RenderSystem.enableBlend();
		Matrix4f matrix4f = guiGraphics.pose().last().pose();
		BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		bufferbuilder.addVertex(matrix4f, x1, y1, 0f).setColor(color1);
		bufferbuilder.addVertex(matrix4f, x2, y2, 0f).setColor(color2);
		BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		RenderSystem.disableBlend();
	}

	public static void drawLine(PoseStack poseStack, float x1, float y1, float z1, float x2, float y2, float z2, int color1, int color2) {
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		RenderSystem.enableBlend();
		Matrix4f matrix4f = poseStack.last().pose();
		BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		bufferbuilder.addVertex(matrix4f, x1, y1, z1).setColor(color1);
		bufferbuilder.addVertex(matrix4f, x2, y2, z2).setColor(color2);
		BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		RenderSystem.disableBlend();
	}

	public static void setShaderColor(int color) {
		float r = ((color >> 16) & 0xFF) / 255.0F;
		float g = ((color >> 8) & 0xFF) / 255.0F;
		float b = (color & 0xFF) / 255.0F;
		float a = ((color >> 24) & 0xFF) / 255.0F;
		RenderSystem.setShaderColor(r, g, b, a);
	}

	public static int brighten(int color, float factor) {
		float r = ((color >> 16) & 0xFF) * factor;
		float g = ((color >> 8) & 0xFF) * factor;
		float b = (color & 0xFF) * factor;
		float a = ((color >> 24) & 0xFF);
		float threshold = 255.999f;

		float max = Math.max(Math.max(r, g), b);
		if(max <= threshold) {
			return ((int)a << 24) + ((int)r << 16) + ((int)g << 8) + (int)b;
		}

		float total = r + g + b;
		if (total >= 3 * threshold) {
			return ((int)a << 24) + ((int)threshold << 16) + ((int)threshold << 8) + (int)threshold;
		}

		float x = (3 * threshold - total) / (3 * max - total);
		float gray = threshold - x * max;

		int newR = (int)(gray + x * r);
		int newG = (int)(gray + x * g);
		int newB = (int)(gray + x * b);

		return ((int)a << 24) + ((newR & 0xFF) << 16) + ((newG & 0xFF) << 8) + (newB & 0xFF);
	}


	public static void fillHorizontalGradient(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int colorFrom, int colorTo) {
		fillHorizontalGradient(guiGraphics, x1, y1, x2, y2, 0, colorFrom, colorTo);
	}

	public static void fillHorizontalGradient(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int z, int colorFrom, int colorTo) {
		fillHorizontalGradient(guiGraphics, RenderType.gui(), x1, y1, x2, y2, colorFrom, colorTo, z);
	}

	public static void fillHorizontalGradient(GuiGraphics guiGraphics, RenderType renderType, int x1, int y1, int x2, int y2, int colorFrom, int colorTo, int z) {
		VertexConsumer vertexconsumer = guiGraphics.bufferSource().getBuffer(renderType);
		fillHorizontalGradient(guiGraphics, vertexconsumer, x1, y1, x2, y2, z, colorFrom, colorTo);
	}

	private static void fillHorizontalGradient(GuiGraphics guiGraphics, VertexConsumer consumer, int x1, int y1, int x2, int y2, int z, int colorFrom, int colorTo) {
		Matrix4f matrix4f = guiGraphics.pose().last().pose();
		consumer.addVertex(matrix4f, (float)x1, (float)y1, (float)z).setColor(colorFrom);
		consumer.addVertex(matrix4f, (float)x1, (float)y2, (float)z).setColor(colorFrom);
		consumer.addVertex(matrix4f, (float)x2, (float)y2, (float)z).setColor(colorTo);
		consumer.addVertex(matrix4f, (float)x2, (float)y1, (float)z).setColor(colorTo);
	}

	public static Vector4f shortenLineSym(int x1, int y1, int x2, int y2, float length) {
		float angle = (float) Math.atan2(y2 - y1, x2 - x1);
		float newX1 = x1 + length * (float) Math.cos(angle);
		float newY1 = y1 + length * (float) Math.sin(angle);
		float newX2 = x2 - length * (float) Math.cos(angle);
		float newY2 = y2 - length * (float) Math.sin(angle);

		return new Vector4f(newX1, newY1, newX2, newY2);
	}

	public static Vector2f drawFatLine(GuiGraphics guiGraphics, float x1, float y1, float x2, float y2, float lineWidth, int color) {
		float angle = (float) Math.atan2(y2 - y1, x2 - x1);
		float length = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		RenderSystem.enableBlend();

		float midX = (x1 + x2) / 2f;
		float midY = (y1 + y2) / 2f;

		guiGraphics.pose().pushPose();
		guiGraphics.pose().translate(midX - (length/2.0f), midY - (lineWidth/2.0f), 0);
		guiGraphics.pose().rotateAround(Axis.ZP.rotationDegrees((float)Math.toDegrees(angle)), length / 2.0f, lineWidth/2.0f, 0);
		guiGraphics.fill(0, 0, (int)length, Math.round(lineWidth), color);

		guiGraphics.pose().popPose();

		RenderSystem.disableBlend();

		return new Vector2f(angle, length);
	}

	public static Vector2f drawTiledLine(GuiGraphics guiGraphics, float x1, float y1, float x2, float y2, GUISpriteInfo spriteInfo, int color) {
		return drawTiledLine(guiGraphics, x1, y1, x2, y2, spriteInfo, color, 0);
	}

	public static Vector2f drawTiledLine(GuiGraphics guiGraphics, float x1, float y1, float x2, float y2, GUISpriteInfo spriteInfo, int color, int padding) {
		float angle = (float) Math.atan2(y2 - y1, x2 - x1);
		float length = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

		int lineWidth = spriteInfo.height();

		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
		RenderSystem.enableBlend();

		float midX = (x1 + x2) / 2f;
		float midY = (y1 + y2) / 2f;

		guiGraphics.pose().pushPose();
		guiGraphics.pose().translate(midX - (length/2.0f), midY - (lineWidth/2.0f), 0);
		guiGraphics.pose().rotateAround(Axis.ZP.rotationDegrees((float)Math.toDegrees(angle)), length / 2.0f, lineWidth/2.0f, 0);
		//guiGraphics.pose().scale(0.25f, 0.25f, 1.0f);

		setShaderColor(color);
		int xPos = 0;
		while( xPos < length) {
			guiGraphics.blit(spriteInfo.sprite(), xPos, 0, 0, 0, spriteInfo.width(), spriteInfo.height(), spriteInfo.width(), spriteInfo.height());
			xPos += (spriteInfo.width()) + padding;
		}
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f); // Reset color to white

		guiGraphics.pose().popPose();
		RenderSystem.disableBlend();

		return new Vector2f(angle, length);
	}

	public static void drawArrowHead(GuiGraphics guiGraphics, float x1, float y1, float x2, float y2, int color) {
		float angle = (float) Math.atan2(y2 - y1, x2 - x1);
		float length = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

		float midX = (x1 + x2) / 2f;
		float midY = (y1 + y2) / 2f;

		ResourceLocation arrowHeadTexture = PipeGoggles.resource("textures/gui/arrowhead.png");
		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
		RenderSystem.setShaderTexture(0, arrowHeadTexture);
		RenderSystem.enableBlend();


		guiGraphics.pose().pushPose();
		guiGraphics.pose().translate(midX-6, midY-6, 0);
		guiGraphics.pose().rotateAround(Axis.ZP.rotationDegrees((float)Math.toDegrees(angle)), 6f, 6f, 0);

		guiGraphics.fill(2, 2, 10, 10, 0xFFFFFFFF); // Draw a rectangle to cover the center of the arrow head

		setShaderColor(color);
		guiGraphics.blit(arrowHeadTexture, 0, 0, 0, 0, 12, 12, 12, 12);

		guiGraphics.pose().popPose();

		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f); // Reset color to white

		RenderSystem.disableBlend();
	}

	public static void drawArrowLine(GuiGraphics guiGraphics, float x1, float y1, float x2, float y2, float lineWidth, int color) {
		drawFatLine(guiGraphics, x1, y1, x2, y2, lineWidth, color);
		drawArrowHead(guiGraphics, x1, y1, x2, y2, color);
	}

	public static int longestWrappedLine(Font font, FormattedText text, int lineWidth) {
		int longest = 0;
		for(FormattedCharSequence formattedcharsequence : font.split(text, lineWidth)) {
			longest = Math.max(longest, font.width(formattedcharsequence));
		}
		return longest;
	}

	public static void drawWordWrap(GuiGraphics pGuiGraphics, Font font, FormattedText text, int x, int y, int lineWidth, int color) {
		for(FormattedCharSequence formattedcharsequence : font.split(text, lineWidth)) {
			pGuiGraphics.drawString(font, formattedcharsequence, x, y, color, false);
			y += 10;
		}
	}

	public static int wordWrapHeight(Font font, FormattedText text, int maxWidth) {
		return 10 * font.split(text, maxWidth).size();
	}

	public static void drawStringCentered(GuiGraphics pGuiGraphics, String str, Screen screen, float x, float y, int color) {
		Font renderer = screen.getMinecraft().font;
		float xPos = x - ((float) renderer.width(str) / 2.0f);
		var old = RenderSystem.getShader();
		pGuiGraphics.drawCenteredString(renderer, str, (int) xPos, (int) y, color);
		RenderSystem.setShader(() -> old);
	}

	public static void drawSplitStringCentered(GuiGraphics pGuiGraphics, String str, Screen screen, int x, int y, int width, int color) {
		Font renderer = screen.getMinecraft().font;
		int yOffset = 0;

		for(FormattedText row : renderer.getSplitter().splitLines(str, width, Style.EMPTY)) {
			drawStringCentered(pGuiGraphics, row.getString(), screen, x + width / 2, y + yOffset, color);
			yOffset += renderer.lineHeight;
		}
	}

	public static void drawColoredRectangle(GuiGraphics pGuiGraphics, int x, int y, int width, int height, int argb) {
		int a = (argb >> 24) & 0xFF;
		int r = (argb >> 16) & 0xFF;
		int g = (argb >> 8) & 0xFF;
		int b = (argb & 0xFF);
		drawColoredRectangle(pGuiGraphics, x, y, width, height, r, g, b, a);
	}

	public static void drawColoredRectangle(GuiGraphics pGuiGraphics, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
		float zLevel = 0.0f;

		//		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		//		// RenderSystem.disableTexture();
		//		RenderSystem.enableBlend();
		//		RenderSystem.disableDepthTest();
		//		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		//
		//		var tesselator = Tesselator.getInstance();
		//		var builder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		//
		//		Matrix4f matrix = pGuiGraphics.pose().last().pose();
		//
		//		builder.addVertex(matrix, (x + 0), (y + 0), zLevel).setColor(red, green, blue, alpha);
		//		builder.addVertex(matrix, (x + 0), (y + height), zLevel).setColor(red, green, blue, alpha);
		//		builder.addVertex(matrix, (x + width), (y + height), zLevel).setColor(red, green, blue, alpha);
		//		builder.addVertex(matrix, (x + width), (y + 0), zLevel).setColor(red, green, blue, alpha);
		//		builder.build().close();
		//
		//		RenderSystem.disableBlend();
		//		RenderSystem.enableDepthTest();
		// RenderSystem.enableTexture(); /?
	}

	public static void drawStretchedTabIconsTexture(GuiGraphics pGuiGraphics, int x, int y, int width, int height, int textureX, int textureY, int textureWidth, int textureHeight) {
		final float uScale = 1f / 0x100;
		final float vScale = 1f / 0x100;

		float zLevel = 0.0f;

		RenderSystem.setShaderTexture(0, tabIcons);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		Matrix4f matrix = pGuiGraphics.pose().last().pose();
		BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		builder.addVertex(matrix, (float) x, (float) (y + height), zLevel).setUv((float) textureX * uScale, (float) (textureY + textureHeight) * vScale);
		builder.addVertex(matrix, (float) (x + width), (float) (y + height), zLevel).setUv((float) (textureX + textureWidth) * uScale, (float) (textureY + textureHeight) * vScale);
		builder.addVertex(matrix, (float) (x + width), (float) y, zLevel).setUv((float) (textureX + textureWidth) * uScale, (float) textureY * vScale);
		builder.addVertex(matrix, (float) x, (float) y, zLevel).setUv((float) textureX * uScale, (float) textureY * vScale);
		BufferUploader.drawWithShader(builder.buildOrThrow());
	}

	public static void drawStretchedTexture(GuiGraphics pGuiGraphics, ResourceLocation texture, int x, int y, int width, int height, int textureX, int textureY, int textureWidth,
		int textureHeight)
	{
		final float uScale = 1f / 0x100;
		final float vScale = 1f / 0x100;

		float zLevel = 0.0f;

		RenderSystem.setShaderTexture(0, texture);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		Matrix4f matrix = pGuiGraphics.pose().last().pose();
		BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		builder.addVertex(matrix, (float) x, (float) (y + height), zLevel).setUv((float) textureX * uScale, (float) (textureY + textureHeight) * vScale);
		builder.addVertex(matrix, (float) (x + width), (float) (y + height), zLevel).setUv((float) (textureX + textureWidth) * uScale, (float) (textureY + textureHeight) * vScale);
		builder.addVertex(matrix, (float) (x + width), (float) y, zLevel).setUv((float) (textureX + textureWidth) * uScale, (float) textureY * vScale);
		builder.addVertex(matrix, (float) x, (float) y, zLevel).setUv((float) textureX * uScale, (float) textureY * vScale);
		BufferUploader.drawWithShader(builder.buildOrThrow());

	}

	public static void drawModalRectWithCustomSizedTexture(GuiGraphics pGuiGraphics, int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
		float f = 1.0F / textureWidth;
		float f1 = 1.0F / textureHeight;

		RenderSystem.setShaderTexture(0, tabIcons);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		Matrix4f matrix = pGuiGraphics.pose().last().pose();
		BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		builder.addVertex(matrix, (float) x, (float) (y + height), 0.0f).setUv((u * f), ((v + (float) height) * f1));
		builder.addVertex(matrix, (float) (x + width), (float) (y + height), 0.0f).setUv(((u + (float) width) * f), ((v + (float) height) * f1));
		builder.addVertex(matrix, (float) (x + width), (float) y, 0.0f).setUv(((u + (float) width) * f), (v * f1));
		builder.addVertex(matrix, (float) x, (float) y, 0.0f).setUv((u * f), (v * f1));
		BufferUploader.drawWithShader(builder.buildOrThrow());
	}

	public static void renderGuiItem(GuiGraphics pGuiGraphics, ItemStack pStack, int pX, int pY, boolean blackOut) {
		var pBakedmodel = Minecraft.getInstance().getItemRenderer().getModel(pStack, null, null, 0);
		Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
		RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		PoseStack posestack = pGuiGraphics.pose();
		posestack.pushPose();
		posestack.translate(pX, pY, 100.0d);
		posestack.translate(8.0D, 8.0D, 0.0D);
		posestack.scale(1.0F, -1.0F, 1.0F);
		posestack.scale(16.0F, 16.0F, 16.0F);
		RenderSystem.applyModelViewMatrix();
		PoseStack posestack1 = new PoseStack();
		MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
		boolean flag = !pBakedmodel.usesBlockLight();
		if(flag) {
			Lighting.setupForFlatItems();
		}

		Minecraft.getInstance().getItemRenderer()
			.render(pStack, ItemDisplayContext.GUI, false, posestack1, multibuffersource$buffersource, blackOut ? 0 : 15728880, OverlayTexture.NO_OVERLAY, pBakedmodel);
		multibuffersource$buffersource.endBatch();
		RenderSystem.enableDepthTest();
		if(flag) {
			Lighting.setupFor3DItems();
		}

		posestack.popPose();
		RenderSystem.applyModelViewMatrix();
	}


	/**
	 * <p>Fills a specified area on the screen with the provided {@link TextureAtlasSprite}.</p>
	 *
	 * @param icon   The {@link TextureAtlasSprite} to be displayed
	 * @param x      The X coordinate to start drawing from
	 * @param y      The Y coordinate to start drawing form
	 * @param width  The width of the provided icon to draw on the screen
	 * @param height The height of the provided icon to draw on the screen
	 */
	public static void fillAreaWithIcon(GuiGraphics pGuiGraphics, TextureAtlasSprite icon, int x, int y, int width, int height) {
		Tesselator t = Tesselator.getInstance();
		BufferBuilder b = t.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

		float zLevel = 0.0f;

		int iconWidth = icon.contents().width();
		int iconHeight = icon.contents().height();

		// number of rows & cols of full size icons
		int fullCols = width / iconWidth;
		int fullRows = height / iconHeight;

		float minU = icon.getU0();
		float maxU = icon.getU1();
		float minV = icon.getV0();
		float maxV = icon.getV1();

		int excessWidth = width % iconWidth;
		int excessHeight = height % iconHeight;

		// interpolated max u/v for the excess row / col
		float partialMaxU = minU + (maxU - minU) * ((float) excessWidth / iconWidth);
		float partialMaxV = minV + (maxV - minV) * ((float) excessHeight / iconHeight);

		int xNow;
		int yNow;
		for(int row = 0; row < fullRows; row++) {
			yNow = y + row * iconHeight;
			for(int col = 0; col < fullCols; col++) {
				// main tile, only full icons
				xNow = x + col * iconWidth;
				drawRect(pGuiGraphics, xNow, yNow, iconWidth, iconHeight, zLevel, minU, minV, maxU, maxV);
			}
			if(excessWidth != 0) {
				// last not full width column in every row at the end
				xNow = x + fullCols * iconWidth;
				drawRect(pGuiGraphics, xNow, yNow, iconWidth, iconHeight, zLevel, minU, minV, maxU, maxV);
			}
		}
		if(excessHeight != 0) {
			// last not full height row
			for(int col = 0; col < fullCols; col++) {
				xNow = x + col * iconWidth;
				yNow = y + fullRows * iconHeight;
				drawRect(pGuiGraphics, xNow, yNow, iconWidth, excessHeight, zLevel, minU, minV, maxU, partialMaxV);
			}
			if(excessWidth != 0) {
				// missing quad in the bottom right corner of neither full height nor full width
				xNow = x + fullCols * iconWidth;
				yNow = y + fullRows * iconHeight;
				drawRect(pGuiGraphics, xNow, yNow, excessWidth, excessHeight, zLevel, minU, minV, partialMaxU, partialMaxV);
			}
		}

		b.build();
	}


	private static void drawRect(GuiGraphics pGuiGraphics, float x, float y, float width, float height, float z, float u, float v, float maxU, float maxV) {
		BufferBuilder b = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		b.addVertex(x, y + height, z).setUv(u, maxV);
		b.addVertex(x + width, y + height, z).setUv(maxU, maxV);
		b.addVertex(x + width, y, z).setUv(maxU, v);
		b.addVertex(x, y, z).setUv(u, v);
		b.build();
	}

	public static void drawEmbossedWindow(GuiGraphics pGuiGraphics, ResourceLocation texture, int pWidth, int pHeight) {
		drawEmbossedWindow(pGuiGraphics, texture, pWidth, pHeight, 0, 0);
	}

	public static void drawEmbossedWindow(GuiGraphics pGuiGraphics, ResourceLocation texture, int pWidth, int pHeight, int x, int y) {
		int texOffsetY = 0;
		int texOffsetX = 0;
		int borderSize = 8;

		int width = pWidth;
		int xOffset = x;

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, texture);

		// Top Left corner
		pGuiGraphics.blit(texture, x, y, borderSize, borderSize, 0f, 0f, borderSize, borderSize, 64, 64);

		// Top right corner
		pGuiGraphics.blit(texture, x + width - borderSize, y, borderSize, borderSize, 64f - borderSize, 0, borderSize, borderSize, 64, 64);

		// Bottom Left corner
		pGuiGraphics.blit(texture, x, y + pHeight - borderSize, borderSize, borderSize, 0f, 64f - borderSize, borderSize, borderSize, 64, 64);

		// Bottom Right corner
		pGuiGraphics.blit(texture, x + width - borderSize, y + pHeight - borderSize, borderSize, borderSize, 64f - borderSize, 64f - borderSize, borderSize, borderSize, 64, 64);


		// Top edge

		pGuiGraphics.blit(
			texture,
			x + 4, y,
			width - borderSize, borderSize,
			0f + borderSize, 0f,
			52, borderSize,
			64, 64
		);

		// Bottom edge
		pGuiGraphics.blit(texture, x + 4, y + pHeight - borderSize, width - borderSize, borderSize, 0f + borderSize, 64f - borderSize, 52, borderSize, 64, 64);


		// Left edge
		pGuiGraphics.blit(
			texture,
			x, y + borderSize,
			borderSize, pHeight - (2 * borderSize),
			0f, borderSize,
			borderSize, 8,
			64, 64
		);

		// Left edge
		pGuiGraphics.blit(
			texture,
			x + width - borderSize, y + borderSize,
			borderSize, pHeight - (2 * borderSize),
			64 - borderSize, borderSize,
			borderSize, 8,
			64, 64
		);

		// Fill
		pGuiGraphics.blit(
			texture,
			x + borderSize, y + borderSize,
			pWidth - (2 * borderSize), pHeight - (2 * borderSize),
			borderSize, borderSize,
			52, 52,
			64, 64
		);
	}

	public static void drawWindow(GuiGraphics pGuiGraphics, int pWidth, int pHeight, boolean hasTabs) {
		drawWindow(pGuiGraphics, pWidth, pHeight, hasTabs, 0, 0);
	}

	public static void drawColoredCanvas(GuiGraphics pGuiGraphics, int pWidth, int pHeight, int color) {
		int texOffsetY = 85;
		int texOffsetX = 65;

		int width = pWidth;
		int xOffset = 0;
		int y = 0;

		pGuiGraphics.fill(0, 0, pWidth, pHeight, color);

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, tabIcons);
		RenderSystem.enableBlend();

		int spriteSize = 18;
		int borderSize = 3;

		// Top Left corner
		pGuiGraphics.blit(tabIcons, xOffset, y, texOffsetX, texOffsetY, borderSize, borderSize);

		// Top right corner
		pGuiGraphics.blit(tabIcons, xOffset + width - borderSize, y, texOffsetX + spriteSize - borderSize, texOffsetY, borderSize, borderSize);

		// Bottom Left corner
		pGuiGraphics.blit(tabIcons, xOffset, y + pHeight - borderSize, texOffsetX, texOffsetY + spriteSize - borderSize, borderSize, borderSize);

		// Bottom Right corner
		pGuiGraphics.blit(tabIcons, xOffset + width - borderSize, y + pHeight - borderSize, texOffsetX + spriteSize - borderSize, texOffsetY + spriteSize - borderSize, borderSize, borderSize);


		// Top edge
		drawStretchedTabIconsTexture(pGuiGraphics, xOffset + borderSize, y, width - (2*borderSize), borderSize, texOffsetX + 4, texOffsetY, 1, borderSize);

		// Bottom edge
		drawStretchedTabIconsTexture(pGuiGraphics, xOffset + borderSize, y + pHeight - borderSize, width - (2*borderSize), borderSize, texOffsetX + 4, texOffsetY + spriteSize - borderSize, 1, borderSize);

		// Left edge
		drawStretchedTabIconsTexture(pGuiGraphics, xOffset, y + borderSize, borderSize, pHeight - (2*borderSize), texOffsetX, texOffsetY + 4, borderSize, 1);

		// Right edge
		drawStretchedTabIconsTexture(pGuiGraphics, xOffset + width - borderSize, y + borderSize, borderSize, pHeight - (2*borderSize), texOffsetX + spriteSize - borderSize, texOffsetY + 4, borderSize, 1);
	}

	public static void drawWindow(GuiGraphics pGuiGraphics, int pWidth, int pHeight, boolean hasTabs, int x, int y) {
		int texOffsetY = 11;
		int texOffsetX = 64;

		int width = pWidth;
		int xOffset = x;

		if(hasTabs) {
			width -= 32;
			xOffset += 32;
		}

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, tabIcons);

		// Top Left corner
		pGuiGraphics.blit(tabIcons, xOffset, y, texOffsetX, texOffsetY, 4, 4);

		// Top right corner
		pGuiGraphics.blit(tabIcons, xOffset + width - 4, y, texOffsetX + 4 + 64, texOffsetY, 4, 4);

		// Bottom Left corner
		pGuiGraphics.blit(tabIcons, xOffset, y + pHeight - 4, texOffsetX, texOffsetY + 4 + 64, 4, 4);

		// Bottom Right corner
		pGuiGraphics.blit(tabIcons, xOffset + width - 4, y + pHeight - 4, texOffsetX + 4 + 64, texOffsetY + 4 + 64, 4, 4);


		// Top edge
		drawStretchedTabIconsTexture(pGuiGraphics, xOffset + 4, y, width - 8, 4, texOffsetX + 4, texOffsetY, 64, 4);

		// Bottom edge
		drawStretchedTabIconsTexture(pGuiGraphics, xOffset + 4, y + pHeight - 4, width - 8, 4, texOffsetX + 4, texOffsetY + 4 + 64, 64, 4);

		// Left edge
		drawStretchedTabIconsTexture(pGuiGraphics, xOffset, y + 4, 4, pHeight - 8, texOffsetX, texOffsetY + 4, 4, 64);

		// Right edge
		drawStretchedTabIconsTexture(pGuiGraphics, xOffset + width - 4, y + 4, 4, pHeight - 8, texOffsetX + 64 + 4, texOffsetY + 3, 4, 64);

		drawStretchedTabIconsTexture(pGuiGraphics, xOffset + 4, y + 4, width - 8, pHeight - 8, texOffsetX + 4, texOffsetY + 4, 64, 64);
	}
}
