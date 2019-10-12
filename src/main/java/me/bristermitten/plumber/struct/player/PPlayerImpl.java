package me.bristermitten.plumber.struct.player;

import com.google.inject.Inject;
import me.bristermitten.plumber.struct.builder.BuilderFactory;
import me.bristermitten.plumber.struct.builder.PlayerActionBuilder;
import me.bristermitten.plumber.struct.builder.TaskLengthConfiguration;
import me.bristermitten.plumber.struct.event.EventController;
import me.bristermitten.plumber.struct.event.EventControllerFactory;
import me.bristermitten.plumber.struct.key.DataKey;
import me.bristermitten.plumber.struct.key.KeyHolder;
import me.bristermitten.plumber.struct.key.KeyMap;
import me.bristermitten.plumber.util.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;

/**
 * Default implementation for {@link PPlayer}
 * <p>
 * TODO: Replace implementation of {@link KeyHolder} by extending something like "KeyHolderImpl"?
 */
class PPlayerImpl implements PPlayer {

    /**
     * Storage of Data Key values
     */
    private final KeyMap keyValues = new KeyMap();
    /**
     * The underlying Player
     */
    private final Player player;

    @Inject
    private BuilderFactory factory;

    @Inject
    private EventControllerFactory ecFactory;

    PPlayerImpl(Player player) {
        this.player = player;
    }

    @Override
    public Player player() {
        return player;
    }

    @Override
    public <T extends PlayerEvent & Cancellable> TaskLengthConfiguration<PlayerActionBuilder> blockEvent(Class<T> e) {
        EventController<?> controller = ecFactory.createController(e);
        controller.cancelAll();
        PlayerActionBuilder actionBuilder = factory.createPlayerActionBuilder(this, controller::ignoreAll);
        return factory.createPlayerTaskLengthConfiguration(actionBuilder);
    }

    @Override
    public void message(String msg) {
        player.sendMessage(ChatUtil.color(msg));
    }

//    public DistancePicker<CompareRootTimePicker> lastMoved() {
//        return null;
//    }
//
//    public void kick() {
//        player.kickPlayer("TODO");
//    }

    @Override
    public <K> void setData(DataKey<K> key, K data) {
        key.execHandlers(data);
        keyValues.put(key, data);
    }

    @Override
    public <K> void rawSetData(DataKey<K> key, K data) {
        keyValues.put(key, data);
    }

    @Override
    public <K> K getData(DataKey<K> key) {
        return (K) keyValues.getOrDefault(key, key.getDefaultValue());
    }
}
