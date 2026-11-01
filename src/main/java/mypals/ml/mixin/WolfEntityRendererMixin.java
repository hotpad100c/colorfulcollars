package mypals.ml.mixin;

import mypals.ml.interfaces.IWolfEntityMixin;
import mypals.ml.interfaces.IWolfEntityRenderStateMixin;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.world.entity.animal.wolf.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WolfRenderer.class)
public class WolfEntityRendererMixin {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/animal/wolf/Wolf;Lnet/minecraft/client/renderer/entity/state/WolfRenderState;F)V", at = @At(value = "TAIL"))
    private void updateRenderState(Wolf entity, WolfRenderState state, float f, CallbackInfo ci) {
        ((IWolfEntityRenderStateMixin)state).colorfulcollars_24w45a$setRGBCollar(entity.isTame() ? ((IWolfEntityMixin)entity).colorfulcollars_24w45a$getCollarColorRgb() : null);
    }
}
