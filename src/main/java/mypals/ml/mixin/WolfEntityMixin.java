package mypals.ml.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mypals.ml.ColorHelper;
import mypals.ml.interfaces.IWolfEntityMixin;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.VariantHolder;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WolfEntity.class)
public abstract class WolfEntityMixin extends TameableEntity implements VariantHolder<RegistryEntry<WolfVariant>>, IWolfEntityMixin{


	protected WolfEntityMixin(EntityType<? extends TameableEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow public abstract DyeColor getCollarColor();
	@Unique
	private static final TrackedData<Integer> COLLAR_COLOR_RGB = DataTracker.registerData(WolfEntity.class, TrackedDataHandlerRegistry.INTEGER);

	@Override
	public void colorfulcollars_24w45a$setCollarColorRgb(Integer color){
		this.getDataTracker().set(COLLAR_COLOR_RGB, color);
	}
	public void setCollarColorRgbWhenDye(Integer color){
		int colorOld = colorfulcollars_24w45a$getCollarColorRgb();
		int colorNew = ColorHelper.blendColors(colorOld,color,10);
		colorfulcollars_24w45a$setCollarColorRgb(colorNew);
	}
	@Override
	public Integer colorfulcollars_24w45a$getCollarColorRgb(){
		return this.getDataTracker().get(COLLAR_COLOR_RGB);
	}
	@WrapMethod(method = "interactMob")
	private ActionResult setRGBColorWhenUseDye(PlayerEntity player, Hand hand, Operation<ActionResult> original) {
		ItemStack itemStack = player.getStackInHand(hand);
		Item item = itemStack.getItem();
		if (item instanceof DyeItem dyeItem && this.isOwner(player)) {
			DyeColor dyeColor = dyeItem.getColor();
			if (dyeColor.getEntityColor() != colorfulcollars_24w45a$getCollarColorRgb()) {
				setCollarColorRgbWhenDye(dyeColor.getEntityColor());
				itemStack.decrementUnlessCreative(1, player);
				return ActionResult.SUCCESS;
			}
			return original.call(player,hand);
		}
		return original.call(player,hand);
	}
	/*@WrapMethod(method = "setCollarColor")
	private void setColor(DyeColor color, Operation<Void> original) {
		colorfulcollars_24w45a$setCollarColorRgb(color.getEntityColor());
		original.call(color);
	}*/
	@Inject(method = "readCustomDataFromNbt", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/entity/passive/WolfEntity;setCollarColor(Lnet/minecraft/util/DyeColor;)V",
			shift = At.Shift.AFTER))
	private void setColorWhenReadNBT(NbtCompound nbt, CallbackInfo ci) {
		if (nbt.contains("CollarColorRGB", NbtElement.INT_ARRAY_TYPE)) {
			int[] rgb = (nbt.getIntArray("CollarColorRGB"));
			int color = (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];
			colorfulcollars_24w45a$setCollarColorRgb(color);
		}else if (nbt.contains("CollarColor", NbtElement.NUMBER_TYPE)) {
			colorfulcollars_24w45a$setCollarColorRgb(DyeColor.byId(nbt.getInt("CollarColor")).getEntityColor());
		}
	}

	/*@Inject(method = "createChild", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/entity/passive/WolfEntity;setCollarColor(Lnet/minecraft/util/DyeColor;)V"))

	private void setColorWhenCreateChild(ServerWorld serverWorld, PassiveEntity passiveEntity, @Local WolfEntity wolfEntity)  {


	}*/
	@WrapMethod(method = "createChild")
	public WolfEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity, Operation<WolfEntity> original) {
		WolfEntity wolfEntity = EntityType.WOLF.create(serverWorld, SpawnReason.BREEDING);
		if (wolfEntity != null && passiveEntity instanceof WolfEntity wolfEntity2) {
			if (this.random.nextBoolean()) {
				wolfEntity.setVariant(this.getVariant());
			} else {
				wolfEntity.setVariant(wolfEntity2.getVariant());
			}

			if (this.isTamed()) {
				wolfEntity.setOwnerUuid(this.getOwnerUuid());
				wolfEntity.setTamed(true, true);
				Integer dyeColor = this.getCollarColor().getEntityColor();
				Integer dyeColor2 = ((IWolfEntityMixin)wolfEntity2).colorfulcollars_24w45a$getCollarColorRgb();
				((IWolfEntityMixin)wolfEntity).colorfulcollars_24w45a$setCollarColorRgb(ColorHelper.blendColors(dyeColor,dyeColor2,2));
				//wolfEntity.setCollarColor(DyeColor.method_65355(serverWorld, dyeColor, dyeColor2));
			}
		}

		return wolfEntity;
	}

	@Inject(method = "writeCustomDataToNbt", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/nbt/NbtCompound;putByte(Ljava/lang/String;B)V",
			shift = At.Shift.AFTER))
	private void setColorWhenWriteNBT(NbtCompound nbt, CallbackInfo ci) {
		int r = (colorfulcollars_24w45a$getCollarColorRgb() >> 16) & 0xFF;
		int g = (colorfulcollars_24w45a$getCollarColorRgb() >> 8) & 0xFF;
		int b = colorfulcollars_24w45a$getCollarColorRgb() & 0xFF;
		nbt.putIntArray("CollarColorRGB", new int[]{r,g,b});
	}
	@Inject(method = "initDataTracker",at = @At(value = "TAIL"))
	private void setColorWhenBuildDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
		builder.add(COLLAR_COLOR_RGB, DyeColor.RED.getEntityColor());
	}
}