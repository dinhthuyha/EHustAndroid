package com.prdcv.ehust.ui.profile

import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.prdcv.ehust.R
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.model.User
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.main.MainFragmentDirections
import com.prdcv.ehust.utils.SharedPreferencesKey

@Composable
fun ProfileInfoRow(label: String, content: String) {
    Column(
        modifier = Modifier
            .padding(all = 2.dp)
    ) {
        Divider(thickness = 0.5.dp)
        Spacer(modifier = Modifier.size(6.dp))
        Text(text = label, fontSize = 13.sp, fontWeight = FontWeight.Light)
        SelectionContainer {
            Text(text = content, fontSize = 15.sp)
        }
    }
}

@Composable
fun ProfilePicture() {
    Image(
        painter = painterResource(id = R.drawable.ic_avatar_default),
        contentDescription = null,
        modifier = Modifier
            .padding(bottom = 15.dp)
            .size(120.dp)
            .clip(CircleShape)
            .border(
                2.dp,
                MaterialTheme.colors.secondaryVariant,
                CircleShape
            )
    )
}

@Composable
fun ProfileInfoStudent(user: User?) {
    Column(modifier = Modifier.width(IntrinsicSize.Max)) {

        ProfileInfoRow(
            label = stringResource(id = R.string.title_id_user),
            content = user?.id.toString()
        )
        ProfileInfoRow(
            label = stringResource(id = R.string.title_class),
            content = user?.grade.toString()
        )
        ProfileInfoRow(
            label = stringResource(id = R.string.title_course),
            content = user?.course.toString()
        )
        ProfileInfoRow(
            label = stringResource(id = R.string.title_institute),
            content = user?.instituteOfManagement.toString()
        )
        ProfileInfoRow(
            label = stringResource(id = R.string.title_email),
            content = user?.email.toString()
        )
    }
}

@Composable
fun ProfileInfoTeacher(user: User?) {
    Column(modifier = Modifier.width(IntrinsicSize.Max)) {

        ProfileInfoRow(
            label = "Mã cán bộ",
            content = user?.id.toString()
        )
        ProfileInfoRow(
            label = "Trạng thái cán bộ",
            content = user?.cadreStatus.toString()
        )
        ProfileInfoRow(
            label = stringResource(id = R.string.title_institute),
            content = user?.instituteOfManagement.toString()
        )
        ProfileInfoRow(
            label = "Đơn vị",
            content = user?.unit.toString()
        )
        ProfileInfoRow(
            label = stringResource(id = R.string.title_email),
            content = user?.email.toString()
        )
    }
}

@Composable
fun ToolBar(title: String) {
    TopAppBar(backgroundColor = colorResource(id = R.color.text_color)) {
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun ProfileName(fullName: String?) {
    Text(
        text = fullName.toString(),
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 15.dp)
    )
}

@Composable
fun ProfileCard(user: User?,navigateFromAdmin: Boolean = false,  navController: NavController, sharedPreferences: SharedPreferences) {
    DefaultTheme {
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            item { ToolBar(stringResource(id = R.string.profile_title)) }
            item { Row {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painter = painterResource(id = R.drawable.background_hust), null)
                    Surface(
                        elevation = 5.dp,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .padding(all = 15.dp)
                            .offset(y = (-60).dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(all = 15.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ProfilePicture()
                            ProfileName(user?.fullName)
                            when (user?.roleId) {
                                Role.ROLE_TEACHER -> ProfileInfoTeacher(user)
                                Role.ROLE_STUDENT -> ProfileInfoStudent(user)
                                else -> {}
                            }
                        }
                    }
                    Button(
                        onClick = {
                            try {
                                navController?.navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
                            } catch (e: Exception) {
                                navController?.navigate(MainFragmentDirections.actionMainFragmentToLoginFragment())
                            }
                            sharedPreferences.edit().remove(SharedPreferencesKey.TOKEN).commit()
                        },
                        modifier = Modifier
                            .width(200.dp)
                            .offset(y = (-30).dp)
                            .alpha(if(navigateFromAdmin) 0f else 1f),
                        content = {
                            Text(
                                text = "Logout",
                                style = MaterialTheme.typography.button,
                                color = Color.White
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = com.prdcv.ehust.ui.compose.Button
                        )
                    )
                }
            } }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DefaultPreview() {
    val placeholderUser = User(
        id = 20173086,
        roleId = Role.ROLE_STUDENT,
        course = "K62",
        grade = "Lớp kỹ thuật máy tính 08 - K62",
        fullName = "Đinh Thúy Hà",
        instituteOfManagement = "Viện công nghệ thông tin và truyền thông",
        email = "ha.dt173086@sis.hust.edu.vn"
    )
    //ProfileCard(placeholderUser)
}