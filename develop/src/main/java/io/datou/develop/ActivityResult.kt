package io.datou.develop

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.core.app.ActivityOptionsCompat

fun ActivityResultLauncher<PickVisualMediaRequest>.launchImageOnly(
    options: ActivityOptionsCompat? = null
) = launch(
    PickVisualMediaRequest(
        mediaType = PickVisualMedia.ImageOnly
    ),
    options
)

fun ActivityResultLauncher<PickVisualMediaRequest>.launchVideoOnly(
    options: ActivityOptionsCompat? = null,
) = launch(
    PickVisualMediaRequest(
        mediaType = PickVisualMedia.VideoOnly
    ),
    options
)

fun ActivityResultLauncher<PickVisualMediaRequest>.launchImageAndVideo(
    options: ActivityOptionsCompat? = null,
) = launch(
    PickVisualMediaRequest(
        mediaType = PickVisualMedia.ImageAndVideo
    ),
    options
)

inline fun <I, O, T> ActivityResultContract<I, O>.map(
    crossinline transform: (O) -> T
): ActivityResultContract<I, T> {
    return object : ActivityResultContract<I, T>() {
        override fun createIntent(context: Context, input: I): Intent {
            return this@map.createIntent(
                context,
                input
            )
        }

        override fun parseResult(resultCode: Int, intent: Intent?): T {
            val originalResult = this@map.parseResult(
                resultCode,
                intent
            )
            return transform(originalResult)
        }

        override fun getSynchronousResult(
            context: Context,
            input: I
        ): SynchronousResult<T>? {
            return this@map.getSynchronousResult(context, input)?.let {
                SynchronousResult(transform(it.value))
            }
        }
    }
}

