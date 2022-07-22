
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import org.daisy.pipeline.ui.bridge.ScriptField
import javax.swing.JFileChooser


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ParameterView(
    field: ScriptField,
    focusRequester: FocusRequester?,
    onValueChange: (value: String) -> (Unit) = {}
) {

    var answer by remember { mutableStateOf(field.defaultValue.toString()) }
    val focusManager = LocalFocusManager.current
    // TODO for better accesible switch/checkbox, make the row clickable

    Row(
        Modifier.padding(5.dp)
            .then(Modifier.fillMaxWidth())
            .then(
                if (field.dataType == ScriptField.DataType.BOOLEAN) Modifier.toggleable(
                    value = answer.lowercase() == "true",
                    role = Role.Checkbox,
                    onValueChange = {
                        answer = it.toString().lowercase()
                        onValueChange.invoke(it.toString().lowercase())
                    }
                ).then(Modifier.onPreviewKeyEvent {
                    if (it.type == KeyEventType.KeyDown &&
                        (it.key == Key.Spacebar || it.key == Key.Enter)
                    ) {
                        answer = if (answer.lowercase() == "true") "false" else "true"
                        onValueChange.invoke(answer.toString().lowercase())
                        true
                    } else false
                }) else Modifier
            ).then(
                Modifier.semantics {
                    this.text = AnnotatedString(field.niceName)
                }.then(
                    if(focusRequester != null)
                        Modifier.focusRequester(focusRequester)
                    else Modifier
                )
            ),
        Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(field.niceName)
        // Create the input matching the parameter type
        when (field.dataType) {
            ScriptField.DataType.BOOLEAN -> Checkbox(
                checked = answer.lowercase() == "true",
                onCheckedChange = null
            )
            ScriptField.DataType.DIRECTORY,
            ScriptField.DataType.FILE -> {
                Button(
                    onClick = {
                        val jfc = JFileChooser()
                        jfc.fileSelectionMode = (
                                if (field.dataType == ScriptField.DataType.DIRECTORY)
                                    JFileChooser.DIRECTORIES_ONLY
                                else JFileChooser.FILES_ONLY
                                )
                        val returnValue: Int = jfc.showOpenDialog(null)
                        if (returnValue == JFileChooser.APPROVE_OPTION) {
                            answer = jfc.selectedFile.absolutePath
                            onValueChange.invoke(answer.toString())
                        }
                    }
                ) {
                    Text(
                        if (field.dataType == ScriptField.DataType.DIRECTORY)
                            "Select a directory"
                        else "Select a file"
                    )
                }
                OutlinedTextField(
                    value = answer.toString(),
                    singleLine = true,
                    label = {
                        Text(field.name)
                    },
                    onValueChange = {
                        answer = it.toString()
                        onValueChange.invoke(it.toString())
                    },
                )
            }
            else -> OutlinedTextField(
                value = answer.toString(),
                onValueChange = {
                    answer = it.toString()
                    onValueChange.invoke(it.toString())
                },
                label = {
                    Text(field.name)
                },
                modifier = Modifier.semantics() {
                    contentDescription = field.niceName + " input"
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
                    },
                )
            )
        }
    }
    if(focusRequester != null){
        LaunchedEffect(Unit){
            focusRequester.requestFocus()
        }
    }



}

