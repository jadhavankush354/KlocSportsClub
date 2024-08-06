package com.firstapplication.file.ui.ComponentsUI


import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource


@Composable
fun ZoomableImage(modifier: Modifier = Modifier, imageResId: Int) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .graphicsLayer(
                scaleX = maxOf(1f, scale),
                scaleY = maxOf(1f, scale),
                translationX = offset.x,
                translationY = offset.y
            )
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, rotation ->
                    scale *= zoom
                    offset += pan
                }
            }
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = "Zoomable Image",
            contentScale = ContentScale.Fit
        )
    }
}
