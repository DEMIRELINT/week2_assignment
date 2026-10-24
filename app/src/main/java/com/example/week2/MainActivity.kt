package com.example.week2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.week2.ui.theme.DevClusterTheme
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        setContent { DevClusterTheme { DevClusterScreen() } }
    }
}

/** Single state owner. All custom UI below receives values and emits callbacks. */
@Composable
fun DevClusterScreen() {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf<String?>(null) }
    // Save only the eight stock counts; fixed IDs, names and prices come from the dataset.
    val stockSaver = listSaver<List<HardwareItem>, Int>(
        save = { items -> items.map { it.stockCount } },
        restore = { counts ->
            initialHardwareItems.mapIndexed { index, item -> item.copy(stockCount = counts[index]) }
        }
    )
    var hardwareItems by rememberSaveable(stateSaver = stockSaver) {
        mutableStateOf(initialHardwareItems)
    }
    // Deriving expenditure from stock keeps both values consistent, including after rotation.
    val totalSpent = remember(hardwareItems) { totalSpentFor(hardwareItems) }
    val filteredItems = remember(searchQuery, selectedCategory, hardwareItems) {
        filterHardware(hardwareItems, searchQuery, selectedCategory)
    }
    val categories = remember { initialHardwareItems.map { it.category }.distinct() }
    val listState = rememberLazyListState()
    val categoryListState = rememberLazyListState()
    val keyboard = LocalSoftwareKeyboardController.current
    val categoryChipColors = FilterChipDefaults.filterChipColors(
        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
    )

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(
            modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).imePadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            // Headers scroll too: landscape, large fonts and the IME cannot squeeze out the list.
            LazyColumn(
                state = listState,
                modifier = Modifier.widthIn(max = 680.dp).fillMaxSize().testTag("hardware_list"),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item(key = "header", contentType = "header") {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Icon(painterResource(R.drawable.ic_devcluster), contentDescription = null, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.size(48.dp).clip(MaterialTheme.shapes.small))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(stringResource(R.string.app_name), style = MaterialTheme.typography.headlineSmall, modifier = Modifier.semantics { heading() })
                            Text(stringResource(R.string.app_subtitle), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                item(key = "expenditure", contentType = "summary") {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(stringResource(R.string.spent_label), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Text(formatAmount(totalSpent), style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.testTag("total_spent"))
                            Text(stringResource(R.string.allocation_hint), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                }
                item(key = "search", contentType = "search") {
                    HardwareSearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearch = { keyboard?.hide() }
                    )
                }
                item(key = "categories", contentType = "filter") {
                    Column(modifier = Modifier.testTag("category_filter"), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(stringResource(R.string.category_label), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 8.dp))
                        LazyRow(state = categoryListState, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            item(key = "all") {
                                FilterChip(selected = selectedCategory == null, onClick = { selectedCategory = null }, label = { Text(stringResource(R.string.all_categories)) }, colors = categoryChipColors, modifier = Modifier.testTag("category_all"))
                            }
                            items(categories, key = { it }) { category ->
                                FilterChip(selected = selectedCategory == category, onClick = { selectedCategory = category }, label = { Text(category) }, colors = categoryChipColors, modifier = Modifier.testTag("category_$category"))
                            }
                        }
                    }
                }
                item(key = "catalog", contentType = "label") {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(stringResource(R.string.catalog_title), style = MaterialTheme.typography.titleMedium, modifier = Modifier.semantics { heading() })
                        Text(pluralStringResource(R.plurals.result_count, filteredItems.size, filteredItems.size), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.testTag("result_count"))
                    }
                }
                if (filteredItems.isEmpty()) {
                    item(key = "empty", contentType = "empty") {
                        EmptyResults(onClear = { searchQuery = ""; selectedCategory = null })
                    }
                }
                items(filteredItems, key = { it.id }, contentType = { "hardware" }) { item ->
                    HardwareCard(item = item, onAllocate = { id -> hardwareItems = allocateHardware(hardwareItems, id) })
                }
            }
        }
    }
}

@Composable
fun HardwareSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth().testTag("hardware_search"),
        label = { Text(stringResource(R.string.search_label)) },
        placeholder = { Text(stringResource(R.string.search_placeholder)) },
        leadingIcon = { Icon(painterResource(R.drawable.ic_search), contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(painterResource(R.drawable.ic_close), contentDescription = stringResource(R.string.clear_search))
                }
            }
        },
        singleLine = true,
        shape = MaterialTheme.shapes.medium,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() })
    )
}

@Composable
fun HardwareCard(item: HardwareItem, onAllocate: (String) -> Unit, modifier: Modifier = Modifier) {
    val available = item.stockCount > 0
    val clickLabel = stringResource(R.string.allocate_accessibility, item.name)
    val stockDescription = stringResource(R.string.stock_accessibility, item.stockCount)
    Column(
        modifier = modifier.fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surface)
            .clickable(enabled = available, role = Role.Button, onClickLabel = clickLabel) { onAllocate(item.id) }
            .semantics { stateDescription = stockDescription }
            .testTag("hardware_${item.id}")
            // Clickable precedes inner padding. LazyColumn spacing is the outer gap.
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(item.category, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(item.name, style = MaterialTheme.typography.titleLarge)
        }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(formatAmount(item.cost), style = MaterialTheme.typography.titleMedium)
            Text(
                text = if (available) pluralStringResource(R.plurals.stock_count, item.stockCount, item.stockCount) else stringResource(R.string.out_of_stock),
                color = if (available) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.testTag("stock_${item.id}")
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        if (available) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.allocate_action), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f))
                Icon(painterResource(R.drawable.ic_arrow), contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
        } else {
            Text(stringResource(R.string.out_of_stock), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun EmptyResults(onClear: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(stringResource(R.string.empty_title), style = MaterialTheme.typography.titleLarge)
        Text(stringResource(R.string.empty_description), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Button(onClick = onClear) { Text(stringResource(R.string.clear_filters)) }
    }
}

@Composable
private fun formatAmount(amount: Double): String {
    val formatted = NumberFormat.getNumberInstance(Locale.forLanguageTag("tr-TR")).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }.format(amount)
    return stringResource(R.string.amount_units, formatted)
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Preview(showBackground = true, widthDp = 640, heightDp = 360, fontScale = 1.5f)
@Composable
private fun DevClusterPreview() { DevClusterTheme { DevClusterScreen() } }
