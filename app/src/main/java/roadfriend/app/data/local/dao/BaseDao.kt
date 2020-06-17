package roadfriend.app.data.local.dao

import androidx.room.*

@Dao
interface BaseDao<T> {
    @Insert
    fun insert(obj: T)

    @Insert
    fun insert(vararg obj: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(obj: List<T>)

    @Update
    fun update(obj: T)

    @Delete
    fun delete(obj: T)
}