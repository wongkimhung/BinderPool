package com.rtfsc.binderpool;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.rtfsc.binderpool.binders.ComputerImpl;
import com.rtfsc.binderpool.binders.SecurityCenterImpl;
import com.rtfsc.binderpool.services.BinderPoolService;

import java.util.concurrent.CountDownLatch;

/**
 * Created by wongkimhung on 2016/7/29.
 */
public class BinderPool {

	public static final int BINDER_NONE_CODE = -1;
	public static final int BINDER_SECURITY_CODE = 0;
	public static final int BINDER_COMPUTER_CODE = 1;

	private Context mContext;
	private IBinderPool mBinderPool;

	private static volatile BinderPool mInstance;
	private CountDownLatch mConnectBinderPoolCountDownLatch;


	public BinderPool(Context context) {
		mContext = context.getApplicationContext();
		connectBinderPoolService();
	}

	public static BinderPool getInstance(Context context) {
		if (mInstance == null) {
			synchronized (BinderPool.class) {
				if (mInstance == null) {
					mInstance = new BinderPool(context);
				}
			}
		}
		return mInstance;
	}

	private synchronized void connectBinderPoolService() {
		//  将bindService操作从异步转换为同步，所以要不能在主线程中执行
		mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
		Intent intent = new Intent(mContext, BinderPoolService.class);
		mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
		try {
			mConnectBinderPoolCountDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("BinderPool.connectBinderPoolService");
	}

	public IBinder queryBinder(int binderCode) {
		IBinder binder = null;
		try {
			if (mBinderPool != null) {
				binder = mBinderPool.queryBinder(binderCode);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return binder;
	}

	public static class BinderPoolImpl extends IBinderPool.Stub {

		@Override
		public IBinder queryBinder(int binderCode) throws RemoteException {
			IBinder binder = null;
			switch (binderCode) {
				case BINDER_COMPUTER_CODE:
					binder = new ComputerImpl();
					break;
				case BINDER_SECURITY_CODE:
					binder = new SecurityCenterImpl();
					break;
				default:
					break;
			}
			return binder;
		}
	}

	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBinderPool = IBinderPool.Stub.asInterface(service);
			try {
				mBinderPool.asBinder().linkToDeath(mDeathRecipient, 0);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			//  释放锁
			mConnectBinderPoolCountDownLatch.countDown();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}
	};

	private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
		@Override
		public void binderDied() {
			mBinderPool.asBinder().unlinkToDeath(mDeathRecipient, 0);
			mBinderPool = null;
			connectBinderPoolService();
		}
	};
}
