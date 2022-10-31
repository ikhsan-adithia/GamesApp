package com.test.perqaratest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.perqaratest.data.models.Game

@Database(
    entities = [Game::class],
    version = GameDatabase.dbVersion
)
abstract class GameDatabase: RoomDatabase() {
    abstract val dao: GameDao

    companion object {
        const val dbVersion = 1
        const val dbName = "GameDB"
    }
}