
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import org.daisy.pipeline.ui.bridge.ScriptField
import javax.swing.JFileChooser


@Composable
fun ParameterView(
    field:ScriptField,
    onValueChange:(value:String) -> (Unit) = {}
) {
    var answer by remember { mutableStateOf(field.defaultValue.toString()) }
    Column {
        Text(field.name)
        Row(
            Modifier.padding(5.dp)
                .then(Modifier.fillMaxWidth()),
            Arrangement.spacedBy(5.dp)
        ) {
            Text(field.niceName)
            // Create the input matching the parameter type
            when(field.dataType){
                ScriptField.DataType.BOOLEAN -> Switch(
                    checked = answer.lowercase() == "true",
                    onCheckedChange = {
                        answer = it.toString().lowercase()
                        onValueChange.invoke(it.toString().lowercase())
                    },
                    modifier = Modifier.semantics() {
                        text = AnnotatedString(field.niceName +
                                (if (answer.lowercase() == "true") " option is enabled" else " option is disabled" )
                                + ". Click here to change it.")
                    }
                )
                ScriptField.DataType.DIRECTORY,
                ScriptField.DataType.FILE -> {
                    TextField(
                        value = answer.toString(),
                        singleLine = true,
                        onValueChange = {
                            answer = it.toString()
                            onValueChange.invoke(it.toString())
                        },
                        modifier = Modifier.semantics() {
                            text = AnnotatedString("Enter a value for " + field.niceName
                                    + " or select a value with the next button")
                        }
                    )
                    Button(
                        onClick = {
                            val jfc = JFileChooser()
                            jfc.fileSelectionMode = (
                                    if(field.dataType == ScriptField.DataType.DIRECTORY)
                                        JFileChooser.DIRECTORIES_ONLY
                                    else JFileChooser.FILES_ONLY
                                    )
                            val returnValue: Int = jfc.showOpenDialog(null)
                            if (returnValue == JFileChooser.APPROVE_OPTION) {
                                answer = jfc.selectedFile.absolutePath
                                onValueChange.invoke(answer.toString())
                            }
                        }
                    ){
                        Text(
                            if(field.dataType == ScriptField.DataType.DIRECTORY)
                                "Select a directory"
                            else "Select a file"
                        )
                    }
                }
                else -> TextField(
                    value = answer.toString(),
                    onValueChange = {
                        answer = it.toString()
                        onValueChange.invoke(it.toString())
                    }
                )
            }
        }
    }

}

