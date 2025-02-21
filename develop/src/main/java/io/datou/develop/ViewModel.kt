package io.datou.develop

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

inline fun <reified T : ViewModel> appViewModels(): Lazy<T> {
    return lazy {
        ViewModelProvider(AppViewModelStoreOwner.INSTANCE)[T::class]
    }
}

@PublishedApi
internal class AppViewModelStoreOwner :
    ViewModelStoreOwner,
    LifecycleOwner {
    companion object {
        val INSTANCE = AppViewModelStoreOwner()
    }

    override val viewModelStore = ViewModelStore()
    override val lifecycle: Lifecycle get() = ProcessLifecycleOwner.get().lifecycle
}