package space.yurisi.rideon.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import space.yurisi.rideon.RideOn;

import java.util.Objects;
import java.util.UUID;

public class acceptRequestCommand extends RideOnRequestCommandManager {

    private final RideOn main;

    public acceptRequestCommand(RideOn main){
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args){
        if(!(sender instanceof Player player)){
            return false;
        }

        if(!main.isExistTargetRequest(player.getUniqueId())){
            player.sendMessage(getErrorMessage("騎乗依頼はありません。"));
            return false;
        }

        UUID senderUUID = main.getSenderUUIDfromTargetUUID(player.getUniqueId());
        Player senderPlayer = Bukkit.getPlayer(senderUUID);

        if(senderPlayer == null){
            player.sendMessage(getErrorMessage("乗りたいプレイヤーがオンラインではありません。"));
            main.removeRequest(senderUUID);
            return false;
        }

        if(senderPlayer.getWorld() != player.getWorld()){
            player.sendMessage(getErrorMessage("同じワールドにいるプレイヤーにしか乗れません。"));
            return false;
        }

        // 距離が20ブロック以内かどうか
        if(senderPlayer.getLocation().distance(player.getLocation()) > 20){
            player.sendMessage(getErrorMessage("乗りたいプレイヤーに近づいてください。"));
            return false;
        }

        if(senderPlayer.isInsideVehicle()){
            Objects.requireNonNull(senderPlayer.getVehicle()).eject();
        }

        player.addPassenger(senderPlayer);
        main.removeRequest(senderUUID);
        player.sendMessage(getSuccessMessage("§b" + player.getName() + "§2さんに乗りました。"));
        senderPlayer.sendMessage(getSuccessMessage("§b" + senderPlayer.getName() + "§2さんを乗せました。"));

        return true;
    }

}
