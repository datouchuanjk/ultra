package com.module.basic.http;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
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
public final class RetrofitModule_ProvideFactory implements Factory<Retrofit> {
  @Override
  public Retrofit get() {
    return provide();
  }

  public static RetrofitModule_ProvideFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static Retrofit provide() {
    return Preconditions.checkNotNullFromProvides(RetrofitModule.INSTANCE.provide());
  }

  private static final class InstanceHolder {
    private static final RetrofitModule_ProvideFactory INSTANCE = new RetrofitModule_ProvideFactory();
  }
}
