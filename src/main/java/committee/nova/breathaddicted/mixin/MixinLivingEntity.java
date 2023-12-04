package committee.nova.breathaddicted.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
    @Shadow
    public abstract void heal(float p_21116_);

    public MixinLivingEntity(EntityType<?> t, Level l) {
        super(t, l);
    }

    @Inject(method = "actuallyHurt", at = @At("HEAD"), cancellable = true)
    private void inject$actuallyHurt(DamageSource dmg, float value, CallbackInfo ci) {
        final LivingEntity thisInstance = (LivingEntity) (Object) this;
        if (!(thisInstance instanceof EnderMan e) || !dmg.is(DamageTypes.DRAGON_BREATH)) return;
        ci.cancel();
        if (level().getDifficulty().getId() < 3) return;
        heal(value);
        if (e.getTarget() != null) return;
        e.setTarget(level().getNearestPlayer(e, 64.0));
    }
}
