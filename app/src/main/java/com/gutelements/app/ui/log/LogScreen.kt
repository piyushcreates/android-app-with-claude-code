package com.gutelements.app.ui.log

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gutelements.app.R
import com.gutelements.app.domain.BristolType
import com.gutelements.app.ui.AppViewModels
import com.gutelements.app.ui.components.BristolOptionCard
import com.gutelements.app.ui.components.DateTimeField
import com.gutelements.app.ui.components.GutIcons
import com.gutelements.app.ui.components.PrimaryButton
import com.gutelements.app.ui.components.ScreenPadding
import com.gutelements.app.ui.components.TopHeader
import com.gutelements.app.ui.theme.BorderSubtle
import com.gutelements.app.ui.theme.Charcoal
import com.gutelements.app.ui.theme.Linen
import com.gutelements.app.ui.theme.TextMuted

@Composable
fun LogScreen(onClose: () -> Unit, viewModel: LogViewModel = viewModel(factory = AppViewModels.Factory)) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize().background(Linen).statusBarsPadding()) {
        TopHeader(
            title = "",
            navigationIcon = GutIcons.Close,
            navigationLabel = stringResource(R.string.close),
            onNavigate = onClose,
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = ScreenPadding, end = ScreenPadding, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                Column {
                    Text(stringResource(R.string.log_bowel_movement), style = MaterialTheme.typography.headlineMedium, color = Charcoal)
                    Text(
                        stringResource(R.string.log_question),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
                    )
                }
            }
            items(BristolType.entries, key = { it.number }) { type ->
                BristolOptionCard(type, selected = state.selectedType == type.number, onSelect = { viewModel.select(type.number) })
            }
            item {
                DateTimeField(
                    value = state.dateTime,
                    onChange = viewModel::setDateTime,
                    isFuture = state.isFuture,
                    modifier = Modifier.padding(top = 12.dp),
                )
            }
        }
        // Sticky save bar.
        Column(Modifier.fillMaxWidth().background(Linen).navigationBarsPadding()) {
            HorizontalDivider(color = BorderSubtle)
            PrimaryButton(
                text = stringResource(R.string.save),
                icon = GutIcons.Check,
                enabled = state.canSave,
                onClick = { viewModel.save(onClose) },
                modifier = Modifier.padding(horizontal = ScreenPadding, vertical = 12.dp),
            )
        }
    }
}
