package org.kikermo.matrix8.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PedalItem(
    text: String,
    textColour: Color = Color.Black,
    enabled: Boolean,
    bgColour: Color = Color.White,
    clickListener: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(140.dp)
            .background(color = bgColour, shape = RoundedCornerShape(12.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = text,
                color = textColour,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        }

        Image(
            painter = painterResource(id = enabled.toImagerResourceId()),
            contentDescription = "LED",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(40.dp)
                .weight(2f)
        )

        Image(
            painter = painterResource(id = CommonR.drawable.ic_footswitch),
            contentDescription = "Footswitch",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(60.dp)
                .weight(1f)
                .clickable { clickListener?.invoke() }
        )
    }
}

private fun Boolean.toImagerResourceId() = when (this) {
    true -> CommonR.drawable.led_on
    false -> CommonR.drawable.led_off
}

@Preview
@Composable
fun PedalPreview() {
    PedalItem(
        text = "Tube Screamer",
        textColour = Color.White,
        enabled = true,
        bgColour = Color(0xff4ea541)
    )
}
