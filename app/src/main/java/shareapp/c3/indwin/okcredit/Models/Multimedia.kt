package shareapp.c3.indwin.okcredit.Models

import com.google.gson.annotations.SerializedName

data class Multimedia (

    @SerializedName("url") val url : String,
    @SerializedName("format") val format : String = "",
    @SerializedName("height") val height : Int = 0,
    @SerializedName("width") val width : Int = 0,
    @SerializedName("type") val type : String = "",
    @SerializedName("subtype") val subtype : String = "",
    @SerializedName("caption") val caption : String = "",
    @SerializedName("copyright") val copyright : String = ""
)