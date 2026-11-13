package pwk.utils.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
//? if >=1.20.5 {
import net.neoforged.neoforge.event.tick.ServerTickEvent;
//?} else {
/*import net.neoforged.neoforge.event.TickEvent;
 *///?}

import static net.minecraft.commands.Commands.literal;

public class TPSCommand {
    private static long lastTickTime = System.currentTimeMillis();
    private static volatile double tps = 20.0;

    //? if >=1.20.5 {
    public static void onServerTick(ServerTickEvent.Pre event) {
//?} else {
    /*public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
*///?}
        long now = System.currentTimeMillis();
        long delta = now - lastTickTime;
        lastTickTime = now;
        if (delta <= 0) return;
        tps = Math.min(20.0, 1000.0 / delta);
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("tps").executes(context -> {
            context.getSource().sendSuccess(() -> Component.literal(
                    "§8[§3PWKUtils§8] §cTPS: §6" + String.format("%.2f", tps)), false);
            return 1;
        }));
    }
}