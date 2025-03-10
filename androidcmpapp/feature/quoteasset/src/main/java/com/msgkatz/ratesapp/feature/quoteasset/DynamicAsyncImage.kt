package com.msgkatz.ratesapp.feature.quoteasset

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
//import coil.compose.AsyncImage
//import coil.compose.AsyncImagePainter.State.Error
//import coil.compose.AsyncImagePainter.State.Loading
//import coil.compose.rememberAsyncImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.Placeholder
import com.bumptech.glide.integration.compose.placeholder
import com.msgkatz.ratesapp.core.uikit.theme.LocalTintTheme

//import com.msgkatz.ratesapp.presentation.theme.LocalTintTheme

/**
 * A wrapper around [AsyncImage] which determines the colorFilter based on the theme
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DynamicAsyncImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    priceListUIState: PriceListUIState,
    //placeholder: Placeholder = placeholder(R.drawable.cur_bnb),
    //placeholder: Painter = painterResource(R.drawable.cur_bnb),
) {
    val iconTint = LocalTintTheme.current.iconTint
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    val placeholder by remember { mutableStateOf((priceListUIState as? PriceListUIState.PriceList)?.placeHolder ?: placeholder(R.drawable.cur_bnb) ) }
//    val imageLoader = rememberAsyncImagePainter(
//        model = imageUrl,
//        onState = { state ->
//            isLoading = state is Loading
//            isError = state is Error
//        },
//    )
    val isLocalInspection = LocalInspectionMode.current
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
//        if (isLoading && !isLocalInspection) {
//            // Display a progress bar while loading
//            CircularProgressIndicator(
//                modifier = Modifier
//                    .align(Alignment.Center)
//                    .size(80.dp),
//                color = MaterialTheme.colorScheme.tertiary,
//            )
//        }
//        Image(
//            contentScale = ContentScale.Crop,
//            painter = if (isError.not() && !isLocalInspection) imageLoader else placeholder,
//            contentDescription = contentDescription,
//            colorFilter = if (iconTint != null) ColorFilter.tint(iconTint) else null,
//        )

        if (isLocalInspection) {
            Image(
                contentScale = ContentScale.Crop,
                painter = painterResource(R.drawable.cur_bnb),
                contentDescription = contentDescription,
                colorFilter = if (iconTint != null) ColorFilter.tint(iconTint) else null,
            )
        } else {
            GlideImage(
                contentScale = ContentScale.Crop,
                model = imageUrl,
                loading = placeholder,
                failure = placeholder,
                contentDescription = contentDescription,
                colorFilter = if (iconTint != null) ColorFilter.tint(iconTint) else null,
                //modifier = Modifier.padding(1.dp).clickable(onClick = { }).fillMaxSize(),
            )
        }
    }
}
