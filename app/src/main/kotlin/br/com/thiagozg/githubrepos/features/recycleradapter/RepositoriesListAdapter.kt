package br.com.thiagozg.githubrepos.features.recycleradapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.thiagozg.githubrepos.base.bindImageView
import br.com.thiagozg.githubrepos.features.model.RepositoryVO
import kotlinx.android.synthetic.main.item_repository.view.*
import javax.inject.Inject


/*
 * Created by Thiago Zagui Giacomini on 18/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
class RepositoriesListAdapter @Inject constructor()
    : RecyclerView.Adapter<RepositoriesListAdapter.JobResultHolder>() {

    private val repositoryList = mutableListOf<RepositoryVO>()
//    private val asyncListDiffer = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobResultHolder =
        LayoutInflater.from(parent.context)
            .inflate(br.com.thiagozg.githubrepos.R.layout.item_repository, parent, false)
            .run { JobResultHolder(this) }

    override fun onBindViewHolder(holder: JobResultHolder, position: Int) =
//        holder.bind(asyncListDiffer.currentList[position])
        holder.bind(repositoryList[position])

//    override fun getItemCount() = asyncListDiffer.currentList.size
    override fun getItemCount() = repositoryList.size

    fun addItems(items: List<RepositoryVO>) {
        val positionStarted = repositoryList.size - 1
//        if (repositoryList.size == 100) {
//            val newList = repositoryList.slice(IntRange(75 - 1, 100)).toMutableList()
//            newList.addAll(items)
//            repositoryList.clear()
//            repositoryList.addAll(newList)
//        } else {
            repositoryList.addAll(items)
//        }
//        asyncListDiffer.submitList(repositoryList)
        notifyItemRangeInserted(positionStarted, repositoryList.size - 1)
    }

    inner class JobResultHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(vo: RepositoryVO) = with(itemView) {
            ivPhoto.bindImageView(vo.photoUrl)
            tvName.text = vo.name
            tvStarsCount.text = resources.getString(br.com.thiagozg.githubrepos.R.string.stars_count_label, vo.starsCount)
            tvForksCount.text = resources.getString(br.com.thiagozg.githubrepos.R.string.forks_count_label, vo.forkCount)
            tvAuthor.text = resources.getString(br.com.thiagozg.githubrepos.R.string.author_label, vo.authorName)
            setScaleAnimation()
        }

        private fun View.setScaleAnimation() {
            val anim = ScaleAnimation(
                0.0F,
                1.0F,
                0.0F,
                1.0F,
                Animation.RELATIVE_TO_SELF,
                PIVOT_VALUE,
                Animation.RELATIVE_TO_SELF,
                PIVOT_VALUE
            ).also {
                it.duration = FADE_DURATION
            }
            startAnimation(anim)
        }
    }

    private object DIFF_CALLBACK: DiffUtil.ItemCallback<RepositoryVO>() {
        override fun areItemsTheSame(oldItem: RepositoryVO, newItem: RepositoryVO) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: RepositoryVO, newItem: RepositoryVO) = oldItem == newItem
    }

    companion object {
        private const val FADE_DURATION = 300L
        private const val PIVOT_VALUE = 0.5f
    }

}