package ru.neosvet.flickr.views

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.davemorrissey.labs.subscaleview.ImageSource
import com.google.android.material.bottomsheet.BottomSheetBehavior
import moxy.ktx.moxyPresenter
import ru.neosvet.flickr.BackEvent
import ru.neosvet.flickr.R
import ru.neosvet.flickr.abs.AbsFragment
import ru.neosvet.flickr.abs.getTranslateMessage
import ru.neosvet.flickr.abs.showMessageRetry
import ru.neosvet.flickr.databinding.FragmentPhotoBinding
import ru.neosvet.flickr.loader.PRDnlrLoader
import ru.neosvet.flickr.list.InfoAdapter
import ru.neosvet.flickr.photo.IPhotoSource
import ru.neosvet.flickr.photo.PhotoPresenter
import ru.neosvet.flickr.photo.PhotoView
import ru.neosvet.flickr.photo.TitleIds
import ru.neosvet.flickr.scheduler.Schedulers
import ru.neosvet.flickr.storage.FlickrStorage
import javax.inject.Inject

class PhotoFragment : AbsFragment(), PhotoView, BackEvent {
    companion object {
        private const val ARG_PHOTO = "photo"
        private const val ARG_URL_MINI = "url_mini"

        @JvmStatic
        fun newInstance(photoId: String, urlMini: String) =
            PhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PHOTO, photoId)
                    putString(ARG_URL_MINI, urlMini)
                }
            }
    }

    @Inject
    lateinit var source: IPhotoSource

    @Inject
    lateinit var schedulers: Schedulers

    @Inject
    lateinit var storage: FlickrStorage

    private val titleIds by lazy {
        TitleIds(
            owner = R.string.owner,
            date = R.string.date,
            sizes = R.string.sizes,
            title = R.string.title,
            description = R.string.description
        )
    }

    private val presenter by moxyPresenter {
        PhotoPresenter(
            titleIds = titleIds,
            photo = source,
            image = ru.neosvet.flickr.image.ImageSource(
                context = requireContext(),
                schedulers = schedulers,
                loader = PRDnlrLoader,
                storage = storage
            ),
            schedulers = schedulers
        )
    }

    private var vb: FragmentPhotoBinding? = null
    private var adapter: InfoAdapter? = null
    private var photoId: String? = null
    private lateinit var bottomSheet: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        val v = FragmentPhotoBinding.inflate(inflater, container, false)
        vb = v
        bottomSheet = BottomSheetBehavior.from(v.bottomSheetContainer)
        bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN

        arguments?.let {
            it.getString(ARG_PHOTO)?.let { id ->
                photoId = id
                val url = it.getString(ARG_URL_MINI)
                presenter.load(id, url)
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
        if (bottomSheet.state == BottomSheetBehavior.STATE_HIDDEN)
            openInfo()
        else
            bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN

        return super.onOptionsItemSelected(item)
    }

    private fun openInfo() {
        photoId?.let {
            if (adapter == null)
                initList()
            presenter.getInfo(it)
        }
    }

    private fun initList() {
        vb?.rvInfo?.layoutManager = LinearLayoutManager(requireContext())
        adapter = InfoAdapter(
            presenter = presenter.infoListPresenter
        )
        vb?.rvInfo?.adapter = adapter
    }

    override fun setImage(bitmap: Bitmap) {
        vb?.run {
            lProgress.visibility = View.GONE
            zoomingView.setImage(ImageSource.bitmap(bitmap))
        }
    }

    override fun setVideo(uri: Uri) {
        vb?.run {
            lProgress.visibility = View.GONE
            ivPlay.visibility = View.VISIBLE
            ivPlay.setOnClickListener {
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(uri, "video/mp4")
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun setNoPhoto() {
        vb?.run {
            lProgress.visibility = View.GONE
            zoomingView.setImage(ImageSource.resource(R.drawable.no_photo))
        }
    }

    override fun updateInfo() {
        bottomSheet.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        adapter?.notifyDataSetChanged()
        vb?.lProgress?.visibility = View.GONE
    }

    override fun showLoading() {
        vb?.run {
            tvProgress.text = ""
            lProgress.visibility = View.VISIBLE
        }
    }

    override fun setLoadProgress(stat: String) {
        vb?.tvProgress?.text = stat
    }

    override fun showError(t: Throwable) {
        t.printStackTrace()
        vb?.run {
            showMessageRetry(root, getTranslateMessage(t)) {
                bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
                presenter.retryLastAction()
            }
        }
    }

    override fun back(): Boolean {
        if (bottomSheet.state == BottomSheetBehavior.STATE_HIDDEN) {
            return false
        } else {
            bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
            return true
        }
    }
}