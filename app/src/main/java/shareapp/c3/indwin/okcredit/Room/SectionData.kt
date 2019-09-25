package shareapp.c3.indwin.okcredit.Room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class SectionData(
    @PrimaryKey @ColumnInfo(name = "section") val section: String,
    @ColumnInfo(name = "data") val data: String?
)