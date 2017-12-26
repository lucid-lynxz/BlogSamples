用于记录测试碰到的viewpager相关问题

## 权限测试
入口: 主页面 - `权限测试` 按钮
相关页面: `PermissionDemoActivity`

测试方法:
安装运行后,允许申请的权限,然后切换到手机设置页面,禁止任一权限,通过最近程序列表进入应用,查看日志,会发现fragment的hashCode()值不一致
类似:
```shell
D/permissionTest: afterCreate(PermissionDemoActivity.kt:45) 232348461 -- 18987106  // 在Activity中通过 new FragmentXXX() 的方式生成的对象hashCode()
D/permissionTest: onViewCreated(BaseFragment.kt:25) FragmentOne 40302766 // 在 fragment 的 onViewCreated() 生命周期内打印的当前对象hahsCode()
D/permissionTest: onViewCreated(BaseFragment.kt:25) FragmentTwo 106045384
```
