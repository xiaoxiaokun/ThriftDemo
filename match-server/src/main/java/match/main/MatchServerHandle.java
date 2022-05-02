package match.main;

import lombok.extern.slf4j.Slf4j;
import match.server.Match;
import match.server.Player;

/**
 * match service调用方法实现
 * author: jxx
 * time: 2022/4/26 8:24 上午
 */
@Slf4j
public class MatchServerHandle implements Match.Iface {
	@Override
	public int add(int id, String name, int score) {
		log.info("add...");

		MatchServer.Task task = new MatchServer.Task();
		task.player = new Player(id, name, score);
		task.opType = "add";
		synchronized (MatchServer.taskQueue) {
			MatchServer.taskQueue.add(task);
			MatchServer.taskQueue.notify();
		}
		return 0;
	}

	@Override
	public int remove(int id, String name) {
		log.info("remove...");

		MatchServer.Task task = new MatchServer.Task();
		task.player = new Player(id, name);
		task.opType = "remove";
		synchronized (MatchServer.taskQueue) {
			MatchServer.taskQueue.add(task);
			MatchServer.taskQueue.notify();
		}
		return 0;
	}
}
