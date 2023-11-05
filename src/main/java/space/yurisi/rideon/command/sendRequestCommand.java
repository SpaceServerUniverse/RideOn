package space.yurisi.rideon.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import space.yurisi.rideon.RideOn;

import java.util.UUID;

public class sendRequestCommand extends RideOnRequestCommandManager {

    private final RideOn main;

    public sendRequestCommand(RideOn main){
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            return false;
        }
        if(args.length == 0){
            player.sendMessage(getErrorMessage("プレイヤー名を入力してください。"));
            return false;
        }

        String targetName = args[0];
        if(targetName.equals(player.getName())){
            player.sendMessage(getErrorMessage("自分に乗ることはできません。"));
            return false;
        }

        Player targetPlayer = Bukkit.getPlayerExact(targetName);
        if(targetPlayer == null){
            player.sendMessage(getErrorMessage("プレイヤーが見つかりませんでした。"));
            return false;
        }

        UUID targetUUID = targetPlayer.getUniqueId();
        UUID senderUUID = player.getUniqueId();

        if(targetUUID.equals(senderUUID)){
            player.sendMessage(getErrorMessage("自分に乗ることはできません。"));
            return false;
        }

        if(targetPlayer.getWorld() != player.getWorld()){
            player.sendMessage(getErrorMessage("同じワールドにいるプレイヤーにしか乗れません。"));
            return false;
        }

        // 距離が20ブロック以内かどうか
        if(targetPlayer.getLocation().distance(player.getLocation()) > 20){
            player.sendMessage(getErrorMessage("乗りたいプレイヤーに近づいてください。"));
            return false;
        }

        if(main.isExistSenderRequest(senderUUID)){
            player.sendMessage(getErrorMessage("既に騎乗依頼を出しています。"));
            return false;
        }

        if(main.isExistTargetRequest(targetUUID)){
            player.sendMessage(getErrorMessage("そのプレイヤーは既に騎乗依頼を受けています。"));
            return false;
        }

        main.setRequest(senderUUID, targetUUID);

        targetPlayer.sendMessage(getSuccessMessage("§b" + player.getName() + "§2さんから騎乗依頼が届きました。"));
        targetPlayer.sendMessage(getSuccessMessage("§6/rideok§2で承認できます。"));
        targetPlayer.sendMessage(getSuccessMessage("§6/rideno§2で拒否できます。"));

        player.sendMessage(getSuccessMessage("§b" + targetName + "§2さんに騎乗依頼を送信しました。"));
        player.sendMessage(getSuccessMessage("§6/ridecancel§2でキャンセルできます。"));

        return true;
    }
}
