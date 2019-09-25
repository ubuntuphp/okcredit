package shareapp.c3.indwin.okcredit.Activities

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import shareapp.c3.indwin.okcredit.Fragments.NewsFragment
import shareapp.c3.indwin.okcredit.Fragments.PageViewFragment
import shareapp.c3.indwin.okcredit.Models.Results
import shareapp.c3.indwin.okcredit.R
import shareapp.c3.indwin.okcredit.ViewModel.NewsVM

class MainActivity : AppCompatActivity() {

    private lateinit var vm: NewsVM
    private lateinit var newsFragment: NewsFragment
    private lateinit var pageViewFragment: PageViewFragment
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpViewModel()
        setUpSpinner()
        setUpListeners()
        showNewsFragment()
    }

    private fun setUpListeners() {
        sectionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                showNews(sectionSpinner.adapter.getItem(0) as String)
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                showNews(sectionSpinner.adapter.getItem(position) as String)
            }
        }

        refreshImg.setOnClickListener {
            vm.recatchData(sectionSpinner.adapter.getItem(sectionSpinner.selectedItemPosition).toString())
            newsFragment.showNews(sectionSpinner.adapter.getItem(sectionSpinner.selectedItemPosition).toString())
        }

        val disposable = vm.pageData.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                showPageView(it)
            }.subscribe() {
            }
        compositeDisposable.add(disposable)
    }

    private fun showNews(section: String) {
        showNewsFragment()
        getNewsFragment().showNews(section)
    }

    private fun showPageView(result: Results) {
        if (!getPageViewFragment().isAdded) {
            showPageFragment()
            getPageViewFragment().loadPage(result)
        }
    }

    private fun setUpViewModel() {
        vm = ViewModelProviders.of(this).get(NewsVM::class.java)
    }

    private fun setUpSpinner() {
        sectionSpinner.adapter = ArrayAdapter<String>(this, R.layout.spinner_item, vm.getSectionNames())
    }

    private fun showNewsFragment() {
        if (!getNewsFragment().isAdded)
            supportFragmentManager.beginTransaction().addToBackStack("1").add(
                R.id.fragment_container,
                getNewsFragment()
            ).commit()
    }

    private fun showPageFragment() {
        supportFragmentManager.beginTransaction().addToBackStack("1")
            .add(R.id.fragment_container, getPageViewFragment()).commit()
    }

    private fun getNewsFragment(): NewsFragment {
        if (::newsFragment.isInitialized) return newsFragment
        newsFragment = NewsFragment()
        return newsFragment
    }

    private fun getPageViewFragment(): PageViewFragment {
        if (::pageViewFragment.isInitialized) return pageViewFragment
        pageViewFragment = PageViewFragment()
        return pageViewFragment
    }

    override fun onBackPressed() {
        supportFragmentManager.beginTransaction().remove(getPageViewFragment()).commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
