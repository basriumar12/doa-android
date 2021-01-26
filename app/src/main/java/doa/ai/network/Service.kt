package doa.ai.network

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import doa.ai.base.base_model.BaseResponse
import doa.ai.fcm.BodyNotif
import doa.ai.login.forgot.confirmPassword
import doa.ai.login.forgot.emailBody
import doa.ai.login.logins.Login
import doa.ai.login.logins.LoginResponse
import doa.ai.main.event.*
import doa.ai.main.event.Result
import doa.ai.main.event.model.ResponseUploadImage
import doa.ai.main.home.HomeResponse
import doa.ai.main.notes.bplan.business.BusinessModel
import doa.ai.main.notes.bplan.business.BusinessSegmentResponse
import doa.ai.main.notes.bplan.business.edit.ResponseSubDistrict
import doa.ai.main.notes.bplan.finance.FinanceModel
import doa.ai.main.notes.bplan.finance.FinanceResponse
import doa.ai.main.notes.bplan.market.MarketModel
import doa.ai.main.notes.bplan.market.MarketResponse
import doa.ai.main.notes.bplan.market.MarketResponses
import doa.ai.main.notes.bplan.plan.*
import doa.ai.main.notes.bplan.setup.PlanResponse
import doa.ai.main.notes.bplan.setup.PlanSectors
import doa.ai.main.notes.bplan.setup.PlanSubSectors
import doa.ai.main.notes.bplan.setup.SetupPlan
import doa.ai.main.notes.bplan.strategy.StrategyModel
import doa.ai.main.notes.bplan.strategy.StrategyResponse
import doa.ai.main.notes.bplan.summary.SummaryBody
import doa.ai.main.notes.bplan.summary.SummaryResponse
import doa.ai.main.notes.ideation.model.*
import doa.ai.model.*
import doa.ai.profile.collab.BodyDeleteCollab
import doa.ai.profile.collab.BodySendCollab
import doa.ai.profile.collab.ModelCollab
import doa.ai.profile.collab.ModelCollabRequested
import doa.ai.profile.edit.EditProfileBody
import doa.ai.profile.edit.EditProfileResponse
import doa.ai.profile.profiles.ProfileResponse
import doa.ai.register.*
import doa.ai.utils.Constans
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Multipart


interface Service {

    @POST("account/rest-auth/register/")
    fun postAuth(@Body auth: AuthBody): Observable<AuthResponse>

    @POST("account/rest-auth/login/")
    fun postLogin(@Body login: Login): Observable<LoginResponse>

    @POST("account/rest-auth/password/reset/confirm/")
    fun postResetPassword(@Body login: confirmPassword): Observable<BaseResponse>

    @POST("account/rest-auth/check-availability/")
    fun postCheckUser(@Body login: CheckUserBody): Observable<BaseResponse>

    @POST("api/business_sample/")
    fun postBusinessSample()

    @POST("plan/")
    fun postPlan(@Header("Authorization") token: String, @Body plan: SetupPlan): Observable<PlanResponse>

    @PUT("plan/")
    fun postEditPlan(@Header("Authorization") token: String, @Body plan: BodyEditBplan): Observable<PlanResponse>

    @PUT("plan/change-status/")
    fun postEditPlans(@Header("Authorization") token: String, @Body plan: BodyEditPlans): Observable<PlanResponse>

    @PUT("plan/copy/")
    fun postCopyPlan(@Header("Authorization") token: String, @Body plan: BodyCopyPlan): Observable<PlanResponse>

    @POST("plan/pinned/")
    fun postPinnedPlan(@Header("Authorization") token: String, @Body plan: BodyPinnedPlan): Observable<PlanResponse>

    @POST("plan/pinned/")
    fun postPinnedPlans(@Header("Authorization") token: String, @Body plan: BodyPinnedPlans): Observable<PlanResponse>

    @HTTP(method = "DELETE", path = "plan/pinned/", hasBody = true)
    fun deletePinnedPlan(@Header("Authorization") token: String, @Body plan: BodyDelPinnedPlans): Observable<PlanResponse>

    @HTTP(method = "DELETE", path = "plan/", hasBody = true)
    fun deletePlan(@Header("Authorization") token: String, @Body plan: BodyDelPlan): Observable<PlanResponse>

    @GET("plan/")
    fun getPlanList(@Header("Authorization") token: String
                    , @Query("page") page: Int
                    , @Query("search") search: String
                    , @Query("include_pinned") include_pinned: String
                    , @Query("status") status: String

    ): Observable<PlanList>

    @GET("account/connection/suggestion/")
    fun getSuggestionCollab(@Header("Authorization") token: String
                    , @Query("page") page: Int
                    , @Query("search") search: String
                    //, @Query("sub_sector") sub_sector: String
                    , @Query("order_by") status: String

    ): Observable<ModelCollab>

