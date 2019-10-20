package br.com.thiagozg.githubrepos.features.recycleradapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
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
    : RecyclerView.Adapter<RepositoriesListAdapter.RepositoryHolder>() {

    private val repositoryList = mutableListOf<RepositoryVO>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryHolder =
        LayoutInflater.from(parent.context)
            .inflate(br.com.thiagozg.githubrepos.R.layout.item_repository, parent, false)
            .run { RepositoryHolder(this) }

    override fun onBindViewHolder(holder: RepositoryHolder, position: Int) =
        holder.bind(repositoryList[position])

    override fun getItemCount() = repositoryList.size

    fun addItems(items: List<RepositoryVO>) {
        val positionStarted = repositoryList.size
        repositoryList.addAll(items)
        notifyItemRangeInserted(positionStarted, repositoryList.size - 1)
    }

    inner class RepositoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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

    companion object {
        private const val FADE_DURATION = 300L
        private const val PIVOT_VALUE = 0.5f
    }

}