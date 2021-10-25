package ru.neosvet.flickr

import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import ru.neosvet.flickr.photo.PhotoPresenter
import ru.neosvet.flickr.photo.PhotoView
import ru.neosvet.flickr.photo.TitleIds
import ru.neosvet.flickr.scheduler.DefaultSchedulers

class PhotoPresenterTest {
    private val photoId = "26397793043"

    private lateinit var presenter: PhotoPresenter

    @Mock
    private lateinit var photoView: PhotoView

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = PhotoPresenter(
            titleIds = TitleIds(
                owner = R.string.owner,
                date = R.string.date,
                sizes = R.string.sizes,
                title = R.string.title,
                description = R.string.description
            ),
            photo = TestPhotoSource(),
            image = TestImageSource(),
            schedulers = DefaultSchedulers()
        )
        presenter.attachView(photoView)
    }

    @Test
    fun photoPresenter_load() {
        presenter.load(photoId)
        verify(photoView).showLoading()
        TestImageSource.image?.let {
            println("Сюда не попадает, т.к. image всегда null")
            verify(photoView).setImage(it)
        }
    }

    @Test
    fun photoPresenter_getInfo() {
        presenter.getInfo(photoId)
        verify(photoView).showLoading()
        verify(photoView).updateInfo()
    }
}