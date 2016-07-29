// ISecurityCenter.aidl
package com.rtfsc.binderpool;

// Declare any non-default types here with import statements

interface ISecurityCenter {

    String encrypt(String content);


    String decrypy(String password);
}