    @GET("account/connection/suggestion/")
    fun getSuggestionCollabSearch(@Header("Authorization") token: String
                    , @Query("page") page: Int
                    , @Query("search") search: String
                    , @Query("sub_sector") sub_sector: String
                    , @Query("order_by") order: String

    ): Observable<ModelCollab>

    @GET("account/connection/")
    fun getListCollab(@Header("Authorization") token: String
                    , @Query("page") page: Int
                    , @Query("status") search: String
                    //, @Query("sub_sector") sub_sector: String
                    , @Query("search") status: String

    ): Observable<ModelCollabRequested>

    @POST("share/")
    fun postShare(@Header("Authorization") token: String, @Body plan: BodyShare): Observable<ModelCollab>

    @POST("account/connection/")
    fun postSendCollab(@Header("Authorization") token: String, @Body plan: BodySendCollab): Observable<ModelCollab>

    @POST("account/connection/")
    fun postSendApproveCollab(@Header("Authorization") token: String, @Body plan: BodySendCollab):
            Observable<ModelCollabRequested>

    @HTTP(method = "DELETE", path = "account/connection/", hasBody = true)
    fun postSendDeleteCollab(@Header("Authorization") token: String, @Body plan: BodyDeleteCollab):
            Observable<ModelCollabRequested>



    @GET("account/connection/")
    fun getListCollabRequested(@Header("Authorization") token: String
                    , @Query("page") page: Int
                    , @Query("status") search: String
                    //, @Query("sub_sector") sub_sector: String
                    , @Query("search") status: String

    ): Observable<ModelCollabRequested>

    @GET("plan/pinned")
    fun getPlanPinnedList(@Header("Authorization") token: String
                    , @Query("page") page: Int
                    , @Query("search") search: String

    ): Observable<PlanListPinned>

    @GET("sector/")
    fun getSectorPlan(@Header("Authorization") token: String): Observable<PlanSectors>

    @GET("sector/")
    fun getSubSectorPlan(@Header("Authorization") token: String, @Query("id") id: Int): Observable<PlanSubSectors>

    @POST("segment/business/")
    fun posSegmentBusiness(@Header("Authorization") token: String, @Body business: BusinessModel): Observable<BusinessSegmentResponse>

    @PUT("segment/business/")
    fun posSegmentEditBusiness(@Header("Authorization") token: String, @Body business: BusinessModel): Observable<BusinessSegmentResponse>


    @GET("segment/business/")
    fun getDetailSegmentBusiness(@Header("Authorization") token: String,
                                 @Query("id")id: Int): Observable<BusinessSegmentResponse>

    @POST("segment/market/")
    fun postSegmentMarket(@Header("Authorization") token: String, @Body market: MarketModel): Observable<MarketResponse>

    @PUT("segment/market/")
    fun postSegmentMarketEdit(@Header("Authorization") token: String, @Body market: MarketModel): Observable<MarketResponse>

    @GET("segment/market/")
    fun postDetailSegmentMarket(@Header("Authorization") token: String,
                                @Query ("id")id : Long):
            Observable<MarketResponse>



    @POST("segment/strategy/")
    fun postSegmentStrategy(@Header("Authorization") token: String, @Body strategy: StrategyModel): Observable<StrategyResponse>

    @PUT("segment/strategy/")
    fun postSegmentStrategyEdit(@Header("Authorization") token: String, @Body strategy: StrategyModel): Observable<StrategyResponse>

    @GET("segment/strategy/")
    fun getDetailSegmentStrategy(@Header("Authorization") token: String,
                                 @Query ("id")id : Int): Observable<StrategyResponse>

    @POST("segment/finance/")
    fun postSegmentFinance(@Header("Authorization") token: String, @Body finance: FinanceModel): Observable<FinanceResponse>

 @POST("segment/finance/")
    fun postSegmentFinanceEdit(@Header("Authorization") token: String, @Body finance: FinanceModel): Observable<FinanceResponse>

    @GET("segment/finance/")
    fun getDetailSegmentFinance(@Header("Authorization") token: String,
                           @Query ("id")id: Int): Observable<FinanceResponse>

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

    @POST("notification/mobile/")
    fun postNotif(@Header("Authorization") token: String, @Body body: BodyNotif): Call<EventResponse>

    @POST("event/program/")
    fun postEventWithoutBplanDocument(@Header("Authorization") token: String, @Body event: EventModelWithoutBplanDocument): Observable<EventResponse>

    @POST("event/program/")
    fun postEventWithoutBplan(@Header("Authorization") token: String, @Body event: EventModelWithoutBplan): Observable<EventResponse>

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
    fun getSummaryList(@Header("Authorization") token: String, @Query("page")page : Int): Observable<SummaryList>

