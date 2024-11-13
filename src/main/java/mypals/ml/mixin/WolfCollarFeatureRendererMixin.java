package mypals.ml.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mypals.ml.interfaces.IWolfEntityRenderStateMixin;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.WolfCollarFeatureRenderer;
import net.minecraft.client.render.entity.model.WolfEntityModel;
import net.minecraft.client.render.entity.state.WolfEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(WolfCollarFeatureRenderer.class)
public abstract class WolfCollarFeatureRendererMixin extends FeatureRenderer<WolfEntityRenderState, WolfEntityModel>{

    @Shadow private static final Identifier SKIN = Identifier.ofVanilla("textures/entity/wolf/wolf_collar.png");
    public WolfCollarFeatureRendererMixin(FeatureRendererContext<WolfEntityRenderState, WolfEntityModel> featureRendererContext) {
        super(featureRendererContext);
    }

    @WrapMethod(method = "render")
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, WolfEntityRenderState wolfEntityRenderState, float f, float g
    , Operation<WolfCollarFeatureRenderer> operation){
        Integer dyeColor = ((IWolfEntityRenderStateMixin) wolfEntityRenderState).colorfulcollars_24w45a$getRGBCollar();
        if (dyeColor != null && !wolfEntityRenderState.invisible) {
            int j = dyeColor;
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(SKIN));
            this.getContextModel().render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, j);
        }
    }
}
