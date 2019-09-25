package shareapp.c3.indwin.okcredit.ViewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import android.arch.persistence.room.Room
import org.json.JSONArray
import org.json.JSONObject
import shareapp.c3.indwin.okcredit.Constants.Constants
import shareapp.c3.indwin.okcredit.Models.Multimedia
import shareapp.c3.indwin.okcredit.Models.NewsDataModel
import shareapp.c3.indwin.okcredit.Models.Results
import shareapp.c3.indwin.okcredit.Network.RetrofitModule
import shareapp.c3.indwin.okcredit.Room.AppDatabase
import shareapp.c3.indwin.okcredit.Room.SectionData


class NewsVM(application: Application)  : AndroidViewModel(application) {
    var pageData :PublishSubject<Results> = PublishSubject.create();
    lateinit var database : AppDatabase

    fun getRoomDatabase(): AppDatabase {
        if(::database.isInitialized)return database
        database = Room.databaseBuilder(getApplication(), AppDatabase::class.java, "mydb")
            .allowMainThreadQueries()
            .build()
        return database
    }

    fun getNewsData(section:String): Observable<NewsDataModel>? {
        if(isDataCached(section)){
            val sectionData = getDataCached(section)
            val jsonObject = JSONObject(sectionData.data)
            val jsonArray = jsonObject.getJSONArray("data")
            val results = ArrayList<Results>()
            for(i in 0..jsonArray.length() - 1){
                val multimediaList = ArrayList<Multimedia>()
                for(j in 0..jsonArray.getJSONObject(i).getJSONArray("multimedia").length() - 1){
                    multimediaList.add(
                        Multimedia(
                            url = jsonArray.getJSONObject(i).getJSONArray(
                                "multimedia"
                            ).getJSONObject(j).getString("url")
                        )
                    )
                }
                results.add(
                    Results(
                        section = section,
                        title = jsonArray.getJSONObject(i).getString("title"),
                        abstract = jsonArray.getJSONObject(i).getString("abstract"),
                        url = jsonArray.getJSONObject(i).getString("url"),
                        byline = jsonArray.getJSONObject(i).getString("byline"),
                        published_date = jsonArray.getJSONObject(i).getString("published_date"),
                        multimedia = multimediaList
                    )
                )
            }
            val newsDataModel = NewsDataModel(
                section = section,
                num_results = results.count(),
                results = results
            )
            return Observable.just(newsDataModel);
        }
        return RetrofitModule.create().getNewsData(section , Constants.API_KEY)
    }

    fun isDataCached(section: String):Boolean{
        return getRoomDatabase().room.getSection(section)!= null && getRoomDatabase().room.getSection(section).data!= null
    }

    fun getDataCached(section: String): SectionData {
        return getRoomDatabase().room.getSection(section)
    }

    fun removeDataCahced(section: String){
        getRoomDatabase().room.deleteSection(section)
    }
    fun saveDataCached(section: String , newsDataModel: NewsDataModel){
        if(isDataCached(section))return
        val data = JSONArray()
        for(i in 0..newsDataModel.num_results - 1){
            val rData = JSONObject()
            rData.put("title" , newsDataModel.results[i].title)
            rData.put("byline" , newsDataModel.results[i].byline)
            rData.put("abstract" , newsDataModel.results[i].abstract)
            rData.put("published_date" , newsDataModel.results[i].published_date)
            rData.put("url" , newsDataModel.results[i].url)

            val rMData = JSONArray()
            for(j in 0..newsDataModel.results[i].multimedia.size - 1){
                val rmData = JSONObject().put("url" , newsDataModel.results[i].multimedia[j].url)
                rMData.put(rmData)
            }
            rData.put("multimedia" , rMData)

            data.put(rData)
        }
        val jsonObject = JSONObject()
        jsonObject.put("data" , data)

        val sectionData = SectionData(section, jsonObject.toString())
        getRoomDatabase().room.insert(sectionData)
    }

    fun getSectionNames():ArrayList<String>{
        val sectionList =  java.util.ArrayList<String>()
        sectionList.addAll(Constants.sections)
        return sectionList
    }

    fun recatchData(section: String) {
        removeDataCahced(section)
    }
}