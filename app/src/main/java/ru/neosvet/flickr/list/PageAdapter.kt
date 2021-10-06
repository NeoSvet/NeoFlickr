package ru.neosvet.flickr.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import ru.neosvet.flickr.R

class PageAdapter(
    private val callback: PageEvent
) : RecyclerView.Adapter<PageAdapter.ViewHolder>() {
    private var current: Int = 1
    private var count: Int = 0

    interface PageEvent {
        fun onPage(page: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.page_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PageAdapter.ViewHolder, position: Int) {
        val page = position + 1
        holder.setItem(page, page == current)
    }

    override fun getItemCount() = count

    fun setPages(page: Int, page_count: Int) {
        current = page
        count = page_count
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvPage: MaterialTextView = itemView as MaterialTextView

        @SuppressLint("ResourceAsColor")
        fun setItem(page: Int, select: Boolean) {
            tvPage.text = page.toString()
            if (select) {
                tvPage.background = ContextCompat.getDrawable(
                    tvPage.context, R.drawable.field_with_border
                )
            } else {
                tvPage.setBackgroundColor(android.R.color.transparent)
                tvPage.setOnClickListener {
                    callback.onPage(page)
                }
            }
        }
    }
}