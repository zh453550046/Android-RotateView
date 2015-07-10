# Android-RotateView
旋转组件,包含圆形和六边形

使用时调用CircleMenuView.setMenuResource(int[] drawable, int[] titles, int[] yin, int type) 
drawble为资源图片数组,titles为标题,yin为内容,type决定旋转图形的类型分别可设置为CircleMenuView.CIRCLE圆形,
CircleMenuView.HEXAGON六边形。
点击事件设置setOnMenuClickListener()设定回调
