package mypals.ml.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mypals.ml.ColorHelper;
import mypals.ml.interfaces.ICatEntityMixin;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CatEntity.class)
public abstract class CatEntityMixin extends TameableEntity implements VariantHolder<RegistryEntry<CatVariant>>, ICatEntityMixin {
	protected CatEntityMixin(EntityType<? extends TameableEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow
	public abstract DyeColor getCollarColor();
	@Unique
	private static final TrackedData<Integer> COLLAR_COLOR_RGB = DataTracker.registerData(CatEntity.class, TrackedDataHandlerRegistry.INTEGER);

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
			target = "Lnet/minecraft/entity/passive/CatEntity;setCollarColor(Lnet/minecraft/util/DyeColor;)V",
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

	@WrapMethod(method = "createChild")
	public CatEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity, Operation<CatEntity> original) {
		CatEntity catEntity = EntityType.CAT.create(serverWorld, SpawnReason.BREEDING);
		if (catEntity != null && passiveEntity instanceof CatEntity catEntity2) {
			if (this.random.nextBoolean()) {
				catEntity.setVariant(this.getVariant());
			} else {
				catEntity.setVariant(catEntity2.getVariant());
			}
			if (this.isTamed()) {
				catEntity.setOwnerUuid(this.getOwnerUuid());
				catEntity.setTamed(true, true);
				Integer dyeColor = this.getCollarColor().getEntityColor();
				Integer dyeColor2 = ((ICatEntityMixin)catEntity2).colorfulcollars_24w45a$getCollarColorRgb();
				((ICatEntityMixin)catEntity).colorfulcollars_24w45a$setCollarColorRgb(ColorHelper.blendColors(dyeColor,dyeColor2,2));
			}
		}

		return catEntity;
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