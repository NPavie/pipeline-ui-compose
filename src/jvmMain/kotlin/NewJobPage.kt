

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.TrayState
import androidx.compose.ui.window.rememberNotification
import org.daisy.pipeline.ui.bridge.Script
import org.daisy.pipeline.ui.bridge.ScriptField

@Composable
fun ScriptSpinner(
    label: String,
    list: List<Script>,
    onSelectionChanged: (selection: Script) -> Unit
) {
    var hasSelection by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(list[0]) }
    var expanded by remember { mutableStateOf(false) } // initial value

    Box {
        Column (Modifier.padding(5.dp), Arrangement.SpaceBetween) {
            /*Text(
                label,
                fontWeight = FontWeight.Bold
            )*/

            Row (Modifier.clickable { expanded = !expanded }){ // Anchor view
                Text(text = if(hasSelection) selected.name else label) // City name label
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "open list")
            }
            /*OutlinedTextField(
                value = (selected.name),
                onValueChange = { },
                label = { Text(label) },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                trailingIcon = { Icon(Icons.Outlined.ArrowDropDown, null) },
                readOnly = true
            )*/

            DropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                list.forEach { entry ->
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                selected = entry
                                expanded = false
                                hasSelection = true
                                onSelectionChanged?.invoke(selected)
                        },
                    ){
                        Text(
                            text = (entry.name),
                            modifier = Modifier.wrapContentWidth()
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .padding(10.dp)
                .clickable(
                    onClick = { expanded = !expanded }
                )
        )
    }
}

/**
 * Job creation page
 *
 */
@Composable
@Preview
fun NewJobPage(trayState: TrayState){
    val notification = rememberNotification("New job", "Launching a new job")
    val scriptList = listOf(Script.MockDaisy3ToEpub3());
    var fieldsAnswer = mutableMapOf<ScriptField, String>()
    MaterialTheme {
        var aScriptIsSelected by remember { mutableStateOf(false) }
        var selectedScript by remember { mutableStateOf(scriptList[0]) }
        var text by remember { mutableStateOf("Launch a job") }
        Column {
            Row (Modifier.padding(5.dp)) {
                ScriptSpinner(
                    "Click here to select a script",
                    scriptList,
                    onSelectionChanged = { script ->
                        selectedScript = script;
                        fieldsAnswer = mutableMapOf<ScriptField, String>()
                        selectedScript.requiredOptionFields.forEach {
                            field -> fieldsAnswer[field] = field.defaultValue
                        }
                        selectedScript.optionalOptionFields.forEach {
                                field -> fieldsAnswer[field] = field.defaultValue
                        }
                        aScriptIsSelected = true
                    }
                )
            }
            if(aScriptIsSelected) {
                selectedScript.requiredOptionFields.forEach {
                    entry -> ParameterView(
                        entry
                    ) {
                        fieldsAnswer[entry] = it
                    }
                }
                selectedScript.optionalOptionFields.forEach {
                        entry -> ParameterView(
                    entry
                ) {
                    fieldsAnswer[entry] = it
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

    }


}