package match.main;

import lombok.extern.slf4j.Slf4j;
import match.server.Match;
import match.server.Player;
import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 服务器任务创建
 * author: jxx
 * time: 2022/4/26 8:25 上午
 */
@Slf4j
public class MatchServer {
	private static MatchServerHandle handler;
	private static Match.Processor<MatchServerHandle> processor;
	private static MatchPool pool;

	static class Task {
		Player player;
		String opType;
	}

	static final Queue<Task> taskQueue = new ConcurrentLinkedQueue<>();

	public static void main(String[] args) throws TException {
		try {
			handler = new MatchServerHandle();
			processor = new Match.Processor<>(handler);
			pool = new MatchPool();

			TServerTransport serverTransport = new TServerSocket(9090);
			TServer server = new TSimpleServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
			log.info("Match Server Starting...");

			Runnable createTask = MatchServer::createTask;
			new Thread(createTask, "crtTaskTread").start();

			server.serve();

		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	/**
	 * 消费任务，进行添加、删除、匹配用户等操作
	 *
	 */
	private static void createTask() {
		while (true) {
			synchronized (taskQueue) {
				if (taskQueue.isEmpty()) {
					try {
						taskQueue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
						return;
					}
				} else {
					// 创建add任务或remove任务
					Task t = taskQueue.poll();
					if ("add".equals(t.opType)) {
						pool.add(t.player);
					} else if ("remove".equals(t.opType)) {
						pool.remove(t.player);
					}
				}
				pool.match();
			}
		}
	}
}
