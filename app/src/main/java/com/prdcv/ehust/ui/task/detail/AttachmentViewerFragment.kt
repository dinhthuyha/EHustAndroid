package com.prdcv.ehust.ui.task.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.navArgs
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.prdcv.ehust.R
import com.prdcv.ehust.base.BaseFragment

class AttachmentViewerFragment : BaseFragment() {
    private val args: AttachmentViewerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val url = "http://104.215.150.77:9000/attachment/${args.attachmentId}"

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = args.fileName ?: "") },
                            backgroundColor = colorResource(id = R.color.text_color),
                            contentColor = Color.White,
                            elevation = 2.dp
                        )
                    },
                ) {
                    // TODO: WebView only support pdf, word, excel, powerpoint file
                    WebViewContainer(url = url)
                }
            }
        }
    }

    @Composable
    private fun WebViewContainer(url: String) {
        val state = rememberWebViewState(url = "https://docs.google.com/viewer?embedded=true&url=$url")

        Column {
            val loadingState = state.loadingState
            if (loadingState is LoadingState.Loading) {
                LinearProgressIndicator(
                    progress = loadingState.progress,
                    modifier = Modifier.fillMaxWidth(),
                    color = colorResource(id = R.color.text_color)
                )
            }

            WebView(
                state = state,
                modifier = Modifier.weight(1f),
                onCreated = { webView ->
                    webView.settings.javaScriptEnabled = true
                }, client = object : AccompanistWebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        if (view?.title.isNullOrBlank()) view?.reload()
                    }
                })
        }
    }
}