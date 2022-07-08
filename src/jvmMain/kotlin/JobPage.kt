
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * page that displays and control the pipeline that should run as a background service
 *
 * The page should display a start/stop button for the service
 * and the output and error log of the pipeline
 */
@Composable
@Preview
fun JobPage(){
    Box(Modifier.focusable()){
        MaterialTheme {
            Text("Job page to be designed")
        }
    }


}