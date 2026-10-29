package com.ma4z.soiwu;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.Random;

public final class DahdaWatcher {
	private static final Identifier TEXTURE = Identifier.of("dahda", "dahda.png");
	private static final Random RANDOM = new Random();
	private static Vec3d position;
	private static Vec3d destination;
	private static boolean enabled = true;
	private static int ticks;
	private static float walkPhase;
	private static boolean testRequested;

	private DahdaWatcher() {
	}

	public static void registerClient() {
		ClientTickEvents.END_CLIENT_TICK.register(DahdaWatcher::tick);
		WorldRenderEvents.AFTER_ENTITIES.register(DahdaWatcher::render);
		HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
			MinecraftClient client = MinecraftClient.getInstance();
			if (enabled && client.player != null && client.options.hudHidden == false) {
				TextRenderer textRenderer = client.textRenderer;
				String red = "Dahda";
				String white = " dekh ra ha";
				int width = textRenderer.getWidth(red + white);
				int x = (drawContext.getScaledWindowWidth() - width) / 2;
				int y = drawContext.getScaledWindowHeight() - 52;
				drawContext.drawTextWithShadow(textRenderer, red, x, y, 0xFFE3262E);
				drawContext.drawTextWithShadow(textRenderer, white, x + textRenderer.getWidth(red), y, 0xFFFFFFFF);
			}
		});
	}

	public static void requestTest() {
		testRequested = true;
		enabled = true;
	}

	private static void tick(MinecraftClient client) {
		if (client.player == null || client.world == null || !enabled) {
			return;
		}
		if (position == null || testRequested) {
			position = client.player.getPos().add(0.0, 0.0, -12.0);
			destination = client.player.getPos();
			testRequested = false;
			ticks = 0;
		}

		ticks++;
		Vec3d playerPosition = client.player.getPos();
		boolean fighting = !client.world.getEntitiesByClass(HostileEntity.class,
				client.player.getBoundingBox().expand(10.0), mob -> mob.isAlive()).isEmpty();
		if (destination == null || ticks % 180 == 0 || position.distanceTo(playerPosition) < 3.5) {
			Vec3d direction = new Vec3d(RANDOM.nextDouble() - 0.5, 0.0, RANDOM.nextDouble() - 0.5).normalize();
			destination = playerPosition.add(direction.multiply(fighting ? 9.0 : 13.0));
		}

		Vec3d toDestination = destination.subtract(position);
		double speed = position.distanceTo(playerPosition) > 5.0 ? 0.075 : 0.025;
		if (toDestination.lengthSquared() > 0.25) {
			position = position.add(toDestination.normalize().multiply(speed));
			walkPhase += (float) (speed * 4.0);
		} else {
			walkPhase += 0.025F;
		}
	}

	private static void render(WorldRenderContext context) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (!enabled || position == null || client.player == null) {
			return;
		}
		MatrixStack matrices = context.matrixStack();
		Camera camera = context.camera();
		Vec3d cameraPosition = camera.getPos();
		float tickDelta = client.getRenderTickCounter().getTickDelta(false);
		float bob = (float) Math.abs(Math.sin((walkPhase + tickDelta * 0.08F) * 2.0F)) * 0.08F;
		float sway = (float) Math.sin((walkPhase + tickDelta * 0.08F) * 2.0F) * 4.0F;

		matrices.push();
		matrices.translate(position.x - cameraPosition.x, position.y - cameraPosition.y + bob, position.z - cameraPosition.z);
		matrices.multiply(camera.getRotation());
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(sway));
		matrices.scale(2.2F, 2.2F, 2.2F);

		VertexConsumerProvider.Immediate consumers = client.getBufferBuilders().getEntityVertexConsumers();
		VertexConsumer vertexConsumer = consumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));
		Matrix4f matrix = matrices.peek().getPositionMatrix();
		int light = 0xF000F0;
		vertexConsumer.vertex(matrix, -0.5F, 0.0F, 0.0F).color(255, 255, 255, 255).texture(0.0F, 1.0F).overlay(0, 10).light(light).normal(0.0F, 1.0F, 0.0F);
		vertexConsumer.vertex(matrix, 0.5F, 0.0F, 0.0F).color(255, 255, 255, 255).texture(1.0F, 1.0F).overlay(0, 10).light(light).normal(0.0F, 1.0F, 0.0F);
		vertexConsumer.vertex(matrix, 0.5F, 1.0F, 0.0F).color(255, 255, 255, 255).texture(1.0F, 0.0F).overlay(0, 10).light(light).normal(0.0F, 1.0F, 0.0F);
		vertexConsumer.vertex(matrix, -0.5F, 1.0F, 0.0F).color(255, 255, 255, 255).texture(0.0F, 0.0F).overlay(0, 10).light(light).normal(0.0F, 1.0F, 0.0F);
		consumers.draw();
		matrices.pop();
	}
}