package mypals.ml.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import mypals.ml.interfaces.ICatEntityRenderStateMixin;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.client.renderer.entity.state.CatRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CatCollarLayer.class)
public abstract class CatCollarFeatureRendererMixin {
    @ModifyExpressionValue(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/CatRenderState;FF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/DyeColor;getTextureDiffuseColor()I"))
    private int useRGBCollar(int original, @Local(argsOnly = true) CatRenderState state) {
        Integer dyeColor = ((ICatEntityRenderStateMixin) state).colorfulcollars_24w45a$getRGBCollar();
        return dyeColor != null ? 0xFF000000 | dyeColor : original;
    }
}
