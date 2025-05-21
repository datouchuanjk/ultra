package io.datou.develop

import android.view.View
import androidx.core.view.doOnAttach
import androidx.core.view.doOnDetach
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

fun View.bindViewTreeLifecycle() {
    if (findViewTreeLifecycleOwner() == null &&
        findViewTreeViewModelStoreOwner() == null &&
        findViewTreeSavedStateRegistryOwner() == null
    ) {
        val owner = ViewTreeLifecycleDelegate(this)
        doOnAttach {
            owner.attach()
        }
        doOnDetach {
            owner.detach()
        }
    }
}

internal class ViewTreeLifecycleDelegate(val view: View) : LifecycleOwner,
    ViewModelStoreOwner,
    SavedStateRegistryOwner {

    private val _lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
    private val _savedStateRegistryController = SavedStateRegistryController.create(this)
    private val _viewModelStore = ViewModelStore()
    override val lifecycle: Lifecycle
        get() = _lifecycleRegistry
    override val savedStateRegistry: SavedStateRegistry
        get() = _savedStateRegistryController.savedStateRegistry
    override val viewModelStore: ViewModelStore
        get() = _viewModelStore

    fun attach() {
        _savedStateRegistryController.performRestore(null)
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        view.setViewTreeViewModelStoreOwner(this)
        view.setViewTreeLifecycleOwner(this)
        view.setViewTreeSavedStateRegistryOwner(this)
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    fun detach() {
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        _viewModelStore.clear()
        view.setViewTreeViewModelStoreOwner(null)
        view.setViewTreeLifecycleOwner(null)
        view.setViewTreeSavedStateRegistryOwner(null)
    }
}