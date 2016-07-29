package com.rtfsc.binderpool;

import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		new Thread(new Runnable() {
			@Override
			public void run() {
				doWork();
			}
		}).start();
	}

	private void doWork() {
		BinderPool pool = BinderPool.getInstance(MainActivity.this);
		IBinder securityBinder = pool.queryBinder(BinderPool.BINDER_SECURITY_CODE);
		ISecurityCenter iSecurityCenter = ISecurityCenter.Stub.asInterface(securityBinder);
		try {
			String encrypt = iSecurityCenter.encrypt("hello world");
			System.out.println("encrypt = " + encrypt);
			String decrypt = iSecurityCenter.decrypy(encrypt);
			System.out.println("decrypt = " + decrypt);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
