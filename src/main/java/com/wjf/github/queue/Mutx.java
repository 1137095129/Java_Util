package com.wjf.github.queue;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Mutx implements Lock, Serializable {

	private static final long serialVersionUID = 3910938111960230298L;

	private final Sync sync;

	public Mutx() {
		sync = new NonFair();
	}

	public Mutx(boolean flag) {
		sync = flag ? new Fair() : new NonFair();
	}

	private Mutx(Sync sync) {
		this.sync = sync;
	}

	abstract static class Sync extends AbstractQueuedSynchronizer {
		private static final long serialVersionUID = 1332786783938188682L;

		abstract void lock();

		final boolean nonFairTryAcquire(int arg) {
			Thread current = Thread.currentThread();
			int state = getState();
			if (state == 0) {
				if (compareAndSetState(state, arg)) {
					setExclusiveOwnerThread(current);
					return true;
				}
			} else if (getExclusiveOwnerThread() == current) {
				int tmp = state + arg;
				setState(tmp);
				return true;
			}
			return false;
		}

		private final ConditionObject newCondition() {
			return new ConditionObject();
		}

		@Override
		protected boolean tryRelease(int arg) {
			Thread current = Thread.currentThread();
			int state = getState();
			if (getExclusiveOwnerThread() != current) {
				throw new IllegalMonitorStateException();
			}
			boolean flag = false;
			int i = state - arg;

			if (i == 0) {
				flag = true;
				setExclusiveOwnerThread(null);
			}
			setState(i);
			return flag;
		}
	}

	static class NonFair extends Sync {
		private static final long serialVersionUID = 4937078901181381176L;

		@Override
		void lock() {
			if (compareAndSetState(0, 1)) {
				setExclusiveOwnerThread(Thread.currentThread());
			} else {
				acquire(1);
			}
		}

		@Override
		protected boolean tryAcquire(int arg) {
			return nonFairTryAcquire(arg);
		}
	}

	static class Fair extends Sync {

		private static final long serialVersionUID = 1021811340467318968L;

		@Override
		void lock() {
			acquire(1);
		}

		/**
		 * 尝试一次获取资源的操作
		 *
		 * @param arg
		 * @return
		 */
		@Override
		protected boolean tryAcquire(int arg) {
			Thread current = Thread.currentThread();
			int state = getState();
			if (state == 0) {
				if (!hasQueuedPredecessors() && compareAndSetState(0, arg)) {
					setExclusiveOwnerThread(current);
					return true;
				}
			} else if (getExclusiveOwnerThread() == current) {
				int tmp = state + arg;
				setState(tmp);
				return true;
			}
			return false;
		}
	}

	@Override
	public void lock() {
		sync.lock();
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		sync.acquireInterruptibly(1);
	}

	@Override
	public boolean tryLock() {
		return sync.nonFairTryAcquire(1);
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return sync.tryAcquireNanos(1, unit.toNanos(time));
	}

	@Override
	public void unlock() {
		sync.release(1);
	}

	@Override
	public Condition newCondition() {
		return sync.newCondition();
	}
}
