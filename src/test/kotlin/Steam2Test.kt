import com.microsoft.playwright.options.LoadState
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable


class Steam2Test: BaseTest() {

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