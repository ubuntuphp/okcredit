package shareapp.c3.indwin.okcredit.Room

import android.arch.persistence.room.*


@Dao
interface RoomInterface {
        @Insert
        fun insert(vararg items: SectionData)

        @Update
        fun update(vararg items: SectionData)

        @Delete
        fun delete(item: SectionData)

        @Query("SELECT * FROM SectionData WHERE section = :value")
        fun getSection(value: String?): SectionData

        @Query("DELETE FROM SectionData WHERE section = :value")
        fun deleteSection(value:String?)
}