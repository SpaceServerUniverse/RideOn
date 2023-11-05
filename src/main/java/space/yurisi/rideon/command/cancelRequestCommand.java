package space.yurisi.rideon.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import space.yurisi.rideon.RideOn;

import java.util.UUID;

public class cancelRequestCommand extends RideOnRequestCommandManager {

    private final RideOn main;

    public cancelRequestCommand(RideOn main){
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args){
        if(!(sender instanceof Player player)){
            return false;
        }

        if(!main.isExistSenderRequest(player.getUniqueId())){
            player.sendMessage(getErrorMessage("保留中の騎乗依頼はありません。"));
            return false;
        }

        UUID targetUUID = main.getTargetUUIDfromSenderUUID(player.getUniqueId());
        Player targetPlayer = Bukkit.getPlayer(targetUUID);

        if(targetPlayer == null){
            player.sendMessage(getSuccessMessage("オフラインのプレイヤーの騎乗依頼をキャンセルしました。"));
            main.removeRequest(player.getUniqueId());
            return true;
        }

        main.removeRequest(player.getUniqueId());
        player.sendMessage(getSuccessMessage("§b" + targetPlayer.getName() + "§2さんへの騎乗依頼をキャンセルしました。"));
        targetPlayer.sendMessage(getErrorMessage("§b" + player.getName() + "§4さんが騎乗依頼をキャンセルしました。"));

        return true;
    }
}

