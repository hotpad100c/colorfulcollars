package mypals.ml.mixin;

import mypals.ml.interfaces.ICatEntityMixin;
import mypals.ml.interfaces.ICatEntityRenderStateMixin;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.state.CatRenderState;
import net.minecraft.world.entity.animal.feline.Cat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CatRenderer.class)
public class CatEntityRendererMixin {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/animal/feline/Cat;Lnet/minecraft/client/renderer/entity/state/CatRenderState;F)V", at = @At(value = "TAIL"))
    private void updateRenderState(Cat entity, CatRenderState state, float f, CallbackInfo ci) {
        ((ICatEntityRenderStateMixin)state).colorfulcollars_24w45a$setRGBCollar(entity.isTame() ? ((ICatEntityMixin)entity).colorfulcollars_24w45a$getCollarColorRgb() : null);
    }
}
