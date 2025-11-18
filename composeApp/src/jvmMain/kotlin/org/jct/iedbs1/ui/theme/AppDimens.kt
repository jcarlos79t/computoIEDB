package org.jct.iedbs1.ui.theme

import androidx.compose.ui.unit.sp

/**
 * Desktop-specific implementation of AppDimens.
 * We're making the fonts slightly larger for better readability on a monitor.
 */
actual object AppDimens {
    actual val title = (18 * 1.25).sp
    actual val subtitle = (13 * 1.25).sp
    actual val card_title = (16 * 1.25).sp
    actual val card_subtitle = (14 * 1.25).sp
    actual val card_body = (12 * 1.25).sp
    actual val display = (24 * 1.25).sp
    actual val headline = (18 * 1.25).sp
    actual val body = (16 * 1.25).sp
    actual val label = (14 * 1.25).sp
    actual val caption = (12 * 1.25).sp
    actual val tiny = (10 * 1.25).sp
}
