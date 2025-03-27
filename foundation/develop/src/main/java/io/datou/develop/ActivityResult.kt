package io.datou.develop

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.core.app.ActivityOptionsCompat

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



