package ru.neosvet.flickr.views

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.Router
import com.google.android.material.bottomsheet.BottomSheetBehavior
import moxy.ktx.moxyPresenter
import ru.neosvet.flickr.R
import ru.neosvet.flickr.abs.AbsFragment
import ru.neosvet.flickr.abs.showMessageRetry
import ru.neosvet.flickr.databinding.FragmentGalleryBinding
import ru.neosvet.flickr.gallery.GalleryPresenter
import ru.neosvet.flickr.gallery.GalleryView
import ru.neosvet.flickr.gallery.IGallerySource
import ru.neosvet.flickr.image.ImageSource
import ru.neosvet.flickr.image.PRDnlrImageLoader
import ru.neosvet.flickr.list.GalleryAdapter
import ru.neosvet.flickr.list.PageAdapter
import ru.neosvet.flickr.scheduler.Schedulers
import ru.neosvet.flickr.settings.ISettings
import ru.neosvet.flickr.storage.FlickrStorage
import javax.inject.Inject

class GalleryFragment : AbsFragment(), GalleryView, PageAdapter.PageEvent {

    @Inject
    lateinit var source: IGallerySource

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var schedulers: Schedulers

    @Inject
    lateinit var settings: ISettings

    @Inject
    lateinit var storage: FlickrStorage

    private val orientation: Orientation by lazy {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            Orientation.LANDSCAPE
        else
            Orientation.PORTRAIT
    }

    private val presenter by moxyPresenter {
        GalleryPresenter(
            source = source,
            settings = settings,
            router = router,
            schedulers = schedulers
        )
    }

    private var vb: FragmentGalleryBinding? = null
    private var adGallery: GalleryAdapter? = null
    private var adPage: PageAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentGalleryBinding.inflate(inflater, container, false).also {
        vb = it
        setHasOptionsMenu(true)
    }.root

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.gallery, menu)

        val search = menu.findItem(R.id.search).actionView as SearchView
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                presenter.search(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
        search.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                if (presenter.query != null)
                    presenter.loadHome()
                return false
            }
        })

        presenter.query?.let {
            search.setQuery(it, false)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings)
            presenter.openSettings()

        return super.onOptionsItemSelected(item)
    }

    override fun init() {
        if (orientation == Orientation.LANDSCAPE) {
            vb?.rvGallery?.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        } else {
            vb?.rvGallery?.layoutManager = LinearLayoutManager(requireContext())
        }
        adGallery = GalleryAdapter(
            presenter = presenter.galleryListPresenter,
            source = ImageSource(
                context = requireContext(),
                schedulers = schedulers,
                loader = PRDnlrImageLoader,
                storage = storage
            ),
            orientation = orientation
        )
        vb?.rvGallery?.adapter = adGallery

        vb?.rvPages?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adPage = PageAdapter(this)
        vb?.rvPages?.adapter = adPage
        vb?.rvPages?.visibility = View.GONE
    }

    override fun showLoading() {
        vb?.lProgress?.visibility = View.VISIBLE
    }

    override fun updateGallery() {
        vb?.lProgress?.visibility = View.GONE
        adGallery?.notifyDataSetChanged()

        presenter.galleryListPresenter.lastClickedPos?.let {
            vb?.rvGallery?.scrollToPosition(it)
            presenter.galleryListPresenter.lastClickedPos = null
        } ?: vb?.rvGallery?.scrollToPosition(0)
    }

    override fun updatePages(page: Int, pages: Int) {
        if (pages == 1) {
            vb?.rvPages?.visibility = View.GONE
        } else {
            vb?.rvPages?.visibility = View.VISIBLE
            adPage?.setPages(page, pages)
            adPage?.notifyDataSetChanged()
            vb?.rvPages?.scrollToPosition(page - 1)
        }
    }

    override fun showError(t: Throwable) {
        vb?.lProgress?.visibility = View.GONE
        t.printStackTrace()
        vb?.run {
            showMessageRetry(root, t.message.toString()) {
                presenter.retryLastAction()
            }
        }
    }

    override fun onDestroyView() {
        vb = null
        super.onDestroyView()
    }

    override fun onPage(page: Int) {
        presenter.galleryListPresenter.lastClickedPos = null
        presenter.changePage(page)
    }
}