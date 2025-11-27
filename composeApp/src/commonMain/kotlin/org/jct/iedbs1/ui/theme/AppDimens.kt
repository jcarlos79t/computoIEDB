package org.jct.iedbs1.ui.theme

import androidx.compose.ui.unit.TextUnit

/**
 * A multiplatform dimension definition object.
 * It's declared as an `expect` object in commonMain, with `actual`
 * implementations in androidMain and jvmMain.
 */
expect object AppDimens {
    val title: TextUnit
    val subtitle: TextUnit
    val card_title: TextUnit
    val card_subtitle: TextUnit
    val card_body: TextUnit
    val display: TextUnit
    val headline: TextUnit
    val body: TextUnit
    val label: TextUnit
    val caption: TextUnit
    val tiny: TextUnit
    val title_main: TextUnit
}
