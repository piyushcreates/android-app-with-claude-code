package com.gutelements.app.ui.settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gutelements.app.BuildConfig
import com.gutelements.app.R
import com.gutelements.app.ui.AppViewModels
import com.gutelements.app.ui.components.GutCard
import com.gutelements.app.ui.components.GutIcons
import com.gutelements.app.ui.components.GutLogo
import com.gutelements.app.ui.components.ScreenPadding
import com.gutelements.app.ui.components.SectionLabel
import com.gutelements.app.ui.components.TopHeader
import com.gutelements.app.ui.theme.BorderSubtle
import com.gutelements.app.ui.theme.CardWhite
import com.gutelements.app.ui.theme.Charcoal
import com.gutelements.app.ui.theme.Destructive
import com.gutelements.app.ui.theme.Sage
import com.gutelements.app.ui.theme.TextMuted

private const val WEBSITE_URL = "https://gutelements.com"

/** Must match the privacy policy URL entered in Play Console (see docs/release/privacy-policy.md). */
private const val PRIVACY_POLICY_URL = "https://gutelements.com/app-privacy"

@Composable
fun SettingsScreen(onBack: () -> Unit, viewModel: SettingsViewModel = viewModel(factory = AppViewModels.Factory)) {
    val context = LocalContext.current
    var confirmDeleteAll by remember { mutableStateOf(false) }
    val deletedMessage = stringResource(R.string.deleted_all)

    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        TopHeader(
            title = stringResource(R.string.settings),
            navigationIcon = GutIcons.Back,
            navigationLabel = stringResource(R.string.back),
            onNavigate = onBack,
        )
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = ScreenPadding)
                .navigationBarsPadding()
                .padding(top = 8.dp, bottom = 24.dp),
        ) {
            GutCard(Modifier.fillMaxWidth(), contentPadding = PaddingValues(0.dp)) {
                Column(Modifier.padding(16.dp)) {
                    CardTitle(GutIcons.Lock, stringResource(R.string.settings_privacy))
                    Spacer(Modifier.height(6.dp))
                    Text(stringResource(R.string.settings_privacy_body), style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                }
                HorizontalDivider(color = BorderSubtle)
                LinkRow(stringResource(R.string.settings_privacy_policy)) { openUrl(context, PRIVACY_POLICY_URL) }
            }
            Spacer(Modifier.height(12.dp))
            InfoCard(GutIcons.Info, stringResource(R.string.settings_disclaimer), stringResource(R.string.settings_disclaimer_body))
            Spacer(Modifier.height(12.dp))
            GutCard(Modifier.fillMaxWidth(), contentPadding = PaddingValues(0.dp)) {
                Column(Modifier.padding(16.dp)) {
                    CardTitle(stringResource(R.string.settings_about)) { GutLogo(Modifier.size(22.dp)) }
                    Spacer(Modifier.height(6.dp))
                    Text(stringResource(R.string.settings_about_body), style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                }
                HorizontalDivider(color = BorderSubtle)
                LinkRow(stringResource(R.string.settings_website)) { openUrl(context, WEBSITE_URL) }
            }

            Spacer(Modifier.height(28.dp))
            SectionLabel(stringResource(R.string.settings_data), Modifier.padding(start = 4.dp, bottom = 8.dp))
            GutCard(Modifier.fillMaxWidth(), onClick = { confirmDeleteAll = true }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(GutIcons.Trash, null, tint = Destructive, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(12.dp))
                    Text(stringResource(R.string.delete_all), style = MaterialTheme.typography.titleSmall, color = Destructive)
                }
            }

            Spacer(Modifier.height(24.dp))
            Text(
                stringResource(R.string.settings_version, BuildConfig.VERSION_NAME),
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                modifier = Modifier.fillMaxWidth().padding(start = 4.dp),
            )
        }
    }

    if (confirmDeleteAll) {
        AlertDialog(
            onDismissRequest = { confirmDeleteAll = false },
            containerColor = CardWhite,
            title = { Text(stringResource(R.string.delete_all_confirm_title)) },
            text = { Text(stringResource(R.string.delete_all_confirm_body)) },
            confirmButton = {
                TextButton(onClick = {
                    confirmDeleteAll = false
                    viewModel.deleteAll { Toast.makeText(context, deletedMessage, Toast.LENGTH_SHORT).show() }
                }) { Text(stringResource(R.string.delete_all_confirm), color = Destructive) }
            },
            dismissButton = { TextButton(onClick = { confirmDeleteAll = false }) { Text(stringResource(R.string.cancel)) } },
        )
    }
}

private fun openUrl(context: Context, url: String) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
    } catch (_: ActivityNotFoundException) {
        // No browser installed; nothing sensible to do.
    }
}

@Composable
private fun LinkRow(text: String, onClick: () -> Unit) {
    TextButton(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)) {
        Text(text, color = Sage, modifier = Modifier.weight(1f))
        Icon(GutIcons.External, null, tint = Sage, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun CardTitle(icon: ImageVector, title: String) =
    CardTitle(title) { Icon(icon, null, tint = Sage, modifier = Modifier.size(20.dp)) }

@Composable
private fun CardTitle(title: String, leading: @Composable () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        leading()
        Spacer(Modifier.width(10.dp))
        Text(title, style = MaterialTheme.typography.titleSmall, color = Charcoal)
    }
}

@Composable
private fun InfoCard(icon: ImageVector, title: String, body: String) {
    GutCard(Modifier.fillMaxWidth()) {
        CardTitle(icon, title)
        Spacer(Modifier.height(6.dp))
        Text(body, style = MaterialTheme.typography.bodyMedium, color = TextMuted)
    }
}
