package doa.ai.utils

import doa.ai.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class Constans {

    object Staging {
        const val DEVEL : String = BuildConfig.BASE_URL //"https://apidev.doain.ai/api/"
    }

    object OkHttpLoging{
        fun loggingInterceptor(): OkHttpClient {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            return OkHttpClient().newBuilder()
                    .addInterceptor(getInterceptor())
                    .addInterceptor(loggingInterceptor)
                    .build()
        }

        private fun getInterceptor(): Interceptor {
            return Interceptor { chain ->
                val request = chain.request()
                //val builder = request.newBuilder().addHeader("Content-Type","application/json")
                val builder = request.newBuilder().addHeader("Accept-Language","id")
                chain.proceed(builder.build())
            }
        }
    }
    object OkHttpLogings{
        fun loggingInterceptor(): OkHttpClient {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            return OkHttpClient().newBuilder()
                    .addInterceptor(getInterceptor())
                    .addInterceptor(loggingInterceptor)
                    .build()
        }

        private fun getInterceptor(): Interceptor {
            return Interceptor { chain ->
                val request = chain.request()
                val builder = request.newBuilder().addHeader("Content-Type","multipart/form-data")
                //val builder = request.newBuilder().addHeader("Accept-Language","id")
                chain.proceed(builder.build())
            }
        }
    }



}