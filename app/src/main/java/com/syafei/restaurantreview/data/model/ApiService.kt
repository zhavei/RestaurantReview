package com.syafei.restaurantreview.data.model

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("detail/{id}")

    fun getRestaurant(
        @Path("id") id: String
    ): Call<RestourantRespone>


    @FormUrlEncoded
    @Headers("Authorization: token 12345")
    @POST("/review")
    fun postReview(
        @Field("id") id: String,
        @Field("name") name: String,
        @Field("review") review: String
    ): Call<PostReviewRespone>
}