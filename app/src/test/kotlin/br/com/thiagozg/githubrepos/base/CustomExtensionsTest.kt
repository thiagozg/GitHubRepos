package br.com.thiagozg.githubrepos.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import br.com.thiagozg.githubrepos.CustomApplication
import br.com.thiagozg.githubrepos.features.MainActivity
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.reactivex.Single
import io.reactivex.internal.operators.single.SingleObserveOn
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.Robolectric.setupActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/*
 * Created by Thiago Zagui Giacomini on 20/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = CustomApplication::class, sdk = [Build.VERSION_CODES.O_MR1])
class CustomExtensionsTest {

    private val context = ApplicationProvider.getApplicationContext<CustomApplication>()

    @Test
    fun bindImageViewTest() {
        val imageView = ImageView(context)
        assertThat(imageView.drawable).isNull()
        imageView.bindImageView("")
        assertThat(imageView.drawable).isNotNull
    }

    @Test
    fun observeNonNullTest() {
        // given
        var countOnChanged = 0
        class TestActivity : AppCompatActivity()
        val spykLiveData = spyk(MutableLiveData<Int>())
        val activity = buildActivity(TestActivity::class.java).setup().get()

        // when
        spykLiveData.observeNonNull(activity) {
            countOnChanged++
        }
        spykLiveData.value = null

        // then
        assertThat(countOnChanged).isEqualTo(0)
    }

    @Test
    fun handleSchedulersTest() {
        val single = Single.just(0).handleSchedulers()
        assertThat(single).isExactlyInstanceOf(SingleObserveOn::class.java)
    }

    @Test
    fun isOnlineTest() {
        val mockConnectivityManager = mockk<ConnectivityManager>()
        val spykContext = spyk(context)
        every {
            spykContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        } returns mockConnectivityManager

        val mockActiveNetwork = mockk<NetworkInfo>()
        every {
            mockConnectivityManager.activeNetworkInfo
        } returns mockActiveNetwork

        every { mockActiveNetwork.isConnected } returns true
        assertThat(spykContext.isOnline()).isTrue()

        every { mockActiveNetwork.isConnected } returns false
        assertThat(spykContext.isOnline()).isFalse()

    }

}