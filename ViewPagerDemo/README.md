用于记录测试碰到的viewpager相关问题

## 权限测试
入口: 主页面 - `权限测试` 按钮
相关页面: `PermissionDemoActivity`

测试方法:
安装运行后,允许申请的权限,然后切换到手机设置页面,禁止任一权限,通过最近程序列表进入应用,查看日志,会发现fragment的hashCode()值不一致
类似:
```shell
// 正常运行
D/permissionTest: afterCreate(PermissionDemoActivity.kt:58) 64481327 -- 252075580 // 在Activity中通过 new FragmentXXX() 的方式生成的对象hashCode()
D/permissionTest: onAttach(BaseFragment.kt:22) FragmentOne 64481327 // 在 fragment 的生命周期内打印的当前对象hahsCode()
D/permissionTest: onCreate(BaseFragment.kt:27) FragmentOne 64481327
D/permissionTest: onViewCreated(BaseFragment.kt:41) FragmentOne 64481327
D/permissionTest: onActivityCreated(BaseFragment.kt:36) FragmentOne 64481327

// 保持程序运行,切换到手机设置界面,关闭权限后,通过最近程序列表进入app
// 发现fragment的生命周期 onAttach() onCreate() 没有被触发,hashCode()值也不通
D/permissionTest: afterCreate(PermissionDemoActivity.kt:58) 18987106 -- 257265139
D/permissionTest: onViewCreated(BaseFragment.kt:41) FragmentOne 92290738 
D/permissionTest: onActivityCreated(BaseFragment.kt:36) FragmentOne 92290738
```
