package com.prdcv.ehust.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.fragment.navArgs
import com.prdcv.ehust.R
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.databinding.FragmentProfileBinding
import com.prdcv.ehust.model.User
import com.prdcv.ehust.ui.compose.ComposeTutorialTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragmentWithBinding<FragmentProfileBinding>() {
    private var user: User? = null
    var args: ProfileFragmentArgs? = null
    private val TAG = "ProfileFragment"

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.composeViewProfile.setContent {
            ComposeTutorialTheme {
                Column {
                    Profile()
                }
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun Profile() {
        ComposeTutorialTheme {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(Modifier.padding(bottom = 65.dp)) {
                    Box(contentAlignment = Alignment.BottomCenter) {
                        Image(painter = painterResource(id = R.drawable.background_hust), null)
                        Image(
                            painter = painterResource(id = R.drawable.ic_avatar_default),
                            contentDescription = null,
                            modifier = Modifier
                                .offset(y = 45.dp)
                                .size(90.dp)
                                .clip(CircleShape)
                                .border(3.dp, MaterialTheme.colors.secondaryVariant, CircleShape)
                        )
                    }
                }
                Text(text = user?.fullName ?: "Đinh Thúy Hà", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                Row {
                    Card(elevation = 4.dp) {
                        Column {
                            Row {
                                Text(text = "Ma sinh vien")
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(text = "20173086")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initArgs() {
        try {
            user = shareViewModel.user
            val args: ProfileFragmentArgs by navArgs()
            user = args.user
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentProfileBinding =
        FragmentProfileBinding.inflate(inflater).apply {
            lifecycleOwner = viewLifecycleOwner
        }

    override fun init() {}

}