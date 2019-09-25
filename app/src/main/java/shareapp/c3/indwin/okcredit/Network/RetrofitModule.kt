package shareapp.c3.indwin.okcredit.Network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import shareapp.c3.indwin.okcredit.Models.NewsDataModel

interface RetrofitModule {
    companion object {
        fun create(): RetrofitModule {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    GsonConverterFactory.create())
                .baseUrl("https://api.nytimes.com/svc/")
                .build()

            return retrofit.create(RetrofitModule::class.java)
        }
    }
    //?api-key=xI4AA4gcMj9JyFlyQn2dSAj689PGjKjA
    @GET("topstories/v2/{section}.json")
    fun getNewsData(@Path("section") section:String , @Query("api-key") apiKey:String):io.reactivex.Observable<NewsDataModel>
}