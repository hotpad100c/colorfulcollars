package mypals.ml.mixin;

import mypals.ml.interfaces.IWolfEntityRenderStateMixin;
import net.minecraft.client.render.entity.state.WolfEntityRenderState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;


@Mixin(WolfEntityRenderState.class)
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
