

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*


fun main() = application {

    var isOpen by remember { mutableStateOf(true) }

    var currentRoute by remember { mutableStateOf("newJob") }


    // Launch pipeline service

    // pass the service registries to rendering functions
    // simplified routing within the app :
    //
    val trayState = rememberTrayState()
    val notification = rememberNotification("Notification", "Message from MyApp!")
    // Keep tray opened
    Tray(
        state = trayState,
        icon = TrayIcon,
        menu = {
            Item(
                "Open windows",
                onClick = {
                    isOpen = true
                }
            )
            /*Item(
                "Send notification",
                onClick = {
                    trayState.sendNotification(notification)
                }
            )*/
            Item(
                "Exit",
                onClick = {
                    // stop pipeline thread
                    exitApplication()
                }
            )
        }
    )
    // Managed window
    if(isOpen){
        Window(
            onCloseRequest = {
                isOpen = false
                // To remove to work as a background app
                exitApplication()
            },
            icon = MyAppIcon
        ) {
            var focusManager = LocalFocusManager.current

            var routes = mutableMapOf<String, @Composable () -> Unit>()
            routes["newJob"] = {
                NewJobPage()
                focusManager.moveFocus(FocusDirection.Right)
            }
            routes["status"] = {
                PipelinePage()
                focusManager.moveFocus(FocusDirection.Right)
            }
            routes["job"] = {
                JobPage()
                focusManager.moveFocus(FocusDirection.Right)
            }

            // content
            MaterialTheme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        Modifier.padding(5.dp)
                            .then(Modifier.fillMaxWidth())
                            .then(Modifier.align(Alignment.TopStart)),
                        Arrangement.spacedBy(5.dp)
                    )  {
                        Column( Modifier.width(200.dp)) {
                            Button(
                                onClick = {
                                    currentRoute = "newJob"
                                },
                                Modifier.fillMaxWidth()
                            ) {
                                Text("+ New job")
                            }
                            Button(
                                onClick = {
                                    currentRoute = "job"
                                },
                                Modifier.fillMaxWidth()
                            ) {
                                Text("Jobs")
                            }
                            Button(
                                onClick = {
                                    currentRoute = "status"

                                },
                                Modifier.fillMaxWidth()
                            ) {
                                Text("Service status")
                            }
                        }
                        Column {
                            Box(Modifier.focusable()){
                                if(routes.keys.contains(currentRoute)) routes[currentRoute]?.invoke()
                            }
                        }
                    }
                }
            }

        }
    }
}

object MyAppIcon : Painter() {
    override val intrinsicSize = Size(256f, 256f)

    override fun DrawScope.onDraw() {
        drawOval(Color.Green, Offset(size.width / 4, 0f), Size(size.width / 2f, size.height))
        drawOval(Color.Blue, Offset(0f, size.height / 4), Size(size.width, size.height / 2f))
        drawOval(Color.Red, Offset(size.width / 4, size.height / 4), Size(size.width / 2f, size.height / 2f))
    }
}

object TrayIcon : Painter() {
    override val intrinsicSize = Size(256f, 256f)

    override fun DrawScope.onDraw() {
        drawOval(Color(0xFFFFA500))
    }
}