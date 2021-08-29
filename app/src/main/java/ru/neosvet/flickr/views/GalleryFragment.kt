package ru.neosvet.flickr.views

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.Router
import moxy.ktx.moxyPresenter
import ru.neosvet.flickr.BackEvent
import ru.neosvet.flickr.abs.AbsFragment
import ru.neosvet.flickr.databinding.FragmentGalleryBinding
import ru.neosvet.flickr.gallery.GalleryPresenter
import ru.neosvet.flickr.gallery.GalleryView
import ru.neosvet.flickr.gallery.IGallerySource
import ru.neosvet.flickr.image.PicassoImageLoader
import ru.neosvet.flickr.list.GalleryAdapter
import ru.neosvet.flickr.scheduler.Schedulers
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
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            vb?.rvGallery?.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        } else {
            vb?.rvGallery?.layoutManager = LinearLayoutManager(requireContext())
        }
        adapter = GalleryAdapter(
            presenter = presenter.galleryListPresenter,
            imageLoader = PicassoImageLoader
        )
        vb?.rvGallery?.adapter = adapter
    }

    override fun updateList() {
        adapter?.notifyDataSetChanged()
    }

    override fun showError(t: Throwable) {
        t.printStackTrace()
        Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
    }

    override fun back() = presenter.back()

    override fun onDestroyView() {
        vb = null
        super.onDestroyView()
    }
}