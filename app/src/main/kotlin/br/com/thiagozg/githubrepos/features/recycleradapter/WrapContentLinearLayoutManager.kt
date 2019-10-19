package br.com.thiagozg.githubrepos.features.recycleradapter

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager


/*
 * Created by Thiago Zagui Giacomini on 19/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
class WrapContentLinearLayoutManager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
): LinearLayoutManager(context, attrs, defStyleAttr, defStyleRes) {

    override fun supportsPredictiveItemAnimations() = false

}