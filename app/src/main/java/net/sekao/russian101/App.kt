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
import trikita.anvil.Anvil
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
    var isTablet = false

    constructor (c: Context, lessonNum: Int) : this(c) {
        setLessonNum(c, lessonNum)
    }

    fun setLessonNum (c: Context, num: Int) {
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
        Anvil.render(this)
    }

    override fun view() {
        val spacing = (10 * resources.displayMetrics.density).toInt()

        gridView {
            size(FILL, FILL)
            backgroundColor(Color.parseColor(if (isTablet) backgroundColorAlpha else backgroundColor))
            padding((5 * resources.displayMetrics.density).toInt())
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
    var selectedLesson = 0

    override fun view() {
        val isTablet = resources.displayMetrics.widthPixels / resources.displayMetrics.density >= 900
        val tabletWidth = (300 * resources.displayMetrics.density).toInt()
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
                size(MATCH, MATCH)
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
                linearLayout {
                    size(FILL, FILL)
                    orientation(LinearLayout.HORIZONTAL)
                    listView {
                        backgroundColor(Color.parseColor(backgroundColorAlpha))
                        size(if (isTablet) tabletWidth else FILL, FILL)
                        itemsCanFocus(true)
                        onItemClick { parent, view, pos, id ->
                            run {
                                if (isTablet) {
                                    selectedLesson = pos
                                }
                                else {
                                    val intent = Intent(context, LessonDetailActivity::class.java)
                                    intent.putExtra(argItemId, pos)
                                    context.startActivity(intent)
                                }
                            }
                        }
                        adapter(listAdapter)
                    }
                    if (isTablet) {
                        v(LessonDetail::class.java) {
                            val v = Anvil.currentView<LessonDetail>()
                            v.setLessonNum(this.context, selectedLesson)
                            v.isTablet = true
                        }
                    }
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
