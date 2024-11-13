package mypals.ml.mixin;

import mypals.ml.interfaces.IWolfEntityMixin;
import mypals.ml.interfaces.IWolfEntityRenderStateMixin;
import net.minecraft.client.render.entity.WolfEntityRenderer;
import net.minecraft.client.render.entity.state.WolfEntityRenderState;
import net.minecraft.entity.passive.WolfEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WolfEntityRenderer.class)
public class WolfEntityRendererMixin {
    @Inject(method = "updateRenderState", at = @At(value = "TAIL"))
    private void updateRenderState(WolfEntity wolfEntity, WolfEntityRenderState wolfEntityRenderState, float f, CallbackInfo ci) {
        ((IWolfEntityRenderStateMixin)wolfEntityRenderState).colorfulcollars_24w45a$setRGBCollar(wolfEntity.isTamed() ? ((IWolfEntityMixin)wolfEntity).colorfulcollars_24w45a$getCollarColorRgb() : null);
    }
}
