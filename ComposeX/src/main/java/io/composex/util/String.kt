package io.composex.util

import android.webkit.MimeTypeMap

val String.extension: String? get() = MimeTypeMap.getFileExtensionFromUrl(this)

val String.mimeType: String? get() = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)




