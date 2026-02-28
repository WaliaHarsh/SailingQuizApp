import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.quizapp.sailing.di.appModule
import com.quizapp.sailing.presentation.ui.App
import kotlinx.browser.document
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
        modules(appModule)
    }
    val container = document.getElementById("app")
    ComposeViewport(container!!) {
        App()
    }
}
