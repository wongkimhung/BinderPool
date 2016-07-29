package com.rtfsc.binderpool.binders;

import android.os.RemoteException;

import com.rtfsc.binderpool.IComputer;

/**
 * Created by wongkimhung on 2016/7/29.
 */
public class ComputerImpl extends IComputer.Stub {
	@Override
	public int add(int a, int b) throws RemoteException {
		return a + b;
	}
}
