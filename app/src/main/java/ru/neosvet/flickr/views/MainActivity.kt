package ru.neosvet.flickr.views

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.google.android.material.snackbar.Snackbar
import moxy.ktx.moxyPresenter
import ru.neosvet.flickr.BackEvent
import ru.neosvet.flickr.R
import ru.neosvet.flickr.abs.AbsActivity
import ru.neosvet.flickr.databinding.ActivityMainBinding
import ru.neosvet.flickr.main.MainPresenter
import ru.neosvet.flickr.main.MainView
import javax.inject.Inject

class MainActivity : AbsActivity(), MainView {
    private val REQUEST_CODE = 472
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
        checkPermission()
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

    private fun requestPermission() {
        requestPermissions(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isEmpty() ||
                grantResults[0] != PackageManager.PERMISSION_GRANTED
            )
                showAboutAccess()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                return
            }
            shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                showAboutAccess()
            }
            else -> {
                requestPermission()
            }
        }
    }

    private fun showAboutAccess() {
        vb?.run {
            Snackbar.make(
                root,
                getString(R.string.about_access_storage),
                Snackbar.LENGTH_INDEFINITE
            ).setAction(getString(android.R.string.ok), {
                requestPermission()
            })
                .show()
        }
    }
}
