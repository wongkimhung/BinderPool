package com.rtfsc.binderpool.binders;

import android.os.RemoteException;

import com.rtfsc.binderpool.ISecurityCenter;

/**
 * Created by wongkimhung on 2016/7/29.
 */
public class SecurityCenterImpl extends ISecurityCenter.Stub {
	public static final char SECRET_CODE = '^';

	@Override
	public String encrypt(String content) throws RemoteException {
		char[] chars = content.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			chars[i] ^= SECRET_CODE;
		}
		return new String(chars);
	}

	@Override
	public String decrypy(String password) throws RemoteException {
		return encrypt(password);
	}
}
