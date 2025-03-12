@file:Suppress("PackageDirectoryMismatch")
//import android.graphics.drawable.BitmapDrawable
//import android.graphics.drawable.Drawable
//import androidx.compose.runtime.RememberObserver
//import androidx.compose.runtime.Stable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableFloatStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.compose.runtime.snapshotFlow
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.ColorFilter
//import androidx.compose.ui.graphics.DefaultAlpha
//import androidx.compose.ui.graphics.asImageBitmap
//import androidx.compose.ui.graphics.drawscope.DrawScope
//import androidx.compose.ui.graphics.painter.BitmapPainter
//import androidx.compose.ui.graphics.painter.Painter
//import androidx.compose.ui.layout.ContentScale
////import coil.ImageLoader
////import coil.compose.CrossfadePainter
////import coil.compose.fakeTransitionTarget
////import coil.compose.toScale
////import coil.compose.toSizeOrNull
////import coil.request.ErrorResult
////import coil.request.ImageRequest
////import coil.request.ImageResult
////import coil.request.SuccessResult
////import coil.size.Precision
////import coil.transition.CrossfadeTransition
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.SupervisorJob
//import kotlinx.coroutines.cancel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.flow.mapLatest
//import kotlinx.coroutines.flow.mapNotNull
//import kotlinx.coroutines.launch
//
//
///**
// * A [Painter] that that executes an [ImageRequest] asynchronously and renders the result.
// */
//@Stable
//class AsyncImagePainter internal constructor(
//    request: ImageRequest,
//    imageLoader: ImageLoader,
//) : Painter(), RememberObserver {
//
//    private var rememberScope: CoroutineScope? = null
//    private val drawSize = MutableStateFlow(Size.Zero)
//
//    private var painter: Painter? by mutableStateOf(null)
//    private var alpha: Float by mutableFloatStateOf(DefaultAlpha)
//    private var colorFilter: ColorFilter? by mutableStateOf(null)
//
//    // These fields allow access to the current value
//    // instead of the value in the current composition.
//    private var _state: State = State.Empty
//        set(value) {
//            field = value
//            state = value
//        }
//    private var _painter: Painter? = null
//        set(value) {
//            field = value
//            painter = value
//        }
//
//    internal var transform = DefaultTransform
//    internal var onState: ((State) -> Unit)? = null
//    internal var contentScale = ContentScale.Fit
//    internal var filterQuality = DrawScope.DefaultFilterQuality
//    internal var isPreview = false
//
//    /** The current [AsyncImagePainter.State]. */
//    var state: State by mutableStateOf(State.Empty)
//        private set
//
//    /** The current [ImageRequest]. */
//    var request: ImageRequest by mutableStateOf(request)
//        internal set
//
//    /** The current [ImageLoader]. */
//    var imageLoader: ImageLoader by mutableStateOf(imageLoader)
//        internal set
//
//    override val intrinsicSize: Size
//        get() = painter?.intrinsicSize ?: Size.Unspecified
//
//    override fun DrawScope.onDraw() {
//        // Update the draw scope's current size.
//        drawSize.value = size
//
//        // Draw the current painter.
//        painter?.apply { draw(size, alpha, colorFilter) }
//    }
//
//    override fun applyAlpha(alpha: Float): Boolean {
//        this.alpha = alpha
//        return true
//    }
//
//    override fun applyColorFilter(colorFilter: ColorFilter?): Boolean {
//        this.colorFilter = colorFilter
//        return true
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    override fun onRemembered() {
//        // Short circuit if we're already remembered.
//        if (rememberScope != null) return
//
//        // Create a new scope to observe state and execute requests while we're remembered.
//        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
//        rememberScope = scope
//
//        // Manually notify the child painter that we're remembered.
//        (_painter as? RememberObserver)?.onRemembered()
//
//        // If we're in inspection mode skip the image request and set the state to loading.
//        if (isPreview) {
//            val request = request.newBuilder().defaults(imageLoader.defaults).build()
//            updateState(State.Loading(request.placeholder?.toPainter()))
//            return
//        }
//
//        // Observe the current request and execute any emissions.
//        scope.launch {
//            snapshotFlow { request }
//                .mapLatest { imageLoader.execute(updateRequest(request)).toState() }
//                .collect(::updateState)
//        }
//    }
//
//    override fun onForgotten() {
//        clear()
//        (_painter as? RememberObserver)?.onForgotten()
//    }
//
//    override fun onAbandoned() {
//        clear()
//        (_painter as? RememberObserver)?.onAbandoned()
//    }
//
//    private fun clear() {
//        rememberScope?.cancel()
//        rememberScope = null
//    }
//
//    /** Update the [request] to work with [AsyncImagePainter]. */
//    private fun updateRequest(request: ImageRequest): ImageRequest {
//        return request.newBuilder()
//            .target(
//                onStart = { placeholder ->
//                    updateState(State.Loading(placeholder?.toPainter()))
//                },
//            )
//            .apply {
//                if (request.defined.sizeResolver == null) {
//                    // If no other size resolver is set, suspend until the canvas size is positive.
//                    size { drawSize.mapNotNull { it.toSizeOrNull() }.first() }
//                }
//                if (request.defined.scale == null) {
//                    // If no other scale resolver is set, use the content scale.
//                    scale(contentScale.toScale())
//                }
//                if (request.defined.precision != Precision.EXACT) {
//                    // AsyncImagePainter scales the image to fit the canvas size at draw time.
//                    precision(Precision.INEXACT)
//                }
//            }
//            .build()
//    }
//
//    private fun updateState(input: State) {
//        val previous = _state
//        val current = transform(input)
//        _state = current
//        _painter = maybeNewCrossfadePainter(previous, current) ?: current.painter
//
//        // Manually forget and remember the old/new painters if we're already remembered.
//        if (rememberScope != null && previous.painter !== current.painter) {
//            (previous.painter as? RememberObserver)?.onForgotten()
//            (current.painter as? RememberObserver)?.onRemembered()
//        }
//
//        // Notify the state listener.
//        onState?.invoke(current)
//    }
//
//    /** Create and return a [CrossfadePainter] if requested. */
//    private fun maybeNewCrossfadePainter(previous: State, current: State): CrossfadePainter? {
//        // We can only invoke the transition factory if the state is success or error.
//        val result = when (current) {
//            is State.Success -> current.result
//            is State.Error -> current.result
//            else -> return null
//        }
//
//        // Invoke the transition factory and wrap the painter in a `CrossfadePainter` if it returns
//        // a `CrossfadeTransformation`.
//        val transition = result.request.transitionFactory.create(fakeTransitionTarget, result)
//        if (transition is CrossfadeTransition) {
//            return CrossfadePainter(
//                start = previous.painter.takeIf { previous is State.Loading },
//                end = current.painter,
//                contentScale = contentScale,
//                durationMillis = transition.durationMillis,
//                fadeStart = result !is SuccessResult || !result.isPlaceholderCached,
//                preferExactIntrinsicSize = transition.preferExactIntrinsicSize,
//            )
//        } else {
//            return null
//        }
//    }
//
//    private fun ImageResult.toState() = when (this) {
//        is SuccessResult -> State.Success(drawable.toPainter(), this)
//        is ErrorResult -> State.Error(drawable?.toPainter(), this)
//    }
//
//    /** Convert this [Drawable] into a [Painter] using Compose primitives if possible. */
//    private fun Drawable.toPainter() = when (this) {
//        is BitmapDrawable -> BitmapPainter(bitmap.asImageBitmap(), filterQuality = filterQuality)
//        else -> DrawablePainter(mutate())
//    }
//
//    /**
//     * The current state of the [AsyncImagePainter].
//     */
//    sealed class State {
//
//        /** The current painter being drawn by [AsyncImagePainter]. */
//        abstract val painter: Painter?
//
//        /** The request has not been started. */
//        data object Empty : State() {
//            override val painter: Painter? get() = null
//        }
//
//        /** The request is in-progress. */
//        data class Loading(
//            override val painter: Painter?,
//        ) : State()
//
//        /** The request was successful. */
//        data class Success(
//            override val painter: Painter,
//            val result: SuccessResult,
//        ) : State()
//
//        /** The request failed due to [ErrorResult.throwable]. */
//        data class Error(
//            override val painter: Painter?,
//            val result: ErrorResult,
//        ) : State()
//    }
//
//    companion object {
//        /**
//         * A state transform that does not modify the state.
//         */
//        val DefaultTransform: (State) -> State = { it }
//    }
//}