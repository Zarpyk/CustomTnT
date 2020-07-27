package guerrero61.customtnt.discord.events;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jetbrains.annotations.NotNull;

import guerrero61.customtnt.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DisableBot {

	private static JDA api;

	public static void Disable(Main main) {
		api = main.api;
		if (api != null) {
			api.getEventManager().getRegisteredListeners()
					.forEach(listener -> api.getEventManager().unregister(listener));
			CompletableFuture<Void> shutdownTask = new CompletableFuture<>();
			api.addEventListener(new ListenerAdapter() {
				@Override
				public void onShutdown(@NotNull ShutdownEvent event) {
					shutdownTask.complete(null);
				}
			});
			api.cancelRequests();
			api.shutdownNow();
			main.api = null;
			api = null;
			try {
				shutdownTask.get(5, TimeUnit.SECONDS);
			} catch (TimeoutException | InterruptedException | ExecutionException e) {
				main.getLogger()
						.warning("JDA no esta cerrando... saltando proceso... Se recomienda reiniciar el servidor.");
			}
		}
	}

}
