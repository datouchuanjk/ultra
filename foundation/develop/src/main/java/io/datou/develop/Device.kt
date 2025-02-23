package io.datou.develop

import android.os.Build
import kotlin.text.lowercase

val isXiaomi get() = Build.MANUFACTURER.lowercase() == "xiaomi"

val isHuawei get() = Build.MANUFACTURER.lowercase() == "huawei"

val isOppo get() = Build.MANUFACTURER.lowercase() == "oppo"

val isViVo get() = Build.MANUFACTURER.lowercase() == "vivo"

val isGoogle get() = Build.MANUFACTURER.lowercase() == "google"

val isSamsung get() = Build.MANUFACTURER.lowercase() == "samsung"