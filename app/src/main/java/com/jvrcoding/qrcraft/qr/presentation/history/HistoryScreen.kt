package com.jvrcoding.qrcraft.qr.presentation.history

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.designsystem.components.QRCraftToolbar
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.OnSurfaceAlt
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.QRCraftTheme
import com.jvrcoding.qrcraft.qr.presentation.history.model.Tab
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryScreenRoot(
    viewModel: HistoryViewModel = koinViewModel(),
) {

    HistoryScreen(
        state = viewModel.state,
        onAction = { action ->
            viewModel.onAction(action)
        }
    )

}

@Composable
fun HistoryScreen(
    state: HistoryState,
    onAction: (HistoryAction) -> Unit,
) {
    Scaffold(
        topBar = {
            QRCraftToolbar(
                title = stringResource(R.string.scan_history),
                textColor = MaterialTheme.colorScheme.onSurface,
                showBackButton = false,
            )
        }
    ) { innerPadding ->
        val indicatorPadding = 16.dp
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
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
                Tab(
                    modifier = Modifier.padding(start = 16.dp),
                    selected = state.activeTab == Tab.SCANNED,
                    onClick = { onAction(HistoryAction.ChangeTab(Tab.SCANNED)) },
                    selectedContentColor = MaterialTheme.colorScheme.onSurface,
                    unselectedContentColor = OnSurfaceAlt,
                    text = {
                        Text(
                            text = stringResource(R.string.scanned),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    },
                )
                Tab(
                    modifier = Modifier.padding(end = 16.dp),
                    selected = state.activeTab == Tab.GENERATED,
                    selectedContentColor = MaterialTheme.colorScheme.onSurface,
                    unselectedContentColor = OnSurfaceAlt,
                    onClick = { onAction(HistoryAction.ChangeTab(Tab.GENERATED)) },
                    text = {
                        Text(
                            text = stringResource(R.string.generated),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                )
            }

            LazyColumn {
//            items(state.filteredItems) { item ->
//                ListItem(item)
//            }
            }
        }
    }

}

@Preview
@Composable
private fun HistoryScreenPreview() {
    QRCraftTheme {
        HistoryScreen(
            state = HistoryState(),
            onAction = {}
        )
    }
}