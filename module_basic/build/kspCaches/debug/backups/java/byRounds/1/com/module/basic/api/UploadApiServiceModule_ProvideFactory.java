package com.module.basic.api;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
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
public final class UploadApiServiceModule_ProvideFactory implements Factory<UploadApiService> {
  private final Provider<Retrofit> retrofitProvider;

  public UploadApiServiceModule_ProvideFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public UploadApiService get() {
    return provide(retrofitProvider.get());
  }

  public static UploadApiServiceModule_ProvideFactory create(Provider<Retrofit> retrofitProvider) {
    return new UploadApiServiceModule_ProvideFactory(retrofitProvider);
  }

  public static UploadApiService provide(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(UploadApiServiceModule.INSTANCE.provide(retrofit));
  }
}
