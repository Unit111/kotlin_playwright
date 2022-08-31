import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.BrowserType.LaunchOptions
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.options.LoadState
import org.junit.jupiter.api.*
import org.junit.jupiter.api.function.Executable


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SteamTest {
    lateinit var playwright: Playwright
    lateinit var browser: Browser
    lateinit var context: BrowserContext
    lateinit var page: Page

    @BeforeAll
    fun launchBrowser() {
        playwright = Playwright.create()
        browser = playwright.chromium().launch(
            LaunchOptions()
                .setHeadless(false)
                .setSlowMo(100.0)
        )
    }

    @BeforeEach
    fun createContextAndPage() {
        context = browser.newContext()
        page = context.newPage()
    }

    @AfterEach
    fun closeContext() {
        context.close()
    }

    @AfterAll
    fun closeBrowser() {
        playwright.close()
    }

    @Test
    @DisplayName("Searching for a title")
    fun searchForGame() {
        page.navigate("https://store.steampowered.com/")
        page.type("#store_nav_search_term", "Witcher")
        page.click("#store_search_link img")
        page.waitForLoadState(LoadState.NETWORKIDLE)

        Assertions.assertAll(
            Executable {
                Assertions.assertEquals(
                    50, page.locator(".search_result_row").count(),
                    "The number of results is not 50"
                )
            },
            Executable {
                Assertions.assertEquals(
                    "The Witcher® 3: Wild Hunt",
                    page.locator("text=The Witcher® 3: Wild Hunt").textContent(),
                    "Title not found"
                )
            },
        )
    }
}