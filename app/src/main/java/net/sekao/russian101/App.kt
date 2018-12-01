package net.sekao.russian101

import android.app.Activity
import android.os.Bundle

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.Typeface.*
import android.util.Log
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
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

val pageCounts = arrayOf(
    35, 9, 8, 13, 8, 27, 23, 18, 18, 24
)

const val argItemId = "item_id"
const val backgroundColor = "#005B98"
const val backgroundColorAlpha = "#55005b98"

class LessonDetail(c: Context) : RenderableView(c) {
    var gridAdapter: RenderableAdapter? = null

    constructor(c: Context, num: Int): this(c) {
        gridAdapter = RenderableAdapter.withItems(0.rangeTo(pageCounts[num]-1).toMutableList()) { pos, value ->
            imageView {
                imageBitmap(
                    BitmapFactory.decodeStream(
                        c.assets.open(
                            "lesson" + (num + 1) + "/" + (pos + 1) + "t.webp"
                        )
                    )
                )
            }
        }
    }

    override fun view() {
        val spacing = (10 * resources.displayMetrics.density).toInt()

        gridView {
            backgroundColor(Color.parseColor(backgroundColor))
            padding((5 * resources.displayMetrics.density).toInt())
            size(MATCH, MATCH)
            columnWidth((90 * resources.displayMetrics.density).toInt())
            numColumns(GridView.AUTO_FIT)
            verticalSpacing(spacing)
            horizontalSpacing(spacing)
            stretchMode(GridView.STRETCH_COLUMN_WIDTH)
            adapter(gridAdapter)
        }
    }
}

class LessonDetailActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LessonDetail(this, this.intent.extras!!.getInt(argItemId)))
    }
}

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

        val outerMargin = (16 * resources.displayMetrics.density).toInt()

        relativeLayout {
            backgroundColor(Color.parseColor(backgroundColor))
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
                    backgroundColor(Color.parseColor(backgroundColorAlpha))
                    size(FILL, FILL)
                    itemsCanFocus(true)
                    onItemClick { parent, view, pos, id ->
                        run {
                            val intent = Intent(context, LessonDetailActivity::class.java)
                            intent.putExtra(argItemId, id.toInt())
                            context.startActivity(intent)
                        }
                    }
                    adapter(listAdapter)
                }
            }
        }
    }
}

class LessonListActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LessonList(this))
    }
}
