// IDemandManager.aidl
package org.lynxz.aidldemo;
import org.lynxz.aidldemo.MessageBean;
import org.lynxz.aidldemo.IListener;

interface IDemandManager {
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