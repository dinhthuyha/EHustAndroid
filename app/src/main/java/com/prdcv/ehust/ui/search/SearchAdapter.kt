package com.prdcv.ehust.ui.search
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.prdcv.ehust.R
import com.prdcv.ehust.base.recyclerview.BaseDiffUtilItemCallback
import com.prdcv.ehust.base.recyclerview.BaseRecyclerAdapter
import com.prdcv.ehust.base.recyclerview.BaseViewHolder
import com.prdcv.ehust.model.ClassStudent

class SearchAdapter(
    private val clickListener: (ItemSearch) -> Unit
) : BaseRecyclerAdapter<ItemSearch, BaseViewHolder<ItemSearch, ViewDataBinding>>(ItemDiffUtilCallback()) {

    class ItemDiffUtilCallback() : BaseDiffUtilItemCallback<ItemSearch>() {
        override fun areContentsTheSame(oldItem: ItemSearch, newItem: ItemSearch): Boolean =
            oldItem == newItem
    }

    override fun getItemLayoutResource(viewType: Int): Int {
        return if (viewType == ViewType.SEARCH_CLASS.type)
            R.layout.item_search_class
        else
            R.layout.item_teacher_student
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ItemSearch, ViewDataBinding> {

        val viewDataBinding = getViewHolderDataBinding(parent, viewType)
        return when(viewType){
            ViewType.SEARCH_CLASS.type -> {
                SearchClassViewHolder(viewDataBinding, clickListener)
            }
            else -> {
                SearchUserViewHolder(viewDataBinding, clickListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
       return when(currentList[0] as? ClassStudent){
            null -> ViewType.SEARCH_USER.type
           else ->ViewType.SEARCH_CLASS.type
        }
    }
}