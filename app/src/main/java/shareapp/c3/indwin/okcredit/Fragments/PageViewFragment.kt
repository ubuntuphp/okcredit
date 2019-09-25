package shareapp.c3.indwin.okcredit.Fragments


import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_page_view.*
import shareapp.c3.indwin.okcredit.Models.Results
import shareapp.c3.indwin.okcredit.ViewModel.NewsVM
import shareapp.c3.indwin.okcredit.R

class PageViewFragment : Fragment() {

    private lateinit var newsVm: NewsVM
    private lateinit var result: Results

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_page_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showPage()
        setUpListeners()
    }

    fun loadPage(data: Results){
        result = data
    }

    private fun setUpListeners(){
        articleLink.setOnClickListener({ v -> openPage((v as TextView).text.toString())})
    }

    private fun openPage(url:String){
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun showPage(){
        if(::result.isInitialized) {
            authorTxt.text = result.byline
            publishTxt.text = result.published_date
            articleLink.text = result.url
            abstractTxt.text = result.abstract
            if(result.multimedia.count() > 3){
                Picasso.get().load(result.multimedia[3].url).into(thumbnailImg)
            }
        }
    }

    private fun getNewsVM(): NewsVM {
        if(::newsVm.isInitialized)return newsVm
        setUpViewModel()
        return newsVm
    }

    private fun setUpViewModel(){
        newsVm = ViewModelProviders.of(this.activity!!).get(NewsVM::class.java)
    }
}
