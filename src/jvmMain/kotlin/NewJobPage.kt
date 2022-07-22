
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.TrayState
import androidx.compose.ui.window.rememberNotification
import org.daisy.pipeline.ui.bridge.Script
import org.daisy.pipeline.ui.bridge.ScriptField

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScriptSelector(
    label: String,
    list: List<Script>,
    focusRequester: FocusRequester,
    onSelectionChanged: (selection: Script) -> Unit
) {

    var selected by remember { mutableStateOf(list[0]) }
    var expanded by remember { mutableStateOf(false) } // initial value
    var color by remember { mutableStateOf(Green) }

    var hasSelection by remember { mutableStateOf(false) } // initial value
    var focusManager = LocalFocusManager.current
    var firstScriptFocusRequester = FocusRequester()
    var otherScriptFocusRequester = FocusRequester()
    Box {
        Column(Modifier.padding(5.dp), Arrangement.SpaceBetween) {
            Row(
                Modifier.clickable(
                    role = Role.Button,
                    onClickLabel = label
                ) {
                    expanded = !expanded
                }.then(Modifier.focusable().focusRequester(focusRequester)).semantics {
                      this.text = AnnotatedString(if (hasSelection) selected.name + " script selected" else label)
                }.then(
                    Modifier.onPreviewKeyEvent {
                        if (it.type == KeyEventType.KeyDown && it.key == Key.Tab) {
                            if (it.isShiftPressed) {
                                focusManager.moveFocus(FocusDirection.Up)
                            } else focusManager.moveFocus(FocusDirection.Down)
                            true
                        } else {
                            false
                        }
                    }
                ),
                Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) { // Anchor view

                Button(
                    onClick = {
                        expanded = !expanded
                    }
                ) {
                    Text(label)
                }
                if (hasSelection) Text(text = selected.name)

            }

            if (expanded) {
                LazyColumn(
                    Modifier.border(
                        border = BorderStroke(1.dp, LightGray),
                        shape = RoundedCornerShape(12)
                    ).padding(5.dp)
                ) {
                    items(
                        count = list.size,
                        itemContent = { index ->
                            Row(
                                modifier = Modifier.fillMaxWidth()
                                    .then(
                                        Modifier.semantics {
                                            text = AnnotatedString("${list[index].name} script")
                                        }
                                    ).then(
                                        Modifier.focusRequester(
                                            if (index == 0) firstScriptFocusRequester
                                            else otherScriptFocusRequester
                                        )
                                    ).then(
                                        Modifier.clickable(
                                            onClickLabel = list[index].name,
                                            role = Role.Button
                                        ) {
                                            selected = list[index]
                                            expanded = false
                                            hasSelection = true
                                            onSelectionChanged?.invoke(selected)
                                        }
                                    ),

                                ) {
                                Text(
                                    text = (list[index].name),
                                    modifier = Modifier.wrapContentWidth()
                                )
                            }
                            LaunchedEffect(Unit) {
                                firstScriptFocusRequester.requestFocus()
                            }
                        }
                    )
                }
            }
        }
    }

}

/**
 * Job creation page
 *
 */
@Composable
@Preview
fun NewJobPage(trayState: TrayState, pageFocusRequester: FocusRequester) {
    val notification = rememberNotification("New job", "Launching a new job")
    val scriptList = listOf(Script.MockDaisy3ToEpub3());
    var fieldsAnswer = mutableMapOf<ScriptField, String>()
    var firstParamFocusRequester = FocusRequester()
    MaterialTheme {
        var aScriptIsSelected by remember { mutableStateOf(false) }
        var selectedScript by remember { mutableStateOf(scriptList[0]) }
        var text by remember { mutableStateOf("Launch a job") }
        Column (
            Modifier.semantics {
                this.text = AnnotatedString("New job panel")
            }
        ) {
            Row(
                Modifier.padding(5.dp)
            ) {
                ScriptSelector(
                    "Select a script",
                    scriptList,
                    onSelectionChanged = { script ->
                        if(selectedScript != script){
                            selectedScript = script;
                            fieldsAnswer = mutableMapOf<ScriptField, String>()
                            selectedScript.requiredOptionFields.forEach { field ->
                                fieldsAnswer[field] = field.defaultValue
                            }
                            selectedScript.optionalOptionFields.forEach { field ->
                                fieldsAnswer[field] = field.defaultValue
                            }
                        } else {
                            if(aScriptIsSelected) firstParamFocusRequester.requestFocus()
                        }
                        aScriptIsSelected = true
                        //scriptSelectorFocusRequester.requestFocus()
                    },
                    focusRequester = pageFocusRequester
                )
            }
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(weight = 1f, fill = false)
            )
            {
                if (aScriptIsSelected) {
                    var counter = 0
                    selectedScript.requiredOptionFields.forEachIndexed { index, entry ->
                        ParameterView(
                            entry,
                            focusRequester = if(index == 0) firstParamFocusRequester else null
                        ) {
                            fieldsAnswer[entry] = it
                        }
                        counter++
                    }
                    selectedScript.optionalOptionFields.forEachIndexed { index, entry ->
                        ParameterView(
                            entry,
                            focusRequester = if(counter == 0 && index == 0) firstParamFocusRequester else null
                        ) {
                            fieldsAnswer[entry] = it
                        }
                    }
                }

            }
            Row {
                Button(
                    enabled = aScriptIsSelected,
                    onClick = {
                        text = "Job launched"
                        trayState.sendNotification(notification)
                    },
                ) {
                    Text(text)
                }
            }
        }
        LaunchedEffect(Unit) {
            pageFocusRequester.requestFocus()
        }
    }


}