package com.andro_sk.eventnotes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import com.andro_sk.eventnotes.domain.contracts.NavigationReceiver
import com.andro_sk.eventnotes.ui.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigationReceiver: NavigationReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppNavigation(
                navigationReceiver = navigationReceiver
            )
        }
    }
}