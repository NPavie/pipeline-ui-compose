
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString

/**
 * page that displays and control the pipeline that should run as a background service
 *
 * The page should display a start/stop button for the service
 * and the output and error log of the pipeline
 */
@Composable
@Preview
fun JobPage(pageFocusRequester: FocusRequester){
    Column (
        Modifier.semantics {
            this.text = AnnotatedString("Jobs panel")
        }
    ) {
        Text("Jobs page to be designed if", Modifier.focusable().focusRequester(pageFocusRequester))
    }

    LaunchedEffect(Unit){
        pageFocusRequester.requestFocus()
    }

}