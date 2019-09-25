package shareapp.c3.indwin.okcredit.Room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import shareapp.c3.indwin.okcredit.Room.RoomInterface
import shareapp.c3.indwin.okcredit.Room.SectionData

@Database(entities = [SectionData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val room: RoomInterface
}