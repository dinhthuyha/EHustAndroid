package com.prdcv.ehust.ui.task.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.prdcv.ehust.R
import com.prdcv.ehust.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AttachmentViewerFragment : BaseFragment() {
    private val args: AttachmentViewerFragmentArgs by navArgs()
    private val viewModel: DetailTaskViewModel by viewModels()

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
                            title = { Text(text = args.fileName ?: "", fontSize = 15.sp) },
                            backgroundColor = colorResource(id = R.color.text_color),
                            contentColor = Color.White,
                            elevation = 2.dp,
                            actions = {
                                IconButton(onClick = { /* doSomething() */ }) {
                                    Icon(
                                        painterResource(id = R.drawable.ic_download ),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .clickable {
                                                viewModel.downloadFile(url, requireContext(), args.fileName?:"")
                                            }

                                    )
                                }
                            }
                        )
                    },
                ) {
                    // TODO: WebView only support pdf, word, excel, powerpoint file
                    if (args.fileName?.isImageFile() == true) {
                        WebViewContainer(url = url, true)
                    } else {
                        WebViewContainer(url = "https://docs.google.com/viewer?embedded=true&url=$url")
                    }
                }
            }
        }
    }

    @Composable
    private fun WebViewContainer(url: String, zoomEnabled: Boolean = false) {
        val state = rememberWebViewState(url = url)

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
                    webView.settings.setSupportZoom(zoomEnabled)
                    webView.settings.builtInZoomControls = zoomEnabled
                    webView.settings.useWideViewPort = true
                    webView.setInitialScale(1)
                }, client = object : AccompanistWebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        if (view?.title.isNullOrBlank()) view?.reload()
                    }
                })
        }
    }

    private val imageExtensionRegex =
        "^.*\\.(gif|jpe?g|tiff?|png|webp|bmp)\$".toRegex(RegexOption.IGNORE_CASE)

    private fun String.isImageFile(): Boolean = this.matches(imageExtensionRegex)
}