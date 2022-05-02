package match.main;

import lombok.extern.slf4j.Slf4j;
import match.server.Player;

import java.util.Comparator;
import java.util.TreeSet;

/**
 * 任务池相关方法
 * author: jxx
 * time: 2022/5/2 12:25 下午
 */
@Slf4j
public class MatchPool {
	TreeSet<Player> players = new TreeSet<>(Comparator.comparingInt(Player::getScore));

	public void add(Player player) {
		log.info("pool add player...");
		players.add(player);
	}

	public void remove(Player player) {
		log.info("pool remove player...");
		players.removeIf(p -> p.id == player.id);
	}

	public void match() {
		if (players.size() < 2) return;

		Player player1 = players.pollFirst();
		Player player2 = players.pollFirst();
		assert player1 != null;
		assert player2 != null;
		System.out.println("match " + player1.name + " and " + player2.name);
	}
}
