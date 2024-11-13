package mypals.ml.mixin;

import mypals.ml.interfaces.ICatEntityRenderStateMixin;
import net.minecraft.client.render.entity.state.CatEntityRenderState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;


@Mixin(CatEntityRenderState.class)
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
