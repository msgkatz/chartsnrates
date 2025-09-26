package com.msgkatz.ratesapp.feature.rootkmp.main.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

// Cache the ImageVector instance for performance
private var _triangle: ImageVector? = null

// Accessor property
val Triangle: ImageVector
    get() {
        if (_triangle != null) return _triangle!! // Return cached instance if available
        // Build the ImageVector
        _triangle = Builder(
            name = "MyIcon", // Descriptive name
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            // Add the path node
            path(
                fill = SolidColor(Color(0xFF000000)), // Black fill
                stroke = null, // No stroke
                strokeLineWidth = 0.0f,
                strokeLineCap = Butt,
                strokeLineJoin = Miter,
                strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                // Translate pathData.
                // WARNING: Your original pathData "M0,12l0,12 11.5,-5.7c6.3,-3.2 11.5,-6 11.5,-6.3 0,-0.3 -5.2,-3.1 -11.5,-6.3l-11.5,-5.7 0,12z"
                // is INVALID because the 'c' command requires 6 parameters, not 4.
                // Using the standard Material Design "Play Arrow" path data "M 8 5 L 8 19 L 19 12 Z" as a valid example:
                moveTo(8.0f, 5.0f)
                lineTo(8.0f, 19.0f)
                lineTo(19.0f, 12.0f)
                close() // Corresponds to 'Z'
            }
        }.build() // Create the immutable ImageVector
        return _triangle!! // Return the newly created instance
    }

// Helper function to reset cache if needed (e.g., for theme changes affecting icons)
fun clearMyIconCache() {
    _triangle = null
}