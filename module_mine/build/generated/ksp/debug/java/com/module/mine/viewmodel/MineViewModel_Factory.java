package com.module.mine.viewmodel;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class MineViewModel_Factory implements Factory<MineViewModel> {
  @Override
  public MineViewModel get() {
    return newInstance();
  }

  public static MineViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MineViewModel newInstance() {
    return new MineViewModel();
  }

  private static final class InstanceHolder {
    private static final MineViewModel_Factory INSTANCE = new MineViewModel_Factory();
  }
}
