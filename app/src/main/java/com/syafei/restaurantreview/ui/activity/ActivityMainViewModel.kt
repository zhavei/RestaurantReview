package com.syafei.restaurantreview.ui.activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.syafei.restaurantreview.data.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityMainViewModel : ViewModel() {

    private val _restaurant = MutableLiveData<Restaurant>()
    val restaurant: LiveData<Restaurant> = _restaurant

    private val _listReview = MutableLiveData<List<CustomerReviewsItem>>()
    val listReview: LiveData<List<CustomerReviewsItem>> = _listReview

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        findRestaurant()
    }

    private fun findRestaurant() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getRestaurant(RESTAURANT_ID)
        client.enqueue(object : Callback<RestourantRespone> {
            override fun onResponse(
                call: Call<RestourantRespone>,
                response: Response<RestourantRespone>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _restaurant.value = response.body()?.restaurant
                    _listReview.value = response.body()?.restaurant?.customerReviews
                } else {
                    Log.e(TAG, "onfailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RestourantRespone>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onfailure: ${t.message}")
            }

        })
    }

    fun postReview(review: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().postReview(RESTAURANT_ID, "ebek", review)
        client.enqueue(object : Callback<PostReviewRespone> {
            override fun onResponse(
                call: Call<PostReviewRespone>,
                response: Response<PostReviewRespone>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listReview.value = response.body()?.customerReviews
                } else {
                    Log.e(TAG, "onfailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PostReviewRespone>, t: Throwable) {
                Log.e(TAG, "onfailure: ${t.message.toString()}")
            }

        })
    }

    companion object {
        private const val TAG = "ActivityMainViewModel"
        private const val RESTAURANT_ID = "uewq1zg2zlskfw1e867"
    }

}