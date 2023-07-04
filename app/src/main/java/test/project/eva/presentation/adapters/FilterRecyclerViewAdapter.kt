package test.project.eva.presentation.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import test.project.eva.databinding.ItemPhotoFilterBinding
import test.project.eva.presentation.models.PhotoFilter

class FilterRecyclerViewAdapter : RecyclerView.Adapter<FilterRecyclerViewAdapter.ViewHolder>() {

    private var list: List<PhotoFilter> = listOf()
    private var onItemClick: ((Int) -> Unit) = {}

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPhotoFilterBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindView(
            context = viewHolder.itemView.context,
            position = position
        )
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(private val binding: ItemPhotoFilterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(context: Context, position: Int) {
            val filter = list[position]
            binding.circleItemPhotoFilter.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, filter.color))
            binding.filterNameTextViewItemPhotoFilter.text = filter.name
            binding.root.setOnClickListener {
                onItemClick.invoke(position)
            }
        }
    }

    fun setUpData(list: List<PhotoFilter>, onItemClick: ((Int) -> Unit)) {
        this.list = list
        this.onItemClick = onItemClick
    }

    fun getItemByPosition(position: Int) = list[position]
}