package mypals.ml.mixin;

import mypals.ml.interfaces.IWolfEntityRenderStateMixin;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;


@Mixin(WolfRenderState.class)
public abstract class WolfEntityRenderStateMixin implements IWolfEntityRenderStateMixin {

    @Unique
    @Nullable
    public Integer collarColorRGB;
    @Override
    public void colorfulcollars_24w45a$setRGBCollar(Integer color) {
        this.collarColorRGB = color;
    }
    @Override
    public Integer colorfulcollars_24w45a$getRGBCollar() {
        return this.collarColorRGB;
    }
}
