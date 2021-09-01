package ru.neosvet.flickr.views

import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.davemorrissey.labs.subscaleview.ImageSource
import com.google.android.material.bottomsheet.BottomSheetBehavior
import moxy.ktx.moxyPresenter
import ru.neosvet.flickr.R
import ru.neosvet.flickr.abs.AbsFragment
import ru.neosvet.flickr.databinding.FragmentPhotoBinding
import ru.neosvet.flickr.image.PicassoImageLoader
import ru.neosvet.flickr.list.InfoAdapter
import ru.neosvet.flickr.photo.IPhotoSource
import ru.neosvet.flickr.photo.PhotoPresenter
import ru.neosvet.flickr.photo.PhotoView
import ru.neosvet.flickr.photo.TitleIds
import ru.neosvet.flickr.scheduler.Schedulers
import javax.inject.Inject

class PhotoFragment : AbsFragment(), PhotoView {
    companion object {
        private const val ARG_PHOTO = "photo"

        @JvmStatic
        fun newInstance(photo_id: String) =
            PhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PHOTO, photo_id)
                }
            }
    }

    @Inject
    lateinit var source: IPhotoSource

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
        v.zoomingView.setImage(ImageSource.resource(R.drawable.load_photo))
        vb = v
        bottomSheetBehavior = BottomSheetBehavior.from(v.bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        arguments?.let {
            it.getString(ARG_PHOTO)?.let { id ->
                photo_id = id
                presenter.load(id)
                presenter.getInfo(id)
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
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
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
        vb?.zoomingView?.setImage(ImageSource.bitmap(bitmap))
    }

    override fun setNoPhoto() {
        vb?.zoomingView?.setImage(ImageSource.resource(R.drawable.no_photo))
    }

    override fun updateInfo() {
        adapter?.notifyDataSetChanged()
    }

    override fun showError(t: Throwable) {
        t.printStackTrace()
        Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
    }
}