package shareapp.c3.indwin.okcredit.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.layout_news_item.view.*
import shareapp.c3.indwin.okcredit.Models.Results
import shareapp.c3.indwin.okcredit.R

class NewsRvAdapter : RecyclerView.Adapter<NewsRvAdapter.NewsVh>() {

    private lateinit var newsList: List<Results>
    private lateinit var pageLoader: PublishSubject<Results>
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): NewsVh {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_news_item, parent, false);
        return NewsVh(view)
    }

    override fun getItemCount(): Int {
        if (!::newsList.isInitialized) return 0
        return newsList.count()
    }

    override fun onBindViewHolder(itemVh: NewsVh, position: Int) {
        itemVh.setData(
            newsList[position],
            newsList[position].byline,
            newsList[position].title,
            if (newsList[position].multimedia.count() > 2) newsList[position].multimedia[3].url else null
        )
    }

    fun setData(results: List<Results>) {
        newsList = results
    }


    fun loadPage(result: Results){
        if(::pageLoader.isInitialized){
            pageLoader.onNext(result)
        }
    }

    fun setPagePublisher(pageData: PublishSubject<Results>) {
        pageLoader = pageData
    }

    inner class NewsVh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var titleTxt: TextView
        private var writterTxt: TextView
        private var thumbnailImg: ImageView
        private lateinit var data: Results

        init {
            titleTxt = itemView.titleTxt
            writterTxt = itemView.writterTxt
            thumbnailImg = itemView.thumbnailmg

        }

        fun setData(result: Results, writter: String, title: String, imgUrl: String?) {
            data = result
            titleTxt.text = title
            writterTxt.text = writter
            itemView.setOnClickListener { v -> loadPage(result) }
            if (imgUrl != null) Picasso.get().load(imgUrl).into(thumbnailImg)
        }

    }
}

