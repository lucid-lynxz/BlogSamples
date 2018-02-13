package org.lynxz.aidldemo

import android.os.Parcel
import android.os.Parcelable


/**
 * Created by lynxz on 12/02/2018.
 * 博客: https://juejin.im/user/5812c2b0570c3500605a15ff
 */
class MessageBean(var content: String? = null,
                  var level: Int = 0) : Parcelable {

    constructor(parcel: Parcel) : this() {
        content = parcel.readString()
        level = parcel.readInt()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(content)
        dest?.writeInt(level)
    }

    //如果需要支持定向tag为out,inout，就要重写该方法
    fun readFromParcel(dest: Parcel) {
        //注意，此处的读值顺序应当是和writeToParcel()方法中一致的
        content = dest.readString()
        level = dest.readInt()
    }


    override fun describeContents() = 0

    override fun toString(): String {
        return "MessageBean(content=$content, level=$level)"
    }

    companion object CREATOR : Parcelable.Creator<MessageBean> {
        override fun createFromParcel(parcel: Parcel): MessageBean {
            return MessageBean(parcel)
        }

        override fun newArray(size: Int): Array<MessageBean?> {
            return arrayOfNulls(size)
        }
    }
}