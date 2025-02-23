package io.standard.paging3.ui.load

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

interface LoadComposable {

    @Composable
    fun LazyItemScope.PrependLoading()

    @Composable
    fun LazyItemScope.PrependError(error: Throwable, retry: () -> Unit)

    @Composable
    fun LazyItemScope.PrependNotLoading(endOfPaginationReached: Boolean)

    @Composable
    fun LazyItemScope.AppendLoading()

    @Composable
    fun LazyItemScope.AppendError(error: Throwable, retry: () -> Unit)

    @Composable
    fun LazyItemScope.AppendNotLoading(endOfPaginationReached: Boolean)

    @Composable
    fun LazyItemScope.Placeholders()
}


internal val DefaultLoadComposable = object : LoadComposable {
    @Composable
    override fun LazyItemScope.PrependLoading() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillParentMaxWidth()
                .padding(vertical = 5.dp)
                .wrapContentSize()
        ) {
            CircularProgressIndicator(
                strokeWidth = 1.dp,
                color = Color.Black,
                modifier = Modifier.size(12.dp)
            )
        }
    }

    @Composable
    override fun LazyItemScope.PrependError(error: Throwable, retry: () -> Unit) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillParentMaxWidth()
                .padding(vertical = 5.dp)
                .clickable {
                    retry()
                }
                .wrapContentSize()
        ) {
            Text(text = "load error,click to retry", fontSize = 12.sp, color = Color.Black)
        }
    }

    @Composable
    override fun LazyItemScope.PrependNotLoading(endOfPaginationReached: Boolean) {
    }

    @Composable
    override fun LazyItemScope.AppendLoading() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillParentMaxWidth()
                .padding(vertical = 5.dp)
                .wrapContentSize()
        ) {
            CircularProgressIndicator(
                strokeWidth = 1.dp,
                color = Color.Black,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "loading...", fontSize = 12.sp, color = Color.Black)
        }
    }

    @Composable
    override fun LazyItemScope.AppendError(error: Throwable, retry: () -> Unit) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillParentMaxWidth()
                .padding(vertical = 5.dp)
                .clickable {
                    retry()
                }
                .wrapContentSize()
        ) {
            Text(text = "load error,click to retry", fontSize = 12.sp, color = Color.Black)
        }
    }

    @Composable
    override fun LazyItemScope.AppendNotLoading(endOfPaginationReached: Boolean) {
        if (endOfPaginationReached) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillParentMaxWidth()
                    .padding(vertical = 5.dp)
                    .wrapContentSize()
            ) {
                Text(text = "no more data", fontSize = 12.sp, color = Color.Black)
            }
        }
    }

    @Composable
    override fun LazyItemScope.Placeholders() {

    }
}