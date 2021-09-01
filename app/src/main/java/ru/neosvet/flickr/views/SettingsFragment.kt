package ru.neosvet.flickr.views

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.github.terrakok.cicerone.Router
import moxy.ktx.moxyPresenter
import ru.neosvet.flickr.BackEvent
import ru.neosvet.flickr.abs.AbsFragment
import ru.neosvet.flickr.databinding.FragmentSettingsBinding
import ru.neosvet.flickr.scheduler.Schedulers
import ru.neosvet.flickr.settings.ISettingsSource
import ru.neosvet.flickr.settings.SettingsField
import ru.neosvet.flickr.settings.SettingsPresenter
import ru.neosvet.flickr.settings.SettingsView
import ru.neosvet.flickr.utils.GalleryType
import ru.neosvet.flickr.utils.ISettings
import javax.inject.Inject

class SettingsFragment : AbsFragment(), SettingsView, BackEvent {

    @Inject
    lateinit var source: ISettingsSource

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var schedulers: Schedulers

    @Inject
    lateinit var settings: ISettings

    private val presenter by moxyPresenter {
        SettingsPresenter(
            source = source,
            settings = settings,
            router = router,
            schedulers = schedulers
        )
    }

    private var vb: FragmentSettingsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSettingsBinding.inflate(inflater, container, false).also {
        vb = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vb?.etUser?.setOnKeyListener(View.OnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action === KeyEvent.ACTION_DOWN && keyEvent.keyCode === KeyEvent.KEYCODE_ENTER
                || keyCode == EditorInfo.IME_ACTION_SEARCH
            ) {
                presenter.setUser(vb?.etUser?.text.toString())
                return@OnKeyListener true
            }
            false
        })

        vb?.etGallery?.setOnKeyListener(View.OnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action === KeyEvent.ACTION_DOWN && keyEvent.keyCode === KeyEvent.KEYCODE_ENTER
                || keyCode == EditorInfo.IME_ACTION_SEARCH
            ) {
                presenter.setGallery(vb?.etGallery?.text.toString())
                return@OnKeyListener true
            }
            false
        })

        vb?.btnPopular?.setOnClickListener {
            presenter.setGalleryType(GalleryType.Popular)
        }

        vb?.btnGallery?.setOnClickListener {
            presenter.setGalleryType(GalleryType.Gallery)
        }
    }

    override fun setGalleryType(type: GalleryType) = when (type) {
        GalleryType.Popular -> {
            vb?.btnPopular?.isChecked = true
            vb?.lUser?.visibility = View.VISIBLE
            vb?.lGallery?.visibility = View.GONE
        }
        GalleryType.Gallery -> {
            vb?.btnGallery?.isChecked = true
            vb?.lUser?.visibility = View.GONE
            vb?.lGallery?.visibility = View.VISIBLE
        }
    }

    override fun back() = presenter.back()

    override fun setUser(user_name: String) {
        vb?.etUser?.setText(user_name)
    }

    override fun setGallery(gallery_url: String) {
        vb?.etGallery?.setText(gallery_url)
    }

    override fun showError(field: SettingsField, t: Throwable) {
        t.printStackTrace()
        when (field) {
            SettingsField.User -> vb?.etUser?.error = t.message
            SettingsField.Gallery -> vb?.etGallery?.error = t.message
            SettingsField.Other -> Toast
                .makeText(requireContext(), t.message, Toast.LENGTH_LONG)
                .show()
        }
    }
}