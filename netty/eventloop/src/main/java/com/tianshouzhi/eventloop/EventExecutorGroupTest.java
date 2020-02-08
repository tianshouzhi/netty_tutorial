package com.tianshouzhi.eventloop;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by tianshouzhi on 2018/9/9.
 */
public class EventExecutorGroupTest {
	@Test
	public void test() throws Exception {
		EventExecutorGroup eventExecutorGroup = new DefaultEventLoopGroup(10);
		System.out.println("isShuttingDown:" + eventExecutorGroup.isShuttingDown());
		System.out.println("isShutdown:" + eventExecutorGroup.isShutdown());
		System.out.println("isTerminated:" + eventExecutorGroup.isTerminated());
		Future<Long> future = eventExecutorGroup.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				Long start = System.currentTimeMillis();
				TimeUnit.SECONDS.sleep(2); // do some task
				Long end = System.currentTimeMillis();
				return end - start;
			}
		});
		Long elapse = future.get();
		System.out.println(elapse);

	}

	@Test
	public void testShutdownGraceFully() throws Exception {
		EventExecutorGroup eventExecutorGroup = new DefaultEventLoopGroup(10);
		System.out.println("isShuttingDown:" + eventExecutorGroup.isShuttingDown());
		System.out.println("isShutdown:" + eventExecutorGroup.isShutdown());
		System.out.println("isTerminated:" + eventExecutorGroup.isTerminated());
		Future<Long> future = eventExecutorGroup.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				Long start = System.currentTimeMillis();
				TimeUnit.SECONDS.sleep(2); // do some task
				Long end = System.currentTimeMillis();
				return end - start;
			}
		});
		eventExecutorGroup.shutdownGracefully();
		Long elapse = future.get();
		System.out.println(elapse);
	}

	//不符合预期
	@Test
	public void testShutdownTime() throws Exception {
		EventExecutorGroup eventExecutorGroup = new DefaultEventLoopGroup(10);
		System.out.println("isShuttingDown:" + eventExecutorGroup.isShuttingDown());
		System.out.println("isShutdown:" + eventExecutorGroup.isShutdown());
		System.out.println("isTerminated:" + eventExecutorGroup.isTerminated());
		Future<Long> future = eventExecutorGroup.submit(new Callable<Long>() {
			@Override
			public Long call() throws Exception {
				Long start = System.currentTimeMillis();
				TimeUnit.SECONDS.sleep(5); // do some task
				Long end = System.currentTimeMillis();
				return end - start;
			}
		});
		eventExecutorGroup.shutdownGracefully(0, 1, TimeUnit.SECONDS);
                Long elapse = future.get();
		System.out.println(elapse);
	}
    @Test
	public void testExecutor(){
        EventExecutorGroup eventExecutorGroup = new DefaultEventLoopGroup(10);
        EventExecutor executor = eventExecutorGroup.next();
        Promise<Object> objectPromise = executor.newPromise();
    }
}
