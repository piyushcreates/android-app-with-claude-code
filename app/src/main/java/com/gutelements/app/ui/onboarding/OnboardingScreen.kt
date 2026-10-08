package com.gutelements.app.ui.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gutelements.app.R
import com.gutelements.app.domain.BristolType
import com.gutelements.app.ui.components.BristolIllustration
import com.gutelements.app.ui.components.GutCard
import com.gutelements.app.ui.components.GutIcons
import com.gutelements.app.ui.components.GutLogo
import com.gutelements.app.ui.components.PrimaryButton
import com.gutelements.app.ui.components.ScreenPadding
import com.gutelements.app.ui.theme.BorderSubtle
import com.gutelements.app.ui.theme.Charcoal
import com.gutelements.app.ui.theme.Linen
import com.gutelements.app.ui.theme.MatchaTint
import com.gutelements.app.ui.theme.Sage
import com.gutelements.app.ui.theme.SageDeep
import com.gutelements.app.ui.theme.SageTile
import com.gutelements.app.ui.theme.TextMuted

@Composable
fun OnboardingScreen(onStarted: () -> Unit, onFinished: () -> Unit) {
    var page by rememberSaveable { mutableIntStateOf(0) }
    LaunchedEffect(Unit) { onStarted() }
    BackHandler(enabled = page == 1) { page = 0 }

    Column(Modifier.fillMaxSize().background(Linen).statusBarsPadding().navigationBarsPadding()) {
        AnimatedContent(
            targetState = page,
            modifier = Modifier.weight(1f),
            transitionSpec = {
                val dir = if (targetState > initialState) 1 else -1
                (slideInHorizontally { it / 5 * dir } + fadeIn()) togetherWith (slideOutHorizontally { -it / 5 * dir } + fadeOut())
            },
            label = "onboarding",
        ) { current ->
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = ScreenPadding),
            ) {
                if (current == 0) WelcomePage() else ScalePage()
            }
        }
        Column(Modifier.padding(horizontal = ScreenPadding, vertical = 16.dp)) {
            PageDots(page)
            Spacer(Modifier.height(16.dp))
            if (page == 0) {
                PrimaryButton(stringResource(R.string.onb_continue), onClick = { page = 1 })
            } else {
                PrimaryButton(stringResource(R.string.onb_get_started), onClick = onFinished)
            }
        }
    }
}

@Composable
private fun WelcomePage() {
    Spacer(Modifier.height(56.dp))
    GutLogo(Modifier.size(72.dp))
    Spacer(Modifier.height(16.dp))
    Text(stringResource(R.string.app_name).uppercase(), style = MaterialTheme.typography.labelSmall, color = Sage)
    Spacer(Modifier.height(8.dp))
    Text(stringResource(R.string.onb1_title), style = MaterialTheme.typography.displaySmall.copy(fontSize = MaterialTheme.typography.headlineMedium.fontSize * 1.15f), color = Charcoal)
    Spacer(Modifier.height(36.dp))
    Benefit(GutIcons.Bolt, stringResource(R.string.onb1_benefit1_title), stringResource(R.string.onb1_benefit1_body))
    Benefit(GutIcons.Calendar, stringResource(R.string.onb1_benefit2_title), stringResource(R.string.onb1_benefit2_body))
    Benefit(GutIcons.Insights, stringResource(R.string.onb1_benefit3_title), stringResource(R.string.onb1_benefit3_body))
}

@Composable
private fun Benefit(icon: ImageVector, title: String, body: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(44.dp).background(SageTile, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = Sage, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title, style = MaterialTheme.typography.titleSmall, color = Charcoal)
            Text(body, style = MaterialTheme.typography.bodyMedium, color = TextMuted)
        }
    }
}

@Composable
private fun ScalePage() {
    Spacer(Modifier.height(32.dp))
    Text(stringResource(R.string.onb2_title), style = MaterialTheme.typography.headlineMedium, color = Charcoal)
    Spacer(Modifier.height(8.dp))
    Text(stringResource(R.string.onb2_body), style = MaterialTheme.typography.bodyMedium, color = TextMuted)
    Spacer(Modifier.height(20.dp))
    GutCard(Modifier.fillMaxWidth(), contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)) {
        BristolType.entries.forEachIndexed { index, type ->
            if (index > 0) HorizontalDivider(color = BorderSubtle)
            Row(Modifier.fillMaxWidth().padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                BristolIllustration(type.number, size = 36.dp)
                Spacer(Modifier.width(14.dp))
                Text(
                    stringResource(R.string.type_number, type.number),
                    style = MaterialTheme.typography.titleSmall,
                    color = Charcoal,
                    modifier = Modifier.width(64.dp),
                )
                Text(stringResource(type.nameRes), style = MaterialTheme.typography.bodyMedium, color = TextMuted)
            }
        }
    }
    Spacer(Modifier.height(12.dp))
    Text(
        stringResource(R.string.onb2_typical),
        style = MaterialTheme.typography.bodyMedium,
        color = SageDeep,
        modifier = Modifier.fillMaxWidth().background(MatchaTint, RoundedCornerShape(14.dp)).padding(14.dp),
    )
    Spacer(Modifier.height(12.dp))
    Text(
        stringResource(R.string.onb_disclaimer),
        style = MaterialTheme.typography.bodySmall,
        color = TextMuted,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun PageDots(page: Int) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        repeat(2) { index ->
            Box(
                Modifier
                    .padding(horizontal = 3.dp)
                    .size(width = if (index == page) 20.dp else 6.dp, height = 6.dp)
                    .background(if (index == page) Sage else BorderSubtle, CircleShape),
            )
        }
    }
}
