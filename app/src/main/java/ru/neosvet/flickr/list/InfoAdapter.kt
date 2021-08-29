package ru.neosvet.flickr.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.neosvet.flickr.databinding.ItemInfoBinding
import ru.neosvet.flickr.photo.IInfoItemView
import ru.neosvet.flickr.photo.IInfoListPresenter

class InfoAdapter(
    private val presenter: IInfoListPresenter
) : RecyclerView.Adapter<InfoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener { presenter.itemClickListener?.invoke(this) }
        }

    override fun getItemCount() = presenter.getCount()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        presenter.bindView(holder.apply { pos = position })

    inner class ViewHolder(private val vb: ItemInfoBinding) : RecyclerView.ViewHolder(vb.root),
        IInfoItemView {
        override var pos = -1

        override fun setText(title_id: Int, value: String) = with(vb) {
            tvTitle.text = root.context.getText(title_id)
            tvValue.text = value
        }
    }
}