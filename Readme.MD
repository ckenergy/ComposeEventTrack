## ComposeEventTrack compose 事件（页面切换和点击）埋点

[中文文档](Readme-CN.MD)

### how to use (last_version = "1.0.0")

#### 1、add dependencies
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

#### 2、set event listener
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