    @GET("address/country/")
    fun getProvince(@Header("Authorization") token: String, @Query("search") search: String): Observable<CountryModel>

    @GET("address/province/")
    fun getDistrict(@Header("Authorization") token: String, @Query("search") search: String): Observable<ProvinceModel>

    @GET("address/district/")
    fun getSubDistrict(@Header("Authorization") token: String, @Query("search") search: String): Observable<DistrictModel>


    @POST("idea/")
    fun setPostIdea(@Header("Authorization") token: String, @Body bodyIdea: BodyIdea): Observable<ResponseIdea>

    @PUT("idea/change-status/")
    fun setPostUpdateStatusIdea(@Header("Authorization") token: String, @Body bodyIdea: BodyIdeaUpdateStatus): Observable<ResponseIdea>


    @PUT("idea/copy/")
    fun setPostCopyIdea(@Header("Authorization") token: String, @Body bodyIdea: BodyCopyIdea): Observable<ResponseIdea>


    @HTTP(method = "DELETE", path = "/idea/", hasBody = true)
    fun setPostDeleteIdea(@Header("Authorization") token: String, @Body bodyIdea: BodyCopyIdea): Observable<ResponseIdea>


    @GET("idea/archived/")
    fun getIdeaArchived(@Header("Authorization") token: String, @Query("page") page: Int,
                @Query("search") search: String): Observable<ResponseGetIdea>

    @GET("idea/trashed/")
    fun getIdeaTrashed(@Header("Authorization") token: String, @Query("page") page: Int,
                @Query("search") search: String): Observable<ResponseGetIdea>


    @POST("idea/pinned/")
    fun setPostIdeationPinned(@Header("Authorization") token: String, @Body bodyIdeaPinned: BodyIdeaPinned): Observable<ResponseIdea>


    @GET("idea/")
    fun getIdea(@Header("Authorization") token: String, @Query("page") page: Int,

                @Query("status") status : String,
                @Query("search") search: String): Observable<ResponseGetIdea>

    @GET("idea/")
    fun getIdeaPinned(@Header("Authorization") token: String, @Query("page") page: Int,
                      @Query("search") search: String): Observable<ResponseGetIdea>


    @POST("account/rest-auth/password/reset/")
    fun setPostResetPass(@Body bodyIdea: emailBody): Observable<BaseResponse>


    @POST("idea/")
    fun editPostIdea(@Header("Authorization") token: String, @Body bodyIdea: BodyIdea): Observable<ResponseIdea>

    @HTTP(method = "DELETE", path = "/idea/", hasBody = true)
    fun deletePostIdea(@Header("Authorization") token: String, @Body bodyIdea: BodyIdeaDelete): Observable<ResponseIdea>

    @HTTP(method = "DELETE", path = "idea/", hasBody = true)
    fun deleteFullPostIdea(@Header("Authorization") token: String, @Body bodyIdea: BodyIdeaDeleteFull): Observable<ResponseIdea>


    @GET("address/sub-district/")
    fun getPostalCode(@Header("Authorization") token: String, @Query("search") search: String): Observable<SubDistrictModel>

    @GET("address/sub-district/")
    fun getDetailSubDistrict(@Header("Authorization") token: String,
                             @Query("id") id: Int,
                             @Query("include_parent") include_parent: Boolean

                             ): Observable<ResponseSubDistrict>


    //    @Headers("Content-Type: multipart/form-data")
    @Multipart
    @POST("uploader/file/")
    fun postUploadFile(
            @Header("Authorization-Uploader") token: String,
            @Part("folder") folder: RequestBody,
            @Part file: MultipartBody.Part?


    ): Call<ResponseUploadImage>@Multipart

    @POST("uploader/image/")
    fun postUploadImage(
            @Header("Authorization-Uploader") token: String,
            @Part("folder") folder: RequestBody,
            @Part file: MultipartBody.Part?


    ): Call<ResponseUploadImage>

    object Create {
        fun service(): Service {
            val retrofit = Retrofit.Builder().baseUrl(Constans.Staging.DEVEL)
                    .client(Constans.OkHttpLoging.loggingInterceptor())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            return retrofit.create(Service::class.java)
        }
    }

    object CreateOther {
        fun service(): Service {
            val retrofit = Retrofit.Builder().baseUrl("https://us-central1-html-hero.cloudfunctions.net/")
                    .client(Constans.OkHttpLoging.loggingInterceptor())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            return retrofit.create(Service::class.java)
        }
    }

    object CreateOtherForUploadImage {
        fun service(): Service {
            val retrofit = Retrofit.Builder().baseUrl(Constans.Staging.DEVEL)
                    .client(Constans.OkHttpLogings.loggingInterceptor())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            return retrofit.create(Service::class.java)
        }
    }
}