package com.jvrcoding.qrcraft.qr.presentation.history

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.designsystem.components.QRCraftToolbar
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.QRCraftTheme
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.onSurfaceAlt
import com.jvrcoding.qrcraft.core.presentation.util.fadingEdge
import com.jvrcoding.qrcraft.core.presentation.util.shareText
import com.jvrcoding.qrcraft.qr.domain.qr.QrDetailId
import com.jvrcoding.qrcraft.qr.presentation.history.components.HistoryItem
import com.jvrcoding.qrcraft.qr.presentation.history.components.HistoryItemMenu
import com.jvrcoding.qrcraft.qr.presentation.history.model.Tab
import com.jvrcoding.qrcraft.qr.presentation.models.QrTypeUi
import com.jvrcoding.qrcraft.qr.presentation.models.QrUi
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryScreenRoot(
    onNavigateToPreviewScreen: (QrDetailId) -> Unit,
    viewModel: HistoryViewModel = koinViewModel(),
) {

    val context = LocalContext.current
    HistoryScreen(
        state = viewModel.state,
        onAction = { action ->
            when(action) {
                is HistoryAction.OnShareClick -> {
                    context.shareText(action.qrContent)
                }
                is HistoryAction.OnItemClick -> {
                    onNavigateToPreviewScreen(action.qrId)
                }
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )

}

@Composable
fun HistoryScreen(
    state: HistoryState,
    onAction: (HistoryAction) -> Unit
) {
    Scaffold(
        topBar = {
            QRCraftToolbar(
                title = stringResource(R.string.scan_history),
                textColor = MaterialTheme.colorScheme.onSurface,
                showBackButton = false,
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        val pagerState = rememberPagerState(initialPage = 0, pageCount = { Tab.entries.size })
        val coroutineScope = rememberCoroutineScope()
        val indicatorPadding = 16.dp

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }
                .collect { page ->
                    onAction(HistoryAction.ChangeTab(Tab.entries[page]))
                }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            TabRow(
                selectedTabIndex = state.activeTab.ordinal,
                indicator = { tabPositions ->
                    val currentTab = tabPositions[state.activeTab.ordinal]

                    val targetLeft = if (state.activeTab.ordinal == 0) {
                        currentTab.left + indicatorPadding
                    } else {
                        currentTab.left
                    }

                    val targetRight = if (state.activeTab.ordinal == 1) {
                        currentTab.right - indicatorPadding
                    } else {
                        currentTab.right
                    }

                    val animatedLeft by animateDpAsState(targetLeft, label = "indicatorLeft")
                    val animatedRight by animateDpAsState(targetRight, label = "indicatorRight")

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.BottomStart)
                            .offset(x = animatedLeft)
                            .width(animatedRight - animatedLeft)
                            .height(2.dp)
                            .background(MaterialTheme.colorScheme.onSurface)
                    )
                },
                divider = {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            ) {
                Tab.entries.forEachIndexed { index, tab ->
                    val paddingValues = if (index == 0)
                        PaddingValues(start = 16.dp)
                    else PaddingValues(end = 16.dp)
                    Tab(
                        modifier = Modifier.padding(paddingValues),
                        selected = state.activeTab == tab,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                            onAction(HistoryAction.ChangeTab(tab))
                        },
                        selectedContentColor = MaterialTheme.colorScheme.onSurface,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceAlt,
                        text = {
                            Text(
                                text = tab.title.asString(),
                                style = MaterialTheme.typography.labelMedium,
                            )
                        },
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> {
                        HistoryList(
                            items = state.scannedQrs,
                            onAction = onAction
                        )
                    }
                    1 -> {
                        HistoryList(
                            items = state.generatedQrs,
                            onAction = onAction
                        )
                    }
                }
            }
        }

        if(state.selectedQr != null) {
            HistoryItemMenu(
                onShareClick = {
                    onAction(HistoryAction.OnShareClick(state.selectedQr.content))
                },
                onDeleteClick = {
                    onAction(HistoryAction.OnDeleteQrClick(state.selectedQr.id))
                },
                onDismiss = {
                    onAction(HistoryAction.OnDismissItemMenu)
                }
            )
        }
    }
}

@Composable
private fun HistoryList(
    items: List<QrUi>,
    onAction: (HistoryAction) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .fadingEdge(500f),
        contentPadding = PaddingValues(
            vertical = 8.dp,
            horizontal = 16.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            items = items,
            key = { it.id }
        ) { item ->
            HistoryItem(
                iconRes = item.qrType.icon,
                iconTint = item.qrType.iconColor,
                title = item.qrTitleText,
                content = item.content,
                dateTime = item.date,
                modifier = Modifier
                    .animateItem()
                    .padding(vertical = 4.dp)
                    .widthIn(max = 552.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .combinedClickable(
                        onClick = { onAction(HistoryAction.OnItemClick(item.id)) },
                        onLongClick = { onAction(HistoryAction.OnItemLongClick(item)) }
                    )
            )
        }
    }
}

@Preview
@Composable
private fun HistoryScreenPreview() {
    QRCraftTheme {
        HistoryScreen(
            state = HistoryState(
                scannedQrs = listOf(
                    QrUi(
                        id = "dwadawd",
                        qrTitleText = "Text",
                        content = "text",
                        qrType = QrTypeUi.TEXT,
                        date = "qwertry"

                    )
                )
            ),
            onAction = {}
        )
    }
}