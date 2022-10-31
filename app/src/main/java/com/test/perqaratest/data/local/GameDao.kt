package com.test.perqaratest.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.perqaratest.data.models.Game
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToBookmark(game: Game)

    @Query("SELECT * FROM Game WHERE id = :id limit 1")
    suspend fun getBookmarkedGame(id: Int): Game?

    @Query("DELETE FROM Game WHERE id = :id")
    suspend fun removeBookmark(id: Int)

    @Query("SELECT * FROM Game")
    fun getBookmarkedGames(): Flow<List<Game>>
}