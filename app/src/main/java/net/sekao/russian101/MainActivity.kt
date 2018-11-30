package net.sekao.russian101

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.Typeface.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import trikita.anvil.BaseDSL

import trikita.anvil.RenderableView

import trikita.anvil.DSL.*
import trikita.anvil.RenderableAdapter

data class Lesson(val title: String, val subtitle : String)

val lessons = arrayOf(
    Lesson("Alphabet", "алфавит"),
    Lesson("Meeting People", "Знакомство"),
    Lesson("Family", "семья"),
    Lesson("Where do you work?", "Где вы работаете?"),
    Lesson("Where do you live?", "Где вы живете?"),
    Lesson("Shopping", "покупки"),
    Lesson("In the restaurant", "В ресторане"),
    Lesson("Transportation", "транспорт"),
    Lesson("In the hotel", "В гостинице"),
    Lesson("The telephone", "телефон")
)

class LessonList(c: Context) : RenderableView(c) {
    var listAdapter = RenderableAdapter.withItems(lessons.toMutableList()) { pos, value ->
        linearLayout {
            size(WRAP, WRAP)
            orientation(LinearLayout.VERTICAL)
            padding((5 * resources.displayMetrics.density).toInt())
            textView {
                size(MATCH, WRAP)
                textSize(24 * resources.displayMetrics.density)
                typeface(Typeface.create(SANS_SERIF, BOLD))
                textColor(Color.WHITE)
                text(value.title)
            }
            textView {
                size(MATCH, WRAP)
                textSize(14 * resources.displayMetrics.density)
                typeface(Typeface.create(SANS_SERIF, NORMAL))
                textColor(Color.WHITE)
                text(value.subtitle)
            }
        }
    }

    override fun view() {
        listAdapter.notifyDataSetChanged()

        val outerMargin = (16 * resources.displayMetrics.density).toInt();

        relativeLayout {
            backgroundColor(Color.parseColor("#005B98"))
            frameLayout {
                size(MATCH, MATCH)
                imageView {
                    imageResource(R.drawable.background)
                    scaleType(ImageView.ScaleType.CENTER_CROP)
                }
            }
            linearLayout {
                gravity(BaseDSL.TOP)
                orientation(LinearLayout.VERTICAL)
                margin(outerMargin, 0, outerMargin, 0)
                textView {
                    textSize(48 * resources.displayMetrics.density)
                    typeface(Typeface.create(SANS_SERIF, BOLD))
                    textColor(Color.WHITE)
                    gravity(BaseDSL.CENTER_HORIZONTAL)
                    text(R.string.app_name)
                }
                listView {
                    backgroundColor(Color.parseColor("#55005b98"))
                    size(FILL, FILL)
                    itemsCanFocus(true)
                    onItemClick { parent, view, pos, id ->

                    }
                    adapter(listAdapter)
                }
            }
        }
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LessonList(this))
    }
}
