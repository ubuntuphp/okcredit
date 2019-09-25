package shareapp.c3.indwin.okcredit.Models

import com.google.gson.annotations.SerializedName

data class NewsDataModel (

    @SerializedName("status") val status : String = "",
    @SerializedName("copyright") val copyright : String = "",
    @SerializedName("section") val section : String,
    @SerializedName("last_updated") val last_updated : String = "",
    @SerializedName("num_results") val num_results:Int = 0,
    @SerializedName("results") val results : List<Results>
)