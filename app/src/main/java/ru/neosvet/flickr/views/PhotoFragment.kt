package ru.neosvet.flickr.views

import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.davemorrissey.labs.subscaleview.ImageSource
import com.github.terrakok.cicerone.Router
import com.google.android.material.bottomsheet.BottomSheetBehavior
import moxy.ktx.moxyPresenter
import ru.neosvet.flickr.BackEvent
import ru.neosvet.flickr.R
import ru.neosvet.flickr.abs.AbsFragment
import ru.neosvet.flickr.databinding.FragmentPhotoBinding
import ru.neosvet.flickr.entities.PhotoItem
import ru.neosvet.flickr.image.PicassoImageLoader
import ru.neosvet.flickr.list.InfoAdapter
import ru.neosvet.flickr.photo.IPhotoSource
import ru.neosvet.flickr.photo.PhotoPresenter
import ru.neosvet.flickr.photo.PhotoView
import ru.neosvet.flickr.photo.TitleIds
import ru.neosvet.flickr.scheduler.Schedulers
import javax.inject.Inject

class PhotoFragment : AbsFragment(), PhotoView, BackEvent {
    companion object {
        private const val ARG_PHOTO = "photo"

        @JvmStatic
        fun newInstance(photo: PhotoItem) =
            PhotoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PHOTO, photo)
                }
            }
    }

    @Inject
    lateinit var source: IPhotoSource

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var schedulers: Schedulers

    private val titleIds by lazy {
        TitleIds(
            owner = R.string.owner,
            date = R.string.date,
            title = R.string.title,
            description = R.string.description
        )
    }

    private val presenter by moxyPresenter {
        PhotoPresenter(
            imageLoader = PicassoImageLoader,
            titleIds = titleIds,
            source = source,
            router = router,
            schedulers = schedulers
        )
    }

    private var vb: FragmentPhotoBinding? = null
    private var adapter: InfoAdapter? = null
    private var photo_id: String? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        val v = FragmentPhotoBinding.inflate(inflater, container, false)
        vb = v
        bottomSheetBehavior = BottomSheetBehavior.from(v.bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        arguments?.let {
            val photo = it.getParcelable(ARG_PHOTO) as PhotoItem?
            if (photo != null) {
                photo_id = photo.id
                presenter.startLoad(photo.id)
                presenter.getInfo(photo.id)
            }
        }

        return v.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val item = menu.add(R.string.info)
        item.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_info)
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN)
            openInfo()
        else
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        return super.onOptionsItemSelected(item)
    }

    private fun openInfo() {
        photo_id?.let {
            if (adapter == null)
                initList()
            presenter.getInfo(it)
        }
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    private fun initList() {
        vb?.rvInfo?.layoutManager = LinearLayoutManager(requireContext())
        adapter = InfoAdapter(
            presenter = presenter.infoListPresenter
        )
        vb?.rvInfo?.adapter = adapter
    }

    override fun setImage(bitmap: Bitmap) {
        vb?.zoomingView?.setImage(ImageSource.bitmap(bitmap))
    }

    override fun updateInfo() {
        adapter?.notifyDataSetChanged()
    }

    override fun showError(t: Throwable) {
        t.printStackTrace()
        Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
    }

    override fun back() = presenter.back()

}