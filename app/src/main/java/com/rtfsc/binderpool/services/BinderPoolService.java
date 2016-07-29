package com.rtfsc.binderpool.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.rtfsc.binderpool.BinderPool;

/**
 * Created by wongkimhung on 2016/7/29.
 */
public class BinderPoolService extends Service {

	private Binder mBinderPool = new BinderPool.BinderPoolImpl();

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("BinderPoolService.onBind");
		return mBinderPool;
	}
}
