package com.cleon.gygreviews.gygreviews.db

import androidx.room.Dao
import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cleon.gygreviews.gygreviews.model.Review

@Dao
interface ReviewDao {

    @Query("SELECT * FROM Review")
    fun getAll(): LiveData<List<Review>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReviews(repositories: List<Review>)

}