package mypals.ml.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mypals.ml.ColorHelper;
import mypals.ml.interfaces.ICatEntityMixin;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Cat.class)
public abstract class CatEntityMixin extends TamableAnimal implements ICatEntityMixin {
	protected CatEntityMixin(EntityType<? extends TamableAnimal> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
	public abstract DyeColor getCollarColor();
	@Unique
	private static final EntityDataAccessor<Integer> COLLAR_COLOR_RGB = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.INT);

	@Override
	public void colorfulcollars_24w45a$setCollarColorRgb(Integer color){
		this.getEntityData().set(COLLAR_COLOR_RGB, color);
	}
	public void setCollarColorRgbWhenDye(Integer color){
		int colorOld = colorfulcollars_24w45a$getCollarColorRgb();
		int colorNew = ColorHelper.blendColors(colorOld,color,10);
		colorfulcollars_24w45a$setCollarColorRgb(colorNew);
	}
	@Override
	public Integer colorfulcollars_24w45a$getCollarColorRgb(){
		return this.getEntityData().get(COLLAR_COLOR_RGB);
	}
	@WrapMethod(method = "mobInteract")
	private InteractionResult setRGBColorWhenUseDye(Player player, InteractionHand hand, Operation<InteractionResult> original) {
		ItemStack itemStack = player.getItemInHand(hand);
		DyeColor dyeColor = itemStack.get(DataComponents.DYE);
		if (dyeColor != null && itemStack.is(ItemTags.CAT_COLLAR_DYES) && this.isTame() && this.isOwnedBy(player)) {
			if ((dyeColor.getTextureDiffuseColor() & 0xFFFFFF) != (colorfulcollars_24w45a$getCollarColorRgb() & 0xFFFFFF)) {
				if (!this.level().isClientSide()) {
					setCollarColorRgbWhenDye(dyeColor.getTextureDiffuseColor());
					itemStack.consume(1, player);
					this.setPersistenceRequired();
				}
				return InteractionResult.SUCCESS;
			}
		}
		return original.call(player,hand);
	}
	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void setColorWhenReadNBT(ValueInput input, CallbackInfo ci) {
		input.getIntArray("CollarColorRGB").filter(rgb -> rgb.length == 3).ifPresentOrElse(
				rgb -> colorfulcollars_24w45a$setCollarColorRgb((rgb[0] << 16) | (rgb[1] << 8) | rgb[2]),
				() -> colorfulcollars_24w45a$setCollarColorRgb(this.getCollarColor().getTextureDiffuseColor()));
	}

	@Inject(method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/feline/Cat;", at = @At("RETURN"))
	private void blendColorWhenBreed(ServerLevel level, AgeableMob partner, CallbackInfoReturnable<Cat> cir) {
		Cat baby = cir.getReturnValue();
		if (baby != null && partner instanceof Cat && this.isTame()) {
			Integer dyeColor = colorfulcollars_24w45a$getCollarColorRgb();
			Integer dyeColor2 = ((ICatEntityMixin)partner).colorfulcollars_24w45a$getCollarColorRgb();
			((ICatEntityMixin)baby).colorfulcollars_24w45a$setCollarColorRgb(ColorHelper.blendColors(dyeColor,dyeColor2,2));
		}
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void setColorWhenWriteNBT(ValueOutput output, CallbackInfo ci) {
		int r = (colorfulcollars_24w45a$getCollarColorRgb() >> 16) & 0xFF;
		int g = (colorfulcollars_24w45a$getCollarColorRgb() >> 8) & 0xFF;
		int b = colorfulcollars_24w45a$getCollarColorRgb() & 0xFF;
		output.putIntArray("CollarColorRGB", new int[]{r,g,b});
	}
	@Inject(method = "defineSynchedData",at = @At(value = "TAIL"))
	private void setColorWhenBuildDataTracker(SynchedEntityData.Builder builder, CallbackInfo ci) {
		builder.define(COLLAR_COLOR_RGB, DyeColor.RED.getTextureDiffuseColor());
	}
}
