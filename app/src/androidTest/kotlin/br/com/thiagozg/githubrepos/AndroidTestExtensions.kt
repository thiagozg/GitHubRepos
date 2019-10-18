package br.com.thiagozg.githubrepos

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import br.com.thiagozg.githubrepos.AndroidTestConstants.GSON
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import java.io.BufferedReader

object AndroidTestConstants {
    val GSON = Gson()
}

fun viewBy(id: Int) = onView(withId(id))

fun viewBy(text: String) = onView(withText(text))

fun viewByDescription(text: String) = onView(withContentDescription(text))

inline fun <reified T : View> viewBy(clazz: Class<T>) = onView(ViewMatchers.isAssignableFrom(clazz))

fun ViewInteraction.onScreen(activity: AppCompatActivity) = inRoot(withDecorView(not(`is`(activity.window.decorView))))

fun ViewInteraction.isDisplayed() = this.check(matches(ViewMatchers.isDisplayed()))

fun ViewInteraction.click() = this.perform(ViewActions.click())

inline fun <reified T> getJsonObject(context: Context, fileName: String, typeOf: TypeToken<T>) =
        GSON.fromJson<T>(readJsonFile(context, fileName), typeOf.type)

inline fun <reified T> getJsonObject(context: Context, fileName: String, clazz: Class<T>) =
        GSON.fromJson(readJsonFile(context, fileName), clazz)

fun readJsonFile(context: Context, filePath: String): String {
    val stream = context.resources.assets.open(filePath)
    val stringFromFile = stream.bufferedReader().use(BufferedReader::readText)
    stream.close()
    return stringFromFile
}

fun sleep(millis: Long = 1000L) = Thread.sleep(millis)
