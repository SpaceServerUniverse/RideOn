package space.yurisi.rideon.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import space.yurisi.rideon.RideOn;

import java.util.UUID;

public class denyRequestCommand extends RideOnRequestCommandManager {

    private final RideOn main;

    public denyRequestCommand(RideOn main){
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
            player.sendMessage(getSuccessMessage("オフラインのプレイヤーの騎乗依頼を拒否しました。"));
            main.removeRequest(senderUUID);
            return true;
        }

        main.removeRequest(senderUUID);
        player.sendMessage(getSuccessMessage("§b" + senderPlayer.getName() + "§2さんの騎乗依頼を拒否しました。"));
        senderPlayer.sendMessage(getErrorMessage("§b" + player.getName() + "§4さんが騎乗依頼を拒否しました。"));

        return true;
    }
}
