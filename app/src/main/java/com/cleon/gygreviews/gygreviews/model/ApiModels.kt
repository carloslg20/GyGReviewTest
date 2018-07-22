package com.cleon.gygreviews.gygreviews.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Review(@PrimaryKey
                  @SerializedName("review_id") val reviewId: Int, val rating: String, val title: String, val message: String,
                  val author: String, val foreignLanguage: Boolean, val date: String,
                  val languageCode: String, @SerializedName("traveler_type")val travelerType: String,
                  val reviewerName: String, val reviewerCountry: String)
@Entity
data class NewReview(@PrimaryKey val uiId: Int, val userId: Int, val rating: String, val title: String, val message: String,
                  val foreignLanguage: Boolean, val date: String)

data class NewReviewResponse(val status: Boolean)

data class ReviewResponse(val status: Boolean, val totalReviewsComments: Int, val data: List<Review>)
