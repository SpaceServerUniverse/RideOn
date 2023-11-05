package space.yurisi.rideon.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import space.yurisi.rideon.RideOn;

public class clearAllRequestsCommand extends RideOnRequestCommandManager {

    private final RideOn main;

    public clearAllRequestsCommand(RideOn main){
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

        main.clearAllRequest();
        player.sendMessage(getSuccessMessage("騎乗依頼を全てキャンセルしました。"));
        return true;
    }

}
