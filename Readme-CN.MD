## ComposeEventTrack compose 事件（页面切换和点击）埋点

### 使用方式 (last_version = "1.0.0")

#### 1、添加依赖
root build.gradle
``` groovy
dependencies {
    classpath  "io.github.ckenergy:cet-gradle-plugin:$last_version"
}
```
app build.gradle
```groovy

plugins {
    id 'cet-plugin'
}

dependencies {
    implementation "io.github.ckenergy:cet-core:$last_version"
}
```

#### 2、设置事件监听
``` kotlin
AutoTrackHelper.setEventListener(object : ComposeEventListener{
    override fun onPageChange(route: String) {
        Log.d(TAG, "onPageChange:$route")
    }

    override fun onClick(event: String) {
        Log.d(TAG, "onClick:$event")
    }
})
```
[大概原理介绍](cet.md)
