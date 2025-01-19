package com.godlia.mobclicker.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobclickerClient implements ClientModInitializer {

    public static final String MOD_ID = "MobClicker";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public boolean isModActive = false;

    public void onInitializeClient() {
        LOGGER.info("MobClicker initialized");
        MinecraftClient client = MinecraftClient.getInstance();

        KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mobclicker", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_R, // The keycode of the key
                "category.mobclicker" // The translation key of the keybinding's category.
        ));


        ClientTickEvents.END_CLIENT_TICK.register(new ClientTickEvents.EndTick() {
            @Override
            public void onEndTick(MinecraftClient Null) {
                if(keyBinding.wasPressed()) {
                    isModActive = !isModActive;
                    client.player.sendMessage(Text.literal("MobClicker is now " + (isModActive ? "enabled!" : "disabled!" )), false);
                }


                if(!isModActive) return;

                HitResult hit = client.crosshairTarget;
                /*
                if(client.player != null) {
                    LOGGER.info(client.player.getAttackCooldownProgress(0.5f) + "");
                }
                */



                if(hit != null) {
                    if(hit.getType() == HitResult.Type.ENTITY) {
                        EntityHitResult target = (EntityHitResult) hit;
                        Entity targetedEntity = target.getEntity();
                        if(!targetedEntity.getType().equals(EntityType.PLAYER)) {
                            float h = client.player.getAttackCooldownProgress(0.5f);
                            if(((int) h == 1.0f)) {
                                client.interactionManager.attackEntity(client.player, targetedEntity);
                            }
                        }
                    }
                }
            }
        });
    }





}
