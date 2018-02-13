
package org.lynxz.aidldemo;

import org.lynxz.aidldemo.MessageBean;

// 创建监听器, 用于客户端接收消息
interface IListener {
   void onReceiveMessage(in MessageBean bean);
}
