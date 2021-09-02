package ru.neosvet.flickr.views

import android.Manifest
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
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager




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
        verifyStoragePermissions()
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
            if (it is BackEvent && it.back())
                return
        }
        presenter.back()
    }

    private fun verifyStoragePermissions(): Boolean {
        //http://stackoverflow.com/questions/38989050/android-6-0-write-to-external-sd-card
        val permission = ActivityCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this@MainActivity, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 1
            )
            return true
        }
        return false
    }
}