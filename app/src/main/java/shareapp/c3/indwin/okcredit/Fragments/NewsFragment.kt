package shareapp.c3.indwin.okcredit.Fragments


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_news.*
import shareapp.c3.indwin.okcredit.Models.NewsDataModel
import shareapp.c3.indwin.okcredit.Models.Results
import shareapp.c3.indwin.okcredit.Adapters.NewsRvAdapter
import shareapp.c3.indwin.okcredit.ViewModel.NewsVM
import shareapp.c3.indwin.okcredit.R

class NewsFragment : Fragment() {

    private lateinit var newsRvAdapter: NewsRvAdapter
    private lateinit var newsVm: NewsVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpViewModel()
    }

    fun showNews(section:String){
        getNewsVM().getNewsData(section)?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                { result -> showNewsData(result) },
                { error -> showError(error) }
            )
    }



    private fun showError(message: Throwable?) {
        Toast.makeText(context , message?.message , Toast.LENGTH_LONG).show()
    }

    private fun showNewsData(result: NewsDataModel?) {
        if(result!=null){
            getNewsVM().saveDataCached(result.section , result)
            showResults(result.results)
        }
    }

    private fun showResults(results: List<Results>) {
        newsRv.layoutManager = LinearLayoutManager(context)
        newsRvAdapter = NewsRvAdapter()
        newsRvAdapter.setData(results)
        newsRvAdapter.setPagePublisher(getNewsVM().pageData)
        newsRv.adapter = newsRvAdapter
        newsRvAdapter.notifyDataSetChanged()
    }

    private fun getNewsVM(): NewsVM {
//        if(::newsVm.isInitialized)return newsVm
//        setUpViewModel()
//        return newsVm
        newsVm =  ViewModelProviders.of(this.activity!!).get(NewsVM::class.java)
        return  newsVm
    }

    private fun setUpViewModel(){
        newsVm =  ViewModelProviders.of(this.activity!!).get(NewsVM::class.java)
    }
}
