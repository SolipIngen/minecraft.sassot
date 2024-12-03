package solipingen.sassot.mixin.server.network;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import solipingen.sassot.util.interfaces.mixin.entity.LivingEntityInterface;

import java.util.Set;


@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin extends ServerCommonNetworkHandler {
    @Shadow public ServerPlayerEntity player;


    public ServerPlayNetworkHandlerMixin(MinecraftServer server, ClientConnection connection, ConnectedClientData clientData) {
        super(server, connection, clientData);
    }

    @Inject(method = "requestTeleport(DDDFFLjava/util/Set;)V", at = @At("TAIL"))
    private void injectedRequestTeleport(double x, double y, double z, float yaw, float pitch, Set<PositionFlag> flags, CallbackInfo cbi) {
        LivingEntityInterface iPlayer = (LivingEntityInterface)this.player;
        if (iPlayer.getIsSkewered() || iPlayer.getSkeweringEntity() != null) {
            iPlayer.setIsSkewered(false);
            iPlayer.setSkeweringEntity(null);
        }
    }



}
