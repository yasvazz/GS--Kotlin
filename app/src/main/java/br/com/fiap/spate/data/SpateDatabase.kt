package br.com.fiap.spate.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/** Banco local do app (arquivo "spate.db"). Os dados continuam salvos ao fechar o app. */
@Database(entities = [Alerta::class], version = 1, exportSchema = false)
abstract class SpateDatabase : RoomDatabase() {

    abstract fun alertaDao(): AlertaDao

    companion object {
        @Volatile
        private var INSTANCE: SpateDatabase? = null

        /** Singleton: garante uma única instância do banco para o app inteiro. */
        fun getInstance(context: Context): SpateDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SpateDatabase::class.java,
                    "spate.db"
                ).build().also { INSTANCE = it }
            }
    }
}
