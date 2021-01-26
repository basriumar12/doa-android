package doa.ai.network

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import doa.ai.base.base_model.BaseResponse
import doa.ai.login.forgot.confirmPassword
import doa.ai.login.forgot.emailBody
import doa.ai.login.logins.Login
import doa.ai.login.logins.LoginResponse
import doa.ai.main.event.*
import doa.ai.main.event.Result
import doa.ai.main.home.HomeResponse
import doa.ai.main.notes.bplan.plan.PlanList
import doa.ai.main.notes.bplan.business.BusinessModel
import doa.ai.main.notes.bplan.business.BusinessSegmentResponse
import doa.ai.main.notes.bplan.finance.FinanceModel
import doa.ai.main.notes.bplan.finance.FinanceResponse
import doa.ai.main.notes.bplan.market.MarketModel
import doa.ai.main.notes.bplan.market.MarketResponse
import doa.ai.main.notes.bplan.plan.SummaryList
import doa.ai.main.notes.bplan.setup.PlanResponse
import doa.ai.main.notes.bplan.setup.PlanSectors
import doa.ai.main.notes.bplan.setup.PlanSubSectors
import doa.ai.main.notes.bplan.setup.SetupPlan
import doa.ai.main.notes.bplan.strategy.StrategyModel
import doa.ai.main.notes.bplan.strategy.StrategyResponse
import doa.ai.main.notes.bplan.summary.SummaryBody
import doa.ai.main.notes.bplan.summary.SummaryResponse
import doa.ai.main.notes.ideation.model.BodyIdea
import doa.ai.main.notes.ideation.model.BodyIdeaDelete
import doa.ai.main.notes.ideation.model.ResponseIdea
import doa.ai.model.*
import doa.ai.profile.edit.EditProfileBody
import doa.ai.profile.edit.EditProfileResponse
import doa.ai.profile.profiles.ProfileResponse
import doa.ai.register.*
import doa.ai.utils.Constans
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ServiceOther {

    @POST("account/rest-auth/register/")
    fun postAuth(@Body auth: AuthBody): Observable<AuthResponse>

    @POST("account/rest-auth/login/")
    fun postLogin(@Body login: Login): Observable<LoginResponse>

    @POST("account/rest-auth/password/reset/confirm/")
    fun postResetPassword(@Body login: confirmPassword): Observable<BaseResponse>


    @POST("account/rest-auth/login/")
    fun postLogin2(@Body login: Login): Call<LoginResponse>

    @POST("api/business_sample/")
    fun postBusinessSample()

    @POST("plan/")
    fun postPlan(@Header("Authorization") token: String, @Body plan: SetupPlan): Observable<PlanResponse>

    @GET("plan")
    fun getPlanList(@Header("Authorization") token: String
                    , @Query("page") page: Int
                    , @Query("search") search: String): Observable<PlanList>

    @GET("sector/")
    fun getSectorPlan(@Header("Authorization") token: String): Observable<PlanSectors>

    @GET("sector/")
    fun getSubSectorPlan(@Header("Authorization") token: String, @Query("id") id: Int): Observable<PlanSubSectors>

    @POST("segment/business/")
    fun posSegmentBusiness(@Header("Authorization") token: String, @Body business: BusinessModel): Observable<BusinessSegmentResponse>

    @POST("segment/market/")
    fun postSegmentMarket(@Header("Authorization") token: String, @Body market: MarketModel): Observable<MarketResponse>

    @POST("segment/strategy/")
    fun postSegmentStrategy(@Header("Authorization") token: String, @Body strategy: StrategyModel): Observable<StrategyResponse>

    @POST("segment/finance/")
    fun postSegmentFinance(@Header("Authorization") token: String, @Body finance: FinanceModel): Observable<FinanceResponse>

    @GET("currency/")
    fun getCurrency(@Header("Authorization") token: String
                    , @Query("page") page: Int
                    , @Query("search") search: String): Observable<Currency>

    @GET("account/profile/")
    fun getProfile(@Header("Authorization") token: String): Observable<ProfileResponse>

    @GET("home")
    fun getHome(@Header("Authorization") token: String): Observable<HomeResponse>

    @POST("event/program/")
    fun postEvent(@Header("Authorization") token: String, @Body event: EventModel): Observable<EventResponse>

    @POST("event/program/")
    fun postEventWithoutBplanDocument(@Header("Authorization") token: String, @Body event: EventModelWithoutBplanDocument): Observable<EventResponse>

    @POST("sync")
    fun postEventsync(@Body event: EventModelSync): Observable<String>


    @GET("program/")
    fun getCheckRegisterEvent(@Header("Authorization") token: String, @Query("id") id: Int): Observable<EventResponse>

    @GET("job/")
    fun getProfession(@Header("Authorization") token: String, @Query("search") search: String): Observable<ResponseProfession>


    @POST("account/profile/")
    fun postProfile(@Header("Authorization") token: String, @Body profile: EditProfileBody): Observable<EditProfileResponse>

    @POST("segment/summary/")
    fun postSummary(@Header("Authorization") token: String, @Body summary: SummaryBody): Observable<SummaryResponse>

    @GET("segment/summary/")
    fun getSummaryDetail(@Header("Authorization") token: String, @Query("id") id: Int): Observable<SummaryResponse>

    @GET("segment/summary/")
    fun getSummaryList(@Header("Authorization") token: String): Observable<SummaryList>

    @GET("address/country/")
    fun getProvince(@Header("Authorization") token: String, @Query("search") search: String): Observable<CountryModel>

    @GET("address/province/")
    fun getDistrict(@Header("Authorization") token: String, @Query("search") search: String): Observable<ProvinceModel>

    @GET("address/district/")
    fun getSubDistrict(@Header("Authorization") token: String, @Query("search") search: String): Observable<DistrictModel>


    @POST("idea/")
    fun setPostIdea(@Header("Authorization") token: String, @Body bodyIdea: BodyIdea): Observable<ResponseIdea>

    @POST("account/rest-auth/password/reset/")
    fun setPostResetPass(@Body bodyIdea: emailBody): Observable<BaseResponse>


    @POST("idea/")
    fun editPostIdea(@Header("Authorization") token: String, @Body bodyIdea: BodyIdea): Observable<ResponseIdea>

    @HTTP(method = "DELETE", path = "/idea/", hasBody = true)
    fun deletePostIdea(@Header("Authorization") token: String, @Body bodyIdea: BodyIdeaDelete): Observable<ResponseIdea>


    @GET("address/sub-district/")
    fun getPostalCode(@Header("Authorization") token: String, @Query("search") search: String): Observable<SubDistrictModel>

    object Create {
        fun service(): ServiceOther {
            val retrofit = Retrofit.Builder().baseUrl(Constans.Staging.DEVEL)
                    .client(Constans.OkHttpLoging.loggingInterceptor())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            return retrofit.create(ServiceOther::class.java)
        }
    }
    object CreateOther {
        fun service(): ServiceOther {
            val retrofit = Retrofit.Builder().baseUrl("https://us-central1-html-hero.cloudfunctions.net/")
                    .client(Constans.OkHttpLoging.loggingInterceptor())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            return retrofit.create(ServiceOther::class.java)
        }
    }
}