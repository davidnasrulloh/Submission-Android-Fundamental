package com.davidnasrulloh.simplegithubuser.data.local.room

import androidx.room.*
import com.davidnasrulloh.simplegithubuser.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userEntity: FavoriteEntity)

    @Update
    suspend fun update(userEntity: FavoriteEntity)

    @Delete
    suspend fun delete(userEntity: FavoriteEntity)

    @Query("SELECT * FROM user ORDER BY id ASC")
    fun getAllUsers(): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT * FROM user WHERE id = :id AND is_favorite = 1)")
    fun isFavoriteUser(id: String): Flow<Boolean>
}