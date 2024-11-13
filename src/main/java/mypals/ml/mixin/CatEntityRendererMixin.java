package mypals.ml.mixin;

import mypals.ml.interfaces.ICatEntityMixin;
import mypals.ml.interfaces.ICatEntityRenderStateMixin;
import net.minecraft.client.render.entity.CatEntityRenderer;
import net.minecraft.client.render.entity.state.CatEntityRenderState;
import net.minecraft.entity.passive.CatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CatEntityRenderer.class)
public class CatEntityRendererMixin {
    @Inject(method = "updateRenderState", at = @At(value = "TAIL"))
    private void updateRenderState(CatEntity catEntity, CatEntityRenderState catEntityRenderState, float f, CallbackInfo ci) {
        ((ICatEntityRenderStateMixin)catEntityRenderState).colorfulcollars_24w45a$setRGBCollar(catEntity.isTamed() ? ((ICatEntityMixin)catEntity).colorfulcollars_24w45a$getCollarColorRgb() : null);
    }
}
