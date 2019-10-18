package br.com.thiagozg.githubrepos.features

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import br.com.thiagozg.githubrepos.R
import br.com.thiagozg.githubrepos.base.BaseActivity
import br.com.thiagozg.githubrepos.di.viewmodel.ViewModelFactory
import javax.inject.Inject

/*
 * Created by Thiago Zagui Giacomini on 17/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
class MainActivity : BaseActivity(R.layout.activity_main) {

    @Inject
    lateinit var viewModeFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MainViewModel> {
        viewModeFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}