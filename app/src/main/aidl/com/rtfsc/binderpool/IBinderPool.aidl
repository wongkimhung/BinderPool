// IBinderPool.aidl
package com.rtfsc.binderpool;

// Declare any non-default types here with import statements

interface IBinderPool {

    IBinder queryBinder(int binderCode);
}
