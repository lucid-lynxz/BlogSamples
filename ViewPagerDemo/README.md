用于记录测试碰到的viewpager相关问题

## 权限测试
入口: 主页面 - `权限测试` 按钮
相关页面: `PermissionDemoActivity`

测试方法:
安装运行后,允许申请的权限,然后切换到手机设置页面,禁止任一权限,通过最近程序列表进入应用,查看日志,会发现fragment的hashCode()值不一致
类似:
```shell
// 正常运行
D/permissionTest: afterCreate(PermissionDemoActivity.kt:51) 124426192 -- 226767817 // 在Activity中通过 new FragmentXXX() 的方式生成的对象hashCode()
......
D/permissionTest: onAttach(BaseFragment.kt:21) FragmentOne 124426192 // 在 fragment 的生命周期内打印的当前对象hahsCode()
D/permissionTest: onCreate(BaseFragment.kt:26) FragmentOne 124426192
......
D/permissionTest: onViewCreated(BaseFragment.kt:40) FragmentOne 124426192
D/permissionTest: onActivityCreated(BaseFragment.kt:35) FragmentOne 124426192
......
D/permissionTest: setPrimaryItem(MyFragmentStatePagerAdapter.kt:20) setPrimaryItem 0 124426192

// app运行过程中,通过系统设置页面,手动禁用权限后返回app
D/permissionTest: afterCreate(PermissionDemoActivity.kt:51) 65796356 -- 242249453 // 在Activity中通过 new FragmentXXX() 的方式生成的对象hashCode()
D/permissionTest: onViewCreated(BaseFragment.kt:40) FragmentOne 8671271 // 在 fragment 的生命周期内打印的当前对象hahsCode()
D/permissionTest: onActivityCreated(BaseFragment.kt:35) FragmentOne 8671271
......
D/permissionTest: onResume(PermissionDemoActivity.kt:56) 65796356 false V.S. 65796356 false
D/permissionTest: setPrimaryItem(MyFragmentStatePagerAdapter.kt:20) setPrimaryItem 0 8671271 //  通过 FragmentStatePagerAdapter 的 setPrimaryItem 方法打印的当前页面的hashCode() 
```

可以发现, 在 `Activity` 中 `new` 出来的 `Fragment` 可能并不是实际显示在 `ViewPager` 中的 `Fragment`, 甚至其所对应的 `Fragment` 都没有真正被创建出来;
解决方案: 重写 `FragmentStatePagerAdapter` 的 `fun setPrimaryItem(container: ViewGroup?, position: Int, obj: Any?)` 来更新 `Fragment` 数据列表来获得实际显示的 `Fragment` 对象

```kotlin
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup
import org.lynxz.viewpagerdemo.Logger
import org.lynxz.viewpagerdemo.base.BaseFragment

class MyFragmentStatePagerAdapter(fm: FragmentManager, var data: ArrayList<BaseFragment> = arrayListOf()) : FragmentStatePagerAdapter(fm) {

    var currentFragment: Fragment? = null

    override fun setPrimaryItem(container: ViewGroup?, position: Int, obj: Any?) {
        super.setPrimaryItem(container, position, obj)
        currentFragment = obj as Fragment?
        if (obj is BaseFragment) {
            if (getItem(position) !== obj) {
                data[position] = obj
            }
        }
    }

    override fun getCount() = data.size
    override fun getItem(position: Int) = data[position]
}
```
