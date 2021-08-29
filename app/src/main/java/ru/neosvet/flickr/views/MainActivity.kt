package ru.neosvet.flickr.views

import android.os.Bundle
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import moxy.ktx.moxyPresenter
import ru.neosvet.flickr.BackEvent
import ru.neosvet.flickr.R
import ru.neosvet.flickr.abs.AbsActivity
import ru.neosvet.flickr.databinding.ActivityMainBinding
import ru.neosvet.flickr.main.MainPresenter
import ru.neosvet.flickr.main.MainView
import javax.inject.Inject

class MainActivity : AbsActivity(), MainView {

    private val navigator = AppNavigator(this, R.id.container)

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private var vb: ActivityMainBinding? = null
    private val presenter by moxyPresenter {
        MainPresenter(
            router = router
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb?.root)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if (it is BackEvent && it.back()) {
                return
            }
        }
        presenter.back()
    }
}