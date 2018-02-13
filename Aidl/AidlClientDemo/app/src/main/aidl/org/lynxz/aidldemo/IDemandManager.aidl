// IDemandManager.aidl
package org.lynxz.aidldemo;
import org.lynxz.aidldemo.MessageBean;
import org.lynxz.aidldemo.IListener;

// Declare any non-default types here with import statements

interface IDemandManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);

    MessageBean getDemand();
    // in: client -> server
    void setDemandIn(in MessageBean bean);

    // out: server -> client
    // out/inout 都需要MessageBean重写readFromParcel()方法
    void setDemandOut(out MessageBean bean);

    // inout: client <-> server
    void setDemandInOut(inout MessageBean bean);

    // 添加信息传递监听器
    void registerListener(IListener listener);
    void unregisterListener(IListener listener);

}
