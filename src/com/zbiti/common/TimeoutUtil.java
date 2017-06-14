package com.zbiti.common;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TimeoutUtil {

	public static Object execute(Callable<Object> task, long waitTime)
			throws Exception {
		ExecutorService service = Executors.newSingleThreadExecutor();
		Future<Object> taskFuture = service.submit(task);
		try {
			return taskFuture.get(waitTime * 1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			taskFuture.cancel(true);
			throw e;
		} catch (ExecutionException e) {
			taskFuture.cancel(true);
			throw e;
		} catch (TimeoutException e) {
			taskFuture.cancel(true);
			throw new Exception("超时 [" + waitTime + "s]！");
		} finally {
			service.shutdown();
		}
	}

}
