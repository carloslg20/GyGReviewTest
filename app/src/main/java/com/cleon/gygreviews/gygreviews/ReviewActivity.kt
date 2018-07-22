package com.cleon.gygreviews.gygreviews

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cleon.gygreviews.gygreviews.model.Review
import com.cleon.gygreviews.gygreviews.ui.ReviewListAdapter
import com.cleon.gygreviews.gygreviews.viewModel.ReviewsViewModel
import com.cleon.gygreviews.gygreviews.vo.Resource
import com.cleon.gygreviews.gygreviews.vo.Status
import kotlinx.android.synthetic.main.activity_review.*


class ReviewActivity : AppCompatActivity() {

    lateinit var reviewViewModel: ReviewsViewModel
    var adapter: ReviewListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ReviewActivity)
        }

        val factory = ReviewsViewModel.Factory(application,
                ServiceLocator.instance(this).getMockRepository())
        reviewViewModel = ViewModelProviders.of(this, factory).get(ReviewsViewModel::class.java)

        adapter = ReviewListAdapter(this, reviewViewModel.allData.value)
        recyclerView.adapter = adapter

        reviewViewModel.list.observe(this, Observer { it ->
            updateUI(it)
        })

        reviewViewModel.allData.observe(this, Observer { it ->
            if (adapter?.isDataNull() == true) {
                adapter = ReviewListAdapter(this, reviewViewModel.allData.value)
                recyclerView.adapter = adapter
            } else {
                adapter?.notifyDataSetChanged()
            }
        })

//        reviewViewModel.allData.observe(this, Observer { _ ->
//            adapter.notifyDataSetChanged()
//        })


//        val repo = ServiceLocator.instance(applicationContext).getMockRepository()
//        repo.loadReviews(0).observe(this, Observer { resource ->
//
//        })

//        val repo = ServiceLocator.instance(applicationContext).getRepository()
//        repo.loadReviews(0).observe(this, Observer { resource ->
//
//        })

//        val service = ServiceLocator.instance(applicationContext).getMockReviewService()
//
//        service.getReviews(0).observe(this, Observer { result ->

//        })
    }

    fun updateUI(resource: Resource<List<Review>>) {
        when (resource.status) {
            Status.LOADING -> progressBar.visibility = View.VISIBLE
            Status.SUCCESS -> {
                progressBar.visibility = View.GONE
                reviewViewModel.addReviewData(resource)
            }
            else -> progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.my_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.loadMore -> {
                var currentPage = reviewViewModel.currentPage.value ?: 0
                currentPage += 1
                reviewViewModel.currentPage.value = currentPage
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
