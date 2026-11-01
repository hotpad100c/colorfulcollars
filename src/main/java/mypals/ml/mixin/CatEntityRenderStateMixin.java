package mypals.ml.mixin;

import mypals.ml.interfaces.ICatEntityRenderStateMixin;
import net.minecraft.client.renderer.entity.state.CatRenderState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;


@Mixin(CatRenderState.class)
public abstract class CatEntityRenderStateMixin implements ICatEntityRenderStateMixin {

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
