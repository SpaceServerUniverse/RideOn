package space.yurisi.rideon;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import space.yurisi.rideon.command.*;

public final class RideOn extends JavaPlugin {

    private final HashMap<UUID, UUID> requests = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        Objects.requireNonNull(getCommand("ride")).setExecutor(new sendRequestCommand(this));
        Objects.requireNonNull(getCommand("rideok")).setExecutor(new acceptRequestCommand(this));
        Objects.requireNonNull(getCommand("rideno")).setExecutor(new denyRequestCommand(this));
        Objects.requireNonNull(getCommand("ridecancel")).setExecutor(new cancelRequestCommand(this));
        Objects.requireNonNull(getCommand("RideReqClear")).setExecutor(new clearAllRequestsCommand(this));
    }

    public void setRequest(UUID sender, UUID target){
        this.requests.put(sender, target);
    }

    public UUID getSenderUUIDfromTargetUUID(UUID target){
        return this.requests.entrySet().stream().filter(entry -> entry.getValue().equals(target)).findFirst().get().getKey();
    }

    public UUID getTargetUUIDfromSenderUUID(UUID sender){
        return this.requests.get(sender);
    }

    public boolean isExistTargetRequest(UUID target){
        return this.requests.containsValue(target);
    }

    public boolean isExistSenderRequest(UUID sender){
        return this.requests.containsKey(sender);
    }

    public void removeRequest(UUID sender){
        this.requests.remove(sender);
    }

    public void clearAllRequest(){
        this.requests.clear();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
