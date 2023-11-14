package hama.industries.jackal.mixin.compat.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.simibubi.create.content.trains.station.GlobalStation;

import hama.industries.jackal.logic.manager.ICLTrigger;


@Mixin(com.simibubi.create.content.trains.entity.Train.class)
public abstract class TrainMixin {

    @Inject(method = "arriveAt", at = @At("RETURN"), remap = false)
    public void arriveAt(GlobalStation station, CallbackInfo ci) {
        System.out.println("train arrived");
        ((ICLTrigger) (Object) station).registerCLTrigger();
    }

}