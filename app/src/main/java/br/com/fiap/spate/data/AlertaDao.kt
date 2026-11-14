package br.com.fiap.spate.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/** DAO: define as operações que o app pode fazer na tabela "alertas". */
@Dao
interface AlertaDao {

    /** Flow: sempre que a tabela muda, a lista é emitida de novo e a tela se atualiza sozinha. */
    @Query("SELECT * FROM alertas ORDER BY dataCriacao DESC")
    fun observarTodos(): Flow<List<Alerta>>

    @Query("SELECT * FROM alertas WHERE id = :id")
    suspend fun buscarPorId(id: Int): Alerta?

    @Insert
    suspend fun inserir(alerta: Alerta)

    @Update
    suspend fun atualizar(alerta: Alerta)

    @Delete
    suspend fun excluir(alerta: Alerta)
}
