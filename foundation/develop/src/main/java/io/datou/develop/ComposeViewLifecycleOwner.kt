package io.datou.develop

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

class ComposeViewLifecycleOwner : LifecycleOwner, ViewModelStoreOwner,
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


    fun onCreate() {
        _savedStateRegistryController.performRestore(null)
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    fun onStart() {
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    fun onResume() {
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    fun onPause() {
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }

    fun onStop() {
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    fun onDestroy() {
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        _viewModelStore.clear()
    }

    fun attachToDecorView(decorView: View?) {
        decorView?.let {
            it.setViewTreeViewModelStoreOwner(this)
            it.setViewTreeLifecycleOwner(this)
            it.setViewTreeSavedStateRegistryOwner(this)
        } ?: return
    }
}

