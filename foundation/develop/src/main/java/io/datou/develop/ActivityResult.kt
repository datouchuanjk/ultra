package io.datou.develop

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.core.app.ActivityOptionsCompat
import java.io.File

fun ActivityResultLauncher<PickVisualMediaRequest>.launchImageOnly(
    options: ActivityOptionsCompat? = null
) = launch(
    PickVisualMediaRequest(PickVisualMedia.ImageOnly),
    options
)

fun ActivityResultLauncher<PickVisualMediaRequest>.launchVideoOnly(
    options: ActivityOptionsCompat? = null,
) = launch(
    PickVisualMediaRequest(PickVisualMedia.VideoOnly),
    options
)

fun ActivityResultLauncher<PickVisualMediaRequest>.launchImageAndVideo(
    options: ActivityOptionsCompat? = null,
) = launch(
    PickVisualMediaRequest(PickVisualMedia.ImageAndVideo),
    options
)

inline fun <reified T> ActivityResultLauncher<Array<T>>.launch(vararg input: T) {
    launch(input.toList().toTypedArray())
}

fun <T> ActivityResultLauncher<List<T>>.launch(vararg input: T) {
    launch(input.toList())
}

fun <T> ActivityResultLauncher<Uri>.launch(file: File) {
    launch(file.toSharedUri()!!)
}



