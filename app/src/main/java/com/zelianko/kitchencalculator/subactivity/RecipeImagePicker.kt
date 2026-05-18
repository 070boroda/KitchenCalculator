package com.zelianko.kitchencalculator.subactivity

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.ui.theme.Dimens

/**
 * Карточка-загрузчик фото рецепта.
 *
 * @param initialUri если непустая строка — отрисовывается как существующая фотка
 * @param onImagePicked вызывается ровно один раз, когда пользователь выбрал/изменил фотку
 */
@Composable
fun RecipeImagePicker(
    initialUri: String,
    onImagePicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var imageUri: Uri? by remember(initialUri) {
        mutableStateOf(if (initialUri.isBlank()) null else initialUri.toUri())
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        runCatching {
            context.contentResolver.takePersistableUriPermission(
                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        imageUri = uri
    }

    // Уведомляем родителя только когда uri действительно поменялся, не на каждой рекомпозиции.
    LaunchedEffect(imageUri) {
        imageUri?.let { onImagePicked(it.toString()) }
    }

    Card(
        modifier = modifier
            .padding(horizontal = Dimens.SpacingL, vertical = Dimens.SpacingS)
            .height(180.dp),
        shape = RoundedCornerShape(Dimens.RadiusL),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.grey),
        ),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                IconButton(
                    modifier = Modifier.size(80.dp),
                    onClick = {
                        launcher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_camera_plus),
                        contentDescription = stringResource(id = R.string.cd_add_photo),
                        tint = Color.White,
                    )
                }
                Text(
                    text = stringResource(id = R.string.add_photo),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                )
            }
            imageUri?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = stringResource(id = R.string.cd_recipe_photo),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}
