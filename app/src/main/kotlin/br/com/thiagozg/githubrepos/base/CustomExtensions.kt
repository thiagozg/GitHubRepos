package br.com.thiagozg.githubrepos.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import br.com.thiagozg.githubrepos.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/*
 * Created by Thiago Zagui Giacomini on 18/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
fun ImageView.bindImageView(
    photoUrl: String
) {
    Glide.with(this)
        .load(photoUrl)
        .centerCrop()
        .placeholder(R.drawable.placehold_background)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

inline fun <T : Any> LiveData<T>.observeNonNull(
    lifecycleOwner: LifecycleOwner,
    crossinline onChanged: (T) -> Unit
) {
    observe(lifecycleOwner, Observer {
        it ?: return@Observer
        onChanged.invoke(it)
    })
}

fun <T> Single<T>.handleSchedulers() = subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())

fun Context.isOnline(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager?.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}