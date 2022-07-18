package com.syafei.restaurantreview.ui.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.syafei.restaurantreview.data.model.*
import com.syafei.restaurantreview.databinding.ActivityMainBinding
import com.syafei.restaurantreview.ui.adapter.ReviewAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        //region ViewModel
        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            ActivityMainViewModel::class.java
        )
        mainViewModel.restaurant.observe(this) { restaurant ->
            setRestourantData(restaurant)
        }

        mainViewModel.listReview.observe(this) { listReview ->
            setReviewData(listReview)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        //endregion

        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)

        findRestourant()

        /*binding.btnSend.setOnClickListener { view ->
            postReview(binding.edReview.text.toString())
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }*/

        binding.btnSend.setOnClickListener { view ->
            mainViewModel.postReview(binding.edReview.text.toString())
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

        }

    }

    private fun postReview(review: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().postReview(RESTAURANT_ID, "Dicoding", review)
        client.enqueue(object : Callback<PostReviewRespone> {
            override fun onResponse(
                call: Call<PostReviewRespone>,
                response: Response<PostReviewRespone>
            ) {
                showLoading(false)
                val responeBody = response.body()
                if (response.isSuccessful && responeBody != null
                ) {
                    setReviewData(responeBody.customerReviews)
                } else {
                    Log.e(TAG, "onfailuer : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PostReviewRespone>, t: Throwable) {
                Log.e(TAG, "onfailure : ${t.message}")
            }

        })
    }

    private fun findRestourant() {
        showLoading(true)
        val client = ApiConfig.getApiService().getRestaurant(RESTAURANT_ID)
        client.enqueue(object : Callback<RestourantRespone> {
            override fun onResponse(
                call: Call<RestourantRespone>,
                response: Response<RestourantRespone>
            ) {
                //if connection succes
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setRestourantData(responseBody.restaurant)
                        setReviewData(responseBody.restaurant.customerReviews)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RestourantRespone>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onfailure: ${t.message}")
            }

        })
    }

    private fun setReviewData(customerReviews: List<CustomerReviewsItem>) {
        /*  val listReview = ArrayList<String>()
          for (review in customerReviews) {
              listReview.add(
                  """
                      ${review.review}
                      - ${review.name}
                  """.trimIndent()
              )
          }*/

        val listReview = customerReviews.map {
            "${it.review}\n- ${it.name}"
        }
        val adapter = ReviewAdapter(listReview)
        binding.rvReview.adapter = adapter
        binding.edReview.setText("")
    }

    private fun setRestourantData(restaurant: Restaurant) {
        binding.tvTitle.text = restaurant.name
        binding.tvDescription.text = restaurant.description
        Glide.with(this@MainActivity)
            .load("https://restaurant-api.dicoding.dev/images/large/${restaurant.pictureId}")
            .into(binding.ivPicture)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }

    }

    companion object {

        private val TAG: String = MainActivity::class.java.simpleName
        private const val RESTAURANT_ID = "uewq1zg2zlskfw1e867"

    }
}