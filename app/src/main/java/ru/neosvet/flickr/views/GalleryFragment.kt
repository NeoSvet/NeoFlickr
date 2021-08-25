package ru.neosvet.flickr.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.Router
import moxy.ktx.moxyPresenter
import ru.neosvet.flickr.BackEvent
import ru.neosvet.flickr.R
import ru.neosvet.flickr.abs.AbsFragment
import ru.neosvet.flickr.databinding.FragmentGalleryBinding
import ru.neosvet.flickr.gallery.GalleryPresenter
import ru.neosvet.flickr.gallery.GalleryView
import ru.neosvet.flickr.gallery.IGallerySource
import ru.neosvet.flickr.list.GalleryAdapter
import ru.neosvet.flickr.scheduler.Schedulers
import ru.neosvet.flickr.utils.GlideImageLoader
import javax.inject.Inject

class GalleryFragment : AbsFragment(), GalleryView, BackEvent {

    @Inject
    lateinit var source: IGallerySource

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var schedulers: Schedulers

    private val presenter by moxyPresenter {
        GalleryPresenter(
            source = source,
            router = router,
            schedulers = schedulers
        )
    }

    private var vb: FragmentGalleryBinding? = null
    private var adapter: GalleryAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentGalleryBinding.inflate(inflater, container, false).also {
        vb = it
    }.root

    override fun init() {
        vb?.rvGallery?.layoutManager = LinearLayoutManager(requireContext())
        adapter = GalleryAdapter(
            presenter = presenter.galleryListPresenter,
            imageLoader = GlideImageLoader()
        )
        vb?.rvGallery?.adapter = adapter
    }

    override fun updateList() {
        adapter?.notifyDataSetChanged()
    }

    override fun showError(t: Throwable) {
        Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
    }

    override fun back() = presenter.back()
}