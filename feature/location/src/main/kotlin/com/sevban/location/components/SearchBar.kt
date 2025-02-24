package com.sevban.location.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sevban.common.extensions.EMPTY
import com.sevban.location.R
import com.sevban.ui.modifiers.clearFocusOnKeyboardDismiss

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChanged,
        placeholder = { Text(text = stringResource(R.string.map_tf_search)) },
        modifier = modifier.clearFocusOnKeyboardDismiss(),
        leadingIcon = {
            AnimatedVisibility(
                visible = searchQuery.isEmpty(),
                enter = slideInHorizontally { -2 * it } + fadeIn(),
                exit = slideOutHorizontally { -2 * it } + fadeOut(),
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.cd_search_icon)
                )
            }
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = searchQuery.isNotEmpty(),
                exit = slideOutHorizontally { 2 * it } + fadeOut(),
                enter = slideInHorizontally { 2 * it } + fadeIn(),
            ) {
                IconButton(onClick = { onSearchQueryChanged(String.EMPTY) }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.cd_clear_search_icon)
                    )
                }
            }
        },
        singleLine = true,
        maxLines = 1,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            errorContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}